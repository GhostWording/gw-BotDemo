package com.ghostwording.chatbot.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.databinding.FragmentTranslationCustomLanguageBinding;
import com.ghostwording.chatbot.dialog.ShareDialog;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.model.texts.TranslationLanguage;
import com.ghostwording.chatbot.model.texts.TranslationResult;
import com.ghostwording.chatbot.utils.PostCardRenderer;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TranslationCustomLanguageFragment extends Fragment {

    private Quote quote;
    private String imageUrl;
    private boolean isBubble;
    private Quote translatedQuote;
    private List<TranslationLanguage> translationLanguages;
    private FragmentTranslationCustomLanguageBinding binding;

    public static TranslationCustomLanguageFragment newInstance(Quote quote, String imageUrl, boolean isBubble, List<TranslationLanguage> translationLanguages) {
        TranslationCustomLanguageFragment translationCustomLanguageFragment = new TranslationCustomLanguageFragment();
        translationCustomLanguageFragment.quote = quote;
        translationCustomLanguageFragment.imageUrl = imageUrl;
        translationCustomLanguageFragment.isBubble = isBubble;
        translationCustomLanguageFragment.translationLanguages = translationLanguages;
        return translationCustomLanguageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_translation_custom_language, container, false);
        binding = DataBindingUtil.bind(rootView);

        translatedQuote = quote;

        binding.ivPreview.setOnClickListener(v -> ShareDialog.show(getActivity(), imageUrl, translatedQuote, isBubble));

        initLanguages(translationLanguages);
        return rootView;
    }

    private void initLanguages(final List<TranslationLanguage> languageItems) {
        if (languageItems == null || getContext() == null) return;

        final List<String> acceptedLanguageNames = new ArrayList<>();
        acceptedLanguageNames.add(getString(R.string.please_choose_language));
        for (TranslationLanguage item : languageItems) {
            acceptedLanguageNames.add(item.getName());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, acceptedLanguageNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setPrompt(getString(R.string.settings_language));
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (quote == null) return;
                PrefManager.instance().setLastLanguageIndex(position);

                if (position == 0) {
                    translatedQuote = quote;
                    displayQuote();
                    return;
                }

                ApiClient.getInstance().translationService.translate(quote.getTextId(), quote.getContent(), quote.getCulture().substring(0, 2), languageItems.get(position - 1).getCode()).enqueue(new Callback<TranslationResult>(getActivity()) {
                    @Override
                    public void onDataLoaded(@Nullable TranslationResult result) {
                        if (result != null) {
                            AnalyticsHelper.sendEvent(AnalyticsHelper.Events.TRANSLATE_TO, result.getTo());
                            translatedQuote = new Quote();
                            translatedQuote.setTextId(quote.getTextId());
                            translatedQuote.setPrototypeId(quote.getPrototypeId());
                            translatedQuote.setCulture(result.getTo());
                            translatedQuote.setContent(result.getTranslation());
                            displayQuote();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinner.setSelection(PrefManager.instance().getLastLanguageIndex());
    }

    private void displayQuote() {
        PostCardRenderer.renderPostCardPreviewImage(getActivity(), imageUrl, translatedQuote == null ? null : translatedQuote.getContent(), isBubble).subscribe(bitmap -> binding.ivPreview.setImageBitmap(bitmap));
    }

    public Quote getQuote() {
        return translatedQuote;
    }

}


