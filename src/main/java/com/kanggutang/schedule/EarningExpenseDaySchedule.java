package com.kanggutang.schedule;

import com.kanggutang.dto.EarningDTO;
import com.kanggutang.dto.EarningExpenseDayDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.service.EarningExpenseDayService;
import com.kanggutang.service.EarningService;
import com.kanggutang.service.ExpenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
@EnableScheduling
@Component
public class EarningExpenseDaySchedule {

    private static Logger logger = LoggerFactory.getLogger(EarningExpenseDaySchedule.class);

    @Autowired
    private EarningService earningService;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private EarningExpenseDayService earningExpenseDayService;

    @Scheduled(cron = "${dayAmount.start.time}")
    @Transactional
    public void daySchedule(){
        try{
            logger.info("每日的收入支出定时任务运行。。。。。");
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer day = calendar.get(Calendar.DAY_OF_MONTH) -1;
            //获取昨天的收入情况
            EarningDTO earningDTO = new EarningDTO();
            earningDTO.setYear(year);
            earningDTO.setMonth(month);
            earningDTO.setDay(day);
            BigDecimal earningAmount = earningService.getSumEarning(earningDTO);
            if(earningAmount == null || earningAmount.compareTo(new BigDecimal(0)) == 0){
                earningAmount = new BigDecimal(0);
            }
            //获取昨天的支出情况
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setYear(year);
            expenseDTO.setMonth(month);
            expenseDTO.setDay(day);
            BigDecimal expenseAmount = expenseService.getSumExpense(expenseDTO);
            if(expenseAmount == null || expenseAmount.compareTo(new BigDecimal(0)) == 0){
                expenseAmount = new BigDecimal(0);
            }
            //新增收入支出
            EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
            earningExpenseDayDTO.setYear(year);
            earningExpenseDayDTO.setMonth(month);
            earningExpenseDayDTO.setDay(day);
            earningExpenseDayDTO.setEarningDay(earningAmount);
            earningExpenseDayDTO.setExpenseDay(expenseAmount);
            earningExpenseDayDTO.setLastUpdateDate(date);
            //判断是否已经存在当前的收入支出
            List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayService.getAllEarningExpenseDays(earningExpenseDayDTO);
            if(earningExpenseDayDTOList == null || earningExpenseDayDTOList.size() == 0){
                earningExpenseDayDTO.setCreationDate(date);
                earningExpenseDayService.addEarningExpenseDay(earningExpenseDayDTO);
            }else {
                earningExpenseDayDTO.setEarningExpenseDayId(earningExpenseDayDTOList.get(0).getEarningExpenseDayId());
                earningExpenseDayService.updateEarningExpenseDay(earningExpenseDayDTO);
            }
        }catch (Exception e){
            logger.error("每日收入支出定时任务出错:"+e);
            e.printStackTrace();
        }
    }
}
