package com.greenwich.holiday.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Package com.greenwich.holiday.model in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/22/21
 */
public class Department extends RealmObject {
    @PrimaryKey
    public int id;
    public String name;
    public String created_at;
    public String updated_at;
}
