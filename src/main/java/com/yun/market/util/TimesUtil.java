package com.yun.market.util;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;


public class TimesUtil {

    /**
     * 当前时间转化为字符串
     *
     * @param date
     * @return
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        String str = format.format(date);

        return str;
    }

    /**
     * 返回三天前的时间
     *
     * @param date
     * @return
     */
    public static String oldThreeToFormart(Date date,int days) {

        Calendar calendar = new GregorianCalendar();

        calendar.setTime(date);

        calendar.add(calendar.DATE, days); //把日期往后增加一天,整数  往后推,负数往前移动

        date = calendar.getTime(); //这个时间就是日期往后推一天的结果

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String str = format.format(date);

        return str;
    }

    /**
     * 当前时间转化为字符串
     *
     * @param date
     * @return
     */
    public static String DateToStrOn(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String str = format.format(date);

        return str;
    }

    /**
     * 返回时间分时秒
     *
     * @param date
     * @return
     */
    public static String dateToFormart(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String str = format.format(date);

        return str;
    }

    /**
     * 获取下一天的日期
     * @return
     */
    public static Date dateAddOne() {

        Date date = new Date();

        Calendar calendar = new GregorianCalendar();

        calendar.setTime(date);

        calendar.add(calendar.DATE, 1); //把日期往后增加一天,整数  往后推,负数往前移动

        date = calendar.getTime(); //这个时间就是日期往后推一天的结果

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String str = format.format(date);

        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static long getCurrentTimeMillis() {//use 188

        Date date = new Date();

        Calendar calendar = Calendar.getInstance();//new一个Calendar类,把Date放进去

        calendar.setTime(date);

        calendar.add(Calendar.DATE, 0);

        long currentTime = calendar.getTimeInMillis() / 1000;

        return currentTime;
    }

    /**
     * 获取今天的时间戳
     *
     * @return
     */
    public static long getTodayCurrentTimeMillis() {//use 188

        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String dateString = formatter.format(date);

        dateString = dateString + " 00:00:00";

        Calendar calendar = Calendar.getInstance();//new一个Calendar类,把Date放进去

        calendar.setTime(strToDateLong(dateString));

        calendar.add(Calendar.DATE, 0);

        long currentTime = calendar.getTimeInMillis() / 1000;

        return currentTime;
    }

    /**
     * 将String时间转化为Date时间戳
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        ParsePosition pos = new ParsePosition(0);

        Date strtodate = formatter.parse(strDate, pos);

        return strtodate;
    }

    /**
     * cron表达式转为日期
     *
     * @param cron
     * @return
     */
    public static List<Date> getCronToDate(String cron) {

        CronDefinition definition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);

        CronParser parser = new CronParser(definition);

        ZonedDateTime now = ZonedDateTime.now();

        List<Date> data = new ArrayList<>();

        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));

        for (int i = 0; i < 86400; i++) {

            Optional<ZonedDateTime> next = executionTime.nextExecution(now);

            now = next.get();

            Date date = Date.from(now.toInstant());

            Date addOnedate = dateAddOne();

            if (date.after(addOnedate)||date.equals(addOnedate)){
                break;
            }

            data.add(date);
        }

        return data;

    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_Date = sdf.format(new Date(Long.valueOf(s) * 1000L));
        return time_Date;
    }

}
