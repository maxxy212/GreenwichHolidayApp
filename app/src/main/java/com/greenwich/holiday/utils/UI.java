package com.greenwich.holiday.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.greenwich.holiday.R;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.varunjohn1990.iosdialogs4android.IOSDialog;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityScoped;

/**
 * Package com.greenwich.holiday.utils in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
@ActivityScoped
public class UI {

    private final Activity activity;
    private KProgressHUD hud;

    @Inject
    public UI(Activity activity) {
        this.activity = activity;
    }

    public void showOkayDialog(String title, String content, boolean doOnBackPress){
        if (!activity.isFinishing())
            new IOSDialog.Builder(activity)
                    .title(title)
                    .message(content)
                    .positiveButtonText(android.R.string.ok)
                    .enableAnimation(true)
                    .cancelable(false)
                    .positiveClickListener(iosDialog -> {
                        iosDialog.dismiss();
                        if (doOnBackPress) activity.onBackPressed();
                    })
                    .build()
                    .show();
    }


    public void showErrorDialog(String title, String message){
        if (!activity.isFinishing())
            new IOSDialog.Builder(activity)
                    .title(title)
                    .message(message)
                    .enableAnimation(true)
                    .cancelable(true)
                    .build()
                    .show();
    }

    public void showInfoDialog(String title, String message){
        if (!activity.isFinishing())
            new IOSDialog.Builder(activity)
                    .title(title)
                    .message(message)
                    .build()
                    .show();
    }

    public void showInfoDialog(String title, String message, String btn_message){
        if (!activity.isFinishing())
            new IOSDialog.Builder(activity)
                    .title(title)
                    .message(message)
                    .enableAnimation(false)
                    .positiveButtonText(btn_message)
                    .build()
                    .show();
    }

    public void showNonCloseableProgress(String title){
        if (!activity.isFinishing())
            if (title == null || title.isEmpty()){
                hud =  KProgressHUD.create(activity)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel(activity.getString(R.string.load))
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
            }else {
                hud =  KProgressHUD.create(activity)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setDetailsLabel(title)
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
            }

    }

    public void dismissProgress(){
        if (hud != null){
            hud.dismiss();
            hud = null;
        }
    }

    public void forceHideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            }
    }
}
