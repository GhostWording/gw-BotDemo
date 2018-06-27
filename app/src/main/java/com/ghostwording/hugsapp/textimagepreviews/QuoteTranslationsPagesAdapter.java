package com.ghostwording.hugsapp.textimagepreviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.io.Callback;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.model.texts.QuoteLanguageComparator;
import com.ghostwording.hugsapp.model.texts.TranslationLanguage;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.LocaleManager;
import com.ghostwording.hugsapp.utils.PrefManager;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.utils.UtilsMessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class QuoteTranslationsPagesAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> tabFragments = new ArrayList<>();
    private List<String> tabTitles = new ArrayList<>();
    private Context context;
    private List<Quote> items;
    private TranslationCustomLanguageFragment translationCustomLanguageFragment;

    public QuoteTranslationsPagesAdapter(final AppCompatActivity activity, final Quote quote, final String imageUrl, final boolean isBubble) {
        super(activity.getSupportFragmentManager());
        this.context = activity.getApplicationContext();
        this.items = new ArrayList<>();
        items.add(quote);
        tabFragments.add(TranslationPreviewFragment.newInstance(quote, imageUrl, isBubble));
        tabTitles.add(quote.getCulture());

        ApiClient.getInstance().coreApiService.getQuotesFromRealizations(AppConfiguration.getAppAreaId(), quote.getPrototypeId()).enqueue(new Callback<List<Quote>>(activity) {
            @Override
            public void onDataLoaded(@Nullable List<Quote> quotes) {
                if (quotes != null) {
                    quotes = UtilsMessages.filterQuotes(quotes, PrefManager.instance().getSelectedGender(), false);
                    Collections.sort(quotes, new QuoteLanguageComparator());
                    for (int i = 0; i < quotes.size(); i++) {
                        if (!quote.getTextId().equals(quotes.get(i).getTextId())) {
                            items.add(quotes.get(i));
                            tabTitles.add(quotes.get(i).getCulture());
                            tabFragments.add(TranslationPreviewFragment.newInstance(quotes.get(i), imageUrl, isBubble));
                        }
                    }
                    notifyDataSetChanged();
                }
                ApiClient.getInstance().translationService.getSupportedLanguages(Utils.getLanguageString(LocaleManager.getSelectedLanguage(context))).enqueue(new retrofit2.Callback<List<TranslationLanguage>>() {
                    @Override
                    public void onResponse(Call<List<TranslationLanguage>> call, Response<List<TranslationLanguage>> response) {
                        if (response.isSuccessful()) {
                            translationCustomLanguageFragment = TranslationCustomLanguageFragment.newInstance(quote, imageUrl, isBubble, response.body());
                            tabFragments.add(translationCustomLanguageFragment);
                            tabTitles.add(activity.getString(R.string.translations));
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<TranslationLanguage>> call, Throwable t) {

                    }
                });
            }
        });


    }

    @Override
    public Fragment getItem(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getCount() {
        return tabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabTitles.get(position) == null) {
            return "";
        }
        String culture = tabTitles.get(position).toLowerCase();
        if (culture.contains("es")) {
            return context.getString(R.string.spanish_language);
        }
        if (culture.contains("fr")) {
            return context.getString(R.string.french_language);
        }
        if (culture.contains("en")) {
            return context.getString(R.string.english_language);
        }
        return tabTitles.get(position);
    }

    public Quote getQuote(int position) {
        if (position < items.size()) {
            return items.get(position);
        }
        return translationCustomLanguageFragment != null ? translationCustomLanguageFragment.getQuote() : null;
    }
}
