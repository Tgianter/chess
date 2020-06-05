package com.example.demo.constant;

public interface UserConstant {
    // 请求的网址
    public static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    // appid
//    public static final String WX_LOGIN_APPID = "wx2f13c97aded66a0e";
    public static final String WX_LOGIN_APPID = "wx552b423f878a12cf";
    // 密匙
//    public static final String WX_LOGIN_SECRET = "1f5b936b60a92a5721143770d12466fd";
    public static final String WX_LOGIN_SECRET = "9c90574fe19ed43da6ce2e7b5004f21f";
    // 固定参数
    public static final String WX_LOGIN_GRANT_TYPE = "authorization_code";
}
