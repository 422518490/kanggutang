package com.kanggutang.dao;

import com.kanggutang.dto.EarningExpenseMonthDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/22
 * @description
 */
@Mapper
public interface EarningExpenseMonthMapperExt extends EarningExpenseMonthMapper {

    /**
     * 获取月收入支出
     * @param earningExpenseMonthDTO
     * @return
     */
    public List<EarningExpenseMonthDTO> getEarningExpenseMonths(EarningExpenseMonthDTO earningExpenseMonthDTO);

}
