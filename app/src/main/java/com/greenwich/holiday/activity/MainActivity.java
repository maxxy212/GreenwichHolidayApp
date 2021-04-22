package com.greenwich.holiday.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.os.Bundle;

import com.greenwich.holiday.R;
import com.greenwich.holiday.databinding.ActivityMainBinding;
import com.greenwich.holiday.model.Department;
import com.greenwich.holiday.model.Role;
import com.greenwich.holiday.model.User;
import com.greenwich.holiday.services.RoleDeptService;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        Role role = realm.where(Role.class).findFirst();
        Department department = realm.where(Department.class).findFirst();

        if (role == null || department == null){
            RoleDeptService.startActionRefresh(this, new Intent());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        User user = realm.where(User.class).findFirst();
        if (user != null){
            DashboardActivity.start(this);
            finishAffinity();
        }else {
            LoginActivity.start(this);
            finishAffinity();
        }

    }
}