package com.kanggutang.service.impl;

import com.github.pagehelper.PageHelper;
import com.kanggutang.dao.ExpenseMapperExt;
import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.model.Expense;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import com.kanggutang.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseMapperExt expenseMapperExt;
    @Autowired
    private BaseSubCategoriesDataInfoService baseSubCategoriesDataInfoService;

    @Value("${pageSize}")
    private Integer pageSize;

    @Override
    public void addExpense(ExpenseDTO expenseDTO) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        expenseDTO.setYear(calendar.get(Calendar.YEAR));
        expenseDTO.setMonth(calendar.get(Calendar.MONTH)+1);
        expenseDTO.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        expenseDTO.setCreationDate(date);
        expenseDTO.setCreatedBy(expenseDTO.getUserId());
        expenseDTO.setLastUpdateDate(date);
        expenseDTO.setLastUpdatedBy(expenseDTO.getUserId());
        BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO = new BaseSubCategoriesDataInfoDTO();
        baseSubCategoriesDataInfoDTO.setCategoriesName("EXPENSE_TYPE");
        List categoriesCodeList = new ArrayList();
        categoriesCodeList.add(expenseDTO.getExpenseType());
        baseSubCategoriesDataInfoDTO.setCategoriesCodeList(categoriesCodeList);
        List<BaseSubCategoriesDataInfoDTO> baseSubCategoriesDataInfoDTOList = baseSubCategoriesDataInfoService.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
        if(baseSubCategoriesDataInfoDTOList != null && baseSubCategoriesDataInfoDTOList.size() > 0){
            expenseDTO.setExpenseName(baseSubCategoriesDataInfoDTOList.get(0).getCategoriesValue());
        }
        expenseMapperExt.insertSelective(expenseDTO);
    }

    @Override
    public List<ExpenseDTO> getExpenses(ExpenseDTO expenseDTO) {
        Integer pageNum = expenseDTO.getPageNum();
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<ExpenseDTO> expenseDTOList = expenseMapperExt.getExpenses(expenseDTO);
        return expenseDTOList;
    }

    @Override
    public List<ExpenseDTO> getAllExpenses(ExpenseDTO expenseDTO) {
        List<ExpenseDTO> expenseDTOList = expenseMapperExt.getExpenses(expenseDTO);
        return expenseDTOList;
    }

    @Override
    public List<ExpenseDTO> getMonthExpense(ExpenseDTO expenseDTO) {
        Integer pageNum = expenseDTO.getPageNum();
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<ExpenseDTO> expenseDTOList = expenseMapperExt.getMonthExpense(expenseDTO);
        return expenseDTOList;
    }

    @Override
    public List<ExpenseDTO> getAllMonthExpense(ExpenseDTO expenseDTO) {
        List<ExpenseDTO> expenseDTOList = expenseMapperExt.getMonthExpense(expenseDTO);
        return expenseDTOList;
    }

    @Override
    public List<ExpenseDTO> getYearExpense(ExpenseDTO expenseDTO) {
        Integer pageNum = expenseDTO.getPageNum();
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<ExpenseDTO> expenseDTOList = expenseMapperExt.getYearExpense(expenseDTO);
        return expenseDTOList;
    }

    @Override
    public List<ExpenseDTO> getAllYearExpense(ExpenseDTO expenseDTO) {
        List<ExpenseDTO> expenseDTOList = expenseMapperExt.getYearExpense(expenseDTO);
        return expenseDTOList;
    }

    @Override
    public BigDecimal getSumExpense(ExpenseDTO expenseDTO) {
        return expenseMapperExt.getSumExpense(expenseDTO);
    }
}
