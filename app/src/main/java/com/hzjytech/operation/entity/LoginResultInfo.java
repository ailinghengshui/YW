package com.hzjytech.operation.entity;

/**
 * Created by hehongcan on 2017/12/25.
 */

public class LoginResultInfo {
    private LoginInfo loginInfo;
    private String identifyCodeUrl;

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getIdentifyCodeUrl() {
        return identifyCodeUrl;
    }

    public void setIdentifyCodeUrl(String identifyCodeUrl) {
        this.identifyCodeUrl = identifyCodeUrl;
    }

    @Override
    public String toString() {
        return "LoginResultInfo{" + "loginInfo=" + loginInfo + ", identifyCodeUrl='" +
                identifyCodeUrl + '\'' + '}';
    }
}
