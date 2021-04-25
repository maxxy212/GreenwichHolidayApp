package com.greenwich.holiday.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.google.android.material.appbar.AppBarLayout;
import com.greenwich.holiday.R;
import com.greenwich.holiday.adapter.HolidayAdapter;
import com.greenwich.holiday.databinding.ActivityDashboardBinding;
import com.greenwich.holiday.databinding.LayoutCalendarBinding;
import com.greenwich.holiday.databinding.LayoutHistoryBinding;
import com.greenwich.holiday.model.Department;
import com.greenwich.holiday.model.Holiday;
import com.greenwich.holiday.model.Role;
import com.greenwich.holiday.model.User;
import com.greenwich.holiday.network.ApiCallHandler;
import com.greenwich.holiday.services.HolidayDataService;
import com.greenwich.holiday.utils.DateUtil;
import com.greenwich.holiday.utils.UI;
import com.greenwich.holiday.viewmodel.UserCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;
import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.ObjectChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObjectChangeListener;
import io.realm.RealmResults;

@AndroidEntryPoint
public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private Calendar start, end;
    private Realm realm;
    private User user;
    private Date start_date, end_date;

    @Inject
    UserCall userCall;
    @Inject
    DateUtil dateUtil;
    @Inject
    UI ui;

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
        binding.requestHistory.setOnClickListener(v -> reqHistory());
        binding.logout.setOnClickListener(v -> logUserOut());

        binding.setUser(user);
        loadRoleDept();
    }

    private void loadRoleDept(){
        Role role = realm.where(Role.class).equalTo("id", user.role_id).findFirst();
        Department department = realm.where(Department.class).equalTo("id", user.department_id).findFirst();

        binding.setRole(role);
        binding.setDept(department);

        role.addChangeListener((roles, changeSet) -> {
            binding.setRole((Role) roles);
        });

        department.addChangeListener((depts, changeSet) -> {
            binding.setDept((Department) depts);
        });

        RealmResults<Holiday> data = realm.where(Holiday.class).findAll();
        if (data.isEmpty()){
            HolidayDataService.startActionRefresh(this, new Intent());
        }
    }

    private void logUserOut(){
        realm.executeTransaction(realm1 -> {
            realm1.delete(User.class);
            realm1.delete(Holiday.class);
        });
        MainActivity.start(this);
        finishAffinity();
    }

    private void reqHistory(){
        HolidayDataService.startActionRefresh(this, new Intent());
        final Dialog d = new Dialog(this);
        d.getWindow();
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutHistoryBinding bind = LayoutHistoryBinding.inflate(LayoutInflater.from(this), (ViewGroup) binding.getRoot(), false);
        d.setContentView(bind.getRoot());

        User user = realm.where(User.class).findFirst();
        RealmResults<Holiday> data = realm.where(Holiday.class).equalTo("user_id", Integer.valueOf(user.id)).findAll();
        if (data == null || data.isEmpty()){
            bind.noData.setVisibility(View.VISIBLE);
        }else {
            bind.noData.setVisibility(View.GONE);
        }

        RecyclerView recyclerView = bind.recycler;
        HolidayAdapter adapter = new HolidayAdapter(data, this);
        RecyclerView.LayoutManager _mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(_mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

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
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                start_date = startDate.getTime();
                end_date = endDate.getTime();
            }
        });

        bind.cancel.setOnClickListener(v -> d.dismiss());
        bind.book.setOnClickListener(v -> {
            bookHoliday(d);
        });

        d.show();

        Window window = d.getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    private void bookHoliday(Dialog d){
        JSONObject object = new JSONObject();
        try {
            object.put("start_date", dateUtil.formatDate(start_date, dateUtil.sqlformat));
            object.put("end_date", dateUtil.formatDate(end_date, dateUtil.sqlformat));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ui.showNonCloseableProgress(null);
        userCall.createHoliday(object, new ApiCallHandler() {
            @Override
            protected void done() {
                ui.dismissProgress();
            }

            @Override
            public void success(Object data) {
                super.success(data);
                ui.showOkayDialog("Decision made", String.valueOf(data), false);
                HolidayDataService.startActionRefresh(DashboardActivity.this, new Intent());
                d.dismiss();
            }

            @Override
            public void failed(String title, String reason) {
                super.failed(title, reason);
                ui.showErrorDialog(title, reason);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm = null;
    }
}