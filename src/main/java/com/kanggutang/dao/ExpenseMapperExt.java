package com.kanggutang.dao;

import com.kanggutang.dto.ExpenseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
@Mapper
public interface ExpenseMapperExt extends ExpenseMapper {

    /**
     * 获取支出明细
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getExpenses(ExpenseDTO expenseDTO);

    /**
     * 获取月支出
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getMonthExpense(ExpenseDTO expenseDTO);

    /**
     * 获取年支出
     * @param expenseDTO
     * @return
     */
    public List<ExpenseDTO> getYearExpense(ExpenseDTO expenseDTO);

    /**
     * 获取支出金额
     * @param expenseDTO
     * @return
     */
    public BigDecimal getSumExpense(ExpenseDTO expenseDTO);
}
