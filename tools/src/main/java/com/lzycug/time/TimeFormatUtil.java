package com.lzycug.time;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * 时间处理工具类
 *
 * @author lzycug
 * @since 2020-01-22
 */
public class TimeFormatUtil {
    private static final Logger LOG = LogManager.getLogger(TimeFormatUtil.class);

    /**
     * 日期格式
     */
    private static final String YEAR_MONTH_DAY_MINUTE_SECOND_MILLISECOND = "yyyyMMddHHmmssSSS";

    /**
     * 世界标准时间
     */
    private static final String UTC = "UTC";

    /**
     * 区域8小时偏移量
     */
    private static final int ZONE_OFFSET_EIGHT_HOURS = 8;

    private TimeFormatUtil() {
    }

    /**
     * 获取标准世界时间：年月日时分秒毫秒
     *
     * @return 标准世界时间
     */
    public static String getUtcTime() {
        String utcTime = StringUtils.EMPTY;
        Instant instant = Clock.systemUTC().instant();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_MINUTE_SECOND_MILLISECOND);
            utcTime = formatter.format(LocalDateTime.ofInstant(instant, ZoneId.ofOffset(UTC, ZoneOffset.UTC)));
        } catch (IllegalArgumentException illegalArgumentException) {
            LOG.error("get utc time IllegalArgumentException");
        } catch (DateTimeException dateTimeException) {
            LOG.error("get utc time DateTimeException");
        }
        return utcTime;
    }

    /**
     * 获取相对时间
     *
     * @param timestamp 时间串，比如yyyy-MM-dd HH:mm:ss
     * @param pattern 时间格式
     * @return 相对时间ms
     */
    public static long getTimeByDateFormat(String timestamp, String pattern) {
        long timeStampReturn = 0L;
        if (StringUtils.isAnyEmpty(timestamp, pattern)) {
            LOG.warn("timestamp or pattern is empty");
            return timeStampReturn;
        }
        try {
            DateTimeFormatter returnTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localData = LocalDateTime.parse(timestamp, returnTimeFormatter);
            timeStampReturn = localData.toInstant(ZoneOffset.ofHours(ZONE_OFFSET_EIGHT_HOURS)).toEpochMilli();
        } catch (IllegalArgumentException argumentException) {
            LOG.error("parse time IllegalArgumentException");
        } catch (DateTimeParseException parseException) {
            LOG.error("parse time DateTimeParseException");
        } catch (DateTimeException timeException) {
            LOG.error("parse time DateTimeException");
        }
        return timeStampReturn;
    }

    /**
     * 时间格式转换
     *
     * @param inputTime 输入时间串
     * @param inputPatten 输入时间格式，比如：yyyy-MM-dd HH:mm:ss
     * @param outPatten 输出时间格式，比如：yyyy-MM-dd'T'HH:mm:ss
     * @return 转换后的时间字符串
     */
    public static String getConvertTime(String inputTime, String inputPatten, String outPatten) {
        String timeReturn = StringUtils.EMPTY;
        if (StringUtils.isAnyEmpty(inputTime, inputPatten, outPatten)) {
            LOG.warn("time parameter is empty");
            return timeReturn;
        }
        try {
            DateTimeFormatter returnTimeFormatter = DateTimeFormatter.ofPattern(outPatten);
            DateTimeFormatter inputTimeFormatter = DateTimeFormatter.ofPattern(inputPatten);
            LocalDateTime localData = LocalDateTime.parse(inputTime, inputTimeFormatter);
            timeReturn = returnTimeFormatter.format(localData);
        } catch (IllegalArgumentException argumentException) {
            LOG.error("parse time IllegalArgumentException");
        } catch (DateTimeParseException parseException) {
            LOG.error("parse time DateTimeParseException");
        } catch (DateTimeException timeException) {
            LOG.error("parse time DateTimeException");
        }
        return timeReturn;
    }
}
