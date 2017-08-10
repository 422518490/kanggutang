package com.kanggutang.service.impl;

import com.github.pagehelper.PageHelper;
import com.kanggutang.dao.EarningExpenseDayMapperExt;
import com.kanggutang.dto.EarningDTO;
import com.kanggutang.dto.EarningExpenseDayDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.service.EarningExpenseDayService;
import com.kanggutang.service.EarningService;
import com.kanggutang.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
@Service
public class EarningExpenseDayServiceImpl implements EarningExpenseDayService {

    @Autowired
    private EarningExpenseDayMapperExt earningExpenseDayMapperExt;
    @Autowired
    private EarningService earningService;
    @Autowired
    private ExpenseService expenseService;


    @Value("${pageSize}")
    private Integer pageSize;

    @Override
    public void addEarningExpenseDay(EarningExpenseDayDTO earningExpenseDayDTO) {
        earningExpenseDayMapperExt.insertSelective(earningExpenseDayDTO);
    }

    @Override
    public List<EarningExpenseDayDTO> getEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO) {
        Integer pageNum = earningExpenseDayDTO.getPageNum();
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayMapperExt.getEarningExpenseDays(earningExpenseDayDTO);
        return earningExpenseDayDTOList;
    }

    @Override
    public List<EarningExpenseDayDTO> getAllEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO) {
        List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayMapperExt.getEarningExpenseDays(earningExpenseDayDTO);
        return earningExpenseDayDTOList;
    }

    @Override
    public void updateEarningExpenseDay(EarningExpenseDayDTO earningExpenseDayDTO) {
        earningExpenseDayMapperExt.updateByPrimaryKeySelective(earningExpenseDayDTO);
    }

    @Override
    @Transactional
    public void runEarningExpenseDay(EarningDTO earningDTO, ExpenseDTO expenseDTO) {
        //收入
        List<EarningDTO> earningDTOList = earningService.getAllEarnings(earningDTO);
        Date date = new Date();
        String earningCreationDateStr = "";
        BigDecimal earningDay = new BigDecimal(0);
        if(earningDTOList != null && earningDTOList.size() > 0){
            for(int i = 0 ;i < earningDTOList.size();i++){ //第一次的时候判断
                if("".equals(earningCreationDateStr)){
                    earningCreationDateStr = earningDTOList.get(i).getCreationDateStr();
                }
                //判断日期是否相同
                if(earningCreationDateStr.equals(earningDTOList.get(i).getCreationDateStr())){
                    earningDay = earningDay.add(earningDTOList.get(i).getEarningAmount());
                    //判断是否是最后一次循环
                    if(i == earningDTOList.size() - 1){
                        EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
                        earningExpenseDayDTO.setYear(earningDTOList.get(i).getYear());
                        earningExpenseDayDTO.setMonth(earningDTOList.get(i).getMonth());
                        earningExpenseDayDTO.setDay(earningDTOList.get(i).getDay());
                        earningExpenseDayDTO.setEarningDay(earningDay);
                        earningExpenseDayDTO.setLastUpdateDate(date);
                        List<EarningExpenseDayDTO> earningExpenseDayDTOList = getAllEarningExpenseDays(earningExpenseDayDTO);
                        addUpdateEarningDay(earningExpenseDayDTOList,earningExpenseDayDTO,date);
                    }
                }else {
                    //不相等则判断数据库中是否有当前日期的数据，没有则新增，有则更新，同时重新赋值日期和收入金额
                    EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
                    earningExpenseDayDTO.setYear(earningDTOList.get(i-1).getYear());
                    earningExpenseDayDTO.setMonth(earningDTOList.get(i-1).getMonth());
                    earningExpenseDayDTO.setDay(earningDTOList.get(i-1).getDay());
                    earningExpenseDayDTO.setEarningDay(earningDay);
                    earningExpenseDayDTO.setLastUpdateDate(date);
                    List<EarningExpenseDayDTO> earningExpenseDayDTOList = getAllEarningExpenseDays(earningExpenseDayDTO);
                    addUpdateEarningDay(earningExpenseDayDTOList,earningExpenseDayDTO,date);
                    //重新赋值
                    earningDay = earningDTOList.get(i).getEarningAmount();
                    earningCreationDateStr = earningDTOList.get(i).getCreationDateStr();
                    //判断是否是最后一次循环
                    if(i == earningDTOList.size() - 1){
                        earningExpenseDayDTO = new EarningExpenseDayDTO();
                        earningExpenseDayDTO.setYear(earningDTOList.get(i).getYear());
                        earningExpenseDayDTO.setMonth(earningDTOList.get(i).getMonth());
                        earningExpenseDayDTO.setDay(earningDTOList.get(i).getDay());
                        earningExpenseDayDTO.setEarningDay(earningDay);
                        earningExpenseDayDTO.setLastUpdateDate(date);
                        earningExpenseDayDTOList = getAllEarningExpenseDays(earningExpenseDayDTO);
                        addUpdateEarningDay(earningExpenseDayDTOList,earningExpenseDayDTO,date);
                    }
                }
            }
        }
        //支出
        List<ExpenseDTO> expenseDTOList = expenseService.getAllExpenses(expenseDTO);
        String expenseCreationDateStr = "";
        BigDecimal expenseDay = new BigDecimal(0);
        if(expenseDTOList != null && expenseDTOList.size() > 0){
            for(int j =0 ;j < expenseDTOList.size();j++){//第一次的时候判断
                if("".equals(expenseCreationDateStr)){
                    expenseCreationDateStr = expenseDTOList.get(j).getCreationDateStr();
                }
                //判断日期是否相同
                if(expenseCreationDateStr.equals(expenseDTOList.get(j).getCreationDateStr())){
                    expenseDay = expenseDay.add(expenseDTOList.get(j).getExpenseAmount());
                    if(j == expenseDTOList.size() -1){
                        EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
                        earningExpenseDayDTO.setYear(expenseDTOList.get(j).getYear());
                        earningExpenseDayDTO.setMonth(expenseDTOList.get(j).getMonth());
                        earningExpenseDayDTO.setDay(expenseDTOList.get(j).getDay());
                        earningExpenseDayDTO.setLastUpdateDate(date);
                        earningExpenseDayDTO.setExpenseDay(expenseDay);
                        List<EarningExpenseDayDTO> earningExpenseDayDTOList = getAllEarningExpenseDays(earningExpenseDayDTO);
                        addUpdateExpenseDay(earningExpenseDayDTOList,earningExpenseDayDTO,date);
                    }
                }else{
                    //不相同则更新当前日期数据
                    EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
                    earningExpenseDayDTO.setYear(expenseDTOList.get(j-1).getYear());
                    earningExpenseDayDTO.setMonth(expenseDTOList.get(j-1).getMonth());
                    earningExpenseDayDTO.setDay(expenseDTOList.get(j-1).getDay());
                    earningExpenseDayDTO.setLastUpdateDate(date);
                    earningExpenseDayDTO.setExpenseDay(expenseDay);
                    //判断是否已经存在
                    List<EarningExpenseDayDTO> earningExpenseDayDTOList = getAllEarningExpenseDays(earningExpenseDayDTO);
                    addUpdateExpenseDay(earningExpenseDayDTOList,earningExpenseDayDTO,date);
                    //重新赋值
                    expenseDay = expenseDTOList.get(j).getExpenseAmount();
                    expenseCreationDateStr = expenseDTOList.get(j).getCreationDateStr();
                    //判断是否是最后一次循环
                    if(j == expenseDTOList.size() -1){
                        earningExpenseDayDTO = new EarningExpenseDayDTO();
                        earningExpenseDayDTO.setYear(expenseDTOList.get(j).getYear());
                        earningExpenseDayDTO.setMonth(expenseDTOList.get(j).getMonth());
                        earningExpenseDayDTO.setDay(expenseDTOList.get(j).getDay());
                        earningExpenseDayDTO.setLastUpdateDate(date);
                        earningExpenseDayDTO.setExpenseDay(expenseDay);
                        earningExpenseDayDTOList = getAllEarningExpenseDays(earningExpenseDayDTO);
                        addUpdateExpenseDay(earningExpenseDayDTOList,earningExpenseDayDTO,date);
                    }
                }
            }
        }
    }

    @Override
    public List<EarningExpenseDayDTO> getAllLessThanEarningExpenseDays(EarningExpenseDayDTO earningExpenseDayDTO) {
        List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayMapperExt.getLessThanEarningExpenseDays(earningExpenseDayDTO);
        return earningExpenseDayDTOList;
    }

    private void addUpdateEarningDay(List<EarningExpenseDayDTO> earningExpenseDayDTOList,EarningExpenseDayDTO earningExpenseDayDTO,Date date){
        if(earningExpenseDayDTOList == null || earningExpenseDayDTOList.size() == 0){
            earningExpenseDayDTO.setCreationDate(date);
            earningExpenseDayDTO.setExpenseDay(new BigDecimal(0));
            addEarningExpenseDay(earningExpenseDayDTO);
        }else{
            updateEarningExpenseDay(earningExpenseDayDTO);
        }
    }

    private void addUpdateExpenseDay(List<EarningExpenseDayDTO> earningExpenseDayDTOList,EarningExpenseDayDTO earningExpenseDayDTO,Date date){
        if(earningExpenseDayDTOList == null || earningExpenseDayDTOList.size() == 0){
            earningExpenseDayDTO.setCreationDate(date);
            earningExpenseDayDTO.setEarningDay(new BigDecimal(0));
            addEarningExpenseDay(earningExpenseDayDTO);
        }else{
            Integer earningExpenseDayId = earningExpenseDayDTOList.get(0).getEarningExpenseDayId();
            earningExpenseDayDTO.setEarningExpenseDayId(earningExpenseDayId);
            updateEarningExpenseDay(earningExpenseDayDTO);
        }
    }
}
