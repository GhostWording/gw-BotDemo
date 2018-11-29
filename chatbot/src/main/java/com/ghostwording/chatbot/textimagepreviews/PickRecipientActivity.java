package com.ghostwording.chatbot.textimagepreviews;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.databinding.ActivityRecyclerViewBinding;
import com.ghostwording.chatbot.model.MoodMenuItem;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.model.recipients.RecipientsPopularAndImportanceComparator;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.UtilsMessages;
import com.ghostwording.chatbot.viewmodel.RecyclerViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PickRecipientActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityRecyclerViewBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);
        RecyclerViewModel recyclerViewModel = new RecyclerViewModel();
        binding.setViewModel(recyclerViewModel);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setTitle(R.string.menu_choose_recipient);

        binding.recyclerMenuItems.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerMenuItems.setHasFixedSize(true);

        List<Recipient> recipients = AppConfiguration.getRecipientsList();
        UtilsMessages.filterAndUpdateRecipients(recipients);
        Collections.sort(recipients, new RecipientsPopularAndImportanceComparator());
        List<MoodMenuItem> moodMenuItems = new ArrayList<>();
        for (Recipient recipient : recipients) {
            moodMenuItems.add(new MoodMenuItem(recipient));
        }
        binding.recyclerMenuItems.setAdapter(new MoodMenuAdapter(moodMenuItems, item -> {
            PickHelper.with(PickRecipientActivity.this).onRecipientPicked(item.getRecipient());
            finish();
        }));
    }

}
