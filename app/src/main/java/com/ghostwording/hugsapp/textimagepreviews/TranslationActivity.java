package com.ghostwording.hugsapp.textimagepreviews;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.ghostwording.hugsapp.BaseActivity;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.databinding.ActivityTranslationBinding;
import com.ghostwording.hugsapp.dialog.ShareDialog;
import com.ghostwording.hugsapp.io.QuotesLoader;
import com.ghostwording.hugsapp.model.listeners.OnPageSelectedListener;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.utils.Utils;

import java.util.Locale;

public class TranslationActivity extends BaseActivity {

    public static final String IMAGE_URL = "image_url";
    public static final String QUOTE = "quote";
    public static final String IMAGE_NAME = "image_name";
    public static final String RECIPIENT_NAME = "recipient_name";
    public static final String RECIPIENT_ID = "recipient_id";
    public static final String RECIPIENT_RELATION = "recipient_relation";

    public ObservableBoolean isDataLoading = new ObservableBoolean(false);

    private ActivityTranslationBinding binding;
    private String imageUrl;
    private String imageName;
    private boolean isBubble;
    private TextToSpeech tts;
    private QuoteTranslationsPagesAdapter adapter;
    private Quote quote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_translation);
        binding.setViewModel(this);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageName = Utils.getFilenameFromUri(getIntent().getStringExtra(IMAGE_URL));
        imageUrl = Utils.getFullImagePrefix() + imageName;
        if (getIntent().getStringExtra(IMAGE_NAME) != null) {
            imageName = getIntent().getStringExtra(IMAGE_NAME);
        }

        quote = getIntent().getParcelableExtra(QUOTE);
        AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.TRANSLATION_OPENED, quote.getTextId(), Utils.getFilenameFromUri(imageUrl));

        isDataLoading.set(true);
        QuotesLoader.instance().getQuoteById(quote.getTextId()).subscribe(result -> {
            isDataLoading.set(false);
            quote = result;
            updateViewPager();
        });

        initControls();
        initTts();
    }

    private void initControls() {
        binding.switchBubble.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isBubble = isChecked;
            updateViewPager();
        });

        binding.btnPlay.setOnClickListener(v -> {
            try {
                Quote quote = adapter.getQuote(binding.vpItems.getCurrentItem());
                setupTts(quote);
                tts.speak(quote.getContent(), TextToSpeech.QUEUE_FLUSH, null);
                AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.TEXT, AnalyticsHelper.Events.PLAY_TEXT, quote.getTextId());
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
        });

        binding.btnSend.setOnClickListener(v -> {
            try {
                ShareDialog.show(TranslationActivity.this, imageUrl, imageName, adapter.getQuote(binding.vpItems.getCurrentItem()), isBubble);
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
        });

        binding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TranslationActivity.this, EditQuoteActivity.class);
            intent.putExtra(EditQuoteActivity.IMAGE_ASSET, imageUrl);
            try {
                intent.putExtra(EditQuoteActivity.QUOTE_TEXT, adapter.getQuote(binding.vpItems.getCurrentItem()).getContent());
            } catch (NullPointerException ex) {
                Logger.e(ex.toString());
            }
            startActivity(intent);
        });

        binding.btnImageRecommendation.setOnClickListener(v -> {
            if (adapter == null || quote.getTextId() == null) return;
            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.IMAGE_RECOMMEND);
            Quote selectedQuote = adapter.getQuote(binding.vpItems.getCurrentItem());
            if (selectedQuote != null) {
                Intent intent = new Intent(TranslationActivity.this, PicturesRecommendationActivity.class);
                intent.putExtra(PicturesRecommendationActivity.QUOTE_ID, selectedQuote.getTextId());
                intent.putExtra(PicturesRecommendationActivity.QUOTE_PROTOTYPE_ID, selectedQuote.getPrototypeId());
                intent.putExtra(PicturesRecommendationActivity.QUOTE_TEXT, selectedQuote.getContent());
                intent.putExtra(PicturesRecommendationActivity.IMAGE_PATH, Utils.getImagePath(selectedQuote));
                startActivity(intent);
            }
        });
    }

    private void initTts() {
        tts = new TextToSpeech(getApplicationContext(), status -> {
        });
    }

    private void updateViewPager() {
        int lastSelectedTab = binding.vpItems.getCurrentItem();
        adapter = new QuoteTranslationsPagesAdapter(TranslationActivity.this, quote, imageUrl, isBubble);
        binding.vpItems.setAdapter(adapter);
        binding.tabs.setupWithViewPager(binding.vpItems);
        binding.vpItems.setCurrentItem(lastSelectedTab);
        binding.vpItems.addOnPageChangeListener(new OnPageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                Quote currentQuote = adapter.getQuote(binding.vpItems.getCurrentItem());
                if (currentQuote != null) {
                    AnalyticsHelper.sendEvent(AnalyticsHelper.Events.SWIPE_LANGUAGE, currentQuote.getCulture());
                }
                if (position == adapter.getCount() - 1) {
                    if (PrefManager.instance().isFirstTimeAction("TranslationHint")) {
                        Toast.makeText(TranslationActivity.this, R.string.translation_warning, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        tts.shutdown();
        super.onDestroy();
    }

    private void setupTts(Quote quote) {
        if (quote.getCulture().toLowerCase().contains("en")) {
            tts.setLanguage(Locale.US);
        } else if (quote.getCulture().toLowerCase().contains("fr")) {
            tts.setLanguage(Locale.CANADA_FRENCH);
        } else if (quote.getCulture().toLowerCase().contains("es")) {
            tts.setLanguage(new Locale("es", "ES"));
        } else {
            tts.setLanguage(new Locale(quote.getCulture(), quote.getCulture().toUpperCase()));
        }
    }

}
