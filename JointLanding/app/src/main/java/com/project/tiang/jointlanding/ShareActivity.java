package com.project.tiang.jointlanding;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.tiang.jointlanding.http.DialogUtils;
import com.project.tiang.jointlanding.utils.AccountConstants;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tiang on 2016/4/24.
 */
public class ShareActivity extends AppCompatActivity  implements IWeiboHandler.Response{
    private static final String TAG = "ShareActivity";
    private boolean isShareText = false;

    @Bind(R.id.ll_share_text)
    LinearLayout shareTextLayout;
    @Bind(R.id.ll_share_picture)
    LinearLayout sharePictureLayout;

    @Bind(R.id.edt_input_content)
    EditText inputEditText;
    @Bind(R.id.img_share_content)
    ImageView shareImageView;

    @Bind(R.id.btnQQ)
    Button btnQQ;
    @Bind(R.id.btnWeiBo)
    Button btnWeiBo;
    @Bind(R.id.btnWeiXin)
    Button btnWeiXin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getFlags() == MainActivity.SHARE_TEXT) {
                isShareText = true;
            } else {
                isShareText = false;
            }
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if (isShareText) {
            shareTextLayout.setVisibility(View.VISIBLE);
            sharePictureLayout.setVisibility(View.GONE);
        } else {
            shareTextLayout.setVisibility(View.GONE);
            sharePictureLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK: {
                if (Build.VERSION.SDK_INT > 21) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            }
        }
        return true;
    }

    @OnClick({R.id.btnQQ,R.id.btnWeiBo,R.id.btnWeiXin})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.btnQQ:{
                shareByQQ();
                break;
            }
            case R.id.btnWeiBo:{
                shareByWeiBo();
                break;
            }
            case R.id.btnWeiXin:{
                break;
            }
        }
    }
    private Dialog mShareDialog;
    private void shareByQQ() {
        mShareDialog = DialogUtils.showWaitDialog(ShareActivity.this, getString(R.string.text_account_sharing), false, false);
        final Bundle params = new Bundle();
        Tencent mTencent = Tencent.createInstance(AccountConstants.QQ.CLIENT_ID, this.getApplicationContext());
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");//跳转地址
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
        mTencent.shareToQQ(ShareActivity.this, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
               hideDialog();
            }

            @Override
            public void onError(UiError uiError) {
                Log.e(TAG,"shareError:"+uiError.errorMessage);
                hideDialog();
                Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                hideDialog();
            }
        });
    }
    private IWeiboShareAPI mWeiboShareAPI;
    private void shareByWeiBo() {
        //图片分享不能大于32K
        if(!TextUtils.isEmpty(inputEditText.getText().toString())) {
            mShareDialog = DialogUtils.showWaitDialog(ShareActivity.this, getString(R.string.text_account_sharing), false, false);
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, AccountConstants.WeiXin.CLIENT_ID);
            mWeiboShareAPI.registerApp();
            TextObject textObject = new TextObject();
            textObject.text = inputEditText.getText().toString();
            ImageObject imageObject = new ImageObject();
            imageObject.imageData = getResources().getDrawable(R.mipmap.ic_launcher).toString().getBytes();
            WeiboMultiMessage multiMessage = new WeiboMultiMessage();
            multiMessage.textObject  =textObject;
            multiMessage.imageObject = imageObject;
            SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
            request.transaction = String.valueOf(System.currentTimeMillis());
            request.multiMessage = multiMessage;
            mWeiboShareAPI.sendRequest(ShareActivity.this,request);
        }

    }
    private void hideDialog(){
        if(mShareDialog!=null&&mShareDialog.isShowing()){
            mShareDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideDialog();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent,this);
    }

    //微博响应回复
    @Override
    public void onResponse(BaseResponse baseResponse) {
        hideDialog();
        Log.d(TAG,"weibo response code:"+baseResponse.errCode);
        switch (baseResponse.errCode){
            case WBConstants.ErrorCode.ERR_OK:{
                Toast.makeText(getApplicationContext(),"分享成功",Toast.LENGTH_SHORT).show();
            }
            default:{
                Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
