package com.kanggutang.controller;

import com.kanggutang.common.ResponseCode;
import com.kanggutang.common.ServiceUtil;
import com.kanggutang.dto.EarningExpenseYearDTO;
import com.kanggutang.response.BaseResponse;
import com.kanggutang.response.MultiDataResponse;
import com.kanggutang.service.EarningExpenseYearService;
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
 * @version 1.0 2017/5/22
 * @description
 */
@Controller
public class EarningExpenseYearController {
    
    private static Logger logger = LoggerFactory.getLogger(EarningExpenseYearController.class);
    
    @Autowired
    private EarningExpenseYearService earningExpenseYearService;

    @RequestMapping(value = "/searchEarningExpenseYearPage")
    public void searchEarningExpenseYearPage(Map<String,Object> map){
        map.put("yearList", ServiceUtil.getYearList());
        EarningExpenseYearDTO earningExpenseYearDTO = new EarningExpenseYearDTO();
        earningExpenseYearDTO.setBeginYear(ServiceUtil.getYearList().get(0));
        earningExpenseYearDTO.setEndYear(ServiceUtil.getYearList().get(0));
        earningExpenseYearDTO.setPageNum(1);
        List<EarningExpenseYearDTO> earningExpenseYearDTOList = earningExpenseYearService.getEarningExpenseYears(earningExpenseYearDTO);
        map.put("earningExpenseYearDTOList",earningExpenseYearDTOList);
        if(earningExpenseYearDTOList != null && earningExpenseYearDTOList.size() > 0){
            map.put("earningExpenseYearCount",earningExpenseYearDTOList.size());
        }else {
            map.put("earningExpenseYearCount",1);
        }
    }

    @RequestMapping(value = "/getEarningExpenseYears",method = RequestMethod.POST)
    @ResponseBody
    public MultiDataResponse getEarningExpenseYears(@RequestBody EarningExpenseYearDTO earningExpenseYearDTO){
        MultiDataResponse multiDataResponse = new MultiDataResponse();
        multiDataResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningExpenseYearDTO.getBeginYear();
            Integer endYear = earningExpenseYearDTO.getEndYear();
            if(beginYear == null || beginYear == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询开始年不能为空");
                return multiDataResponse;
            }
            if(endYear == null || endYear == 0){
                multiDataResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                multiDataResponse.setResponseDesc("查询结束年不能为空");
                return multiDataResponse;
            }
            BaseResponse baseResponse = ServiceUtil.checkDate(3,beginYear,0,0,endYear,0,0);
            if(baseResponse.getResponseCode() != ResponseCode.SUCCESS){
                multiDataResponse.setResponseCode(baseResponse.getResponseCode());
                multiDataResponse.setResponseDesc(baseResponse.getResponseDesc());
                return multiDataResponse;
            }
            List<EarningExpenseYearDTO> earningExpenseYearDTOList = earningExpenseYearService.getEarningExpenseYears(earningExpenseYearDTO);
            multiDataResponse.setData(earningExpenseYearDTOList);
        }catch (Exception e){
            logger.error("查询每年收入支出出错:"+e);
            multiDataResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            multiDataResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return multiDataResponse;
    }

    @RequestMapping(value = "/exportEarningExpenseYear",method = RequestMethod.GET)
    public void exportEarningExpenseYear(@RequestParam(value = "beginYear") Integer beginYear,
                                          @RequestParam(value = "endYear") Integer endYear,
                                          HttpServletResponse response){

        try {
            EarningExpenseYearDTO earningExpenseYearDTO = new EarningExpenseYearDTO();
            earningExpenseYearDTO.setBeginYear(beginYear);
            earningExpenseYearDTO.setEndYear(endYear);
            String excelName = beginYear + "年到" + endYear + "年的收入支出.xls";
            List<EarningExpenseYearDTO> earningExpenseDayDTOList = earningExpenseYearService.getAllEarningExpenseYears(earningExpenseYearDTO);
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
            logger.error("导出每年收入支出出错:"+e);
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/searchRunEarningExpenseYearPage")
    public void searchRunEarningExpenseYearPage(Map<String,Object> map){
        map.put("yearList", ServiceUtil.getYearList());
    }

    @RequestMapping(value = "/runEarningExpenseYears",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse runEarningExpenseYears(@RequestBody EarningExpenseYearDTO earningExpenseYearDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer beginYear = earningExpenseYearDTO.getBeginYear();
            Integer endYear = earningExpenseYearDTO.getEndYear();
            if(beginYear == null || beginYear == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("运行开始年月不能为空");
                return baseResponse;
            }
            if(endYear == null || endYear == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("运行结束年月不能为空");
                return baseResponse;
            }
            baseResponse = ServiceUtil.checkDate(1,beginYear,0,0,endYear,0,0);
            if(baseResponse.getResponseCode() != ResponseCode.SUCCESS){
                return baseResponse;
            }
            earningExpenseYearService.runEarningExpenseYear(earningExpenseYearDTO);
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
     * @param earningExpenseYearDTOList
     * @return
     */
    private HSSFWorkbook getExcel(List<EarningExpenseYearDTO> earningExpenseYearDTOList){
        HSSFWorkbook workbook = new HSSFWorkbook();
        //sheet的名字
        String sheetName = "每年收入支出";
        HSSFSheet hssfSheet = workbook.createSheet(sheetName);
        //创建第一行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        List<String> firstRow = new ArrayList();
        firstRow.add("年");
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
        for(int j = 0;j < earningExpenseYearDTOList.size();j++){
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
                    hssfCell.setCellValue(earningExpenseYearDTOList.get(j).getYear()+"");
                }else if (k == 1){
                    hssfCell.setCellValue(earningExpenseYearDTOList.get(j).getEarningYear()+"");
                }else if(k ==  2){
                    hssfCell.setCellValue(earningExpenseYearDTOList.get(j).getExpenseYear()+"");
                }else if ((k == 3)){
                    hssfCell.setCellValue(earningExpenseYearDTOList.get(j).getCreationDateStr());
                }
            }
        }
        return workbook;
    }
    
}
