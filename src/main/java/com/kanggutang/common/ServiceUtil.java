package com.kanggutang.common;

import com.kanggutang.response.BaseResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class ServiceUtil {

    /**
     * 把以‘,’号分隔的区域转换为区域数组
     * 如把"重庆#北京#上海"转换为一个列表，包含三个元素，重庆、北京、上海
     *
     * @param areas 形如"重庆#北京#上海"的字符串
     * @return 链表
     */
    public static List<String> getAreas(String areas) {
        if (isEmpty(areas)) {
            return null;
        }
        List<String> areasList = null;
        String[] areaArray = areas.split(",");
        if (areaArray.length > 0) {
            areasList = new ArrayList<String>();
            for (String s : areaArray) {
                areasList.add(s);
            }
        }
        return areasList;
    }

    /**
     * 把以‘,’号分隔的区域转换为数据库Province In (?) 中问号中应有的形式
     *
     * @param areas
     * @return
     */
    public static String getAreasAsString(String areas) {
        if (isEmpty(areas)) {
            return null;
        }

        StringBuilder builder = new StringBuilder(256);
        String[] areaArray = areas.split(",");
        boolean bFirst = true;
        for (String s : areaArray) {
            if (!bFirst) {
                builder.append(",");
            }
            builder.append("\'");
            builder.append(s);
            builder.append("\'");
            bFirst = false;
        }
        return builder.toString();
    }

    public static boolean isEmpty(String str){
        if (str ==null ||"".equals(str))
            return true;
        return false;
    }

    /**
     * 生成随机字符串
     * @param length 表示生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@$%^*()";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static Date getCurrentDay(){
        Calendar day=Calendar.getInstance();
        day.set(Calendar.HOUR_OF_DAY,0);
        day.set(Calendar.MINUTE,0);
        day.set(Calendar.SECOND,0);
        day.set(Calendar.MILLISECOND,0);
        return day.getTime();
    }


    public static String CreateAccessToken(String loginName,String timestamp){
        String[] paramArr = new String[]{loginName,timestamp};
        Arrays.sort(paramArr);
        String content = paramArr[0].concat(paramArr[1]);
        String access_token = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.toString().getBytes());
            access_token = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return access_token;
    }

    private static String byteToStr(byte[] byteArray){
        String strDigest="";
        for(int i=0;i<byteArray.length;i++){
            strDigest +=byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte mByte){
        char[] Digit = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    public static List<Integer> getYearList(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        List<Integer> yearList = new ArrayList<Integer>();
        yearList.add(calendar.get(Calendar.YEAR));
        yearList.add(calendar.get(Calendar.YEAR)-1);
        yearList.add(calendar.get(Calendar.YEAR)-2);
        yearList.add(calendar.get(Calendar.YEAR)-3);
        return yearList;
    }

    public static List<Integer> getMonthList(){
        List<Integer> monthList = new ArrayList<Integer>();
        monthList.add(1);
        monthList.add(2);
        monthList.add(3);
        monthList.add(4);
        monthList.add(5);
        monthList.add(6);
        monthList.add(7);
        monthList.add(8);
        monthList.add(9);
        monthList.add(10);
        monthList.add(11);
        monthList.add(12);
        return monthList;
    }

    public static List<Integer> getDayList(){
        List<Integer> dayList = new ArrayList<Integer>();
        dayList.add(1);
        dayList.add(2);
        dayList.add(3);
        dayList.add(4);
        dayList.add(5);
        dayList.add(6);
        dayList.add(7);
        dayList.add(8);
        dayList.add(9);
        dayList.add(10);
        dayList.add(11);
        dayList.add(12);
        dayList.add(13);
        dayList.add(14);
        dayList.add(15);
        dayList.add(16);
        dayList.add(17);
        dayList.add(18);
        dayList.add(19);
        dayList.add(20);
        dayList.add(21);
        dayList.add(22);
        dayList.add(23);
        dayList.add(24);
        dayList.add(25);
        dayList.add(26);
        dayList.add(27);
        dayList.add(28);
        dayList.add(29);
        dayList.add(30);
        dayList.add(31);
        return dayList;
    }

    /**
     *
     * @param checkType 1--日2--月3--年
     * @param beginYear
     * @param beginMonth
     * @param beginDay
     * @param endYear
     * @param endMonth
     * @param endDay
     * @return
     */
    public static BaseResponse checkDate(Integer checkType, Integer beginYear, Integer beginMonth, Integer beginDay, Integer endYear, Integer endMonth, Integer endDay){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);

        if(beginYear > endYear){
            baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
            baseResponse.setResponseDesc("查询开始年份不能大于结束年份");
            return baseResponse;
        }
        if(checkType == 1 || checkType == 2){
            if(beginMonth > endMonth){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("查询开始月份不能大于结束月份");
                return baseResponse;
            }
        }
        if(checkType == 1){
            if(beginDay > endDay){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("查询开始日期不能大于结束日期");
                return baseResponse;
            }
        }
        return baseResponse;
    }
}
