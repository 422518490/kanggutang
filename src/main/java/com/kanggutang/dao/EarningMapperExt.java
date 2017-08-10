package com.kanggutang.dao;

import com.kanggutang.dto.EarningDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.model.Earning;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/16
 * @description
 */
@Mapper
public interface EarningMapperExt extends EarningMapper {

    /**
     * 获取收入明细
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getEarnings(EarningDTO earningDTO);

    /**
     * 按月份获取收入
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getMonthEarning(EarningDTO earningDTO);

    /**
     * 按年份获取收入
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getYearEarning(EarningDTO earningDTO);

    /**
     * 获取收入金额之和
     * @param earningDTO
     * @return
     */
    public BigDecimal getSumEarning(EarningDTO earningDTO);

}
