package com.greenwich.holiday.network;

/**
 * Package com.greenwich.holiday.network in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
public abstract class ApiCallHandler {
    protected abstract void done();
    public void success(Object data){
        this.done();
    }
    public void failed(String title, String reason){
        this.done();
    }
    public void logout(){this.done();}
}
