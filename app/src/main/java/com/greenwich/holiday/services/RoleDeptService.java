package com.greenwich.holiday.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.greenwich.holiday.network.ApiCallHandler;
import com.greenwich.holiday.viewmodel.UserCall;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Package com.greenwich.holiday.services in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/22/21
 */
@AndroidEntryPoint
public class RoleDeptService extends JobIntentService {

    private static final String TAG = RoleDeptService.class.getSimpleName();
    private static final int JOB_ID = 3;

    @Inject
    UserCall userCall;

    public static void startActionRefresh(Context context, Intent work) {
        enqueueWork(context, RoleDeptService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        fetchDeptRole();
    }

    private void fetchDeptRole(){
        userCall.getDept(new ApiCallHandler() {
            @Override
            protected void done() {

            }

            @Override
            public void success(Object data) {
                super.success(data);
                Log.d(TAG, "success: sucesss");
            }

            @Override
            public void failed(String title, String reason) {
                super.failed(title, reason);
                Log.d(TAG, "failed: " + reason);
            }
        });

        userCall.getRoles(new ApiCallHandler() {
            @Override
            protected void done() {

            }

            @Override
            public void success(Object data) {
                super.success(data);
                Log.d(TAG, "success: sucesss");
            }

            @Override
            public void failed(String title, String reason) {
                super.failed(title, reason);
                Log.d(TAG, "failed: " + reason);
            }
        });
    }
}
