package com.greenwich.holiday.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Package com.greenwich.holiday.utils in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
public class DateUtil {
    //private static final String TAG = DateUtil.class.getSimpleName();
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat sqlformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static SimpleDateFormat picFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat referencedateformat = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    public static final SimpleDateFormat fulldateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String formatDate(Date date , SimpleDateFormat dateformat){
        if(dateformat == null){
            dateformat = DateUtil.dateformat;
        }
        if(date != null){
            return dateformat.format(date);
        }
        return "";

    }
}
