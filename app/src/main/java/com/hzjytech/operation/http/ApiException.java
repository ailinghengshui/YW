package com.hzjytech.operation.http;


import android.text.TextUtils;

public class ApiException extends RuntimeException {
    private HttpResult result;

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public ApiException(HttpResult httpResult) {
        this(getExceptionsMsg(httpResult));
        this.result=httpResult;
    }

    public static String getExceptionsMsg(HttpResult status) {
        String msg;
        switch (status.getStatusCode()) {
            case 100:
                msg="账号或密码错误";
                break;
            case 40317:
                msg="token过期，请重新登录!";
                break;
            case 403:
                msg="您没有相关权限";
                break;
            case 500:
                msg="服务器正在玩命加载中";
                break;
            case 101:
                msg="账号或密码错误";
                break;
            case 102:
                msg="验证码错误";
                break;
            default:
                if (TextUtils.isEmpty(status.getStatusMsg())) {
                    msg = "未知错误";
                } else {
                    msg = status.getStatusMsg();
                }
                break;
        }
        return msg;
    }

    public HttpResult getResult() {
        return result;
    }

    public void setResult(HttpResult result) {
        this.result = result;
    }
}

