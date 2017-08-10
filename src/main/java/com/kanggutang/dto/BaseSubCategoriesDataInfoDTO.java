package com.kanggutang.dto;

import com.kanggutang.model.BaseSubCategoriesDataInfo;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/10
 * @description
 */
public class BaseSubCategoriesDataInfoDTO extends BaseSubCategoriesDataInfo {


    //编码字符串集合
    private List<String> categoriesCodeList;
    //大类名称
    private String categoriesName ;

    public List<String> getCategoriesCodeList() {
        return categoriesCodeList;
    }

    public void setCategoriesCodeList(List<String> categoriesCodeList) {
        this.categoriesCodeList = categoriesCodeList;
    }

    public String getCategoriesName() {
        return categoriesName;
    }

    public void setCategoriesName(String categoriesName) {
        this.categoriesName = categoriesName;
    }
}
