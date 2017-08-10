package com.kanggutang.response;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
public class BaseResponse {
    //返回码
    private Integer responseCode;
    //返回描述
    private String responseDesc;


    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }
}
