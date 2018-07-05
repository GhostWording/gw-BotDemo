package com.ghostwording.chatbot.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Toast;

import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.databinding.ActivityTextRecommendationBinding;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.io.DataManager;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsMessages;
import com.ghostwording.chatbot.widget.SimpleDividerItemDecoration;

import java.util.Collections;
import java.util.List;

public class TextsRecommendationActivity extends BaseActivity {

    public interface QuoteSelectedListener {
        void onQuoteSelected(Quote quote, View view);
    }

    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_PATH = "image_path";
    public static final String INTENTION_ID = "intention_id";

    private static final String THEME_PATH = "themes";

    private ActivityTextRecommendationBinding binding;
    private QuoteRecommendationViewModel viewModel;
    private String imageUrl;
    private String intentionId;
    private String imagePath;
    private String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        intentionId = getIntent().getStringExtra(INTENTION_ID);
        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        imageName = Utils.getFilenameFromUri(imageUrl);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_recommendation);
        viewModel = new QuoteRecommendationViewModel();
        binding.setViewModel(viewModel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.BACK_FROM_IMAGE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefManager.instance().setIsHumourMode(false);
    }

    public class QuoteRecommendationViewModel {
        public final ObservableBoolean isDataLoading = new ObservableBoolean(false);
        public final ObservableField<Boolean> isTextEnabled = new ObservableField<>(true);
        public final ObservableField<String> imageAsset = new ObservableField<>();

        public QuoteRecommendationViewModel() {
            setupRecycleView();
            loadTextRecommendations();
            imageAsset.set(imageUrl);
            binding.btnBack.setOnClickListener(view -> onBackPressed());
            binding.btnSendWithoutText.setOnClickListener(view -> sendMessage(null));
            binding.imageView.setOnClickListener(view -> PickHelper.with(TextsRecommendationActivity.this).pickRecipient().subscribe(recipient -> {
                PrefManager.instance().setRecipient(recipient);
                if (imagePath != null && !imagePath.contains(THEME_PATH) && intentionId != null) {
                    loadIntentionTexts(intentionId);
                } else {
                    PickHelper.with(TextsRecommendationActivity.this).pickIntention(recipient.getRelationTypeTag(), intentionId).subscribe(intention -> loadIntentionTexts(intention.getIntentionId()));
                }
            }));
            binding.btnHumourMode.setOnClickListener(view -> {
                if (PrefManager.instance().isHumourMode()) {
                    binding.btnHumourMode.setImageResource(R.drawable.ic_humor_mode_off);
                    if (PrefManager.instance().isFirstTimeAction("HumourOff")) {
                        Toast.makeText(TextsRecommendationActivity.this, R.string.humour_mode_off, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    binding.btnHumourMode.setImageResource(R.drawable.ic_humor_mode_on);
                    if (PrefManager.instance().isFirstTimeAction("HumourOn")) {
                        Toast.makeText(TextsRecommendationActivity.this, R.string.humour_mode_on, Toast.LENGTH_SHORT).show();
                    }
                }
                PrefManager.instance().setIsHumourMode(!PrefManager.instance().isHumourMode());
                loadTextRecommendations();
            });
        }

        private void sendMessage(Quote quote) {
            Utils.shareSticker(TextsRecommendationActivity.this, imageUrl, imageName, quote);
        }

        private void setupRecycleView() {
            binding.recyclerQuotes.setLayoutManager(new LinearLayoutManager(TextsRecommendationActivity.this));
            binding.recyclerQuotes.setHasFixedSize(true);
            binding.recyclerQuotes.addItemDecoration(new SimpleDividerItemDecoration(TextsRecommendationActivity.this));
        }

        private void loadIntentionTexts(String intentionId) {
            PrefManager.instance().setIsHumourMode(false);
            binding.btnHumourMode.setImageResource(R.drawable.ic_humor_mode_off);
            ApiClient.getInstance().coreApiService.getQuotes(AppConfiguration.getAppAreaId(), intentionId).enqueue(new Callback<List<Quote>>(TextsRecommendationActivity.this, isDataLoading) {
                @Override
                public void onDataLoaded(@Nullable List<Quote> quotes) {
                    if (quotes != null) {
                        isTextEnabled.set(true);

                        Animation fadeIn = new AlphaAnimation(0, 1);
                        fadeIn.setDuration(1500);
                        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
                        binding.recyclerQuotes.startAnimation(fadeIn);

                        quotes = UtilsMessages.filterQuotes(quotes, PrefManager.instance().getSelectedGender());
                        initAdapterWithQuotes(UtilsMessages.getWeightedRandomQuotes(quotes, 20));
                    }
                }
            });
        }

        private void loadTextRecommendations() {
            isDataLoading.set(true);
            if (intentionId == null) {
                DataManager.loadPopularTexts(Utils.getFilenameFromUri(imageUrl)).subscribe(quotes -> {
                    isDataLoading.set(false);
                    initAdapterWithQuotes(quotes);
                });
            } else {
                DataManager.loadPopularTextsForIntention(intentionId).subscribe(quotes -> {
                    isDataLoading.set(false);
                    if (quotes != null && !PrefManager.instance().isFirstTimeAction(imageUrl)) {
                        Collections.shuffle(quotes);
                    }
                    initAdapterWithQuotes(quotes);
                });
            }
        }

        private QuoteSelectedListener quoteSelectedListener = (quote, view) -> {
            AnalyticsHelper.setImageTextContext(AnalyticsHelper.ImageTextContexts.NORMAL);
            AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.TEXT, AnalyticsHelper.Events.TEXT_ADDED, quote.getTextId());
            sendMessage(quote);
        };

        private void initAdapterWithQuotes(List<Quote> quotes) {
            if (quotes != null && quotes.size() > 0) {
                QuotesAdapter quotesAdapter = new QuotesAdapter(quotes, quoteSelectedListener);
                binding.recyclerQuotes.setAdapter(quotesAdapter);
            } else {
                isTextEnabled.set(false);
            }
        }
    }
}

