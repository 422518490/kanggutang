package com.kanggutang.schedule;

import com.kanggutang.dto.EarningExpenseMonthDTO;
import com.kanggutang.dto.EarningExpenseYearDTO;
import com.kanggutang.service.EarningExpenseMonthService;
import com.kanggutang.service.EarningExpenseYearService;
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
 * @version 1.0 2017/5/22
 * @description
 */
@EnableScheduling
@Component
public class EarningExpenseYearSchedule {

    private static Logger logger = LoggerFactory.getLogger(EarningExpenseYearSchedule.class);

    @Autowired
    private EarningExpenseMonthService earningExpenseMonthService;
    @Autowired
    private EarningExpenseYearService earningExpenseYearService;

    @Scheduled(cron = "${yearAmount.start.time}")
    @Transactional
    public void yearSchedule(){
        try {
            logger.info("年收入支出定时任务。。。。。");
            Calendar calendar = Calendar.getInstance();
            Date date = new Date();
            calendar.setTime(date);
            Integer year = calendar.get(Calendar.YEAR);
            EarningExpenseMonthDTO earningExpenseMonthDTO = new EarningExpenseMonthDTO();
            earningExpenseMonthDTO.setYear(year);
            List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = earningExpenseMonthService.getAllEarningExpenseMonths(earningExpenseMonthDTO);
            if(earningExpenseMonthDTOList != null && earningExpenseMonthDTOList.size() > 0){
                BigDecimal earningMonthAmount = new BigDecimal(0);
                BigDecimal expenseMonthAmount = new BigDecimal(0);
                for(EarningExpenseMonthDTO monthDTO : earningExpenseMonthDTOList){
                    earningMonthAmount = earningMonthAmount.add(monthDTO.getEarningMonth());
                    expenseMonthAmount = expenseMonthAmount.add(monthDTO.getExpenseMonth());
                }
                EarningExpenseYearDTO earningExpenseYearDTO = new EarningExpenseYearDTO();
                earningExpenseYearDTO.setYear(year);
                earningExpenseYearDTO.setEarningYear(earningMonthAmount);
                earningExpenseYearDTO.setExpenseYear(expenseMonthAmount);
                earningExpenseYearDTO.setLastUpdateDate(date);
                List<EarningExpenseYearDTO> earningExpenseYearDTOList = earningExpenseYearService.getAllEarningExpenseYears(earningExpenseYearDTO);
                if(earningExpenseYearDTOList == null || earningExpenseYearDTOList.size() == 0){
                    earningExpenseYearDTO.setCreationDate(date);
                    earningExpenseYearService.addEarningExpenseYear(earningExpenseYearDTO);
                }else {
                    earningExpenseYearDTO.setEarningExpenseYearId(earningExpenseYearDTOList.get(0).getEarningExpenseYearId());
                    earningExpenseYearService.updateEarningExpenseYear(earningExpenseYearDTO);
                }
            }
        }catch (Exception e){
            logger.error("年收入支出定时任务出错:"+e);
            e.printStackTrace();
        }
    }
}
