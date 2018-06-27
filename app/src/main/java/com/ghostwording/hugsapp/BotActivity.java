package com.ghostwording.hugsapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.databinding.ActivityBotBinding;

public class BotActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBotBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_bot);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.TALK_TO_ME_REACHED);
    }

}
