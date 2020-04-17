package com.atf.demo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.atf.demo.databinding.ActivityMainBinding;
import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.BotActivity;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.SequenceMasterFileResponse;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.io.DataLoader;
import com.ghostwording.chatbot.io.SequencesDatabase;
import com.ghostwording.chatbot.model.texts.UserInfo;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.LocaleManager;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.widget.SingleSelectionGroupView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private SingleSelectionGroupView languageSettings;

    private final Handler handlerForDisplayDeviceId = new Handler();

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
        binding.cbOfflineMode.setOnCheckedChangeListener((compoundButton, b) -> AppConfiguration.setOfflineMode(b));
        binding.tvVersion.setText("v" + BuildConfig.VERSION_NAME);
        languageSettings.setSelectedItem(LocaleManager.getSelectedLanguage(this));
        binding.etBotname.setText(AppConfiguration.getBotName());

        binding.tvVersion.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                handlerForDisplayDeviceId.postDelayed(() -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("deviceId", AppConfiguration.getDeviceId());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, AppConfiguration.getDeviceId(), Toast.LENGTH_LONG).show();
                }, 3000);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                handlerForDisplayDeviceId.removeCallbacksAndMessages(null);
            }
            return false;
        });

        binding.btnStart.setOnClickListener(view -> {
            String botName = binding.etBotname.getText().toString();
            String sequenceName = binding.etFragmentName.getText().toString();

            if (!botName.isEmpty() && !sequenceName.isEmpty()) {
                if (binding.etBotname.isFocused()) {
                    showBot(botName);
                } else {
                    showFragment(sequenceName);
                }
                return;
            }

            if (!sequenceName.isEmpty()) {
                showFragment(sequenceName);
            } else {
                showBot(botName);
            }
        });
        binding.btnClearCache.setOnClickListener(view -> {
            DataLoader.instance().clearDatabase();
            ApiClient.clearData();
            Toast.makeText(MainActivity.this, R.string.cache_cleared, Toast.LENGTH_LONG).show();
        });
    }

    private void showFragment(String sequenceName) {
        Intent intent = new Intent(MainActivity.this, BotActivity.class);
        intent.putExtra(BotActivity.SEQUENCE_ID, sequenceName);
        startActivity(intent);
    }

    private void showBot(String botName) {
        PrefManager.instance().setLastSequenceId(botName, null);
        AppConfiguration.setBotName(botName);
        if (AppConfiguration.isOfflineMode()) {
            ApiClient.getInstance().botService.getMasterSequences(botName).enqueue(new Callback<List<SequenceMasterFileResponse>>(this) {
                @Override
                public void onDataLoaded(@Nullable List<SequenceMasterFileResponse> result) {
                    if (result != null) {
                        List<BotSequence> botSequences = new ArrayList<>();
                        for (SequenceMasterFileResponse sequenceMasterFile : result) {
                            botSequences.add(sequenceMasterFile.getSequencesForLine().get(new Random().nextInt(sequenceMasterFile.getSequencesForLine().size())));
                        }
                        DataLoader.instance().saveSequences(botSequences);
                        startActivity(new Intent(MainActivity.this, BotActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, com.ghostwording.chatbot.R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            ApiClient.getInstance().botService.clearBotHistory(new UserInfo(binding.etBotname.getText().toString())).enqueue(new Callback<ResponseBody>(MainActivity.this) {
                @Override
                public void onDataLoaded(@Nullable ResponseBody result) {
                    if (result != null) {
                        startActivity(new Intent(MainActivity.this, BotActivity.class));
                    } else {
                        Toast.makeText(MainActivity.this, com.ghostwording.chatbot.R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
