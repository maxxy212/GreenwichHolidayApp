package com.greenwich.holiday.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Package com.greenwich.holiday.model in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/23/21
 */
public class Holiday extends RealmObject {
    @PrimaryKey
    public int id;
    public String start_date;
    public String end_date;
    public String category;
    public int status;
    public int user_id;
    public Date created_at;
    public Date updated_at;
}