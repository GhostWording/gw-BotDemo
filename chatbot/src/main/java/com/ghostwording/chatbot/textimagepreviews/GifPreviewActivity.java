package com.ghostwording.chatbot.textimagepreviews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.databinding.ActivityGifPreviewBinding;
import com.ghostwording.chatbot.dialog.ShareGifDialog;
import com.ghostwording.chatbot.utils.PostCardRenderer;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsUI;

public class GifPreviewActivity extends AppCompatActivity {

    public static final String GIF_URL = "GifUrl";
    public static final String GIF_CATEGORY = "GifCategory";
    public static final String GIF_ID = "GifId";

    public static void openGifPreview(Activity activity, String imageUrl, String imageId) {
        openGifPreview(activity, imageUrl, imageId, null);
    }

    public static void openGifPreview(final Activity activity, String imageUrl, final String gifId, final String gifCategory) {
        final ProgressDialog progressDialog = UtilsUI.showProgressDialog(activity);
        progressDialog.setMessage(activity.getString(R.string.processing_image));
        PostCardRenderer.requestDownloadFile(activity, imageUrl, Utils.GIF_FILENAME).subscribe(success -> {
            progressDialog.dismiss();
            if (success) {
                Intent intent = new Intent(activity, GifPreviewActivity.class);
                intent.putExtra(GIF_URL, PostCardRenderer.getShareGifUri(activity).toString());
                intent.putExtra(GIF_CATEGORY, gifCategory);
                intent.putExtra(GIF_ID, gifId);
                activity.startActivity(intent);
            }
        });
    }

    private ActivityGifPreviewBinding binding;
    private String gifUrl;
    private String gifId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gif_preview);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gifUrl = getIntent().getStringExtra(GIF_URL);
        gifId = getIntent().getStringExtra(GIF_ID);
        binding.ivGifPreview.setImageURI(Uri.parse(gifUrl));
        binding.btnSend.setOnClickListener(view -> sendGif());
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.GIF_PREVIEW_OPEN);
    }

    private void sendGif() {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Events.GIF_SEND, gifId);
        ShareGifDialog.show(GifPreviewActivity.this, gifUrl);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
