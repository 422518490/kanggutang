package com.kanggutang.dao;

import com.kanggutang.dto.EarningExpenseDayDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
@Mapper
public interface EarningExpenseDayMapperExt extends EarningExpenseDayMapper {

    /**
     * 获取收入支出
     * @param earningExpenseDayDTO
     * @return
     */
    public List<EarningExpenseDayDTO> getEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO);

    /**
     * 获取小于结束日期的数据
     * @param earningExpenseDayDTO
     * @return
     */
    public List<EarningExpenseDayDTO> getLessThanEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO);
}
