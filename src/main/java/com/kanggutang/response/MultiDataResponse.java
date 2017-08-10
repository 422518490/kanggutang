package com.kanggutang.response;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2016/8/19
 * @description
 */
public class MultiDataResponse<T> extends BaseResponse {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
