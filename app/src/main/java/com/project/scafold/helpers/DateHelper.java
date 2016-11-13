package com.project.scafold.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jayan on 8/29/2016.
 */
public class DateHelper {

    public static final String MMMM_DD_YYYY = "MMMM dd, yyyy";
    public static final String DD_MMM_YYYY = "dd MMM yyyy";

    public static String formatDate(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }

    public static Date parseDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String changeFormat(String dateString, String formatInput, String formatOutput) {
        Date d = parseDate(dateString,formatInput);
        return  formatDate(d,formatOutput);
    }
}
