package com.kanggutang.service.impl;

import com.github.pagehelper.PageHelper;
import com.kanggutang.dao.EarningExpenseYearMapperExt;
import com.kanggutang.dto.EarningExpenseMonthDTO;
import com.kanggutang.dto.EarningExpenseYearDTO;
import com.kanggutang.service.EarningExpenseMonthService;
import com.kanggutang.service.EarningExpenseYearService;
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
public class EarningExpenseYearServiceImpl implements EarningExpenseYearService {

    @Autowired
    private EarningExpenseYearMapperExt earningExpenseYearMapperExt;
    @Autowired
    private EarningExpenseMonthService earningExpenseMonthService;

    @Value("${pageSize}")
    private Integer pageSize;

    @Override
    public void addEarningExpenseYear(EarningExpenseYearDTO earningExpenseYearDTO) {
        earningExpenseYearMapperExt.insertSelective(earningExpenseYearDTO);
    }

    @Override
    public void updateEarningExpenseYear(EarningExpenseYearDTO earningExpenseYearDTO) {
        earningExpenseYearMapperExt.updateByPrimaryKeySelective(earningExpenseYearDTO);
    }

    @Override
    public List<EarningExpenseYearDTO> getEarningExpenseYears(EarningExpenseYearDTO earningExpenseYearDTO) {
        Integer pageNum = earningExpenseYearDTO.getPageNum();
        if(pageNum == null || pageNum == 0){
            pageNum = 1;
        }
        PageHelper.startPage(pageNum,pageSize);
        List<EarningExpenseYearDTO> earningExpenseYearDTOList = earningExpenseYearMapperExt.getEarningExpenseYears(earningExpenseYearDTO);
        return earningExpenseYearDTOList;
    }

    @Override
    public List<EarningExpenseYearDTO> getAllEarningExpenseYears(EarningExpenseYearDTO earningExpenseYearDTO) {
        List<EarningExpenseYearDTO> earningExpenseYearDTOList = earningExpenseYearMapperExt.getEarningExpenseYears(earningExpenseYearDTO);
        return earningExpenseYearDTOList;
    }

    @Override
    public void runEarningExpenseYear(EarningExpenseYearDTO earningExpenseYearDTO) {
        Date date = new Date();
        //获取相应年份的所有月份数据
        EarningExpenseMonthDTO earningExpenseMonthDTO = new EarningExpenseMonthDTO();
        earningExpenseMonthDTO.setBeginYear(earningExpenseYearDTO.getBeginYear());
        earningExpenseMonthDTO.setBeginMonth(1);
        earningExpenseMonthDTO.setEndYear(earningExpenseYearDTO.getEndYear());
        earningExpenseMonthDTO.setEndMonth(12);
        List<EarningExpenseMonthDTO> earningExpenseMonthDTOList = earningExpenseMonthService.getAllEarningExpenseMonths(earningExpenseMonthDTO);
        if(earningExpenseMonthDTOList != null && earningExpenseMonthDTOList.size() > 0){
            int year = 0;
            BigDecimal earningYear = new BigDecimal(0);
            BigDecimal expenseYear = new BigDecimal(0);
            for(int i = 0;i < earningExpenseMonthDTOList.size();i++){
                //第一次的初始化
                if(year == 0){
                    year = earningExpenseMonthDTOList.get(i).getYear();
                }
                if(year == earningExpenseMonthDTOList.get(i).getYear()){
                    earningYear = earningYear.add(earningExpenseMonthDTOList.get(i).getEarningMonth());
                    expenseYear = expenseYear.add(earningExpenseMonthDTOList.get(i).getExpenseMonth());
                    //判断是否是最后一次循环
                    if(i == earningExpenseMonthDTOList.size() - 1){
                        earningExpenseYearDTO.setYear(earningExpenseMonthDTOList.get(i).getYear());
                        earningExpenseYearDTO.setEarningYear(earningYear);
                        earningExpenseYearDTO.setExpenseYear(expenseYear);
                        earningExpenseYearDTO.setLastUpdateDate(date);
                        List<EarningExpenseYearDTO> earningExpenseYearDTOList = getAllEarningExpenseYears(earningExpenseYearDTO);
                        addUpdateExpenseYear(earningExpenseYearDTOList,earningExpenseYearDTO,date);
                    }
                }else{
                    //如果年份不相同则获取上一次年份的数据
                    earningExpenseYearDTO.setYear(earningExpenseMonthDTOList.get(i-1).getYear());
                    earningExpenseYearDTO.setEarningYear(earningYear);
                    earningExpenseYearDTO.setExpenseYear(expenseYear);
                    earningExpenseYearDTO.setLastUpdateDate(date);
                    List<EarningExpenseYearDTO> earningExpenseYearDTOList = getAllEarningExpenseYears(earningExpenseYearDTO);
                    addUpdateExpenseYear(earningExpenseYearDTOList,earningExpenseYearDTO,date);
                    //重新赋值
                    year = earningExpenseMonthDTOList.get(i).getYear();
                    earningYear = earningExpenseMonthDTOList.get(i).getEarningMonth();
                    expenseYear = earningExpenseMonthDTOList.get(i).getExpenseMonth();
                    //判断是否是最后一次循环
                    if(i == earningExpenseMonthDTOList.size() - 1){
                        earningExpenseYearDTO.setYear(earningExpenseMonthDTOList.get(i).getYear());
                        earningExpenseYearDTO.setEarningYear(earningYear);
                        earningExpenseYearDTO.setExpenseYear(expenseYear);
                        earningExpenseYearDTO.setLastUpdateDate(date);
                        earningExpenseYearDTOList = getAllEarningExpenseYears(earningExpenseYearDTO);
                        addUpdateExpenseYear(earningExpenseYearDTOList,earningExpenseYearDTO,date);
                    }
                }
            }
        }
    }

    private void addUpdateExpenseYear(List<EarningExpenseYearDTO> earningExpenseYearDTOList,
                                       EarningExpenseYearDTO earningExpenseYearDTO, Date date){
        if(earningExpenseYearDTOList == null || earningExpenseYearDTOList.size() == 0){
            earningExpenseYearDTO.setCreationDate(date);
            addEarningExpenseYear(earningExpenseYearDTO);
        }else{
            Integer earningExpenseYearId = earningExpenseYearDTOList.get(0).getEarningExpenseYearId();
            earningExpenseYearDTO.setEarningExpenseYearId(earningExpenseYearId);
            updateEarningExpenseYear(earningExpenseYearDTO);
        }
    }
}
