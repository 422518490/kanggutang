package com.kanggutang.service;

import com.kanggutang.dto.EarningDTO;
import com.kanggutang.model.Earning;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/16
 * @description
 */
public interface EarningService {

    /**
     * 新增收入
     * @param earningDTO
     */
    public void addEarning(EarningDTO earningDTO);

    /**
     * 获取收入明细
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getEarnings(EarningDTO earningDTO);

    /**
     * 获取收入明细
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getAllEarnings(EarningDTO earningDTO);

    /**
     * 按月份获取收入
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getMonthEarning(EarningDTO earningDTO);

    /**
     * 按月份获取收入
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getAllMonthEarning(EarningDTO earningDTO);

    /**
     * 按年份获取收入
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getYearEarning(EarningDTO earningDTO);

    /**
     * 按年份获取收入
     * @param earningDTO
     * @return
     */
    public List<EarningDTO> getAllYearEarning(EarningDTO earningDTO);

    /**
     * 获取收入金额之和
     * @param earningDTO
     * @return
     */
    public BigDecimal getSumEarning(EarningDTO earningDTO);

}
