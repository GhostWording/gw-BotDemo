package com.ghostwording.hugsapp.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.Toast;

import com.ghostwording.hugsapp.BaseActivity;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.databinding.ActivityPicturesRecommendationBinding;
import com.ghostwording.hugsapp.dialog.ShareDialog;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.io.Callback;
import com.ghostwording.hugsapp.io.service.PictureService;
import com.ghostwording.hugsapp.model.PopularImages;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.utils.UtilsMessages;
import com.ghostwording.hugsapp.utils.UtilsShare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PicturesRecommendationActivity extends BaseActivity {

    public static final String SHARED_ELEMENT_TRANSITION = "text_transition";
    public static final String QUOTE_ID = "quote_id";
    public static final String QUOTE_PROTOTYPE_ID = "quote_prototype_id";
    public static final String QUOTE_TEXT = "quote_text";
    public static final String IMAGE_PATH = "image_path";
    public static final String ANALYTICS_CONTEXT = "context";

    private ActivityPicturesRecommendationBinding binding;
    private Quote quote;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quote = new Quote();
        quote.setTextId(getIntent().getStringExtra(QUOTE_ID));
        quote.setPrototypeId(getIntent().getStringExtra(QUOTE_PROTOTYPE_ID));
        quote.setContent(getIntent().getStringExtra(QUOTE_TEXT));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pictures_recommendation);
        binding.setViewModel(new PicturesViewModel());

        Toast toast = Toast.makeText(this, R.string.touch_image_to_send, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public class PicturesViewModel {
        public final ObservableField<String> quoteText = new ObservableField<>();
        public final ObservableBoolean isDataLoading = new ObservableBoolean(false);
        public final ObservableField<Boolean> isNoData = new ObservableField<>();

        public PicturesViewModel() {
            this.quoteText.set(quote.getContent());
            isNoData.set(false);
            binding.tvQuote.setMovementMethod(new ScrollingMovementMethod());
            setupRecycleView();
            loadPictureRecommendations();
            binding.tvQuote.setOnClickListener(view -> {
                AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.TEXT, AnalyticsHelper.Events.SHARE_VIA_INTENT, quote.getTextId());
                UtilsShare.shareText(PicturesRecommendationActivity.this, quoteText.get());
            });
            binding.btnBack.setOnClickListener(view -> finish());
        }

        private void setupRecycleView() {
            binding.recyclerPictures.setLayoutManager(new GridLayoutManager(PicturesRecommendationActivity.this, 2));
            binding.recyclerPictures.setHasFixedSize(true);
        }

        private void loadPictureRecommendations() {
            ApiClient.getInstance().popularService.getPopularImagesForText(AppConfiguration.getAppAreaId(), quote.getPrototypeId()).enqueue(new Callback<List<PopularImages>>(PicturesRecommendationActivity.this, isDataLoading) {
                @Override
                public void onDataLoaded(@Nullable List<PopularImages> popularImages) {
                    List<PopularImages.Image> imageList = null;
                    if (popularImages != null && popularImages.size() > 0) {
                        imageList = UtilsMessages.filterPopularImages(popularImages.get(0).getImages());
                    }

                    if (imageList != null && imageList.size() > 3) {
                        binding.recyclerPictures.setAdapter(new PicturesRecommendationAdapter(imageList, imageUrl -> {
                            if (quote.getTextId().equals("0")) {
                                ShareDialog.show(PicturesRecommendationActivity.this, imageUrl, Utils.getFilenameFromUri(imageUrl), quote, false);
                            } else {
                                Utils.shareSticker(PicturesRecommendationActivity.this, imageUrl, quote);
                            }
                        }));
                    } else {
                        displayRandomPictures();
                    }
                }
            });
        }

        private void displayRandomPictures() {
            String imagePath = getIntent().getStringExtra(IMAGE_PATH);
            if (imagePath != null) {
                ApiClient.getInstance().pictureService.getPicturesByPath(imagePath).enqueue(new Callback<List<String>>(PicturesRecommendationActivity.this, isDataLoading) {
                    @Override
                    public void onDataLoaded(@Nullable List<String> result) {
                        onImagesLoaded(result);
                    }
                });
            } else {
                ApiClient.getInstance().pictureService.getDefaultPictures(AppConfiguration.getPictureArea()).enqueue(new Callback<List<String>>(PicturesRecommendationActivity.this, isDataLoading) {
                    @Override
                    public void onDataLoaded(@Nullable List<String> result) {
                        onImagesLoaded(result);
                    }
                });
            }
        }

        private void onImagesLoaded(List<String> result) {
            if (result != null) {
                Collections.shuffle(result);
                List<PopularImages.Image> imageList = new ArrayList<>();
                for (int i = 0; i < Math.min(result.size(), 10); i++) {
                    PopularImages.Image image = new PopularImages.Image();
                    image.setImageLink(PictureService.HOST_URL + result.get(i));
                    imageList.add(image);
                }
                binding.recyclerPictures.setAdapter(new PicturesRecommendationAdapter(imageList, imageUrl -> {
                    if (quote.getTextId().equals("0")) {
                        ShareDialog.show(PicturesRecommendationActivity.this, imageUrl, Utils.getFilenameFromUri(imageUrl), quote, false);
                    } else {
                        Utils.shareSticker(PicturesRecommendationActivity.this, imageUrl, quote);
                    }
                }));
            } else {
                isNoData.set(true);
            }
        }
    }

}
