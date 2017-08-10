package com.kanggutang.response;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
public class LoginResponse extends BaseResponse {

    private String accessToken;
    private Integer userId;
    private String loginName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
