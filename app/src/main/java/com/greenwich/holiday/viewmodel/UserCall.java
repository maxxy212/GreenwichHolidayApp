package com.greenwich.holiday.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.greenwich.holiday.model.Department;
import com.greenwich.holiday.model.Holiday;
import com.greenwich.holiday.model.Role;
import com.greenwich.holiday.model.User;
import com.greenwich.holiday.network.ApiCallHandler;
import com.greenwich.holiday.network.ApiReader;
import com.greenwich.holiday.network.Networking;
import com.greenwich.holiday.services.HolidayDataService;
import com.greenwich.holiday.services.RoleDeptService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.realm.Realm;
import okhttp3.Response;

/**
 * Package com.greenwich.holiday.viewmodel in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
public class UserCall {

    private static final String TAG = UserCall.class.getSimpleName();
    private final Context context;

    private static final String USER_SIGN_IN = "login";
    private static final String ROLES = "all/roles";
    private static final String DEPT = "all/depts";
    private static final String HOLIDAY = "holidays";
    private static final String HOLIDAY_DATA = "holidays/%s";

    @Inject
    public UserCall(@ApplicationContext Context context){
        this.context = context;
    }

    public void signIn(JSONObject jsonObject, final ApiCallHandler callHandler){
        Networking.postData(USER_SIGN_IN, jsonObject, new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                final ApiReader apiReader = new ApiReader(okHttpResponse, response);
                Log.d(TAG, "onResponse: "+apiReader.toString());
                if (apiReader.isSuccess()){
                    try(Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(realm1 -> {
                            Log.d(TAG, "writing: "+apiReader.toString());
                            Role role = realm1.where(Role.class).findFirst();
                            Department department = realm1.where(Department.class).findFirst();

                            if (role == null || department == null){
                                RoleDeptService.startActionRefresh(context, new Intent());
                            }
                            realm1.createOrUpdateObjectFromJson(User.class, apiReader.getData("user"));
                            User user = realm1.where(User.class).findFirst();
                            user.access_token = apiReader.getData().optString("access_token");
                            callHandler.success(apiReader.getData());
                        });
                    }catch (Exception e){
                        callHandler.failed("Database Error", e.getMessage());
                    }
                }else {
                    callHandler.failed(apiReader.getErrorTitle(), apiReader.getErrorMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                final ApiReader apiReader = new ApiReader(anError);
                apiReader.handleError(context, anError, callHandler, TAG);
            }
        });
    }

    public void getDept(final ApiCallHandler callHandler){
        Networking.getDataArray(DEPT, new OkHttpResponseAndJSONArrayRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONArray response) {
                final ApiReader apiReader = new ApiReader(okHttpResponse, response);
                Log.d(TAG, "onResponse: "+apiReader.toString());
                if (apiReader.isSuccess()){
                    try(Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(realm1 -> {
                            Log.d(TAG, "writing: "+apiReader.toString());
                            realm1.createOrUpdateAllFromJson(Department.class, apiReader.getData(true));
                            callHandler.success(apiReader.getData());
                        });
                    }catch (Exception e){
                        callHandler.failed("Database Error", e.getMessage());
                    }
                }else {
                    callHandler.failed(apiReader.getErrorTitle(), apiReader.getErrorMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                final ApiReader apiReader = new ApiReader(anError);
                apiReader.handleError(context, anError, callHandler, TAG);
            }
        });
    }

    public void getRoles(final ApiCallHandler callHandler){
        Networking.getDataArray(ROLES, new OkHttpResponseAndJSONArrayRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONArray response) {
                final ApiReader apiReader = new ApiReader(okHttpResponse, response);
                Log.d(TAG, "onResponse: "+apiReader.toString());
                if (apiReader.isSuccess()){
                    try(Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(realm1 -> {
                            Log.d(TAG, "writing: "+apiReader.toString());
                            realm1.createOrUpdateAllFromJson(Role.class, apiReader.getData(true));
                            callHandler.success(apiReader.getData());
                        });
                    }catch (Exception e){
                        callHandler.failed("Database Error", e.getMessage());
                    }
                }else {
                    callHandler.failed(apiReader.getErrorTitle(), apiReader.getErrorMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                final ApiReader apiReader = new ApiReader(anError);
                apiReader.handleError(context, anError, callHandler, TAG);
            }
        });
    }

    public void createHoliday(JSONObject jsonObject, final ApiCallHandler callHandler){
        Networking.postDataWithAuthorization(HOLIDAY, jsonObject, new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                final ApiReader apiReader = new ApiReader(okHttpResponse, response);
                Log.d(TAG, "onResponse: "+apiReader.toString());
                if (apiReader.isSuccess()){
                    callHandler.success(apiReader.getData().optString("message"));
                    HolidayDataService.startActionRefresh(context, new Intent());
                }else if (apiReader.isNotCreated()){
                    callHandler.failed("Application Unsuccessful", apiReader.getData().optString("message"));
                }else {
                    callHandler.failed(apiReader.getErrorTitle(), apiReader.getErrorMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                final ApiReader apiReader = new ApiReader(anError);
                apiReader.handleError(context, anError, callHandler, TAG);
            }
        });
    }

    public void getHolidayData(final ApiCallHandler callHandler){
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        Networking.getDataArrayWithAuthorization(String.format(HOLIDAY_DATA, user.id), new OkHttpResponseAndJSONArrayRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONArray response) {
                final ApiReader apiReader = new ApiReader(okHttpResponse, response);
                Log.d(TAG, "onResponse: "+apiReader.toString());
                if (apiReader.isSuccess()){
                    try(Realm realm = Realm.getDefaultInstance()) {
                        realm.executeTransaction(realm1 -> {
                            Log.d(TAG, "writing: "+apiReader.toString());
                            realm1.createOrUpdateAllFromJson(Holiday.class, apiReader.getData(true));
                            callHandler.success(apiReader.getData());
                        });
                    }catch (Exception e){
                        callHandler.failed("Database Error", e.getMessage());
                    }
                }else {
                    callHandler.failed(apiReader.getErrorTitle(), apiReader.getErrorMessage());
                }
            }

            @Override
            public void onError(ANError anError) {
                final ApiReader apiReader = new ApiReader(anError);
                apiReader.handleError(context, anError, callHandler, TAG);
            }
        });
    }
}
