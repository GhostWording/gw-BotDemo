package com.ghostwording.hugsapp.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.FragmentTranslationPreviewBinding;
import com.ghostwording.hugsapp.dialog.ShareDialog;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.PostCardRenderer;

public class TranslationPreviewFragment extends Fragment {

    private Quote quote;
    private String imageUrl;
    private boolean isBubble;

    public static TranslationPreviewFragment newInstance(Quote quote, String imageUrl, boolean isBubble) {
        TranslationPreviewFragment translationPreviewFragment = new TranslationPreviewFragment();
        translationPreviewFragment.quote = quote;
        translationPreviewFragment.imageUrl = imageUrl;
        translationPreviewFragment.isBubble = isBubble;
        return translationPreviewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translation_preview, container, false);
        final FragmentTranslationPreviewBinding binding = DataBindingUtil.bind(rootView);

        binding.ivPreview.setOnClickListener(v -> ShareDialog.show(getActivity(), imageUrl, quote, isBubble));

        PostCardRenderer.renderPostCardPreviewImage(getActivity(), imageUrl, quote == null ? null : quote.getContent(), isBubble).subscribe(bitmap -> binding.ivPreview.setImageBitmap(bitmap));

        return rootView;
    }

}
