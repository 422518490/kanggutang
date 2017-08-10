package com.kanggutang.controller;

import com.kanggutang.common.ResponseCode;
import com.kanggutang.common.ServiceUtil;
import com.kanggutang.dto.EarningDTO;
import com.kanggutang.dto.EarningExpenseDayDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.response.BaseResponse;
import com.kanggutang.response.MultiDataResponse;
import com.kanggutang.service.EarningExpenseDayService;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/19
 * @description
 */
@Controller
public class EarningExpenseDayController {

    private static Logger logger = LoggerFactory.getLogger(EarningExpenseDayController.class);
    
    @Autowired
    private EarningExpenseDayService earningExpenseDayService;

    @RequestMapping(value = "/searchEarningExpenseDayPage")
    public void searchEarningExpenseDayPage(Map<String,Object> map){
        map.put("yearList", ServiceUtil.getYearList());
        map.put("monthList",ServiceUtil.getMonthList());
        map.put("dayList",ServiceUtil.getDayList());
        EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
        earningExpenseDayDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        earningExpenseDayDTO.setBeginMonth(1);
        earningExpenseDayDTO.setBeginDay(1);
        earningExpenseDayDTO.setEndYear(ServiceUtil.getYearList().get(0));
        earningExpenseDayDTO.setEndMonth(1);
        earningExpenseDayDTO.setEndDay(1);
        earningExpenseDayDTO.setPageNum(1);
        List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayService.getEarningExpenseDays(earningExpenseDayDTO);
        map.put("earningExpenseDayDTOList",earningExpenseDayDTOList);
        if(earningExpenseDayDTOList != null && earningExpenseDayDTOList.size() > 0){
            map.put("earningExpenseDayCount",earningExpenseDayDTOList.size());
        }else {
            map.put("earningExpenseDayCount",1);
        }
    }

