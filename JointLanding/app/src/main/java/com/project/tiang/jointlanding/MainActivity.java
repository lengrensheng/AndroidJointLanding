package com.project.tiang.jointlanding;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.tiang.jointlanding.http.DialogUtils;
import com.project.tiang.jointlanding.utils.AccountConstants;
import com.project.tiang.jointlanding.wxapi.WXEntryActivity;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements IUiListener {

    @Bind(R.id.btnWeiXin)
    Button btnWeiXin;
    @Bind(R.id.btnQQ)
    Button btnQQ;
    @Bind(R.id.btnWeiBo)
    Button btnWeiBo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (null != intent) {
            String accessToken = intent.getStringExtra(WXEntryActivity.WX_CODE);
            if (!TextUtils.isEmpty(accessToken)) {
                Toast.makeText(getApplicationContext(), accessToken, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

        if (mTencent != null) {
            mTencent.releaseResource();
            mTencent = null;
        }
        if (mIWXinApi != null) {
            mIWXinApi.detach();
            mIWXinApi = null;
        }
        if (mAuthInfo != null) {
            mAuthInfo = null;
        }
    }

    @OnClick({R.id.btnQQ, R.id.btnWeiXin, R.id.btnWeiBo})
    void OnClickBtn(View view) {
        switch (view.getId()) {
            case R.id.btnQQ: {
                LoginByQQ();
                break;
            }
            case R.id.btnWeiXin: {
                LoginByWeixin();
                break;
            }
            case R.id.btnWeiBo: {
                LoginByWeibo();
                break;
            }
        }
    }

    private Tencent mTencent;
    private Dialog mWaitDialog;

    /**
     * 参考资料：http://wiki.open.qq.com/wiki/QQ%E7%99%BB%E5%BD%95%E5%92%8C%E6%B3%A8%E9%94%80#1._.E5.88.9B.E5.BB.BA.E5.B7.A5.E7.A8.8B.E5.B9.B6.E9.85.8D.E7.BD.AE.E5.B7.A5.E7.A8.8B
     */
    private void LoginByQQ() {
        mWaitDialog = DialogUtils.showWaitDialog(MainActivity.this, getString(R.string.text_account_loadding), false, false);
        mTencent = Tencent.createInstance(AccountConstants.QQ.CLIENT_ID, this.getApplicationContext());
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, AccountConstants.QQ.SCOPE, this);

        } else {
            if (mWaitDialog != null && mWaitDialog.isShowing()) {
                mWaitDialog.dismiss();
            }
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }
    }

    private AuthInfo mAuthInfo;
    private SsoHandler mSsoHandler;

    /**
     * java.lang.UnsatisfiedLinkError: com.android.tools.fd.runtime.IncrementalClas 错误 在libs下新建一个armeabi-v7a，然后将liblocSDK3.so复制一份到该文件夹
     * UnsatisfiedLinkError is due to the libffmpeg-jni.so file missing on your android project. Please try for correct library.
     */
    private void LoginByWeibo() {
        mWaitDialog = DialogUtils.showWaitDialog(MainActivity.this, getString(R.string.text_account_loadding), false, false);
        mAuthInfo = new AuthInfo(getApplicationContext(), AccountConstants.SinaWeibo.CLIENT_ID, AccountConstants.SinaWeibo.REDIRECT_URL, AccountConstants.SinaWeibo.SCOPE);
        if (mAuthInfo == null) {
            hideDialog();
            Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();

        } else {
            mSsoHandler = new SsoHandler(MainActivity.this, mAuthInfo);
            mSsoHandler.authorize(new WeiboAuthListener() {
                @Override
                public void onComplete(Bundle bundle) {
                    hideDialog();
                    // 从 Bundle 中解析 Token
                    Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    if (mAccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        Toast.makeText(getApplicationContext(), "授权成功", Toast.LENGTH_LONG).show();
                    } else {
                        // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
                        String code = bundle.getString("code", "");
                        Toast.makeText(getApplicationContext(), "授权失败，错误代码：" + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    hideDialog();
                }
            });
        }
    }

    private IWXAPI mIWXinApi;

    private void LoginByWeixin() {
        mWaitDialog = DialogUtils.showWaitDialog(MainActivity.this, getString(R.string.text_account_loadding), false, false);
        mIWXinApi = WXAPIFactory.createWXAPI(MainActivity.this, AccountConstants.WeiXin.CLIENT_ID, false);
        if (mIWXinApi.isWXAppInstalled()) {
            mIWXinApi.registerApp(AccountConstants.WeiXin.CLIENT_ID);
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = AccountConstants.WeiXin.SCOPE;
            req.state = "wechat_sdk_demo_test";
            mIWXinApi.sendReq(req);
        } else {
            hideDialog();
            Toast.makeText(getApplicationContext(), R.string.account_not_install_wx, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideDialog();
    }

    /**
     * 一般不需要调用该方法
     */
    private void LogoutByQQ() {
        if (mTencent != null) {
            mTencent.logout(this.getApplicationContext());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
            return;
        }
        if (mSsoHandler != null) {
            super.onActivityResult(requestCode, resultCode, data);
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onComplete(Object o) {
        hideDialog();
        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(UiError uiError) {
        hideDialog();
        Toast.makeText(getApplicationContext(), uiError.errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCancel() {
        hideDialog();
    }

    private void hideDialog() {
        if (mWaitDialog != null && mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }
}
