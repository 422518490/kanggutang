package com.kanggutang.dto;

import com.kanggutang.model.EarningExpenseYear;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class EarningExpenseYearDTO extends EarningExpenseYear {
    private Integer userId;
    private String accessToken;

    //开始查询年份
    private Integer beginYear;
    //结束查询年份
    private Integer endYear;
    private String creationDateStr;
    private Integer pageNum;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(Integer beginYear) {
        this.beginYear = beginYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public String getCreationDateStr() {
        return creationDateStr;
    }

    public void setCreationDateStr(String creationDateStr) {
        this.creationDateStr = creationDateStr;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}