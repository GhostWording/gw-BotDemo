package com.atf.demo;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.atf.demo.databinding.ActivityMainBinding;
import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.BotActivity;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.LocaleManager;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.widget.SingleSelectionGroupView;

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
        languageSettings = new SingleSelectionGroupView(binding.containerLanguage, getResources().getStringArray(com.ghostwording.chatbot.R.array.languages_start_screen), com.ghostwording.chatbot.R.drawable.ic_check, position -> {
            if (LocaleManager.getSelectedLanguage(MainActivity.this) != position) {
                Utils.setLanguage(MainActivity.this, position, true);
                restartActivity();
            }
        });
        languageSettings.setSelectedItem(LocaleManager.getSelectedLanguage(this));

        binding.btnStart.setOnClickListener(view -> {
            String botName = binding.etBotname.getText().toString();
            String sequenceName = binding.etFragmentName.getText().toString();

            if (!botName.isEmpty() && !sequenceName.isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.error_fill_bot_name, Toast.LENGTH_LONG).show();
                return;
            }

            if (!sequenceName.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, BotActivity.class);
                intent.putExtra(BotActivity.SEQUENCE_ID, sequenceName);
                startActivity(intent);
                return;
            }

            PrefManager.instance().setLastSequenceId(botName, null);
            AppConfiguration.setBotName(botName);
            ApiClient.getInstance().testDevService.clearBotHistory(binding.etBotname.getText().toString(), AppConfiguration.getDeviceId()).enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, BotActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, com.ghostwording.chatbot.R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, com.ghostwording.chatbot.R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
