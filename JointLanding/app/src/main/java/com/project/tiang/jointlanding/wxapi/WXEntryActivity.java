package com.project.tiang.jointlanding.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.project.tiang.jointlanding.MainActivity;
import com.project.tiang.jointlanding.utils.AccountConstants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by tiang on 2016/4/24.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mIwxapi;
    private final static String TAG = "WXEntryActivity";
    public final static String WX_CODE = "pass_wx_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIwxapi = WXAPIFactory.createWXAPI(this, AccountConstants.WeiXin.CLIENT_ID, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIwxapi.handleIntent(getIntent(), this);
        mFinish();
    }

    private void mFinish() {
        if (Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp!=null&&baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (baseResp instanceof SendAuth.Resp) {
                SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                if (resp.code != null && resp.code.length() > 0) {
                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                    intent.putExtra(WX_CODE, resp.code);
                    startActivity(intent);
                }
            }
        }
        mFinish();
    }
}
