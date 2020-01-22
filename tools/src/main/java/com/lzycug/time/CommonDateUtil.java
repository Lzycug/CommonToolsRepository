package com.lzycug.time;

import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import javax.annotation.Nullable;

/**
 * 日期公共工具类
 *
 * @author lzycug
 * @since 2020-01-22
 */
public abstract class CommonDateUtil {
    /**
     * 一秒包含的毫秒数
     */
    public static final long ONE_SECOND_MILLISECOND = 1000L;

    /**
     * 一分钟包含的毫秒数
     */
    public static final long ONE_MINUTE_MILLISECOND = 60000L;

    /**
     * 一小时包含的毫秒数
     */
    public static final long ONE_HOUR_MILLISECOND = 3600000L;

    /**
     * 一天包含的毫秒数
     */
    public static final long ONE_DAY_MILLISECOND = 86400000L;

    /**
     * 一周包含的毫秒数
     */
    public static final long ONE_WEEK_MILLISECOND = 604800000L;

    /**
     * 一月包含的毫秒数
     */
    public static final long ONE_MONTH_MILLISECOND = 2592000000L;

    /**
     * 一年包含的毫秒数
     */
    public static final long ONE_YEAR_MILLISECOND = 31536000000L;

    /**
     * 最小时间（UTC0秒）
     */
    public static final long MIN_TIME = -28800000L;

    /**
     * 半分钟包含的秒数
     */
    public static final int SECONDS_OF_HALF_MINUTE = 30;

    /**
     * 一分钟包含的秒钟数
     */
    public static final int SECONDS_OF_MINUTE = 60;

    /**
     * 半小时包含的分钟数
     */
    public static final int MINUTES_OF_HALF_HOUR = 30;

    /**
     * 一刻钟包含的分钟数
     */
    public static final int MINUTES_OF_QUARTER = 15;

    /**
     * 一小时包含的分钟数
     */
    public static final int MINUTES_OF_HOUR = 60;

    /**
     * 半天包含的小时数
     */
    public static final int HOURS_OF_HALF_DAY = 12;

    /**
     * 一天包含的小时数
     */
    public static final int HOURS_OF_DAY = 24;

    /**
     * 一周包含的天数
     */
    public static final int DAYS_OF_WEEK = 7;

    /**
     * 一个月包含的最大有效天数
     */
    public static final int MAX_DAYS_OF_MONTH = 31;

    /**
     * 一个季度的月份数
     */
    public static final int MONTHS_OF_SEASON = 3;

    /**
     * 一年的月份数量
     */
    public static final int MONTHS_OF_YEAR = 12;

    /**
     * 数字常量2
     */
    public static final int NUMBER_TWO = 2;

    /**
     * 数字常量3
     */
    public static final int NUMBER_THREE = 3;

    /**
     * 数字常量4
     */
    public static final int NUMBER_FOUR = 4;

    /**
     * 数字常量10
     */
    public static final int NUMBER_TEN = 10;

    /**
     * 数字常量100
     */
    public static final int NUMBER_HUNDRED = 100;

    /**
     * 最小年份
     */
    public static final int MIN_YEAR = 1900;

    /**
     * 基准年份
     */
    public static final int BASE_YEAR = 2000;

    /**
     * 最大年份
     */
    public static final int MAX_YEAR = 38;

    /**
     * 4年常量定义
     */
    public static final int LEAP_YEAR_FOUR = 4;

    /**
     * 100年常量定义
     */
    public static final int LEAP_YEAR_HUNDRED = 100;

    /**
     * 400年常量定义
     */
    public static final int LEAP_YEAR_FOUR_HUNDRED = 400;

    /**
     * 数组长度为零
     */
    public static final int ARRAY_LENGTH_ZERO = 0;

    /**
     * 时间单位年
     */
    public static final String YEAR = "年";

    /**
     * 时间单位月
     */
    public static final String MONTH = "月";

    /**
     * 时间单位“周”
     */
    public static final String WEEK = "周";

    /**
     * 时间单位“天”
     */
    public static final String DAY = "天";

    /**
     * 时间单位“小时”
     */
    public static final String HOUR = "小时";

    /**
     * 时间单位“分钟”
     */
    public static final String MINUTES = "分钟";

    /**
     * 时间单位“分”
     */
    public static final String MINUTE = "分";

