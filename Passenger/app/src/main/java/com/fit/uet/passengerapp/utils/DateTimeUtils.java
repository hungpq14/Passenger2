package com.fit.uet.passengerapp.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Bien-kun on 05/03/2017.
 */

public class DateTimeUtils {
    public static String getLocalTime(String timestamp) {
        long currentDate = getTimeMillis(timestamp);
        currentDate += TimeZone.getDefault().getOffset(currentDate);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("Utils", timestamp + ": " + SimpleDateFormat.getDateInstance().format(currentDate) + " | " + sdfDate.format(currentDate));
        return SimpleDateFormat.getDateInstance().format(currentDate) + " \u2022 "
                + new SimpleDateFormat("hh:mm").format(currentDate);

    }

    public static String getTime(String timestamp, boolean is24Format) {
        long currentDate = getTimeMillis(timestamp);
        SimpleDateFormat format;
        if (is24Format) {
            format
                    = new SimpleDateFormat("hh:mm");
        } else {
            format = new SimpleDateFormat("hh:mm a");
        }
        return format.format(currentDate);
    }

    public static long getTimeMillis(String timestamp) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
            Date date = sdf.parse(timestamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(new Date());
    }

    public static long getMillisFromString(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy/HH/mm");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String dateStringFormat(long msecs) {
        String date = "";
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(msecs));

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        switch (dow) {
            case Calendar.MONDAY:
                date += "Mon, ";
                break;
            case Calendar.TUESDAY:
                date += "Tue, ";
                break;
            case Calendar.WEDNESDAY:
                date += "Wed, ";
                break;
            case Calendar.THURSDAY:
                date += "Thu, ";
                break;
            case Calendar.FRIDAY:
                date += "Fri, ";
                break;
            case Calendar.SATURDAY:
                date += "Sat, ";
                break;
            case Calendar.SUNDAY:
                date += "Sun, ";
                break;
            default:
                date += "Unknown, ";
        }

        switch (cal.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                date += "Jan ";
                break;
            case Calendar.FEBRUARY:
                date += "Feb ";
                break;
            case Calendar.MARCH:
                date += "Mar ";
                break;
            case Calendar.APRIL:
                date += "Apr ";
                break;
            case Calendar.MAY:
                date += "May ";
                break;
            case Calendar.JUNE:
                date += "Jun ";
                break;
            case Calendar.JULY:
                date += "Jul ";
                break;
            case Calendar.AUGUST:
                date += "Aug ";
                break;
            case Calendar.SEPTEMBER:
                date += "Sep ";
                break;
            case Calendar.OCTOBER:
                date += "Oct ";
                break;
            case Calendar.NOVEMBER:
                date += "Nov ";
                break;
            case Calendar.DECEMBER:
                date += "Dec ";
                break;
            default:
                date += "Unknown ";
                break;
        }
        date += cal.get(Calendar.DAY_OF_MONTH);
        date += ", ";
        date += cal.get(Calendar.YEAR);
        return date;
    }

    public static String getTimeFromMs(long ms) {
        long minute = (ms / (1000 * 60)) % 60;
        long hour = (ms / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d", hour, minute);
        return time;
    }
}
