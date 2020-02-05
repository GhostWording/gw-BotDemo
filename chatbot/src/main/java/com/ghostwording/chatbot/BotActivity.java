package com.ghostwording.chatbot;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.ChatbotFragment;
import com.ghostwording.chatbot.databinding.ActivityBotBinding;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.model.texts.UserInfo;
import com.ghostwording.chatbot.utils.AppConfiguration;

import okhttp3.ResponseBody;

public class BotActivity extends BaseActivity {

    public static final String SEQUENCE_ID = "sequenceId";

    private ActivityBotBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bot);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChatbotFragment chatbotFragment = ChatbotFragment.newInstance(getIntent().getStringExtra(SEQUENCE_ID));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragments, chatbotFragment, "Chatbot")
                .commit();
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.TALK_TO_ME_REACHED);
        updateBtnPlayTextState();
        binding.btnPlayText.setOnClickListener(v -> {
            AppConfiguration.setIsPlayText(!AppConfiguration.isPlayText());
            updateBtnPlayTextState();
        });

        binding.btnReset.setOnClickListener(view -> ApiClient.getInstance().botService.clearBotHistory(new UserInfo(AppConfiguration.getBotName())).enqueue(new Callback<ResponseBody>(BotActivity.this) {
            @Override
            public void onDataLoaded(@Nullable ResponseBody result) {
                if (result != null) {
                    Toast.makeText(BotActivity.this, R.string.success_bot_history_clean, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BotActivity.this, R.string.error_bot_loading, Toast.LENGTH_LONG).show();
                }
            }
        }));

        ChatBotApplication.sendUserProperties(this, AppConfiguration.getBotName());
    }

    private void updateBtnPlayTextState() {
        if (AppConfiguration.isPlayText()) {
            binding.btnPlayText.setImageResource(R.drawable.ic_stop_white_24dp);
        } else {
            binding.btnPlayText.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

}
