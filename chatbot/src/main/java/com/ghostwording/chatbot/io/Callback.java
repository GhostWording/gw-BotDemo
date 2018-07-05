package com.ghostwording.chatbot.io;

import android.app.Activity;
import android.databinding.ObservableBoolean;
import android.support.annotation.Nullable;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.UtilsUI;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;

public abstract class Callback<T> implements retrofit2.Callback<T> {

    private WeakReference<Activity> activityReference;
    private ObservableBoolean dataLoadingObserver;
    private boolean showError = true;

    public Callback(Activity activity) {
        this.activityReference = new WeakReference<>(activity);
    }

    public Callback(Activity activity, boolean showError) {
        this.activityReference = new WeakReference<>(activity);
        this.showError = showError;
    }

    public Callback(Activity activity, ObservableBoolean dataLoadingObserver) {
        this(activity);
        this.dataLoadingObserver = dataLoadingObserver;
        if (dataLoadingObserver != null) {
            dataLoadingObserver.set(true);
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Activity activity = activityReference.get();
        if (activity == null) return;

        if (dataLoadingObserver != null) {
            dataLoadingObserver.set(false);
        }

        if (!response.isSuccessful()) {
            try {
                JSONObject errorResponse = new JSONObject(response.errorBody().string());
                if (showError) {
                    UtilsUI.showErrorInSnackBar(activity, errorResponse.getJSONObject("error").getString("message"));
                }
            } catch (Exception e) {
                Logger.e(e.toString());
                if (showError) {
                    UtilsUI.showErrorInSnackBar(activity, activity.getString(R.string.network_error));
                }
            }
        }

        onDataLoaded(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Activity activity = activityReference.get();
        if (activity == null) return;

        if (dataLoadingObserver != null) {
            dataLoadingObserver.set(false);
        }

        if (showError) {
            UtilsUI.showErrorInSnackBar(activity, activity.getString(R.string.network_error));
        }
        onDataLoaded(null);
        Logger.e(t.toString());
    }

    public abstract void onDataLoaded(@Nullable T result);

}

