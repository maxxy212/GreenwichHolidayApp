package com.greenwich.holiday.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.greenwich.holiday.R;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ActivityScoped;

/**
 * Package com.greenwich.holiday.utils in
 * <p>
 * Project HolidayApp
 * <p>
 * Created by Maxwell on 4/20/21
 */
@ActivityScoped
public class DisplayUtil {
    private final Context context;
    private Snackbar snackbar;

    @Inject
    public DisplayUtil(@ApplicationContext Context context){
        this.context = context;
    }

    public void toastLong(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void toastShort(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(View view, String message, int snackBarType){
        snackbar = Snackbar.make(view, message, snackBarType);
        snackbar.setAction("Ok", v -> {
            snackbar.dismiss();
        });
        snackbar.setActionTextColor(context.getResources().getColor(R.color.color_light_text));
        snackbar.show();
    }

    public void hideSnackBar(){
        if (snackbar != null)
            snackbar.dismiss();
    }
}
