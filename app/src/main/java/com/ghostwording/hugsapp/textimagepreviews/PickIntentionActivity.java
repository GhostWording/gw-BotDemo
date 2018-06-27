package com.ghostwording.hugsapp.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.ghostwording.hugsapp.BaseActivity;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.ActivityRecyclerViewBinding;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.io.Callback;
import com.ghostwording.hugsapp.model.MoodMenuItem;
import com.ghostwording.hugsapp.model.intentions.Intention;
import com.ghostwording.hugsapp.model.intentions.IntentionAreaComparator;
import com.ghostwording.hugsapp.model.intentions.IntentionsPopularComparator;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.viewmodel.RecyclerViewModel;
import com.ghostwording.hugsapp.widget.SimpleDividerItemDecoration;

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
