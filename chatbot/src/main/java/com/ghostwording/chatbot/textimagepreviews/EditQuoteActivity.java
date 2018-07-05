package com.ghostwording.chatbot.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;

import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.databinding.ActivityEditQuoteBinding;
import com.ghostwording.chatbot.dialog.ShareDialog;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.Utils;

public class EditQuoteActivity extends BaseActivity {

    public static final String QUOTE_TEXT = "quote_text";
    public static final String IMAGE_ASSET = "image_asset";

    private String imageAsset;
    private ActivityEditQuoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_quote);
        initToolbar();

        binding.etQuote.setText(getIntent().getStringExtra(QUOTE_TEXT));
        imageAsset = getIntent().getStringExtra(IMAGE_ASSET);
        binding.etQuote.setSelection(binding.etQuote.getText().length());
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.EDIT_TEXT);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_send) {
                Quote quote = new Quote();
                quote.setContent(binding.etQuote.getText().toString());
                ShareDialog.show(EditQuoteActivity.this, imageAsset, quote, false);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

}

