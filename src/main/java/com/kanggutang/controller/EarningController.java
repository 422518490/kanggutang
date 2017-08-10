package com.kanggutang.controller;

import com.kanggutang.common.ResponseCode;
import com.kanggutang.common.ServiceUtil;
import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.dto.EarningDTO;
import com.kanggutang.model.Earning;
import com.kanggutang.response.BaseResponse;
import com.kanggutang.response.MultiDataResponse;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import com.kanggutang.service.EarningService;
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
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/16
 * @description
 */
@Controller
public class EarningController {

    private static Logger logger = LoggerFactory.getLogger(EarningController.class);

    @Autowired
    private BaseSubCategoriesDataInfoService baseSubCategoriesDataInfoService;
    @Autowired
    private EarningService earningService;


    @RequestMapping(value = "/addEarningPage")
    public void addEarningPage(Map<String,Object> map){
        BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO = new BaseSubCategoriesDataInfoDTO();
        baseSubCategoriesDataInfoDTO.setCategoriesName("EARNING_TYPE");
        List<BaseSubCategoriesDataInfoDTO> earningTypeList = baseSubCategoriesDataInfoService.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
        map.put("earningTypeList",earningTypeList);
    }

    @RequestMapping(value = "/addEarning",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse addEarning(@RequestBody EarningDTO earningDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            if(earningDTO.getUserId() == null || earningDTO.getUserId() == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("请登录后再做新增");
                return baseResponse;
            }
            if(earningDTO.getEarningType() == null ||earningDTO.getEarningType() == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("收入类型不能为空");
                return baseResponse;
            }
            if(earningDTO.getEarningAmount() == null || earningDTO.getEarningAmount().compareTo(new BigDecimal(0)) == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("收入金额不能为空");
                return baseResponse;
            }
            earningService.addEarning(earningDTO);
            baseResponse.setResponseDesc("新增收入成功");
            logger.info("新增收入:"+ JSONObject.valueToString(earningDTO));
        }catch (Exception e){
            logger.error("新增收入出错:"+e);
            baseResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            baseResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return baseResponse;
    }

    @RequestMapping(value = "/searchEarningPage")
    public void searchEarningPage(Map<String,Object> map){
        map.put("yearList", ServiceUtil.getYearList());
        map.put("monthList",ServiceUtil.getMonthList());
        map.put("dayList",ServiceUtil.getDayList());
        EarningDTO earningDTO = new EarningDTO();
        earningDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        earningDTO.setBeginMonth(1);
        earningDTO.setBeginDay(1);
        earningDTO.setEndYear(ServiceUtil.getYearList().get(0));
        earningDTO.setEndMonth(1);
        earningDTO.setEndDay(1);
        earningDTO.setPageNum(1);
        List<EarningDTO> earningDTOList = earningService.getEarnings(earningDTO);
        map.put("earningDTOList",earningDTOList);
        if(earningDTOList != null && earningDTOList.size() > 0){
            map.put("earningCount",earningDTOList.size());
        }else {
            map.put("earningCount",1);
        }
    }

    @RequestMapping(value = "/getEarnings",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getEarnings(@RequestBody EarningDTO earningDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningDTO.getBeginYear();
            Integer beginMonth = earningDTO.getBeginMonth();
            Integer beginDay = earningDTO.getBeginDay();
            Integer endYear = earningDTO.getEndYear();
            Integer endMonth = earningDTO.getEndMonth();
            Integer endDay = earningDTO.getEndDay();
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
            List<EarningDTO> earningDTOList = earningService.getEarnings(earningDTO);
            multiDataResponse.setData(earningDTOList);
        }catch (Exception e){
            logger.error("查询收入出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/searchMonthEarningPage")
    public void searchMonthEarningPage(Map<String,Object> map){
        map.put("yearList",ServiceUtil.getYearList());
        map.put("monthList",ServiceUtil.getMonthList());
        EarningDTO earningDTO = new EarningDTO();
        earningDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        earningDTO.setBeginMonth(1);
        earningDTO.setEndYear(ServiceUtil.getYearList().get(0));
        earningDTO.setEndMonth(1);
        earningDTO.setPageNum(1);
        List<EarningDTO> earningDTOList = earningService.getMonthEarning(earningDTO);
        map.put("earningDTOList",earningDTOList);
        if(earningDTOList != null && earningDTOList.size() > 0){
            map.put("earningCount",earningDTOList.size());
        }else {
            map.put("earningCount",1);
        }
    }

    @RequestMapping(value = "/getMonthEarnings",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getMonthEarnings(@RequestBody EarningDTO earningDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningDTO.getBeginYear();
            Integer beginMonth = earningDTO.getBeginMonth();
            Integer endYear = earningDTO.getEndYear();
            Integer endMonth = earningDTO.getEndMonth();
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
            BaseResponse baseResponse = ServiceUtil.checkDate(2,beginYear,beginMonth,0,endYear,endMonth,0);
            multiDataResponse.setResponseCode(baseResponse.getResponseCode());
            multiDataResponse.setResponseDesc(baseResponse.getResponseDesc());
            if(multiDataResponse.getResponseCode() != ResponseCode.SUCCESS){
                return multiDataResponse;
            }
            List<EarningDTO> earningDTOList = earningService.getMonthEarning(earningDTO);
            multiDataResponse.setData(earningDTOList);
        }catch (Exception e){
            logger.error("查询月收入出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/searchYearEarningPage")
    public void searchYearEarningPage(Map<String,Object> map){
        map.put("yearList",ServiceUtil.getYearList());
        EarningDTO earningDTO = new EarningDTO();
        earningDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        earningDTO.setEndYear(ServiceUtil.getYearList().get(0));
        earningDTO.setPageNum(1);
        List<EarningDTO> earningDTOList = earningService.getYearEarning(earningDTO);
        map.put("earningDTOList",earningDTOList);
        if(earningDTOList != null && earningDTOList.size() > 0){
            map.put("earningCount",earningDTOList.size());
        }else {
            map.put("earningCount",1);
        }
    }

    @RequestMapping(value = "/getYearEarnings",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getYearEarnings(@RequestBody EarningDTO earningDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningDTO.getBeginYear();
            Integer endYear = earningDTO.getEndYear();
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
            List<EarningDTO> earningDTOList = earningService.getYearEarning(earningDTO);
            multiDataResponse.setData(earningDTOList);
        }catch (Exception e){
            logger.error("查询年收入出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/exportEarning",method = RequestMethod.GET)
    public void exportEarning(@RequestParam(value = "beginYear") Integer beginYear,
                              @RequestParam(value = "beginMonth") Integer beginMonth,
                              @RequestParam(value = "beginDay") Integer beginDay,
                              @RequestParam(value = "endYear") Integer endYear,
                              @RequestParam(value = "endMonth") Integer endMonth,
                              @RequestParam(value = "endDay") Integer endDay,
                              HttpServletResponse response){

        try {
            EarningDTO earningDTO = new EarningDTO();
            earningDTO.setBeginYear(beginYear);
            earningDTO.setBeginMonth(beginMonth);
            earningDTO.setBeginDay(beginDay);
            earningDTO.setEndYear(endYear);
            earningDTO.setEndMonth(endMonth);
            earningDTO.setEndDay(endDay);
            String excelName = beginYear + "年" + beginMonth + "月"+beginDay+"日到" + endYear + "年" + endMonth + "月"+endDay+"日的收入.xls";
            List<EarningDTO> earningDTOList = earningService.getAllEarnings(earningDTO);
            HSSFWorkbook hssfWorkbook = getExcel(earningDTOList,1);
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

    @RequestMapping(value = "/exportMonthEarning",method = RequestMethod.GET)
    public void exportMonthEarning(@RequestParam(value = "beginYear") Integer beginYear,
                                   @RequestParam(value = "beginMonth") Integer beginMonth,
                                   @RequestParam(value = "endYear") Integer endYear,
                                   @RequestParam(value = "endMonth") Integer endMonth,
                                   HttpServletResponse response){

        try {
            EarningDTO earningDTO = new EarningDTO();
            earningDTO.setBeginYear(beginYear);
            earningDTO.setBeginMonth(beginMonth);
            earningDTO.setEndYear(endYear);
            earningDTO.setEndMonth(endMonth);
            String excelName = beginYear + "年" + beginMonth + "月到" + endYear + "年" + endMonth + "月的月收入.xls";
            List<EarningDTO> earningDTOList = earningService.getAllMonthEarning(earningDTO);
            HSSFWorkbook hssfWorkbook = getExcel(earningDTOList,2);
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
            logger.error("导出月收入出错:"+e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/exportYearEarning",method = RequestMethod.GET)
    public void exportYearEarning(@RequestParam(value = "beginYear") Integer beginYear,
                                   @RequestParam(value = "endYear") Integer endYear,
                                   HttpServletResponse response){

        try {
            EarningDTO earningDTO = new EarningDTO();
            earningDTO.setBeginYear(beginYear);
            earningDTO.setEndYear(endYear);
            String excelName = beginYear + "年" + "到" + endYear + "年" + "的年收入.xls";
            List<EarningDTO> earningDTOList = earningService.getAllYearEarning(earningDTO);
            HSSFWorkbook hssfWorkbook = getExcel(earningDTOList,3);
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
            logger.error("导出年收入出错:"+e);
            e.printStackTrace();
        }
    }



    /**
     * 导出方法
     * @param earningDTOList
     * @param exportType 1--明细导出2--月导出3--年导出
     * @return
     */
    private HSSFWorkbook getExcel(List<EarningDTO> earningDTOList,Integer exportType){
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet的名字
        String sheetName = "明细收入";
        HSSFSheet hssfSheet = workbook.createSheet(sheetName);
        //创建第一行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        List<String> firstRow = new ArrayList();
        firstRow.add("收入类型");
        firstRow.add("收入金额");
        firstRow.add("备注");
        firstRow.add("创建时间");
        if(exportType == 2){
            firstRow.clear();
            sheetName = "月收入";
            firstRow.add("年份");
            firstRow.add("月份");
            firstRow.add("收入金额");
        }else if (exportType ==3){
            firstRow.clear();
            sheetName = "年收入";
            firstRow.add("年份");
            firstRow.add("收入金额");
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
        for(int j = 0;j < earningDTOList.size();j++){
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
                        hssfCell.setCellValue(earningDTOList.get(j).getEarningName());
                    }else if (k == 1){
                        hssfCell.setCellValue(earningDTOList.get(j).getEarningAmount()+"");
                    }else if (k == 2){
                        hssfCell.setCellValue(earningDTOList.get(j).getRemark());
                    }else {
                        hssfCell.setCellValue(earningDTOList.get(j).getCreationDateStr());
                    }
                }else if (exportType == 2){
                    if(k == 0){
                        hssfCell.setCellValue(earningDTOList.get(j).getYear());
                    }else if (k == 1){
                        hssfCell.setCellValue(earningDTOList.get(j).getMonth());
                    }else {
                        hssfCell.setCellValue(earningDTOList.get(j).getEarningAmount()+"");
                    }
                }else if(exportType == 3){
                    if (k == 0){
                        hssfCell.setCellValue(earningDTOList.get(j).getYear());
                    }else {
                        hssfCell.setCellValue(earningDTOList.get(j).getEarningAmount()+"");
                    }
                }
            }
        }
        return workbook;
    }
}
