package com.ghostwording.hugsapp.textimagepreviews;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.databinding.ItemQuoteRecommendationBinding;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.viewmodel.QuoteRecommendationViewModel;

import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.BindingHolder> {
    private TextsRecommendationActivity.QuoteSelectedListener quoteSelectedListener;
    private List<Quote> quotes;
    private boolean isAnimatedSelection;
    private boolean isDisplayNumberOfShares = true;

    public QuotesAdapter(List<Quote> quotes, TextsRecommendationActivity.QuoteSelectedListener quoteSelectedListener) {
        this.quotes = quotes;
        this.quoteSelectedListener = quoteSelectedListener;
    }

    public void setAnimatedSelection(boolean isAnimatedSelection) {
        this.isAnimatedSelection = isAnimatedSelection;
    }

    public void setDisplayNumberOfShares(boolean isDisplayNumberOfShares) {
        this.isDisplayNumberOfShares = isDisplayNumberOfShares;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote_recommendation, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        final ItemQuoteRecommendationBinding quoteBinding = holder.getBinding();
        final Quote quote = quotes.get(position);

        quoteBinding.setViewModel(new QuoteRecommendationViewModel(quote, position, isDisplayNumberOfShares));

        quoteBinding.container.setOnClickListener(v -> {
            if (quoteSelectedListener != null) {
                quoteSelectedListener.onQuoteSelected(quote, quoteBinding.container);
            }
        });

        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (quotes == null) {
            return 0;
        }
        return quotes.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemQuoteRecommendationBinding binding;

        public BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        public ItemQuoteRecommendationBinding getBinding() {
            return binding;
        }
    }

}