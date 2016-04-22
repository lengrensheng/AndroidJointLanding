package com.project.tiang.jointlanding.utils;


public class AccountConstants {
    public static final String SCOPE = "uc_trust";
    public static final String CLIENT_ID = "JnhAs2YleUzxdK5xFWDQ";
    public static final String CLIENT_SECRET = "mQbuGcHFcy6l8zfSSVRvPZSvjecypi";
    public final static String REDIRECT_URL = "http://xxx.meizu.com";

    public interface SinaWeibo {
        String CLIENT_ID = "251571501";
        String REDIRECT_URL = "http://xxx.meizu.com";
        String SCOPE = "";
        String GET_ACCOUNT_INFO = "https://api.weibo.com/2/users/show.json";
    }

    public interface QQ {
        String CLIENT_ID = "1032909358";
        String SCOPE = null;
    }

    public interface WeiXin {
        String CLIENT_ID = "wx7ba08e07e51865d3";
        String APP_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
        String SCOPE = "snsapi_userinfo";
        String GET_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token";
        String REFRESH_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/refresh_token";
        String GET_USER_INFO_URL="https://api.weixin.qq.com/sns/userinfo";
        String GRANT_TYPE="authorization_code";
    }
}