    /**
     * 时间单位“秒”
     */
    public static final String SECOND = "秒";

    /**
     * 时间单位“毫秒”
     */
    public static final String MILLISECOND = "毫秒";

    /**
     * 时间拼接字符串“个小时”
     */
    public static final String HOURS = "个小时";

    /**
     * 时间拼接字符串“个月”
     */
    public static final String MONTHS = "个月";

    /**
     * 字符串“前”
     */
    public static final String BEFORE_STRING = "前";

    /**
     * 字符串“后”
     */
    public static final String AFTER_STRING = "后";

    /**
     * 点分割符
     */
    public static final String POINT_SEPARATOR = "\\.";

    /**
     * 点字符串
     */
    public static final String POINT_STRING = ".";

    /**
     * 复活节计算中数字7常量定义
     */
    private static final int EASTER_BASE_NUM_SEVEN = 7;

    /**
     * 复活节计算中数字11常量定义
     */
    private static final int EASTER_BASE_NUM_ELEVEN = 11;

    /**
     * 复活节计算中数字19常量定义
     */
    private static final int EASTER_BASE_NUM_NINETEEN = 19;

    /**
     * 复活节计算中数字25常量定义
     */
    private static final int EASTER_BASE_NUM_TWENTYFIVE = 25;

    /**
     * 复活节计算中数字29常量定义
     */
    private static final int EASTER_BASE_NUM_TWENTYNINE = 29;

    /**
     * 复活节计算中数字31常量定义
     */
    private static final int EASTER_BASE_NUM_THIRTYONE = 31;

    /**
     * 十二星座在对应月份（十二月份）的分隔日期
     */
    private static final int[] CONSTELLATION_EDGE_DAYS = {20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};

    /**
     * 默认日期格式：年-月-日
     */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时分秒格式时间，如：01:43:35
     */
    private static final String HHMMSS_DATE_PATTERN = "HH:mm:ss";

    /**
     * ISO世界标准格式，如：2019-12-07T01:43:35
     */
    private static final String ISO_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 周
     */
    private static final String[] WEEKS_ARRAY = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    /**
     * 星座
     */
    private static final String[] CONSTELLATION_ARRAY =
        {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};

    private static final String[] SMART_DATE_FORMATS = {"yyyy-MM-dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss",
        "yyyy-MM-dd HH:mm", "yyyy.MM.dd HH:mm", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyy.MM.dd", "yyyyMMdd",
        "yyyy-MM-dd-HH-mm-ss", DEFAULT_DATE_PATTERN, HHMMSS_DATE_PATTERN, ISO_DATE_PATTERN};

    /**
     * 获取时间格式
     *
     * @return 时间字符串
     */
    public static String getDatePattern() {
        return DEFAULT_DATE_PATTERN;
    }

    /**
     * 获取日期中的年份
     *
     * @param date 指定日期
     * @return 日期中的年份
     */
    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    /**
     * 获取日期中的月份
     *
     * @param date 指定日期
     * @return 日期中的月份
     */
    public static int getMonth(Date date) {
        return getCalendar(date).get(Calendar.MONTH);
    }