    @RequestMapping(value = "/getEarningExpenseDays",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getEarningExpenseDays(@RequestBody EarningExpenseDayDTO earningExpenseDayDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningExpenseDayDTO.getBeginYear();
            Integer beginMonth = earningExpenseDayDTO.getBeginMonth();
            Integer beginDay = earningExpenseDayDTO.getBeginDay();
            Integer endYear = earningExpenseDayDTO.getEndYear();
            Integer endMonth = earningExpenseDayDTO.getEndMonth();
            Integer endDay = earningExpenseDayDTO.getEndDay();
            if(beginYear == null || beginYear == 0
                    || beginMonth == null || beginMonth == 0
                    || beginDay == null || beginDay == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询开始年月日不能为空");
                return multiDataResponse;
            }
            if(endYear == null || endYear == 0
                    || endMonth == null || endMonth == 0
                    || endDay == null || endDay == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询结束年月日不能为空");
                return multiDataResponse;
            }
            BaseResponse baseResponse = ServiceUtil.checkDate(1,beginYear,beginMonth,beginDay,endYear,endMonth,endDay);
            if(baseResponse.getResponseCode() != ResponseCode.SUCCESS){
                multiDataResponse.setResponseCode(baseResponse.getResponseCode());
                multiDataResponse.setResponseDesc(baseResponse.getResponseDesc());
                return multiDataResponse;
            }
            List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayService.getEarningExpenseDays(earningExpenseDayDTO);
            multiDataResponse.setData(earningExpenseDayDTOList);
        }catch (Exception e){
            logger.error("查询每日收入支出出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/exportEarningExpenseDay",method = RequestMethod.GET)
    public void exportEarningExpenseDay(@RequestParam(value = "beginYear") Integer beginYear,
                              @RequestParam(value = "beginMonth") Integer beginMonth,
                              @RequestParam(value = "beginDay") Integer beginDay,
                              @RequestParam(value = "endYear") Integer endYear,
                              @RequestParam(value = "endMonth") Integer endMonth,
                              @RequestParam(value = "endDay") Integer endDay,
                              HttpServletResponse response){

        try {
            EarningExpenseDayDTO earningExpenseDayDTO = new EarningExpenseDayDTO();
            earningExpenseDayDTO.setBeginYear(beginYear);
            earningExpenseDayDTO.setBeginMonth(beginMonth);
            earningExpenseDayDTO.setBeginDay(beginDay);
            earningExpenseDayDTO.setEndYear(endYear);
            earningExpenseDayDTO.setEndMonth(endMonth);
            earningExpenseDayDTO.setEndDay(endDay);
            String excelName = beginYear + "年" + beginMonth + "月"+beginDay+"日到" + endYear + "年" + endMonth + "月"+endDay+"日的收入支出.xls";
            List<EarningExpenseDayDTO> earningExpenseDayDTOList = earningExpenseDayService.getAllEarningExpenseDays(earningExpenseDayDTO);
            HSSFWorkbook hssfWorkbook = getExcel(earningExpenseDayDTOList);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            hssfWorkbook.write(baos);
            byte[] data = baos.toByteArray();
            if (data != null) {
                response.reset();
                response.setContentType("application/octet-stream;charset=UTF-8");
                response.addHeader("Content-Length", "" + data.length);
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(excelName, "UTF-8"));
                OutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(data);
                out.flush();
                out.close();
            }
        }catch (Exception e){
            logger.error("导出每日收入支出出错:"+e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/searchRunEarningExpenseDayPage")
    public void searchRunEarningExpenseDayPage(Map<String,Object> map){
        map.put("yearList", ServiceUtil.getYearList());
        map.put("monthList",ServiceUtil.getMonthList());
        map.put("dayList",ServiceUtil.getDayList());
    }

    @RequestMapping(value = "/runEarningExpenseDays",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse runEarningExpenseDays(@RequestBody EarningExpenseDayDTO earningExpenseDayDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningExpenseDayDTO.getBeginYear();
            Integer beginMonth = earningExpenseDayDTO.getBeginMonth();
            Integer beginDay = earningExpenseDayDTO.getBeginDay();
            Integer endYear = earningExpenseDayDTO.getEndYear();
            Integer endMonth = earningExpenseDayDTO.getEndMonth();
            Integer endDay = earningExpenseDayDTO.getEndDay();
            if(beginYear == null || beginYear == 0
                    || beginMonth == null || beginMonth == 0
                    || beginDay == null || beginDay == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("运行开始年月日不能为空");
                return baseResponse;
            }
            if(endYear == null || endYear == 0
                    || endMonth == null || endMonth == 0
                    || endDay == null || endDay == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("运行结束年月日不能为空");
                return baseResponse;
            }
            baseResponse = ServiceUtil.checkDate(1,beginYear,beginMonth,beginDay,endYear,endMonth,endDay);
            if(baseResponse.getResponseCode() != ResponseCode.SUCCESS){
                return baseResponse;
            }
            //收入
            EarningDTO earningDTO = new EarningDTO();
            earningDTO.setBeginYear(beginYear);
            earningDTO.setBeginMonth(beginMonth);
            earningDTO.setBeginDay(beginDay);
            earningDTO.setEndYear(endYear);
            earningDTO.setEndMonth(endMonth);
            earningDTO.setEndDay(endDay);
            //支出
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setBeginYear(beginYear);
            expenseDTO.setBeginMonth(beginMonth);
            expenseDTO.setBeginDay(beginDay);
            expenseDTO.setEndYear(endYear);
            expenseDTO.setEndMonth(endMonth);
            expenseDTO.setEndDay(endDay);
            earningExpenseDayService.runEarningExpenseDay(earningDTO,expenseDTO);
            baseResponse.setResponseDesc("手动运行成功");
        }catch (Exception e){
            logger.error("手动运行每日收入支出出错:"+e);
            baseResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            baseResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return baseResponse;
    }

    /**
     * 导出方法
     * @param earningExpenseDayDTOList
     * @return
     */
    private HSSFWorkbook getExcel(List<EarningExpenseDayDTO> earningExpenseDayDTOList){
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet的名字
        String sheetName = "每日收入支出";
        HSSFSheet hssfSheet = workbook.createSheet(sheetName);
        //创建第一行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        List<String> firstRow = new ArrayList();
        firstRow.add("年");
        firstRow.add("月");
        firstRow.add("日");
        firstRow.add("收入金额");
        firstRow.add("支出金额");
        firstRow.add("创建时间");
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        HSSFFont hssfFont = workbook.createFont();
        //字体大小
        hssfFont.setFontHeightInPoints((short)11);
        //加粗
        hssfFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hssfCellStyle.setFont(hssfFont);
        //左右居中
        hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //上下居中
        hssfCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //创建单元格
        HSSFCell hssfCell = null;
        for(int i = 0;i < firstRow.size();i++){
            hssfCell = hssfRow.createCell(i);
            hssfCell.setCellValue(firstRow.get(i));
            hssfCell.setCellStyle(hssfCellStyle);
            //设置列宽，256表示每个字符大小
            hssfSheet.setColumnWidth(i,10*256);
        }
        //从第二行开始
        for(int j = 0;j < earningExpenseDayDTOList.size();j++){
            hssfRow = hssfSheet.createRow(j+1);
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            //左右居中
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            //上下居中
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            //创建单元格
            for(int k = 0;k < firstRow.size();k++) {
                hssfCell = hssfRow.createCell(k);
                hssfCell.setCellStyle(cellStyle);
                if(k == 0){
                    hssfCell.setCellValue(earningExpenseDayDTOList.get(j).getYear()+"");
                }else if (k == 1){
                    hssfCell.setCellValue(earningExpenseDayDTOList.get(j).getMonth()+"");
                }else if (k == 2){
                    hssfCell.setCellValue(earningExpenseDayDTOList.get(j).getDay());
                }else if(k == 3){
                    hssfCell.setCellValue(earningExpenseDayDTOList.get(j).getEarningDay()+"");
                }else if(k == 4){
                    hssfCell.setCellValue(earningExpenseDayDTOList.get(j).getExpenseDay()+"");
                }else if(k == 5){
                    hssfCell.setCellValue(earningExpenseDayDTOList.get(j).getCreationDateStr());
                }
            }
        }
        return workbook;
    }


    
}
