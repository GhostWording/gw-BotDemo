package com.ghostwording.chatbot.textimagepreviews;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.databinding.ActivityRecyclerViewBinding;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.model.MoodMenuItem;
import com.ghostwording.chatbot.model.intentions.Intention;
import com.ghostwording.chatbot.model.intentions.IntentionAreaComparator;
import com.ghostwording.chatbot.model.intentions.IntentionsPopularComparator;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.viewmodel.RecyclerViewModel;
import com.ghostwording.chatbot.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PickIntentionActivity extends BaseActivity {

    public static final String RELATION_TYPE_TAG = "relation_type_tag";
    public static final String AREA_ID = "area_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityRecyclerViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        RecyclerViewModel recyclerViewModel = new RecyclerViewModel();
        binding.setViewModel(recyclerViewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(R.string.select_category);

        binding.recyclerMenuItems.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerMenuItems.setHasFixedSize(true);
        binding.recyclerMenuItems.addItemDecoration(new SimpleDividerItemDecoration(this));

        String areaId = getIntent().getStringExtra(AREA_ID);
        if (areaId == null) {
            areaId = AppConfiguration.getAppAreaId();
        }

        ApiClient.getInstance().coreApiService.getIntentions(areaId).enqueue(new Callback<List<Intention>>(PickIntentionActivity.this, recyclerViewModel.isDataLoading) {
            @Override
            public void onDataLoaded(@Nullable List<Intention> intentions) {
                if (intentions != null) {
                    Collections.sort(intentions, new IntentionAreaComparator());
                    Collections.sort(intentions, new IntentionsPopularComparator());

                    List<Intention> filteredIntentions = new ArrayList<>();

                    String selectedIntentionId = PickHelper.with(PickIntentionActivity.this).getSelectedIntentionId();

                    for (Intention intention : intentions) {
                        if (intention.getIntentionId().equals(selectedIntentionId)) {
                            filteredIntentions.add(0, intention);
                            continue;
                        }
                        if (intention.getRelationTypesString().contains(getIntent().getStringExtra(RELATION_TYPE_TAG))) {
                            filteredIntentions.add(intention);
                        }
                    }

                    List<MoodMenuItem> moodMenuItems = new ArrayList<>();
                    for (Intention intention : filteredIntentions) {
                        moodMenuItems.add(new MoodMenuItem(intention));
                    }

                    binding.recyclerMenuItems.setAdapter(new IntentionsAdapter(moodMenuItems, moodMenuItem -> onIntentionPicked(moodMenuItem.getIntention())));
                }
            }
        });
    }

    private void onIntentionPicked(Intention intention) {
        PrefManager.instance().increaseChooseIntentionCount(intention.getIntentionId());
        PickHelper.with(PickIntentionActivity.this).onIntentionPicked(intention);
        finish();
    }

}
