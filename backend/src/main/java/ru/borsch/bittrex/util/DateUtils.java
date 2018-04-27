package ru.borsch.bittrex.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date addMilliseconds(Date date, int milliseconds){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MILLISECOND, milliseconds);

        return cal.getTime();
    }
}
