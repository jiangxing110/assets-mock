package com.qbit.assets.common.utils;


import com.qbit.assets.common.error.CustomException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author klover
 */
@Slf4j
public class DateUtil {
    /**
     * 缺省格式
     */
    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 一天的毫秒数
     */
    private static final long ONE_DAY_TIME = 24 * 60 * 60 * 1000;
    /**
     * 缺省实例
     */
    private static final DateUtil INSTANCE = new DateUtil();
    /**
     * 年月日
     */
    public static final SimpleDateFormat DATE_FORMAT_0 = new SimpleDateFormat("yy/MM/dd");
    /**
     * 年月日
     */
    public static final SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 年月
     */
    public static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyyMM");
    /**
     * 年月日时分秒
     */
    public static final SimpleDateFormat DATE_FORMAT_3 = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final SimpleDateFormat DATE_FORMAT_4 = new SimpleDateFormat(PATTERN);
    /**
     * 年月日时分秒毫秒
     */
    public static final SimpleDateFormat DATE_FORMAT_5 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    /**
     * 年月日时
     */
    public static final SimpleDateFormat DATE_FORMAT_6 = new SimpleDateFormat("yyyy-MM-dd_HH");
    /**
     * yyyyMMdd
     */
    public static final SimpleDateFormat DATE_FORMAT_7 = new SimpleDateFormat("yyyyMMdd");
    /**
     * yyyy-MM
     */
    public static final SimpleDateFormat DATE_FORMAT_8 = new SimpleDateFormat("yyyy-MM");
    /**
     * HH:mm:ss
     */
    public static final SimpleDateFormat DATE_FORMAT_9 = new SimpleDateFormat("HH:mm:ss");
    /**
     * 年月日
     */
    public static final SimpleDateFormat DATE_FORMAT_10 = new SimpleDateFormat(PATTERN);

    /**
     * 将字符串转换为毫秒数
     *
     * @param dateStr 对应格式的时间字符串
     * @return 毫秒数
     */
    public static Long dateStrToLong(String dateStr) {
        long timeStart;
        try {
            timeStart = DATE_FORMAT_4.parse(dateStr).getTime();
        } catch (ParseException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        return timeStart;
    }

    /**
     * 将毫秒数转换为可显示的字符串
     *
     * @param dateLong 毫秒数
     * @return 对应格式的时间字符串
     */
    public static String dateLongToString(Long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        Date date = new Date(dateLong);
        return sdf.format(date);
    }

    /**
     * 当前时间加/减几天
     * 加为正数，减为负数
     *
     * @param startTime
     * @return java.util.Date
     * @author martinjiang
     * @version 1.0
     * @date 2022/4/19 17:15
     */
    public static Date dateAddSub(Date startTime, Integer day) {
        return dateAddSub(startTime, day, Calendar.DATE);
    }

    /**
     * 当前时间加/减几天
     * 加为正数，减为负数
     *
     * @param startTime  时间
     * @param number     数
     * @param offsetType 时间偏移类型
     * @return
     */
    public static Date dateAddSub(Date startTime, Integer number, int offsetType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(offsetType, number);
        return calendar.getTime();
    }

    public static Date parseTime(String dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            return formatter.parse(dateTime);
        } catch (ParseException e) {
            throw new CustomException(e.getMessage());
        }
    }


    public static String getWeekStartDate(String startTime) {
        Date date;
        try {
            //定义起始日期
            date = parse(startTime);
        } catch (Exception e) {
            System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
            throw new CustomException(e.getMessage());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return DATE_FORMAT_10.format(cal.getTime());
    }


    /**
     * 计算时间(计算传入时间前几天或者后几天的时间)
     *
     * @param nowTime 传入时间
     * @param addNum  要增加的天数(为负数则是减少的天数)
     * @return 计算后的时间
     */
    public Long dateAddDay(Long nowTime, int addNum) {
        return nowTime + (ONE_DAY_TIME * addNum);
    }

    /**
     * 日期格式化
     *
     * @param date 时间
     * @return String
     */
    public static String dateFormat(Date date) {
        return DateUtil.dateFormat(date, PATTERN);
    }

    /**
     * 日期格式化
     *
     * @param date   时间
     * @param format 格式化
     * @return String
     */
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return formatter.format(date);
    }

    /**
     * 字符串转换成时间
     *
     * @param text 时间
     * @return Date
     */
    public static Date parse(String text, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        try {
            return formatter.parse(text);
        } catch (ParseException e) {
            throw new CustomException(e.getMessage());
        }
    }

