package com.kanggutang.service;

import com.kanggutang.dto.ExpenseDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
public interface ExpenseService {

    /**
     * 新增支出
     * @param expenseDTO
     */
    public void addExpense(ExpenseDTO expenseDTO);

    /**
     * 获取支出明细
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getExpenses(ExpenseDTO expenseDTO);

    /**
     * 获取支出明细
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getAllExpenses(ExpenseDTO expenseDTO);

    /**
     * 获取月支出明细
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getMonthExpense(ExpenseDTO expenseDTO);

    /**
     * 获取月支出明细
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getAllMonthExpense(ExpenseDTO expenseDTO);

    /**
     * 获取年支出
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getYearExpense(ExpenseDTO expenseDTO);

    /**
     * 获取年支出
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getAllYearExpense(ExpenseDTO expenseDTO);

    /**
     * 获取支出金额
     * @param expenseDTO
     * @return
     */
    public BigDecimal getSumExpense(ExpenseDTO expenseDTO);
}
