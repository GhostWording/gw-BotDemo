package com.ghostwording.chatbot.textimagepreviews;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.ghostwording.chatbot.BaseActivity;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.databinding.ActivityImageGalleryBinding;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.model.listeners.ImageSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageGalleryActivity extends BaseActivity {

    public static final String IMAGE_PATH = "image_path";
    public static final String SEARCH_KEYWORD = "search_keyword";
    public static final String TITLE = "title";
    public static final String INTENTION_ID = "intention_id";

    public final ObservableBoolean isDataLoading = new ObservableBoolean(false);

    private String imagePath;
    private String intentionId;
    private ActivityImageGalleryBinding binding;
    private GridLayoutManager listLayoutManager;

    public static void show(Activity activity, String imagePath, String searchKeyword, String title, String intentionId) {
        Intent intent = new Intent(activity, ImageGalleryActivity.class);
        intent.putExtra(ImageGalleryActivity.IMAGE_PATH, imagePath);
        intent.putExtra(ImageGalleryActivity.SEARCH_KEYWORD, searchKeyword);
        intent.putExtra(ImageGalleryActivity.TITLE, title);
        intent.putExtra(ImageGalleryActivity.INTENTION_ID, intentionId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePath = getIntent().getStringExtra(IMAGE_PATH);
        intentionId = getIntent().getStringExtra(INTENTION_ID);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_gallery);
        binding.setViewModel(this);
        if (getIntent().getStringExtra(TITLE) != null) {
            binding.toolbar.setTitle(getIntent().getStringExtra(TITLE));
        }
        listLayoutManager = new GridLayoutManager(this, 3);
        binding.recyclerMenuItems.setLayoutManager(listLayoutManager);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadImagesByPath();
    }

    private void loadImagesByPath() {
        ApiClient.getInstance().pictureService.getPicturesByPath(imagePath).enqueue(new Callback<List<String>>(this, isDataLoading) {
            @Override
            public void onDataLoaded(@Nullable List<String> pictures) {
                if (pictures != null) {
                    List<String> picturesFullUrls = new ArrayList<>();
                    for (String pictureRelativePath : pictures) {
                        if (!picturesFullUrls.contains(PictureService.HOST_URL + pictureRelativePath)) {
                            picturesFullUrls.add(PictureService.HOST_URL + pictureRelativePath);
                        }
                    }
                    Collections.shuffle(picturesFullUrls);
                    final ImagesAdapter imagesAdapter = new ImagesAdapter(ImageGalleryActivity.this, picturesFullUrls);
                    imagesAdapter.setImageSelectedListener(imageSelectedListener);
                    binding.recyclerMenuItems.setAdapter(imagesAdapter);

                    listLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch (imagesAdapter.getItemViewType(position)) {
                                case 0:
                                    return 1;
                                case 1:
                                    return 3;
                                default:
                                    return -1;
                            }
                        }
                    });
                }
            }
        });
    }

    private ImageSelectedListener imageSelectedListener = new ImageSelectedListener() {
        @Override
        public void onImageSelected(String imageUrl, String optional) {
            Intent intent = new Intent(ImageGalleryActivity.this, TextsRecommendationActivity.class);
            intent.putExtra(TextsRecommendationActivity.IMAGE_URL, imageUrl);
            intent.putExtra(TextsRecommendationActivity.IMAGE_PATH, imagePath);
            intent.putExtra(TextsRecommendationActivity.INTENTION_ID, intentionId);
            startActivity(intent);
        }
    };
}
