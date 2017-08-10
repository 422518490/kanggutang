package com.kanggutang.service;

import com.kanggutang.dto.EarningExpenseYearDTO;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/22
 * @description
 */
public interface EarningExpenseYearService {

    /**
     * 新增年收入支出
     * @param earningExpenseYearDTO
     */
    public void addEarningExpenseYear(EarningExpenseYearDTO earningExpenseYearDTO);

    /**
     * 更新年收入支出
     * @param earningExpenseYearDTO
     */
    public void updateEarningExpenseYear(EarningExpenseYearDTO earningExpenseYearDTO);

    /**
     * 获取年收入支出
     * @param earningExpenseYearDTO
     * @return
     */
    public List<EarningExpenseYearDTO> getEarningExpenseYears(EarningExpenseYearDTO earningExpenseYearDTO);


    /**
     * 获取年收入支出
     * @param earningExpenseYearDTO
     * @return
     */
    public List<EarningExpenseYearDTO> getAllEarningExpenseYears(EarningExpenseYearDTO earningExpenseYearDTO);

    /**
     * 手动运行年收入
     * @param earningExpenseYearDTO
     */
    public void runEarningExpenseYear(EarningExpenseYearDTO earningExpenseYearDTO);
}
