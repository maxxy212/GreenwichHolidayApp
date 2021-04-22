package com.greenwich.holiday.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.google.android.material.appbar.AppBarLayout;
import com.greenwich.holiday.R;
import com.greenwich.holiday.databinding.ActivityDashboardBinding;
import com.greenwich.holiday.databinding.LayoutCalendarBinding;
import com.greenwich.holiday.databinding.LayoutHistoryBinding;
import com.greenwich.holiday.model.Department;
import com.greenwich.holiday.model.Role;
import com.greenwich.holiday.model.User;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private Calendar start, end;
    private Realm realm;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        realm = Realm.getDefaultInstance();
        user = realm.where(User.class).findFirst();

        AppBarLayout.LayoutParams parameters = (AppBarLayout.LayoutParams)binding.collapse.getLayoutParams();
        parameters.setScrollFlags(0);
        binding.profileImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_avatar));
        start = Calendar.getInstance();
        start.add(Calendar.DAY_OF_YEAR, 1);
        end = Calendar.getInstance();

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        int year = cal.get(Calendar.YEAR);

        end.set(Calendar.YEAR, year);
        end.set(Calendar.MONTH, 11); // 11 = december
        end.set(Calendar.DAY_OF_MONTH, 31); // new years eve

        binding.book.setOnClickListener(v -> activateCalendar());

        binding.logout.setOnClickListener(v -> logUserOut());

        binding.setUser(user);
        loadRoleDept();
    }

    private void loadRoleDept(){
        Role role = realm.where(Role.class).equalTo("id", user.role_id).findFirst();
        Department department = realm.where(Department.class).equalTo("id", user.department_id).findFirst();

        binding.setRole(role);
        binding.setDept(department);
    }

    private void logUserOut(){
        realm.executeTransaction(realm1 -> {
            realm1.delete(User.class);
        });
        LoginActivity.start(this);
        finishAffinity();
    }

    private void reqHistory(){
        final Dialog d = new Dialog(this);
        d.getWindow();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutHistoryBinding bind = LayoutHistoryBinding.inflate(LayoutInflater.from(this), (ViewGroup) binding.getRoot(), false);
        d.setContentView(bind.getRoot());
        //bind.recycler;

        d.show();

        Window window = d.getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public static void start(Context context){
        context.startActivity(new Intent(context, DashboardActivity.class));
    }

    private void activateCalendar(){
        final Dialog d = new Dialog(this);
        d.getWindow();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutCalendarBinding bind = LayoutCalendarBinding.inflate(LayoutInflater.from(this), (ViewGroup) binding.getRoot(), false);
        d.setContentView(bind.getRoot());
        bind.calendar.setSelectableDateRange(start, end);
        bind.calendar.setCalendarListener(new CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar calendar) {
                //Toast.makeText(DashboardActivity.this, "Start Date: " + calendar.getTime().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                //Toast.makeText(DashboardActivity.this, "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        bind.cancel.setOnClickListener(v -> d.dismiss());
        bind.book.setOnClickListener(v -> {
            bookHoliday();
        });

        d.show();

        Window window = d.getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    private void bookHoliday(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm = null;
    }
}