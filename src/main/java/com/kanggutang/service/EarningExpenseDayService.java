package com.kanggutang.service;

import com.kanggutang.dto.EarningDTO;
import com.kanggutang.dto.EarningExpenseDayDTO;
import com.kanggutang.dto.ExpenseDTO;

import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
public interface EarningExpenseDayService {

    /**
     * 新增收入支出信息
     * @param earningExpenseDayDTO
     */
    public void addEarningExpenseDay(EarningExpenseDayDTO earningExpenseDayDTO);

    /**
     * 获取收入支出
     * @param earningExpenseDayDTO
     * @return
     */
    public List<EarningExpenseDayDTO> getEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO);

    /**
     * 获取收入支出
     * @param earningExpenseDayDTO
     * @return
     */
    public List<EarningExpenseDayDTO> getAllEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO);

    /**
     * 更新当前日期的收入支出
     * @param earningExpenseDayDTO
     */
    public void updateEarningExpenseDay(EarningExpenseDayDTO earningExpenseDayDTO);

    /**
     * 手动运行每日的收入支出
     * @param earningDTO
     * @param expenseDTO
     */
    public void runEarningExpenseDay(EarningDTO earningDTO, ExpenseDTO expenseDTO);

    /**
     * 获取小于结束日期的数据
     * @param earningExpenseDayDTO
     * @return
     */
    public List<EarningExpenseDayDTO> getAllLessThanEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO);


}