    /**
     * 获取指定日期
     *
     * @param date 指定日期
     * @return 指定日期
     */
    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期是周几
     *
     * @param date 指定日期
     * @return 日期是周几
     */
    public static int getWeek(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 查询指定日期当月第一天是周几
     *
     * @param date 指定日期
     * @return 日期当月第一天是周几
     */
    public static int getWeekOfFirstDayOfMonth(Date date) {
        return getWeek(getFirstDayOfMonth(date));
    }

    /**
     * 查询指定日期当月最后一天是周几
     *
     * @param date 指定日期
     * @return 日期当月最后一天是周几
     */
    public static int getWeekOfLastDayOfMonth(Date date) {
        return getWeek(getLastDayOfMonth(date));
    }

    /**
     * 将字符时间按照一定的格式转为时间类型时间
     *
     * @param stringDate 字符时间
     * @param format 时间格式
     * @return 时间类型时间，格式非法时返回null
     */
    @Nullable
    public static final Date parseDate(String stringDate, String format) {
        Date date = null;
        if (StringUtils.isAnyEmpty(stringDate, format)) {
            return date;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            date = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

    /**
     * 将字符时间按照一定的格式转为时间类型时间
     *
     * @param stringDate 字符时间
     * @return 时间类型时间 ，格式非法时返回null
     */
    @Nullable
    public static Date parseDate(String stringDate) {
        return parseDate(stringDate, getDatePattern());
    }

    /**
     * 将字符时间按照默认的格式转为时间类型时间
     *
     * @param stringDate 字符时间
     * @return 时间类型时间，格式非法时返回null
     */
    @Nullable
    public static final Date parseDateSmart(String stringDate) {
        Date date = null;
        if (StringUtils.isEmpty(stringDate)) {
            return date;
        }
        for (String dateFormat : SMART_DATE_FORMATS) {
            Date tempDate = parseDate(stringDate, dateFormat);
            if (tempDate != null) {
                String tempStrDate = formatDate(tempDate, dateFormat);
                if (StringUtils.equals(stringDate, tempStrDate)) {
                    return tempDate;
                }
            }
        }
        try {
            long time = Long.parseLong(stringDate);
            date = new Date(time);
        } catch (NumberFormatException e) {
            date = null;
        }
        return date;
    }

    /**
     * 将HH:mm:ss的时间解析为今天的时间
     *
     * @param stringDate HH:mm:ss格式的时间字符，如：“22:10:00”
     * @return 时间类型时间，格式非法时返回null
     */
    @Nullable
    public static Date parseDateByHms(String stringDate) {
        // 先将时间解析为1970年1月1日的时分秒
        Date date = parseDate(stringDate, HHMMSS_DATE_PATTERN);
        if (date == null) {
            return date;
        }

        // 再将时间加上今天的年月日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(new Date());
        calendar.set(calendarToday.get(Calendar.YEAR), calendarToday.get(Calendar.MONTH),
            calendarToday.get(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 判断指定年份是否闰年
     *
     * @param year 指定年份
     * @return 年份是否是闰年
     */
    public static boolean isLeapYear(int year) {
        if (year != year / LEAP_YEAR_FOUR * LEAP_YEAR_FOUR) {
            return false;
        }
        if (year != year / LEAP_YEAR_HUNDRED * LEAP_YEAR_HUNDRED) {
            return true;
        }
        return year == year / LEAP_YEAR_FOUR_HUNDRED * LEAP_YEAR_FOUR_HUNDRED;
    }

    /**
     * 判断指定日期是否周末
     *
     * @param date 指定日期
     * @return 日期是否是周末
     */
    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
    }

    /**
     * 判断指定日期是否周末
     *
     * @return 日期是否是周末
     */
    public static boolean isWeekend() {
        return isWeekend(null);
    }

    /**
     * 获取当前时间
     *
     * @return 时间字符串
     */
    public static String getCurrentTime() {
        return formatDate(new Date());
    }

    /**
     * 获取指定格式的当前时间
     *
     * @param format format
     * @return 当前时间
     */
    public static String getCurrentTime(String format) {
        return formatDate(new Date(), format);
    }

    /**
     * 时间格式化
     *
     * @param date 指定时间
     * @param format 时间格式
     * @return 格式化后的时间
     */
    public static String formatDate(Date date, String format) {
        Date input = date;
        if (date == null) {
            input = new Date();
        }
        String type = format;
        if (format == null) {
            type = getDatePattern();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        return formatter.format(input);
    }

    /**
     * 时间格式化
     *
     * @param date 指定时间
     * @return 格式化后的时间
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        long offset = System.currentTimeMillis() - date.getTime();
        String position = BEFORE_STRING;
        if (offset < 0L) {
            position = AFTER_STRING;
            offset = -offset;
        }
        if (offset >= ONE_YEAR_MILLISECOND) {
            return formatDate(date, getDatePattern());
        } else if (offset >= ONE_MONTH_MILLISECOND * NUMBER_TWO) {
            return (offset + ONE_MONTH_MILLISECOND / NUMBER_TWO) / ONE_MONTH_MILLISECOND + MONTHS + position;
        } else if (offset > ONE_WEEK_MILLISECOND) {
            return (offset + ONE_WEEK_MILLISECOND / NUMBER_TWO) / ONE_WEEK_MILLISECOND + WEEK + position;
        } else if (offset > ONE_DAY_MILLISECOND) {
            return (offset + ONE_DAY_MILLISECOND / NUMBER_TWO) / ONE_DAY_MILLISECOND + DAY + position;
        } else if (offset > ONE_HOUR_MILLISECOND) {
            return (offset + ONE_HOUR_MILLISECOND / NUMBER_TWO) / ONE_HOUR_MILLISECOND + HOUR + position;
        } else if (offset > ONE_MINUTE_MILLISECOND) {
            return (offset + ONE_MINUTE_MILLISECOND / NUMBER_TWO) / ONE_MINUTE_MILLISECOND + MINUTES + position;
        } else {
            return offset / ONE_SECOND_MILLISECOND + SECOND + position;
        }
    }

    /**
     * 获取空的日期；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param day 指定日期
     * @return 日期对象
     */
    public static Date getCleanDay(Date day) {
        return getCleanDay(getCalendar(day));
    }

    /**
     * 获取空的日期
     *
     * @param calendar 当前时间的日历形式
     * @return 日期对象
     */
    private static Date getCleanDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的日历形式
     *
     * @param day 指定日期
     * @return 指定日期的日历形式
     */
    public static Calendar getCalendar(Date day) {
        Calendar calendar = Calendar.getInstance();
        if (day != null) {
            calendar.setTime(day);
        }
        return calendar;
    }

    /**
     * 构造指定年月日的日期
     *
     * @param year 指定的年
     * @param month 指定的月
     * @param day 指定的日
     * @return 指定年月日的日期
     */
    public static Date makeDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        getCleanDay(calendar);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 获取指定日期当前的“周”、“月”、“日”类型第一个日期
     *
     * @param datePart 时间类型
     * @param date 指定日期
     * @return 指定日期当前的“周”、“月”、“日”类型第一个日期
     */
    private static Date getFirstCleanDay(int datePart, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        if (datePart == Calendar.DAY_OF_WEEK) {
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            calendar.set(datePart, Calendar.MONDAY);
        } else if (datePart == Calendar.MONTH) {
            calendar.set(datePart, Calendar.JANUARY);
        } else {
            calendar.set(datePart, 1);
        }
        return getCleanDay(calendar);
    }

    /**
     * 指定日期关于不同时间类型（周、月、日）的加运算
     *
     * @param datePart 时间类型
     * @param delta 增加的值
     * @param date 指定日期
     * @return 加运算后的日期时间
     */
    private static Date add(int datePart, int delta, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.add(datePart, delta);
        return calendar.getTime();
    }

    /**
     * 获取指定日期当前周的第一天；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param date 指定日期
     * @return 指定日期当前周的第一天
     */
    public static Date getFirstDayOfWeek(Date date) {
        return getFirstCleanDay(Calendar.DAY_OF_WEEK, date);
    }

    /**
     * 获取指定日期当前周的第一天；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @return 指定日期当前周的第一天
     */
    public static Date getFirstDayOfWeek() {
        return getFirstDayOfWeek(null);
    }

    /**
     * 获取指定日期当前月的第一天；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param date 指定日期
     * @return 指定日期当前月的第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        return getFirstCleanDay(Calendar.DAY_OF_MONTH, date);
    }

    /**
     * 获取指定日期当前月的第一天
     *
     * @return 指定日期当前月的第一天
     */
    public static Date getFirstDayOfMonth() {
        return getFirstDayOfMonth(null);
    }

    /**
     * 获取指定日期当前月的最后一天
     *
     * @return 指定日期当前月的最后一天
     */
    public static Date getLastDayOfMonth() {
        return getLastDayOfMonth(null);
    }

    /**
     * 获取指定日期当前月的最后一天；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param date 指定日期
     * @return 指定日期当前月的最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = getCalendar(getFirstDayOfMonth(date));

        // 下个月第一天再减1天
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return getCleanDay(calendar);
    }

    /**
     * 获取指定日期当前季度的第一天；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param date 指定日期
     * @return 指定日期当前季度的第一天
     */
    public static Date getFirstDayOfSeason(Date date) {
        Date tempDate = getFirstDayOfMonth(date);
        int delta = getMonth(tempDate) % MONTHS_OF_SEASON;
        if (delta > 0) {
            tempDate = getDateAfterMonths(tempDate, -delta);
        }
        return tempDate;
    }

    /**
     * 获取当前季度的第一天
     *
     * @return 当前季度的第一天
     */
    public static Date getFirstDayOfSeason() {
        return getFirstDayOfMonth(null);
    }

    /**
     * 获取指定日期当年的第一天；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param date 指定日期
     * @return 指定日期当年的第一天
     */
    public static Date getFirstDayOfYear(Date date) {
        return makeDate(getYear(date), 1, 1);
    }

    /**
     * 获取当前年的第一天
     *
     * @return 当前年的第一天
     */
    public static Date getFirstDayOfYear() {
        return getFirstDayOfYear(new Date());
    }

    /**
     * 获取指定日期多少周后的日期；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 指定日期
     * @param weeks 时间间隔
     * @return 指定日期多少周后的日期
     */
    public static Date getDateAfterWeeks(Date start, int weeks) {
        return getDateAfterMs(start, weeks * ONE_WEEK_MILLISECOND);
    }

    /**
     * 获取指定日期多少月后的日期；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 指定日期
     * @param months 时间间隔
     * @return 指定日期多少月后的日期
     */
    public static Date getDateAfterMonths(Date start, int months) {
        return add(Calendar.MONTH, months, start);
    }

    /**
     * 获取指定日期多少年后的日期；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 指定日期
     * @param years 时间间隔
     * @return 指定日期多少年后的日期
     */
    public static Date getDateAfterYears(Date start, int years) {
        return add(Calendar.YEAR, years, start);
    }

    /**
     * 获取指定日期多少天后的日期；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 指定日期
     * @param days 时间间隔
     * @return 指定日期多少天后的日期
     */
    public static Date getDateAfterDays(Date start, int days) {
        return getDateAfterMs(start, days * ONE_DAY_MILLISECOND);
    }

    /**
     * 获取指定日期多少毫秒后的日期
     *
     * @param start 指定时间
     * @param ms 时间间隔
     * @return 指定日期多少毫秒后的日期
     */
    public static Date getDateAfterMs(Date start, long ms) {
        return new Date(start == null ? new Date().getTime() : start.getTime() + ms);
    }

    /**
     * 获取两个日期的时间间隔是指定毫秒时间的倍数；方法内调用接口有对入参start、end做判断处理，在此不再做入参检查
     *
     * @param start 起始时间
     * @param end 截止时间
     * @param msPeriod 指定的毫秒时间
     * @return 两个日期的时间间隔是指定毫秒时间的倍数
     */
    public static int getPeriodNumber(Date start, Date end, long msPeriod) {
        if (msPeriod == 0) {
            return (int) msPeriod;
        }
        long result = getIntervalMs(start, end) / msPeriod;
        if (result > Integer.MAX_VALUE) {
            result = 0;
            return (int) result;
        }
        return (int) result;
    }

    /**
     * 获取两个日期间隔的毫秒数
     *
     * @param start 起始日期
     * @param end 截止日期
     * @return 两个日期间隔的毫秒数
     */
    public static long getIntervalMs(Date start, Date end) {
        if (start == null || end == null) {
            return new Date().getTime();
        }
        return end.getTime() - start.getTime();
    }

    /**
     * 获取两个日期间隔的天数；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 起始日期
     * @param end 截止日期
     * @return 两个日期间隔的天数
     */
    public static int getIntervalDays(Date start, Date end) {
        return getPeriodNumber(start, end, ONE_DAY_MILLISECOND);
    }

    /**
     * 获取两个日期间隔的小时数；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 起始日期
     * @param end 截止日期
     * @return 两个日期间隔的小时数
     */
    public static int getIntervalHours(Date start, Date end) {
        return getPeriodNumber(start, end, ONE_HOUR_MILLISECOND);
    }

    /**
     * 计算两个日期相差几周；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 起始日期
     * @param end 截止日期
     * @return 两个日期相差几周
     */
    public static int getIntervalWeeks(Date start, Date end) {
        return getPeriodNumber(start, end, ONE_WEEK_MILLISECOND);
    }

    /**
     * 计算年份差；方法内调用接口有对入参做判断处理，在此不再做入参检查
     *
     * @param start 起始日期
     * @param end 截止日期
     * @return 年份差
     */
    public static int getIntervalYears(Date start, Date end) {
        return getYear(end) - getYear(start);
    }

    /**
     * 时间对应的星座
     *
     * @param time 时间
     * @return 时间对应的星座
     */
    public static String dateToConstellation(Date time) {
        if (time == null) {
            return StringUtils.EMPTY;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < CONSTELLATION_EDGE_DAYS[month]) {
            --month;
        }
        if (month >= 0) {
            return CONSTELLATION_ARRAY[month];
        }
        return CONSTELLATION_ARRAY[CONSTELLATION_ARRAY.length - 1];
    }

    /**
     * 星座对应的日期
     *
     * @param index 星座索引号
     * @return Date[] 星座对应的时间范围
     */
    public static Optional<Range<Date>> getConstellationDate(int index) {
        if (index < Calendar.JANUARY || index > Calendar.DECEMBER) {
            return Optional.empty();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        int month = index;
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, CONSTELLATION_EDGE_DAYS[month]);
        final Date start = calendar.getTime();
        if (month == Calendar.DECEMBER) {
            month = Calendar.JANUARY;

            // 把结束时间的年份加1年，否则Range会自动排序将12月份放到结果时间里面
            calendar.add(Calendar.YEAR, 1);
        } else {
            month += 1;
        }
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, CONSTELLATION_EDGE_DAYS[month] - 1);
        final Date end = calendar.getTime();
        return Optional.of(Range.between(start, end));
    }

    /**
     * 返回复活节日期
     *
     * @param year 年份
     * @return 复活节日期
     */
    public static Date easterDay(int year) {
        int tempOne = year - MIN_YEAR;
        int tempTwo = tempOne % EASTER_BASE_NUM_NINETEEN;
        int tempThree = tempOne / NUMBER_FOUR;
        int tempFour = (EASTER_BASE_NUM_SEVEN * tempTwo + 1) / EASTER_BASE_NUM_NINETEEN;
        int tempFive = (EASTER_BASE_NUM_ELEVEN * tempTwo + NUMBER_FOUR - tempFour) % EASTER_BASE_NUM_TWENTYNINE;
        int tempSix = (tempOne + tempThree + EASTER_BASE_NUM_THIRTYONE - tempFive) % EASTER_BASE_NUM_SEVEN;
        int tempSeven = EASTER_BASE_NUM_TWENTYFIVE - tempFive - tempSix;
        int month = tempSeven > 0 ? Calendar.APRIL : Calendar.MARCH;
        int day = tempSeven > 0 ? tempSeven : EASTER_BASE_NUM_THIRTYONE + tempSeven;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * 格式化两个时区时间偏移量差
     *
     * @param timeOffset 时间差（单位毫秒）
     * @return 格式化后两个时区时间偏移量差
     */
    public static String formatRarOffset(long timeOffset) {
        StringBuilder stringBuilder = new StringBuilder();

        // 计算相差的时间
        long day = timeOffset / ONE_DAY_MILLISECOND;
        if (day > 0) {
            stringBuilder.append(day).append(DAY);
        }
        long hour = timeOffset % ONE_DAY_MILLISECOND / ONE_HOUR_MILLISECOND;
        if (hour > 0) {
            stringBuilder.append(hour).append(HOURS);
        }
        long minute = timeOffset % ONE_HOUR_MILLISECOND / ONE_MINUTE_MILLISECOND;
        if (minute > 0) {
            stringBuilder.append(minute).append(MINUTE);
        }
        long second = timeOffset % ONE_MINUTE_MILLISECOND / ONE_SECOND_MILLISECOND;
        if (second > 0) {
            stringBuilder.append(second).append(SECOND);
        }
        long milliSecond = timeOffset % ONE_SECOND_MILLISECOND;
        if (milliSecond > 0) {
            stringBuilder.append(milliSecond).append(MILLISECOND);
        }
        return stringBuilder.toString();
    }

    /**
     * 查询某一天是周几
     *
     * @param timeZone 时区
     * @param date 日期
     * @return 周几
     */
    public static String getWeekOfDate(TimeZone timeZone, Date date) {
        Calendar calendar = Calendar.getInstance();
        if (timeZone != null) {
            calendar.setTimeZone(timeZone);
        }
        calendar.setTime(date == null ? new Date() : date);
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return WEEKS_ARRAY[index];
    }
}