    /**
     * 字符串转换成时间
     *
     * @param text 时间
     * @return Date
     */
    public static Date parse(String text) {
        try {
            return parse(text, PATTERN);
        } catch (Exception e) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            try {
                return formatter.parse(text);
            } catch (ParseException e2) {
                throw new CustomException(e.getMessage());
            }
        }
    }

    /**
     * 当前时间日期加一天 或者 加一个月
     *
     * @param time   当前时间
     * @param format Calendar.DAY_OF_MONTH
     * @param index  需要加的天数 1 或者 -1
     * @return Date
     */
    public static Date addTime(Date time, int format, int index) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.add(format, index);
        return calendar.getTime();
    }

    /**
     * 获取某一天的开始时刻
     *
     * @return 2022-03-25 00:00:00
     */
    public static Date getStartTime() {
        return getStartTime(new Date());
    }

    /**
     * 获取某一天的开始时刻
     *
     * @param time 当前时间
     * @return 2022-03-25 00:00:00
     */
    public static Date getStartTime(Date time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某一天的开始时刻
     *
     * @param time 当前时间
     * @return 2022-03-25 23:59:59
     */
    public static Date getEndTime(Date time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getMontStartTime(Date date, int amount) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, amount);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取上个月开始时间
     *
     * @param time time
     * @return date
     */
    public static Date getPreviousMonthStartTime(Date time) {
        return getMontStartTime(time, -1);
    }

    public static Date getNextMontStartTime(Date time) {
        return getMontStartTime(time, 1);
    }

    public static Date getMonthEndTime(Date time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(time);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置创造新日期，这个日期是本月的最后一天
        calendar.set(Calendar.DATE, days);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return calendar.getTime();
    }

    public static String getMonthStartTime(String time) {
        Date begin_date;
        try {
            //定义起始日期
            begin_date = parse(time);
        } catch (Exception e) {
            throw new CustomException("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(begin_date);
        // 设置创造新日期，这个日期是本月的最后一天
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return DATE_FORMAT_10.format(date);
    }

    
    public static class DateVo {
        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        private String startDate;
        private String endDate;

        private DateVo(String startStr, String endStr) {
            this.startDate = startStr;
            this.endDate = endStr;
        }

        private DateVo() {
        }

    }

    /**
     * @Description: 获取时间段内所有自然月, 参数格式为:yyyy-MM-dd
     * @Param: [startDate, endDate]
     * @Return: java.util.List<Test.DateVo>
     */
    public static List<DateVo> getBetweenMonths(String startDate, String endDate) throws ParseException {
        List<DateVo> list = null;
        list = new ArrayList<DateVo>();
        String firstDay = "";
        String lastDay = "";
        Date d1 = DATE_FORMAT_10.parse(startDate);// 定义起始日期
        Date d2 = DATE_FORMAT_10.parse(endDate);// 定义结束日期

        Calendar dd = Calendar.getInstance();// 开始日期日历
        Calendar c = Calendar.getInstance();//结束日期日历
        dd.setTime(d1);// 设置日期起始时间
        c.setTime(d2);
        Calendar cale = Calendar.getInstance();
        int startDay = d1.getDate();
        int endDay = d2.getDate();
        DateVo keyValueForDate = null;
        while (dd.getTime().before(d2)) {// 判断是否到结束日期
            keyValueForDate = new DateVo();
            cale.setTime(dd.getTime());
            if (dd.getTime().equals(d1)) {
                if ((dd.get(Calendar.MONTH) + 1) == (c.get(Calendar.MONTH) + 1) && (dd.get(Calendar.YEAR)) == (c.get(Calendar.YEAR) + 1)) {
                    if (startDay == 1) {
                        cale.set(Calendar.DATE, endDay - startDay + 1);
                    } else {
                        cale.set(Calendar.DATE, endDay);
                    }
                } else {
                    cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                lastDay = DATE_FORMAT_10.format(cale.getTime());
                keyValueForDate.setStartDate(DATE_FORMAT_10.format(d1));
                keyValueForDate.setEndDate(lastDay);
            } else if (dd.get(Calendar.MONTH) == d2.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
                cale.set(Calendar.DAY_OF_MONTH, 1);//取第一天
                firstDay = DATE_FORMAT_10.format(cale.getTime());
                keyValueForDate.setStartDate(firstDay);
                keyValueForDate.setEndDate(DATE_FORMAT_10.format(d2));
            } else {
                cale.set(Calendar.DAY_OF_MONTH, 1);//取第一天
                firstDay = DATE_FORMAT_10.format(cale.getTime());
                cale.set(Calendar.DAY_OF_MONTH, dd.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDay = DATE_FORMAT_10.format(cale.getTime());
                keyValueForDate.setStartDate(firstDay);
                keyValueForDate.setEndDate(lastDay);
            }
            list.add(keyValueForDate);
            dd.add(Calendar.MONTH, 1);// 进行当前日期月份加1
        }
        if (endDay <= startDay) {
            keyValueForDate = new DateVo();
            cale.setTime(d2);
            cale.set(Calendar.DAY_OF_MONTH, 1);//取第一天
            firstDay = DATE_FORMAT_10.format(cale.getTime());
            keyValueForDate.setStartDate(firstDay);
            keyValueForDate.setEndDate(DATE_FORMAT_10.format(d2));
            list.add(keyValueForDate);
        }
        return list;
    }

    /**
     * @Description: 获取时间段内所有自然日, 参数格式为:yyyy-MM-dd
     * @Param: [startDate, endDate]
     * @Return: java.util.List<Test.DateVo>
     */
    public static List<DateVo> getBetweenDates(String startDate, String endDate) throws ParseException {
        Date start = DATE_FORMAT_10.parse(startDate);// 定义起始日期
        Date end = DATE_FORMAT_10.parse(endDate);// 定义结束日期
        List<DateVo> result = new ArrayList<DateVo>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        while (tempStart.before(tempEnd) || tempStart.equals(tempEnd)) {
            DateVo dateVo = new DateVo();
            dateVo.setStartDate(DATE_FORMAT_10.format(tempStart.getTime()));
            dateVo.setEndDate(DATE_FORMAT_10.format(tempStart.getTime()));
            result.add(dateVo);
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return result;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int getBetweenDates(Date startDate, Date endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = sdf.parse(sdf.format(startDate));
        endDate = sdf.parse(sdf.format(endDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * @Description: 获取时间段内所有自然周, 参数格式为:yyyy-MM-dd
     * @Param: [startDate, endDate]
     * @Return: java.util.List<Test.DateVo>
     */
    public static List<DateVo> getBetweenWeeks(String startDate, String endDate) throws ParseException {
        List<String> listWeekOrMonth = new ArrayList<String>();
        List<DateVo> dateVoList = new ArrayList<DateVo>();
        Date sDate = DATE_FORMAT_10.parse(startDate);// 定义起始日期
        Calendar sCalendar = Calendar.getInstance();
        sCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        sCalendar.setTime(sDate);
        Date eDate = DATE_FORMAT_10.parse(endDate);// 定义结束日期
        Calendar eCalendar = Calendar.getInstance();
        eCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        eCalendar.setTime(eDate);
        boolean bool = true;
        while (sCalendar.getTime().getTime() < eCalendar.getTime().getTime()) {
            if (bool || sCalendar.get(Calendar.DAY_OF_WEEK) == 2 || sCalendar.get(Calendar.DAY_OF_WEEK) == 1) {
                listWeekOrMonth.add(DATE_FORMAT_10.format(sCalendar.getTime()));
                bool = false;
            }
            sCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        listWeekOrMonth.add(DATE_FORMAT_10.format(eCalendar.getTime()));
        if (listWeekOrMonth.size() % 2 != 0) {
            listWeekOrMonth.add(DATE_FORMAT_10.format(eCalendar.getTime()));
        }
        for (int i = 0; i < listWeekOrMonth.size() - 1; i++) {
            if (i % 2 == 0) {
                DateVo dateVo = new DateVo();
                dateVo.setStartDate(listWeekOrMonth.get(i));
                dateVo.setEndDate(listWeekOrMonth.get(i + 1));
                dateVoList.add(dateVo);
            }
        }
        return dateVoList;
    }

    /**
     * 获取当前年份都季度
     *
     * @param date
     * @return int
     * @author martinjiang
     * @version 1.0
     * @date 2022/10/26 17:46
     */
    public static int getQuarterOfYear(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(date));
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 获取当前年份
     *
     * @param date
     * @return int
     * @author martinjiang
     * @version 1.0
     * @date 2022/10/26 17:46
     */
    public static String getYear(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date dateTime = parse(date);
        return sdf.format(dateTime);
    }

    /**
     * 获取季度开始时间
     *
     * @param date
     * @return java.util.Date
     * @author martinjiang
     * @version 1.0
     * @date 2022/10/26 20:06
     */
    public static Date getQuarterStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month / 3 * 3);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
