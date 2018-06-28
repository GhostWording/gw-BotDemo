package com.ghostwording.hugsapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ghostwording.hugsapp.databinding.ActivityMainBinding;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.LocaleManager;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.widget.SingleSelectionGroupView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private SingleSelectionGroupView languageSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        languageSettings = new SingleSelectionGroupView(binding.containerLanguage, getResources().getStringArray(R.array.languages_start_screen), R.drawable.ic_check, position -> {
            if (LocaleManager.getSelectedLanguage(MainActivity.this) != position) {
                Utils.setLanguage(MainActivity.this, position, true);
                restartActivity();
            }
        });
        languageSettings.setSelectedItem(LocaleManager.getSelectedLanguage(this));

        binding.btnStart.setOnClickListener(view -> {
            String botName = binding.etBotname.getText().toString();
            PrefManager.instance().setHuggyShown(false);
            PrefManager.instance().setLastSequenceId(botName, null);
            AppConfiguration.setBotName(botName);
            ApiClient.getInstance().testDevService.clearBotHistory(binding.etBotname.getText().toString(), AppConfiguration.getDeviceId()).enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, BotActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
