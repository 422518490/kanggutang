package com.kanggutang.controller;

import com.kanggutang.common.ResponseCode;
import com.kanggutang.common.ServiceUtil;
import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.dto.ExpenseDTO;
import com.kanggutang.response.BaseResponse;
import com.kanggutang.response.MultiDataResponse;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import com.kanggutang.service.ExpenseService;
import org.apache.poi.hssf.usermodel.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
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
public class ExpenseController {

    private static Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private BaseSubCategoriesDataInfoService baseSubCategoriesDataInfoService;

    @RequestMapping("/addExpensePage")
    public void addExpensePage(Map<String,Object> map){
        BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO = new BaseSubCategoriesDataInfoDTO();
        baseSubCategoriesDataInfoDTO.setCategoriesName("EXPENSE_TYPE");
        List<BaseSubCategoriesDataInfoDTO> expenseTypeList = baseSubCategoriesDataInfoService.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
        map.put("expenseTypeList",expenseTypeList);
    }

    @RequestMapping(value = "/addExpense",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse addExpense(@RequestBody ExpenseDTO expenseDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer userId = expenseDTO.getUserId();
            Integer expenseType = expenseDTO.getExpenseType();
            BigDecimal expenseAmount = expenseDTO.getExpenseAmount();
            if(userId == null || userId == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("请登录后再做新增");
                return baseResponse;
            }
            if(expenseType == null || expenseType == 0){
                baseResponse.setResponseCode(ResponseCode.SUCCESS);
                baseResponse.setResponseDesc("支出类型不能为空");
                return baseResponse;
            }
            if(expenseAmount == null || expenseAmount.compareTo(new BigDecimal(0)) == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("支出金额不能为空");
                return baseResponse;
            }
            expenseService.addExpense(expenseDTO);
            baseResponse.setResponseDesc("新增支出成功");
            logger.info("新增支出:"+ JSONObject.valueToString(expenseDTO));
        }catch (Exception e){
            baseResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            baseResponse.setResponseDesc("服务器错误");
            logger.error("新增支出错误:"+e);
            e.printStackTrace();
        }
        return baseResponse;
    }

    @RequestMapping(value = "/searchExpensePage")
    public void searchExpensePage(Map<String,Object> map){
        map.put("yearList", ServiceUtil.getYearList());
        map.put("monthList",ServiceUtil.getMonthList());
        map.put("dayList",ServiceUtil.getDayList());
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        expenseDTO.setBeginMonth(1);
        expenseDTO.setBeginDay(1);
        expenseDTO.setEndYear(ServiceUtil.getYearList().get(0));
        expenseDTO.setEndMonth(1);
        expenseDTO.setEndDay(1);
        expenseDTO.setPageNum(1);
        List<ExpenseDTO> expenseDTOList = expenseService.getExpenses(expenseDTO);
        map.put("expenseDTOList",expenseDTOList);
        if(expenseDTOList != null && expenseDTOList.size() > 0){
            map.put("expenseCount",expenseDTOList.size());
        }else {
            map.put("expenseCount",1);
        }
    }

