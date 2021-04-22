package com.greenwich.holiday.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Package com.greenwich.holiday.model in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
public class User extends RealmObject {
    @PrimaryKey
    public String id;
    public String name;
    public String email;
    public String username;
    public String date_of_joining;
    public int remaining_leaves;
    public int total_leaves;
    public int role_id;
    public int department_id;
    public String email_verified_at;
    public String created_at;
    public String updated_at;
}
