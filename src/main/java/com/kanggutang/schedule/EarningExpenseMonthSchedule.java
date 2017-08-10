package com.kanggutang.schedule;

import com.kanggutang.dto.EarningExpenseDayDTO;
import com.kanggutang.dto.EarningExpenseMonthDTO;
import com.kanggutang.service.EarningExpenseDayService;
import com.kanggutang.service.EarningExpenseMonthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @version 1.0 2017/5/22
 * @description
 */
@EnableScheduling
@Component
public class EarningExpenseMonthSchedule {

    private static Logger logger = LoggerFactory.getLogger(EarningExpenseMonthSchedule.class);

    @Autowired
    private EarningExpenseDayService earningExpenseDayService;
    @Autowired
    private EarningExpenseMonthService earningExpenseMonthService;


    @Scheduled(cron = "${monthAmount.start.time}")
    @Transactional
    public void monthSchedule(){
        try {
            logger.info("月收入支出定时任务。。。。");
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
            earningExpenseDayDTO.setYear(year);
            earningExpenseDayDTO.setMonth(month);
            List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayService.getAllEarningExpenseDays(earningExpenseDayDTO);
            if(earningExpenseDayDTOList != null && earningExpenseDayDTOList.size() > 0){
                BigDecimal earningMonthAmount = new BigDecimal(0);
                BigDecimal expenseMonthAmount = new BigDecimal(0);
                for(EarningExpenseDayDTO dayDTO : earningExpenseDayDTOList){
                    earningMonthAmount = earningMonthAmount.add(dayDTO.getEarningDay());
                    expenseMonthAmount = expenseMonthAmount.add(dayDTO.getExpenseDay());
                }
                EarningExpenseMonthDTO earningExpenseMonthDTO = new EarningExpenseMonthDTO();
                earningExpenseMonthDTO.setYear(year);
                earningExpenseMonthDTO.setMonth(month);
                earningExpenseMonthDTO.setEarningMonth(earningMonthAmount);
                earningExpenseMonthDTO.setExpenseMonth(expenseMonthAmount);
                earningExpenseMonthDTO.setLastUpdateDate(date);
                List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = earningExpenseMonthService.getAllEarningExpenseMonths(earningExpenseMonthDTO);
                if(earningExpenseMonthDTOList == null || earningExpenseMonthDTOList.size() == 0){
                    earningExpenseMonthDTO.setCreationDate(date);
                    earningExpenseMonthService.addEarningExpenseMonth(earningExpenseMonthDTO);
                }else {
                    earningExpenseMonthDTO.setEarningExpenseMonthId(earningExpenseMonthDTOList.get(0).getEarningExpenseMonthId());
                    earningExpenseMonthService.updateEarningExpenseMonth(earningExpenseMonthDTO);
                }
            }
        }catch (Exception e){
            logger.error("月收入支出定时任务出错:"+e);
            e.printStackTrace();
        }
    }

}
