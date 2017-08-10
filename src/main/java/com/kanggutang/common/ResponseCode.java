package com.kanggutang.common;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
public class ResponseCode {

    /**
     * 表示返回成功
     */
    public static final int SUCCESS = 200;
    /**
     * 表示API调用时的参数有误
     */
    public static final int PARAMETER_ERROR = 422;

    /**
     * 表示其他服务端错误
     */
    public static final int SERVER_ERROR = 500;

}
