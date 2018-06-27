package com.ghostwording.hugsapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;

public class UtilsUI {

    public static ProgressDialog createProgressDialog(Activity activity, @StringRes int message, boolean isShow) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getString(message));
        if (isShow) {
            progressDialog.show();
        }
        return progressDialog;
    }

    public static void showErrorInSnackBar(Activity activity, String errorMessage) {
        if (activity == null) return;
        Snackbar.make(activity.findViewById(android.R.id.content).getRootView(),
                errorMessage,
                Snackbar.LENGTH_SHORT).show();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }


}
