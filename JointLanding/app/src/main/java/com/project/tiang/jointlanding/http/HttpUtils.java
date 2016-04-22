package com.project.tiang.jointlanding.http;

import com.project.tiang.jointlanding.utils.AccountConstants;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by tiang on 2016/4/23.
 */
public class HttpUtils {

    private static HttpUtils utils;
    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        if (null == utils) {
            synchronized (HttpUtils.class) {
                if (null == utils) {
                    utils = new HttpUtils();
                }
            }
        }
        return utils;
    }

    public void LoginByWeibo() {
        OkHttpUtils.get()
                .url(AccountConstants.SinaWeibo.GET_ACCOUNT_INFO);
    }

    public void LoginByQQ(){

    }
}
