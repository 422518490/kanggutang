package com.kanggutang.service;

import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/10
 * @description
 */
public interface BaseSubCategoriesDataInfoService {

    /**
     * 获取基础数据
     * @param baseSubCategoriesDataInfoDTO
     * @return
     */
    public List<BaseSubCategoriesDataInfoDTO> getCodeAndValueByBatch(BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO);
}
