package com.greenwich.holiday.network;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.greenwich.holiday.BuildConfig;

import org.json.JSONObject;

/**
 * Package com.greenwich.holiday.network in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
public class Networking {
    private static final String TAG = Networking.class.getSimpleName();

    private final static String API_ENDPOINT = BuildConfig.BASE_URL;

    public static void getData(String endpoint,
                               OkHttpResponseAndJSONObjectRequestListener listener){
        //noinspection MalformedFormatString
        Log.d(TAG, String.format("Getting data from", API_ENDPOINT + endpoint));
        AndroidNetworking.get(API_ENDPOINT + endpoint)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }

    public static void getDataArray(String endpoint,
                               OkHttpResponseAndJSONArrayRequestListener listener){
        //noinspection MalformedFormatString
        Log.d(TAG, String.format("Getting data from", API_ENDPOINT + endpoint));
        AndroidNetworking.get(API_ENDPOINT + endpoint)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONArray(listener);
    }

    public static void getDataWithAuthorization(String endpoint,
                                                OkHttpResponseAndJSONObjectRequestListener listener) {
        Log.d(TAG, "getDataWithAuthorization: " + API_ENDPOINT + endpoint);
        Log.d(TAG, "getDataWithAuthorization: " + getToken());
        AndroidNetworking.get(API_ENDPOINT + endpoint)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", "Bearer " + getToken())
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }

    public static void postData(String endpoint,
                                JSONObject jsonObject,
                                OkHttpResponseAndJSONObjectRequestListener listener) {
        Log.d("posting shit", API_ENDPOINT + endpoint);
        AndroidNetworking.post(API_ENDPOINT + endpoint)
                .addJSONObjectBody(jsonObject)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }


    public static void postDataWithAuthorization(String endpoint,
                                                 JSONObject jsonObject,
                                                 boolean accessToken,
                                                 OkHttpResponseAndJSONObjectRequestListener listener) {
        String tok;
        if (!accessToken){
            tok = getRefreshToken();
        }else{
            tok = getToken();
        }
        Log.d(TAG, "postDataWithAuthorization: " + API_ENDPOINT + endpoint);
        Log.d(TAG, "postDataWithAuthorization: " + tok);
        AndroidNetworking.post(API_ENDPOINT + endpoint)
                .addJSONObjectBody(jsonObject)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Authorization", "Bearer " + tok)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }

    public static void putData(String endpoint,
                               JSONObject jsonObject,
                               OkHttpResponseAndJSONObjectRequestListener listener)
    {
        Log.d(TAG, "putData: "+ API_ENDPOINT + endpoint);
        Log.d(TAG, "putData: " + getToken());
        AndroidNetworking.put(API_ENDPOINT + endpoint)
                .addJSONObjectBody(jsonObject)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }

    public static void putDataWithAuthorization(String endpoint,
                                                JSONObject jsonObject,
                                                OkHttpResponseAndJSONObjectRequestListener listener)
    {
        Log.d(TAG, "putData: "+ API_ENDPOINT + endpoint);
        AndroidNetworking.put(API_ENDPOINT + endpoint)
                .addJSONObjectBody(jsonObject)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer " + getToken())
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }


    public static void deleteData(String endpoint,
                                  OkHttpResponseAndJSONObjectRequestListener listener){
        Log.d(TAG, "deleteData: "+API_ENDPOINT + endpoint+"");
        AndroidNetworking.delete(API_ENDPOINT + endpoint)
                .setTag(endpoint)
                .setPriority(Priority.MEDIUM)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Authorization", "Bearer " + getToken())
                .build()
                .getAsOkHttpResponseAndJSONObject(listener);
    }


    private static String getToken(){
//        try (Realm _realm = Realm.getDefaultInstance()) {
//            User user = _realm.where(User.class).findFirst();
//            if (user != null) {
//                return user.access_token;
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "getToken: " + e.getMessage());
//            return "";
//        }

        return "";
    }

    private static String getRefreshToken(){
//        try (Realm _realm = Realm.getDefaultInstance()) {
//            User user = _realm.where(User.class).findFirst();
//            if (user != null) {
//                return user.refresh_token;
//            }
//        } catch (Exception e) {
//            Log.d(TAG, "getToken: " + e.getMessage());
//            return "";
//        }

        return "";
    }
}
