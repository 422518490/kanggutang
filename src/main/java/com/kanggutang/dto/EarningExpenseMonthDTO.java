package com.kanggutang.dto;

import com.kanggutang.model.EarningExpenseMonth;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class EarningExpenseMonthDTO extends EarningExpenseMonth {
    private Integer userId;
    private String accessToken;

    //开始查询年份
    private Integer beginYear;
    //开始查询月份
    private Integer beginMonth;
    //结束查询年份
    private Integer endYear;
    //结束查询月份
    private Integer endMonth;
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

    public Integer getBeginMonth() {
        return beginMonth;
    }

    public void setBeginMonth(Integer beginMonth) {
        this.beginMonth = beginMonth;
    }



    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
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