    @RequestMapping(value = "/getExpenses",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getExpenses(@RequestBody ExpenseDTO expenseDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = expenseDTO.getBeginYear();
            Integer beginMonth = expenseDTO.getBeginMonth();
            Integer beginDay = expenseDTO.getBeginDay();
            Integer endYear = expenseDTO.getEndYear();
            Integer endMonth = expenseDTO.getEndMonth();
            Integer endDay = expenseDTO.getEndDay();
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
            multiDataResponse.setResponseCode(baseResponse.getResponseCode());
            multiDataResponse.setResponseDesc(baseResponse.getResponseDesc());
            if(multiDataResponse.getResponseCode() != ResponseCode.SUCCESS){
                return multiDataResponse;
            }
            List<ExpenseDTO> expenseDTOList = expenseService.getExpenses(expenseDTO);
            multiDataResponse.setData(expenseDTOList);
        }catch (Exception e){
            logger.error("查询支出出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/exportExpense",method = RequestMethod.GET)
    public void exportExpense(@RequestParam(value = "beginYear") Integer beginYear,
                              @RequestParam(value = "beginMonth") Integer beginMonth,
                              @RequestParam(value = "beginDay") Integer beginDay,
                              @RequestParam(value = "endYear") Integer endYear,
                              @RequestParam(value = "endMonth") Integer endMonth,
                              @RequestParam(value = "endDay") Integer endDay,
                              HttpServletResponse response){

        try {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setBeginYear(beginYear);
            expenseDTO.setBeginMonth(beginMonth);
            expenseDTO.setBeginDay(beginDay);
            expenseDTO.setEndYear(endYear);
            expenseDTO.setEndMonth(endMonth);
            expenseDTO.setEndDay(endDay);
            String excelName = beginYear + "年" + beginMonth + "月"+beginDay+"日到" + endYear + "年" + endMonth + "月"+endDay+"日的支出.xls";
            List<ExpenseDTO> expenseDTOList = expenseService.getAllExpenses(expenseDTO);
            HSSFWorkbook hssfWorkbook = getExcel(expenseDTOList,1);
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
            logger.error("导出收入出错:"+e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/searchMonthExpensePage")
    public void searchMonthExpensePage(Map<String,Object> map){
        map.put("yearList",ServiceUtil.getYearList());
        map.put("monthList",ServiceUtil.getMonthList());
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        expenseDTO.setBeginMonth(1);
        expenseDTO.setEndYear(ServiceUtil.getYearList().get(0));
        expenseDTO.setEndMonth(1);
        expenseDTO.setPageNum(1);
        List<ExpenseDTO> expenseDTOList = expenseService.getMonthExpense(expenseDTO);
        map.put("expenseDTOList",expenseDTOList);
        if(expenseDTOList != null && expenseDTOList.size() > 0){
            map.put("expenseCount",expenseDTOList.size());
        }else {
            map.put("expenseCount",1);
        }
    }

    @RequestMapping(value = "/getMonthExpenses",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getMonthEarnings(@RequestBody ExpenseDTO expenseDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = expenseDTO.getBeginYear();
            Integer beginMonth = expenseDTO.getBeginMonth();
            Integer endYear = expenseDTO.getEndYear();
            Integer endMonth = expenseDTO.getEndMonth();
            if(beginYear == null || beginYear == 0 || beginMonth == null || beginMonth == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询开始年月也不能为空");
                return multiDataResponse;
            }
            if(endYear == null || endYear == 0 || endMonth == null || endMonth == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询结束年月也不能为空");
                return multiDataResponse;
            }
            BaseResponse baseResponse = ServiceUtil.checkDate(2,beginYear,beginMonth,0,endYear,endMonth,0);;
            multiDataResponse.setResponseCode(baseResponse.getResponseCode());
            multiDataResponse.setResponseDesc(baseResponse.getResponseDesc());
            if(multiDataResponse.getResponseCode() != ResponseCode.SUCCESS){
                return multiDataResponse;
            }
            List<ExpenseDTO> earningDTOList = expenseService.getMonthExpense(expenseDTO);
            multiDataResponse.setData(earningDTOList);
        }catch (Exception e){
            logger.error("查询月支出出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/exportMonthExpense",method = RequestMethod.GET)
    public void exportMonthExpense(@RequestParam(value = "beginYear") Integer beginYear,
                                   @RequestParam(value = "beginMonth") Integer beginMonth,
                                   @RequestParam(value = "endYear") Integer endYear,
                                   @RequestParam(value = "endMonth") Integer endMonth,
                                   HttpServletResponse response){

        try {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setBeginYear(beginYear);
            expenseDTO.setBeginMonth(beginMonth);
            expenseDTO.setEndYear(endYear);
            expenseDTO.setEndMonth(endMonth);
            String excelName = beginYear + "年" + beginMonth + "月到" + endYear + "年" + endMonth + "月的月支出.xls";
            List<ExpenseDTO> expenseDTOList = expenseService.getAllMonthExpense(expenseDTO);
            HSSFWorkbook hssfWorkbook = getExcel(expenseDTOList,2);
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
            logger.error("导出月支出出错:"+e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/searchYearExpensePage")
    public void searchYearExpensePage(Map<String,Object> map){
        map.put("yearList",ServiceUtil.getYearList());
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        expenseDTO.setEndYear(ServiceUtil.getYearList().get(0));
        expenseDTO.setPageNum(1);
        List<ExpenseDTO> expenseDTOList = expenseService.getYearExpense(expenseDTO);
        map.put("expenseDTOList",expenseDTOList);
        if(expenseDTOList != null && expenseDTOList.size() > 0){
            map.put("expenseCount",expenseDTOList.size());
        }else {
            map.put("expenseCount",1);
        }
    }

    @RequestMapping(value = "/getYearExpenses",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getYearExpenses(@RequestBody ExpenseDTO expenseDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = expenseDTO.getBeginYear();
            Integer endYear = expenseDTO.getEndYear();
            if(beginYear == null || beginYear == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询开始年份也不能为空");
                return multiDataResponse;
            }
            if(endYear == null || endYear == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询结束年份也不能为空");
                return multiDataResponse;
            }
            BaseResponse baseResponse = ServiceUtil.checkDate(3,beginYear,0,0,endYear,0,0);
            multiDataResponse.setResponseCode(baseResponse.getResponseCode());
            multiDataResponse.setResponseDesc(baseResponse.getResponseDesc());
            if(multiDataResponse.getResponseCode() != ResponseCode.SUCCESS){
                return multiDataResponse;
            }
            List<ExpenseDTO> expenseDTOList = expenseService.getYearExpense(expenseDTO);
            multiDataResponse.setData(expenseDTOList);
        }catch (Exception e){
            logger.error("查询年支出出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/exportYearExpense",method = RequestMethod.GET)
    public void exportYearExpense(@RequestParam(value = "beginYear") Integer beginYear,
                                  @RequestParam(value = "endYear") Integer endYear,
                                  HttpServletResponse response){

        try {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setBeginYear(beginYear);
            expenseDTO.setEndYear(endYear);
            String excelName = beginYear + "年" + "到" + endYear + "年" + "的年支出.xls";
            List<ExpenseDTO> expenseDTOList = expenseService.getAllYearExpense(expenseDTO);
            HSSFWorkbook hssfWorkbook = getExcel(expenseDTOList,3);
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
            logger.error("导出年支出出错:"+e);
            e.printStackTrace();
        }
    }

    /**
     * 导出方法
     * @param expenseDTOList
     * @param exportType 1--明细导出2--月导出3--年导出
     * @return
     */
    private HSSFWorkbook getExcel(List<ExpenseDTO> expenseDTOList,Integer exportType){
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet的名字
        String sheetName = "明细支出";
        HSSFSheet hssfSheet = workbook.createSheet(sheetName);
        //创建第一行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        List<String> firstRow = new ArrayList();
        firstRow.add("支出类型");
        firstRow.add("支出金额");
        firstRow.add("备注");
        firstRow.add("创建时间");
        if(exportType == 2){
            firstRow.clear();
            sheetName = "月支出";
            firstRow.add("年份");
            firstRow.add("月份");
            firstRow.add("支出金额");
        }else if (exportType ==3){
            firstRow.clear();
            sheetName = "年支出";
            firstRow.add("年份");
            firstRow.add("支出金额");
        }
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
        for(int j = 0;j < expenseDTOList.size();j++){
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
                if(exportType == 1){
                    if(k == 0){
                        hssfCell.setCellValue(expenseDTOList.get(j).getExpenseName());
                    }else if (k == 1){
                        hssfCell.setCellValue(expenseDTOList.get(j).getExpenseAmount()+"");
                    }else if (k == 2){
                        hssfCell.setCellValue(expenseDTOList.get(j).getRemark());
                    }else {
                        hssfCell.setCellValue(expenseDTOList.get(j).getCreationDateStr());
                    }
                }else if (exportType == 2){
                    if(k == 0){
                        hssfCell.setCellValue(expenseDTOList.get(j).getYear());
                    }else if (k == 1){
                        hssfCell.setCellValue(expenseDTOList.get(j).getMonth());
                    }else {
                        hssfCell.setCellValue(expenseDTOList.get(j).getExpenseAmount()+"");
                    }
                }else if(exportType == 3){
                    if (k == 0){
                        hssfCell.setCellValue(expenseDTOList.get(j).getYear());
                    }else {
                        hssfCell.setCellValue(expenseDTOList.get(j).getExpenseAmount()+"");
                    }
                }
            }
        }
        return workbook;
    }
}
