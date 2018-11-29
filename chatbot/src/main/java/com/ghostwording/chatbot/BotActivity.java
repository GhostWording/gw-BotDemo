package com.ghostwording.chatbot;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.ChatbotFragment;
import com.ghostwording.chatbot.databinding.ActivityBotBinding;

public class BotActivity extends BaseActivity {

    public static final String SEQUENCE_ID = "sequenceId";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBotBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_bot);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ChatbotFragment chatbotFragment = ChatbotFragment.newInstance(getIntent().getStringExtra(SEQUENCE_ID));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_fragments, chatbotFragment, "Chatbot")
                .commit();
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.TALK_TO_ME_REACHED);
    }

}
