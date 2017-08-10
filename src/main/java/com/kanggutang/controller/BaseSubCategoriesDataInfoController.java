package com.kanggutang.controller;

import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/10
 * @description
 */
@Controller
public class BaseSubCategoriesDataInfoController {

    @Autowired
    private BaseSubCategoriesDataInfoService baseSubCategoriesDataInfoService;

    @RequestMapping(value = "/categoriesDataInfo",method = RequestMethod.GET)
    public void categoriesDataInfo(Map<String,Object> map, @RequestParam("categoriesName")String categoriesName){
        BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO = new BaseSubCategoriesDataInfoDTO();
        baseSubCategoriesDataInfoDTO.setCategoriesName(categoriesName);
        List<BaseSubCategoriesDataInfoDTO> baseSubCategoriesDataInfoDTOList = baseSubCategoriesDataInfoService.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
        map.put("baseSubCategoriesDataInfoDTOList",baseSubCategoriesDataInfoDTOList);
    }

}
