package com.kanggutang.dao;

import com.kanggutang.dto.EarningExpenseYearDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/22
 * @description
 */
@Mapper
public interface EarningExpenseYearMapperExt extends EarningExpenseYearMapper {

    /**
     * 获取年收入支出
     * @param earningExpenseYearDTO
     * @return
     */
    public List<EarningExpenseYearDTO> getEarningExpenseYears(EarningExpenseYearDTO earningExpenseYearDTO);
}
