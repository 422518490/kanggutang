package com.kanggutang.common;

import java.util.Random;


public class RandCodeUtil {


    //随机密码字符数组
    private static char[] passwordSequence = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    //验证码字符数组
    private static char[] smsCodeSequence = "0123456789".toCharArray();

    private static Random random = new Random();

    // 随机生成一个字符
    private static String getRandomChar(char[] charSequence) {
        int index = random.nextInt(charSequence.length);
        return String.valueOf(charSequence[index]);
    }


    // 生成随机密码
    public static String getRandomPwd(int pwdNum){
        StringBuilder sRand = new StringBuilder(pwdNum);
        for (int i = 0; i < pwdNum; i++) {
            // 取得一个随机字符
            String tmp = getRandomChar(passwordSequence);
            sRand.append(tmp);
        }
        return sRand.toString();
    }
}

