package com.kanggutang.service;

import com.kanggutang.dto.EarningExpenseMonthDTO;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/22
 * @description
 */
public interface EarningExpenseMonthService {

    /**
     * 新增月收入支出
     * @param earningExpenseMonthDTO
     */
    public void addEarningExpenseMonth(EarningExpenseMonthDTO earningExpenseMonthDTO);

    /**
     * 更新月收入支出
     * @param earningExpenseMonthDTO
     */
    public void updateEarningExpenseMonth(EarningExpenseMonthDTO earningExpenseMonthDTO);

    /**
     * 获取月收入支出
     * @param earningExpenseMonthDTO
     * @return
     */
    public List<EarningExpenseMonthDTO> getEarningExpenseMonths(EarningExpenseMonthDTO earningExpenseMonthDTO);

    /**
     * 获取月收入支出
     * @param earningExpenseMonthDTO
     * @return
     */
    public List<EarningExpenseMonthDTO> getAllEarningExpenseMonths(EarningExpenseMonthDTO earningExpenseMonthDTO);

    /**
     * 手动运行每月的收入支出
     * @param earningExpenseMonthDTO
     */
    public void runEarningExpenseMonth(EarningExpenseMonthDTO earningExpenseMonthDTO);
}
