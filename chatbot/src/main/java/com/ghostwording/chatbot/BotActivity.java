package com.ghostwording.chatbot;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.ChatbotFragment;
import com.ghostwording.chatbot.databinding.ActivityBotBinding;
import com.ghostwording.chatbot.utils.AppConfiguration;

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
    }

    private void updateBtnPlayTextState() {
        if (AppConfiguration.isPlayText()) {
            binding.btnPlayText.setImageResource(R.drawable.ic_stop_white_24dp);
        } else {
            binding.btnPlayText.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

}
