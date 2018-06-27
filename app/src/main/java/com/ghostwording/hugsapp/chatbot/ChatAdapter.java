package com.ghostwording.hugsapp.chatbot;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghostwording.hugsapp.GifPreviewActivity;
import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.chatbot.model.BotSequence;
import com.ghostwording.hugsapp.chatbot.model.CarouselMessage;
import com.ghostwording.hugsapp.chatbot.model.ChatMessage;
import com.ghostwording.hugsapp.model.DailySuggestion;
import com.ghostwording.hugsapp.chatbot.model.GifImageModel;
import com.ghostwording.hugsapp.model.SuggestionsModel;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.textimagepreviews.TextsRecommendationActivity;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.Utils;
import com.ghostwording.hugsapp.widget.RoundedCornersTransformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BindingHolder> {

    private static final int MAX_CARD_HEIGHT = 600;
    private static final int MAX_TEXT_LENGHT = 80;

    private List<ChatMessage> chatMessages;
    private RecyclerView recyclerView;
    private AppCompatActivity activity;
    private BotCommandsView botCommandsView;
    private int width;
    private boolean onBoarding = false;

    @DrawableRes
    private Integer botAvatarResource = R.drawable.ic_huggy_avatar;

    interface MessageType {
        int BOT_MESSAGE = 0;
        int SELF_MESSAGE = 1;
        int BOT_COMMANDS = 2;
        int BOT_GIF_MESSAGE = 3;
        int BOT_USER_MESSAGE = 4;
        int BOT_LINK = 5;
        int BOT_VIDEO = 6;
        int SELF_IMAGE_MESSAGE = 7;
        int BOT_CAROUSEL = 8;
    }

    public void setOnBoarding() {
        onBoarding = true;
    }

    public ChatAdapter(AppCompatActivity activity, RecyclerView recyclerView) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        chatMessages = new ArrayList<>();
        chatMessages.add(null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public void setAvatarImage(@DrawableRes Integer imageResource) {
        botAvatarResource = imageResource;
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(chatMessages.size() - 1, message);
        notifyItemInserted(chatMessages.size() - 2);
        recyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    public String getLastMessage() {
        if (chatMessages.size() > 1) {
            ChatMessage chatMessage = chatMessages.get(chatMessages.size() - 2);
            if (chatMessage.getBotMessage() != null) {
                return chatMessage.getBotMessage().getPrototypeId();
            }
        }
        return null;
    }

    public void setCommands(BotSequence sequence, SequenceHandler.CommandListener commandListener) {
        botCommandsView.setCommands(sequence, commandListener);
    }

    public void scrollToBottom() {
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(chatMessages.size() - 1, 0);
    }

    public BotCommandsView getBotCommandsView() {
        return botCommandsView;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (chatMessage == null) {
            return MessageType.BOT_COMMANDS;
        }
        if (chatMessage.getGifMessage() != null) {
            return MessageType.BOT_GIF_MESSAGE;
        }
        if (chatMessage.getUserProfile() != null) {
            return MessageType.BOT_USER_MESSAGE;
        }
        if (chatMessage.getYoutubeVideo() != null) {
            return MessageType.BOT_VIDEO;
        }
        if (chatMessage.getLink() != null) {
            return MessageType.BOT_LINK;
        }
        if (chatMessage.getSuggestionsModel() != null || chatMessage.getCarouselMessage() != null) {
            return MessageType.BOT_CAROUSEL;
        }
        if (chatMessage.getBotSequence() != null) {
            return MessageType.SELF_IMAGE_MESSAGE;
        }
        return chatMessages.get(position).isSelf ? MessageType.SELF_MESSAGE : MessageType.BOT_MESSAGE;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MessageType.BOT_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opponent_message, parent, false));
            case MessageType.SELF_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_self_message, parent, false));
            case MessageType.BOT_COMMANDS:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_commands_container, parent, false));
            case MessageType.BOT_CAROUSEL:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_carousel_container, parent, false));
            case MessageType.BOT_GIF_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_gif_message, parent, false));
            case MessageType.BOT_LINK:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_link, parent, false));
            case MessageType.SELF_IMAGE_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_self_image_message, parent, false));
            default:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opponent_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        final ChatMessage chatMessage = chatMessages.get(position);

        if (holder.ivAvatar != null) {
            try {
                holder.ivAvatar.setImageResource(botAvatarResource);
            } catch (Exception ex) {
                Logger.e(ex.toString());
            }
        }

        if (chatMessage == null) {
            initBotCommandsView(holder.rowView.findViewById(R.id.container));
            return;
        }

        if (chatMessage.getCarouselMessage() != null) {
            initCarouselMessage(chatMessage.getCarouselMessage(), (ViewGroup) holder.rowView);
            return;
        }

        if (chatMessage.getSuggestionsModel() != null) {
            initCarouselMessage(chatMessage.getSuggestionsModel(), (ViewGroup) holder.rowView);
            return;
        }

        if (chatMessage.getGifMessage() != null) {
            initGifMessageView(chatMessage.getGifMessage(), holder.rowView);
            return;
        }

        if (chatMessage.getLink() != null) {
            initLink(chatMessage.getLink(), holder);
            return;
        }

        if (chatMessage.getBotSequence() != null) {
            initSelfImageMessage(chatMessage.getBotSequence(), holder.rowView);
            return;
        }

        if (chatMessage.getMessage() != null) {
            if (holder.ivImage != null) {
                holder.ivImage.setVisibility(View.GONE);
            }
            if (holder.btnSend != null) {
                holder.btnSend.setVisibility(View.GONE);
            }
            holder.tvMessage.setVisibility(View.VISIBLE);
            holder.tvMessage.setText(chatMessage.getMessage());
        } else if (chatMessage.getBotMessage() != null) {
            ChatMessage.BotMessage botMessage = chatMessage.getBotMessage();
            holder.ivImage.setVisibility(botMessage.getImageLink() == null ? View.GONE : View.VISIBLE);
            holder.btnSend.setVisibility(botMessage.getImageLink() == null ? View.GONE : View.VISIBLE);
            holder.tvMessage.setVisibility(botMessage.getContent() == null ? View.GONE : View.VISIBLE);
            holder.tvMessage.setText(botMessage.getContent());
            if (botMessage.getImageLink() != null) {
                Glide.with(activity)
                        .load(botMessage.getImageLink())
                        .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 15, 0, RoundedCornersTransformation.CornerType.ALL))
                        .crossFade()
                        .into(holder.ivImage);
            }
        }

        holder.rowView.setOnClickListener(v -> {
            if (chatMessage.getBotMessage() != null) {
                AnalyticsHelper.sendEvent(AnalyticsHelper.Events.ASK_HUGGY_MESSAGE_CLICKED, chatMessage.getBotMessage().getTextId());
            }
            openMessagePreview(chatMessage);
        });
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    private void initCarouselMessage(CarouselMessage carouselMessage, ViewGroup view) {
        view.removeAllViews();
        View commandsContainerView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_bot_commands, null);
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);
        for (BotSequence.CarouselElements carouselElement : carouselMessage.getElements()) {
            addCarouselItem(carouselElement, llCommands);
        }
        view.addView(commandsContainerView);
    }

    private void initCarouselMessage(SuggestionsModel suggestionsModel, ViewGroup view) {
        view.removeAllViews();
        View commandsContainerView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_bot_commands, null);
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);
        for (int i = 0; i < Math.min(suggestionsModel.numberOfCards, suggestionsModel.suggestions.size()); i++) {
            addCarouselItem(suggestionsModel.suggestions.get(i), llCommands);
        }
        view.addView(commandsContainerView);
    }

    private void addCarouselItem(BotSequence.CarouselElements carouselElement, LinearLayout container) {
        View menuItem = LayoutInflater.from(container.getContext()).inflate(R.layout.item_command_carousel_no_btn, null);
        ImageView ivIntentionImage = menuItem.findViewById(R.id.intention_image);
        TextView tvTitle = menuItem.findViewById(R.id.intention_title);
        TextView tvSubtitle = menuItem.findViewById(R.id.intention_subtitle);
        Glide.with(activity)
                .load(carouselElement.getPicturePath())
                .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 13, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(ivIntentionImage);
        if (carouselElement.getTitle() != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(carouselElement.getTitle());
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (carouselElement.getSubtitle() != null) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(carouselElement.getSubtitle());
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        menuItem.setOnClickListener(view -> ImagePreviewActivity.start(activity, carouselElement.getPicturePath()));
        container.addView(menuItem);
    }

    private void addCarouselItem(DailySuggestion dailySuggestion, LinearLayout container) {
        View menuItem = LayoutInflater.from(container.getContext()).inflate(R.layout.item_command_card_view, null);
        ImageView ivIntentionImage = menuItem.findViewById(R.id.intention_image);
        Glide.with(activity)
                .load(dailySuggestion.getImageLink())
                .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 13, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(ivIntentionImage);
        menuItem.findViewById(R.id.intention_title).setVisibility(View.GONE);
        String itemText = dailySuggestion.getContent();
        if (itemText.length() > MAX_TEXT_LENGHT) {
            itemText = itemText.substring(0, MAX_TEXT_LENGHT) + "...";
        }
        ((TextView) menuItem.findViewById(R.id.intention_subtitle)).setText(itemText);
        menuItem.setOnClickListener(view -> {
            Quote quote = new Quote();
            quote.setTextId(dailySuggestion.getTextId());
            quote.setPrototypeId(dailySuggestion.getPrototypeId());
            quote.setContent(dailySuggestion.getContent());
            Utils.shareSticker(activity, dailySuggestion.getImageLink(), quote);
        });
        container.addView(menuItem);
    }

    private void initSelfImageMessage(BotSequence botSequence, View view) {
        ImageView ivIntentionImage = view.findViewById(R.id.intention_image);
        Glide.with(activity)
                .load(botSequence.getCarouselElements().getPicturePath())
                .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 13, 0, RoundedCornersTransformation.CornerType.TOP))
                .into(ivIntentionImage);
        ((TextView) view.findViewById(R.id.intention_title)).setText(botSequence.getCarouselElements().getTitle());
        TextView tvSubtitle = view.findViewById(R.id.intention_subtitle);
        if (botSequence.getCarouselElements().getSubtitle() != null) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(botSequence.getCarouselElements().getSubtitle());
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        ((TextView) view.findViewById(R.id.tv_command_label)).setText(botSequence.getLabel());

        view.findViewById(R.id.container).setOnClickListener(view1 -> ImagePreviewActivity.start(activity, botSequence.getCarouselElements().getPicturePath()));
    }

    private void initBotCommandsView(FrameLayout container) {
        botCommandsView = new BotCommandsView(activity, container, this);
        botCommandsView.setAvatarImage(botAvatarResource);
    }

    private void initLink(final ChatMessage.Link link, BindingHolder holder) {
        TextView tvTitle = holder.rowView.findViewById(R.id.tv_title);
        TextView tvSubTitle = holder.rowView.findViewById(R.id.tv_subtitle);
        if (link.link.getImage() != null) {
            holder.ivImage.setVisibility(View.VISIBLE);
            Glide.with(holder.ivImage.getContext()).load(link.link.getImage().getPath()).centerCrop().into(holder.ivImage);
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }
        if (link.link.getSubtitle() != null) {
            tvSubTitle.setVisibility(View.VISIBLE);
            tvSubTitle.setText(link.link.getSubtitle());
        } else {
            tvSubTitle.setVisibility(View.GONE);
        }
        tvTitle.setText(link.link.getTitle());
        SpannableString string = new SpannableString(link.link.getLinkLabel());
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        holder.tvMessage.setText(string);
        holder.tvMessage.setOnClickListener(view -> activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link.link.getUrl()))));
    }

    private void initGifMessageView(final GifImageModel gifImage, View container) {
        final String gifImageUrl = gifImage.imageUrl;
        final ImageView ivImage = container.findViewById(R.id.iv_image);
        final ProgressBar progressBar = container.findViewById(R.id.progress_bar);

        double heightCoef = (width - Utils.convertDpToPixel(60, activity)) / (Integer.parseInt(gifImage.width) * 1.0);
        int height = (int) (Integer.parseInt(gifImage.height) * heightCoef);
        if (height > MAX_CARD_HEIGHT) {
            height = MAX_CARD_HEIGHT;
        }
        ivImage.getLayoutParams().height = height;

        progressBar.setVisibility(View.VISIBLE);
        ivImage.setImageBitmap(null);
        Glide.with(ivImage.getContext()).load(gifImageUrl)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        progressBar.setVisibility(View.GONE);
                        ivImage.setImageURI(Uri.parse("file://" + resource.getAbsolutePath()));
                    }
                });
        container.setOnClickListener(view -> GifPreviewActivity.openGifPreview(activity, gifImageUrl, gifImage.id));
    }

    private void openMessagePreview(ChatMessage chatMessage) {
        if (chatMessage.getBotMessage() != null) {
            BotQuestionsManager.instance().openMessagePreview(activity, chatMessage.getBotMessage());
        }
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private TextView tvMessage;
        private ImageView ivImage;
        private View rowView;
        private ImageView ivAvatar;
        private View btnSend;

        public BindingHolder(View rowView) {
            super(rowView);
            this.rowView = rowView;
            tvMessage = rowView.findViewById(R.id.tv_message);
            ivImage = rowView.findViewById(R.id.iv_image);
            btnSend = rowView.findViewById(R.id.btn_send);
            View avatarView = rowView.findViewById(R.id.iv_avatar);
            if (avatarView != null) {
                ivAvatar = (ImageView) avatarView;
            }
        }
    }

}

