package org.pojonode.util;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Cosmin Marginean, 11/01/12
 */
public class FormattingUtils {

    public static final String DATEFMT_TIME = "yyyy-MM-dd HH:mm:ss";

    public static Date parseDate(String input, String fmt) throws Exception {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        return new SimpleDateFormat(fmt).parse(input);
    }

    public static String formatDate(Date date, String fmt) throws Exception {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(fmt).format(date);
    }

    public static Date clearTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date addDays(Date date, int dayCount) {
        if (dayCount != 0) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, dayCount);
            return calendar.getTime();
        } else {
            return date;
        }
    }

}
