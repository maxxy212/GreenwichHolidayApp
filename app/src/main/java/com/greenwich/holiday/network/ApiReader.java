package com.greenwich.holiday.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.androidnetworking.error.ANError;
import com.greenwich.holiday.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Response;

/**
 * Package com.greenwich.holiday.network in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
public class ApiReader {
    private static final String connect_error = "connectionError";
    private static final String parse_error = "parseError";
    private static final String request_error = "requestCancelledError";
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private ANError anError;

    private final int code;
    private String message = "";
    private JSONObject error;

    public ApiReader(Response okHttpResponse, JSONObject js){
        this.jsonObject = js;
        this.code = okHttpResponse.code();
        this.message = js.optString("message");
        this.error = js.optJSONObject("error");
    }

    public ApiReader(Response okHttpResponse, JSONArray js){
        this.jsonArray = js;
        this.code = okHttpResponse.code();
    }

    public ApiReader(ANError anError){
        this.anError = anError;
        this.code = anError.getErrorCode();
    }

    public StringBuilder getErrorMsg(){
        StringBuilder finMsg = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(anError.getErrorBody());
            List<String> messages = new ArrayList<>();
            for (final Iterator<String> itr = jsonObject.keys(); itr.hasNext();){
                final String key = itr.next();
                try{
                    final Object val = jsonObject.get(key);
                    if (val instanceof JSONArray){
                        final JSONArray err = (JSONArray) val;
                        messages.add(err.get(0).toString() + "\n");
                    }else {
                        messages.add(val.toString());
                    }
                }catch (final JSONException e){
                    finMsg.append(e.getMessage());
                }
            }
            for (String str: messages){
                finMsg.append(str).append("\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return finMsg;
    }

    public String getNetworkErrorMsg(Context context){
        switch (anError.getErrorDetail()) {
            case ApiReader.connect_error:
                return context.getString(R.string.conn_error);
            case ApiReader.parse_error:
                return context.getString(R.string.parse_error);
            case ApiReader.request_error:
                return context.getString(R.string.req_error);
            default:
                return anError.getErrorDetail();
        }
    }


    public boolean isSuccess(){
        int OK_RESPONSE = 200;
        int CREATED_RESPONSE = 201;
        int DUPLICATE_RESPONSE = 202;
        return code == OK_RESPONSE || code == CREATED_RESPONSE || code == DUPLICATE_RESPONSE;
    }

    public boolean isFailed(){
        int VALIDATION_ERROR = 400;
        return code == VALIDATION_ERROR;
    }

    public boolean isAuthorizationError(){
        int UNAUTHORIZED_ERROR = 401;
        int FORBIDDEN_ERROR = 403;
        return code == UNAUTHORIZED_ERROR || code == FORBIDDEN_ERROR;
    }

    public boolean isSystemError(){
        int NOT_FOUND_ERROR = 500;
        int SYSTEM_ERROR = 501;
        return code == SYSTEM_ERROR || code == NOT_FOUND_ERROR;
    }

    public boolean isBadRequest(){
        int BAD_REQUEST = 404;
        return code == BAD_REQUEST;
    }

    public JSONObject getData(){
        return jsonObject;
    }

    public JSONObject getData(String key){
        JSONObject object = new JSONObject();
        try {
            object = jsonObject.getJSONObject(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public JSONArray getData(boolean isArray){
        return jsonArray;
    }

    public String getErrorMessage(){
        String errorMsg = null;
        try {
            errorMsg = this.error.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return errorMsg;
    }

    public String getErrorTitle(){
        return this.message;
    }

    public void handleError(Context context, ANError anError, ApiCallHandler callHandler, String TAG){
        anError.printStackTrace();
        Log.d(TAG, "onError: "+anError.getErrorCode());
        if (isAuthorizationError()){
            callHandler.logout();
        }else if (anError.getErrorCode() != 0){
            StringBuilder message = getErrorMsg();
            callHandler.failed(anError.getResponse().message(), message.toString());
        }else {
            String error = getNetworkErrorMsg(context);
            callHandler.failed("Network Error", error);
        }
    }
}
