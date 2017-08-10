package com.kanggutang.service.impl;

import com.kanggutang.dao.BaseSubCategoriesDataInfoMapperExt;
import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/10
 * @description
 */
@Service
public class BaseSubCategoriesDataInfoServiceImpl implements BaseSubCategoriesDataInfoService {

    @Autowired
    private BaseSubCategoriesDataInfoMapperExt baseSubCategoriesDataInfoMapperExt;

    @Override
    public List<BaseSubCategoriesDataInfoDTO> getCodeAndValueByBatch(BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO) {
        return baseSubCategoriesDataInfoMapperExt.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
    }
}
