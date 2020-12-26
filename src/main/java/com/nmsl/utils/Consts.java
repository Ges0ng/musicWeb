package com.nmsl.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *  常量
 */
public class Consts {
    /* 登录名 */
    public static final String NAME = "name";
    /* 返回码 */
    public static final String CODE = "code";
    /* 返回信息 */
    public static final String MSG = "msg";



    public static final String TIMETWO(){
        GregorianCalendar ca = new GregorianCalendar();
        //结果为“0”是上午     结果为“1”是下午
        int x = ca.get(GregorianCalendar.AM_PM);
        return x==0?"上午好, ":"下午好, ";
    }

    public static final String TIMETHREE() {
        Date date = new Date();
        String str = new SimpleDateFormat("HH").format(date);
        int a = Integer.parseInt(str);

        if (a >= 0 && a <= 6) {
            str = "晚上好， ";
        }
        if (a > 6 && a <= 11) {
            str = "早上好， ";
        }
        if (a > 11 && a <= 13) {
            str = "中午好， ";
        }
        if (a > 13 && a <= 18) {
            str = "下午好， ";
        }
        if (a > 18 && a <= 24) {
            str = "晚上好， ";
        }
        return str;
    }


}
