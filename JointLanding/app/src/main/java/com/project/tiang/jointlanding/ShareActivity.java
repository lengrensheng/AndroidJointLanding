package com.project.tiang.jointlanding;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tiang on 2016/4/24.
 */
public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";
    private boolean isShareText =false;

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
        if(intent!=null) {
            if (intent.getFlags() == MainActivity.SHARE_TEXT) {
                isShareText = true;
            } else {
                isShareText = false;
            }
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        if(isShareText){
            shareTextLayout.setVisibility(View.VISIBLE);
            sharePictureLayout.setVisibility(View.GONE);
        }else {
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
        switch (event.getKeyCode()){
            case KeyEvent.KEYCODE_BACK:{
                if(Build.VERSION.SDK_INT>21){
                    finishAfterTransition();
                }else{
                    finish();
                }
                break;
            }
        }
        return true;
    }
}
