package com.kanggutang.service.impl;

import com.github.pagehelper.PageHelper;
import com.kanggutang.dao.EarningMapperExt;
import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.dto.EarningDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.model.Earning;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import com.kanggutang.service.EarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/16
 * @description
 */
@Service
public class EarningServiceImpl implements EarningService{

    @Autowired
    private EarningMapperExt earningMapperExt;
    @Autowired
    private BaseSubCategoriesDataInfoService baseSubCategoriesDataInfoService;

    @Value("${pageSize}")
    private Integer pageSize;

    @Override
    public void addEarning(EarningDTO earningDTO) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer userId = earningDTO.getUserId();
        earningDTO.setCreatedBy(userId);
        earningDTO.setCreationDate(date);
        earningDTO.setLastUpdatedBy(userId);
        earningDTO.setLastUpdateDate(date);
        earningDTO.setYear(calendar.get(calendar.YEAR));
        earningDTO.setMonth(calendar.get(calendar.MONTH)+1);
        earningDTO.setDay(calendar.get(calendar.DAY_OF_MONTH));
        BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO = new BaseSubCategoriesDataInfoDTO();
        baseSubCategoriesDataInfoDTO.setCategoriesName("EARNING_TYPE");
        List categoriesCodeList = new ArrayList();
        categoriesCodeList.add(earningDTO.getEarningType());
        baseSubCategoriesDataInfoDTO.setCategoriesCodeList(categoriesCodeList);
        List<BaseSubCategoriesDataInfoDTO> baseSubCategoriesDataInfoDTOList = baseSubCategoriesDataInfoService.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
        if(baseSubCategoriesDataInfoDTOList != null && baseSubCategoriesDataInfoDTOList.size() > 0){
            earningDTO.setEarningName(baseSubCategoriesDataInfoDTOList.get(0).getCategoriesValue());
        }
        earningMapperExt.insertSelective(earningDTO);
    }

    @Override
    public List<EarningDTO> getEarnings(EarningDTO earningDTO) {
        Integer pageNum = earningDTO.getPageNum();
        if(pageNum == null || pageNum ==0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<EarningDTO> earningDTOList = earningMapperExt.getEarnings(earningDTO);
        return earningDTOList;
    }

    @Override
    public List<EarningDTO> getAllEarnings(EarningDTO earningDTO) {
        List<EarningDTO> earningDTOList = earningMapperExt.getEarnings(earningDTO);
        return earningDTOList;
    }

    @Override
    public List<EarningDTO> getMonthEarning(EarningDTO earningDTO) {
        Integer pageNum = earningDTO.getPageNum();
        if(pageNum == null || pageNum ==0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<EarningDTO> earningDTOList = earningMapperExt.getMonthEarning(earningDTO);
        return earningDTOList;
    }

    @Override
    public List<EarningDTO> getAllMonthEarning(EarningDTO earningDTO) {
        List<EarningDTO> earningDTOList = earningMapperExt.getMonthEarning(earningDTO);
        return earningDTOList;
    }

    @Override
    public List<EarningDTO> getYearEarning(EarningDTO earningDTO) {
        Integer pageNum = earningDTO.getPageNum();
        if(pageNum == null || pageNum ==0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<EarningDTO> earningDTOList = earningMapperExt.getYearEarning(earningDTO);
        return earningDTOList;
    }

    @Override
    public List<EarningDTO> getAllYearEarning(EarningDTO earningDTO) {
        List<EarningDTO> earningDTOList = earningMapperExt.getYearEarning(earningDTO);
        return earningDTOList;
    }

    @Override
    public BigDecimal getSumEarning(EarningDTO earningDTO) {
        return earningMapperExt.getSumEarning(earningDTO);
    }

}
