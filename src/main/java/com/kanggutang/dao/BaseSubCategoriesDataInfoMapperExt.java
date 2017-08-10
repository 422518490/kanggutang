package com.kanggutang.dao;

import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/10
 * @description
 */
@Mapper
public interface BaseSubCategoriesDataInfoMapperExt extends BaseSubCategoriesDataInfoMapper {

    /**
     * 获取基础数据
     * @param baseSubCategoriesDataInfoDTO
     * @return
     */
    public List<BaseSubCategoriesDataInfoDTO> getCodeAndValueByBatch(BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO);
}
