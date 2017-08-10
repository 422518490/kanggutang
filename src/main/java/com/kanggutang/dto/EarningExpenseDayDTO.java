package com.kanggutang.dto;

import com.kanggutang.model.EarningExpenseDay;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/10
 * @description
 */
public class EarningExpenseDayDTO extends EarningExpenseDay {

    private Integer userId;
    private String accessToken;

    //开始查询年份
    private Integer beginYear;
    //开始查询月份
    private Integer beginMonth;
    //开始查询日期
    private Integer beginDay;
    //结束查询年份
    private Integer endYear;
    //结束查询月份
    private Integer endMonth;
    //结束查询日期
    private Integer endDay;
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

    public Integer getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(Integer beginDay) {
        this.beginDay = beginDay;
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

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
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
