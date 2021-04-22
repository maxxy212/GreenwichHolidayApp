package com.greenwich.holiday.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.greenwich.holiday.R;
import com.greenwich.holiday.databinding.ActivityLoginBinding;
import com.greenwich.holiday.model.Department;
import com.greenwich.holiday.model.Role;
import com.greenwich.holiday.network.ApiCallHandler;
import com.greenwich.holiday.services.RoleDeptService;
import com.greenwich.holiday.utils.UI;
import com.greenwich.holiday.viewmodel.UserCall;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Realm realm;

    @Inject
    UserCall userCall;
    @Inject
    UI ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        realm = Realm.getDefaultInstance();
        callBackgroundDeptRole();
        binding.login.setOnClickListener(v -> validateDetails());
    }

    private void validateDetails(){
        binding.id.setError(null);
        binding.password.setError(null);

        String username = binding.id.getText().toString();
        String password = binding.password.getText().toString();

        if (username.isEmpty()){
            binding.id.setError("userID is required");
        }else if (password.isEmpty()){
            binding.password.setError("password is required");
        }else {
            loginCall(username, password);
        }
    }

    private void loginCall(String username, String password){
        JSONObject object = new JSONObject();
        try {
            object.put("email", username);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callBackgroundDeptRole();

        ui.showNonCloseableProgress(null);
        userCall.signIn(object, new ApiCallHandler() {
            @Override
            protected void done() {
                ui.dismissProgress();
            }

            @Override
            public void success(Object data) {
                super.success(data);
                DashboardActivity.start(LoginActivity.this);
                finishAffinity();
            }

            @Override
            public void failed(String title, String reason) {
                super.failed(title, reason);
                ui.showErrorDialog(title, reason);
            }
        });
    }

    private void callBackgroundDeptRole(){
        Role role = realm.where(Role.class).findFirst();
        Department department = realm.where(Department.class).findFirst();
        if (role == null || department == null){
            RoleDeptService.startActionRefresh(this, new Intent());
        }
    }

    public static void start(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm = null;
    }
}