package com.kanggutang.service.impl;

import com.github.pagehelper.PageHelper;
import com.kanggutang.dao.EarningExpenseMonthMapperExt;
import com.kanggutang.dto.EarningExpenseDayDTO;
import com.kanggutang.dto.EarningExpenseMonthDTO;
import com.kanggutang.service.EarningExpenseDayService;
import com.kanggutang.service.EarningExpenseMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/22
 * @description
 */
@Service
public class EarningExpenseMonthServiceImpl implements EarningExpenseMonthService {

    @Autowired
    private EarningExpenseMonthMapperExt earningExpenseMonthMapperExt;
    @Autowired
    private EarningExpenseDayService earningExpenseDayService;

    @Value("${pageSize}")
    private Integer pageSize;

    @Override
    public void addEarningExpenseMonth(EarningExpenseMonthDTO earningExpenseMonthDTO) {
        earningExpenseMonthMapperExt.insertSelective(earningExpenseMonthDTO);
    }

    @Override
    public void updateEarningExpenseMonth(EarningExpenseMonthDTO earningExpenseMonthDTO) {
        earningExpenseMonthMapperExt.updateByPrimaryKeySelective(earningExpenseMonthDTO);
    }

    @Override
    public List<EarningExpenseMonthDTO> getEarningExpenseMonths(EarningExpenseMonthDTO earningExpenseMonthDTO) {
        Integer pageNum = earningExpenseMonthDTO.getPageNum();
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = earningExpenseMonthMapperExt.getEarningExpenseMonths(earningExpenseMonthDTO);
        return earningExpenseMonthDTOList;
    }

    @Override
    public List<EarningExpenseMonthDTO> getAllEarningExpenseMonths(EarningExpenseMonthDTO earningExpenseMonthDTO) {
        List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = earningExpenseMonthMapperExt.getEarningExpenseMonths(earningExpenseMonthDTO);
        return earningExpenseMonthDTOList;
    }

    @Override
    public void runEarningExpenseMonth(EarningExpenseMonthDTO earningExpenseMonthDTO) {
        Date date = new Date();
        //收入支出日
        EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
        earningExpenseDayDTO.setBeginYear(earningExpenseMonthDTO.getBeginYear());
        earningExpenseDayDTO.setBeginMonth(earningExpenseMonthDTO.getBeginMonth());
        earningExpenseDayDTO.setBeginDay(1);
        earningExpenseDayDTO.setEndYear(earningExpenseMonthDTO.getEndYear());
        earningExpenseDayDTO.setEndMonth(earningExpenseMonthDTO.getEndMonth()+1);
        earningExpenseDayDTO.setEndDay(1);
        List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayService.getAllLessThanEarningExpenseDays(earningExpenseDayDTO);
        if(earningExpenseDayDTOList != null && earningExpenseDayDTOList.size() > 0){
            BigDecimal earningMonth = new BigDecimal(0);
            BigDecimal expenseMonth = new BigDecimal(0);
            String yearMonth = "";
            for(int i = 0; i < earningExpenseDayDTOList.size();i++){
                //第一次初始月份
                if("".equals(yearMonth)){
                    yearMonth = earningExpenseDayDTOList.get(i).getYear() + "-" +earningExpenseDayDTOList.get(i).getMonth();
                }
                //如果月份相等则相加对应的收入支出
                String tempYearMonth = earningExpenseDayDTOList.get(i).getYear() + "-" +earningExpenseDayDTOList.get(i).getMonth();
                if(yearMonth.equals(tempYearMonth)){
                    earningMonth = earningMonth.add(earningExpenseDayDTOList.get(i).getEarningDay());
                    expenseMonth = expenseMonth.add(earningExpenseDayDTOList.get(i).getExpenseDay());
                    //判断是否是最后一次循环
                    if(i == earningExpenseDayDTOList.size()-1){
                        //获取当前年份月份信息
                        earningExpenseMonthDTO.setYear(earningExpenseDayDTO.getYear());
                        earningExpenseMonthDTO.setMonth(earningExpenseDayDTO.getMonth());
                        earningExpenseMonthDTO.setEarningMonth(earningMonth);
                        earningExpenseMonthDTO.setExpenseMonth(expenseMonth);
                        earningExpenseMonthDTO.setLastUpdateDate(date);
                        List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = getAllEarningExpenseMonths(earningExpenseMonthDTO);
                        addUpdateExpenseMonth(earningExpenseMonthDTOList,earningExpenseMonthDTO,date);
                    }
                }else{
                    //如果和上次不相同，则计算上一次月份的收入支出
                    earningExpenseMonthDTO.setYear(earningExpenseDayDTOList.get(i-1).getYear());
                    earningExpenseMonthDTO.setMonth(earningExpenseDayDTOList.get(i-1).getMonth());
                    earningExpenseMonthDTO.setEarningMonth(earningMonth);
                    earningExpenseMonthDTO.setExpenseMonth(expenseMonth);
                    earningExpenseMonthDTO.setLastUpdateDate(date);
                    List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = getAllEarningExpenseMonths(earningExpenseMonthDTO);
                    addUpdateExpenseMonth(earningExpenseMonthDTOList,earningExpenseMonthDTO,date);
                    //初始化赋值下一个月份的收入支出
                    yearMonth = earningExpenseDayDTOList.get(i).getYear() + "-" +earningExpenseDayDTOList.get(i).getMonth();
                    earningMonth = earningExpenseDayDTOList.get(i).getEarningDay();
                    expenseMonth = earningExpenseDayDTOList.get(i).getExpenseDay();
                    //判断是否是最后一次循环
                    if(i == earningExpenseDayDTOList.size()-1){
                        //获取当前年份月份信息
                        earningExpenseMonthDTO.setYear(earningExpenseDayDTOList.get(i).getYear());
                        earningExpenseMonthDTO.setMonth(earningExpenseDayDTOList.get(i).getMonth());
                        earningExpenseMonthDTO.setEarningMonth(earningMonth);
                        earningExpenseMonthDTO.setExpenseMonth(expenseMonth);
                        earningExpenseMonthDTO.setLastUpdateDate(date);
                        earningExpenseMonthDTOList = getAllEarningExpenseMonths(earningExpenseMonthDTO);
                        addUpdateExpenseMonth(earningExpenseMonthDTOList,earningExpenseMonthDTO,date);
                    }
                }
            }
        }
    }


    private void addUpdateExpenseMonth(List<EarningExpenseMonthDTO> earningExpenseMonthDTOList,
                                       EarningExpenseMonthDTO earningExpenseMonthDTO, Date date){
        if(earningExpenseMonthDTOList == null || earningExpenseMonthDTOList.size() == 0){
            earningExpenseMonthDTO.setCreationDate(date);
            addEarningExpenseMonth(earningExpenseMonthDTO);
        }else{
            Integer earningExpenseMonthId = earningExpenseMonthDTOList.get(0).getEarningExpenseMonthId();
            earningExpenseMonthDTO.setEarningExpenseMonthId(earningExpenseMonthId);
            updateEarningExpenseMonth(earningExpenseMonthDTO);
        }
    }
}
