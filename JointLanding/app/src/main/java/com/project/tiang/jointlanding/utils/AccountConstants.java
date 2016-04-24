package com.project.tiang.jointlanding.utils;


public class AccountConstants {
    public interface SinaWeibo {
        String CLIENT_ID = "251571501";//按需求更换
        String REDIRECT_URL = "http://xxx.xxx.com";
        String SCOPE = "";
        String GET_ACCOUNT_INFO = "https://api.weibo.com/2/users/show.json";
    }

    public interface QQ {
        String CLIENT_ID = "1032909358";//按需求更换

        String SCOPE = null;
    }

    public interface WeiXin {
        String CLIENT_ID = "wx2ba02x26e5626d33";//按需求更换
        String APP_SECRET = "d46246e56265d1d99dcf0547af5443d";//按需求更换
        String SCOPE = "snsapi_userinfo";
        String GET_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/access_token";
        String REFRESH_ACCESS_TOKEN_URL="https://api.weixin.qq.com/sns/oauth2/refresh_token";
        String GET_USER_INFO_URL="https://api.weixin.qq.com/sns/userinfo";
        String GRANT_TYPE="authorization_code";
    }
}
