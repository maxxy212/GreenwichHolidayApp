package com.greenwich.holiday.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

/**
 * Package com.greenwich.holiday.utils in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
@ActivityScoped
public class DateUtil {

    private final Context context;

    @Inject
    public DateUtil(@ApplicationContext Context context){
        this.context = context;
    }

    @SuppressLint("ConstantLocale")
    private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    public final SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    public final SimpleDateFormat fulldateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public String formatDate(Date date , SimpleDateFormat dateformat){
        if(dateformat == null){
            dateformat = DateUtil.dateformat;
        }
        if(date != null){
            return dateformat.format(date);
        }
        return "";

    }
}
