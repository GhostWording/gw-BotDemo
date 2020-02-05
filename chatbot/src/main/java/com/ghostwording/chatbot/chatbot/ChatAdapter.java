package com.ghostwording.chatbot.chatbot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.CarouselMessage;
import com.ghostwording.chatbot.chatbot.model.ChatMessage;
import com.ghostwording.chatbot.chatbot.model.GifImageModel;
import com.ghostwording.chatbot.chatbot.model.ImageMessage;
import com.ghostwording.chatbot.dialog.GifPreviewDialog;
import com.ghostwording.chatbot.io.DataLoader;
import com.ghostwording.chatbot.model.DailySuggestion;
import com.ghostwording.chatbot.model.SuggestionsModel;
import com.ghostwording.chatbot.model.YoutubeVideo;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.textimagepreviews.GifPreviewActivity;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsUI;
import com.ghostwording.chatbot.widget.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_CARD_MESSAGE;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_CAROUSEL;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_COMMANDS;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_GIF_MESSAGE;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_GIF_MESSAGE_FULL;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_IMAGE_MESSAGE;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_IMAGE_MESSAGE_FULL;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_LINK;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_SEQUENCE;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_TEXT_MESSAGE;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.BOT_VIDEO;
import static com.ghostwording.chatbot.chatbot.ChatAdapter.MessageType.USER_MESSAGE;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.BindingHolder> {

    private static final int MAX_CARD_HEIGHT = 600;
    private static final int MAX_TEXT_LENGHT = 80;

    private List<ChatMessage> chatMessages;
    private RecyclerView recyclerView;
    private AppCompatActivity activity;
    private BotCommandsView botCommandsView;
    private int width;
    @DrawableRes
    private Integer botAvatarResource = R.drawable.ic_huggy_avatar;

    interface MessageType {
        int BOT_CARD_MESSAGE = 0;
        int USER_MESSAGE = 1;
        int BOT_COMMANDS = 2;
        int BOT_GIF_MESSAGE = 3;
        int BOT_GIF_MESSAGE_FULL = 10;
        int BOT_IMAGE_MESSAGE_FULL = 11;
        int BOT_IMAGE_MESSAGE = 4;
        int BOT_LINK = 5;
        int BOT_VIDEO = 6;
        int BOT_SEQUENCE = 7;
        int BOT_CAROUSEL = 8;
        int BOT_TEXT_MESSAGE = 9;
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

    public void addMessage(ChatMessage message) {
        chatMessages.add(chatMessages.size() - 1, message);
        notifyItemInserted(chatMessages.size() - 2);
        recyclerView.scrollToPosition(chatMessages.size() - 1);
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
            return BOT_COMMANDS;
        }
        switch (chatMessage.messageType) {
            case GIF_IMAGE:
                return BOT_GIF_MESSAGE;
            case VIDEO:
                return BOT_VIDEO;
            case LINK:
                return BOT_LINK;
            case CAROUSEL:
                return BOT_CAROUSEL;
            case SEQUENCE:
                return BOT_SEQUENCE;
            case IMAGE:
                return BOT_IMAGE_MESSAGE;
            case IMAGE_FULL_SCREEN:
                return BOT_IMAGE_MESSAGE_FULL;
            case GIF_IMAGE_FULL_SCREEN:
                return BOT_GIF_MESSAGE_FULL;
            case QUOTE:
                return BOT_TEXT_MESSAGE;
            case TEXT:
                if (chatMessage.isSelf) {
                    return USER_MESSAGE;
                } else {
                    return BOT_TEXT_MESSAGE;
                }
            default:
                return BOT_CARD_MESSAGE;
        }
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BOT_CARD_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_card_message, parent, false));
            case USER_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_user_message, parent, false));
            case BOT_COMMANDS:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_commands_container, parent, false));
            case BOT_CAROUSEL:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_carousel_container, parent, false));
            case BOT_GIF_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_gif_message, parent, false));
            case BOT_GIF_MESSAGE_FULL:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_gif_message_full, parent, false));
            case BOT_LINK:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_link, parent, false));
            case BOT_VIDEO:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_video, parent, false));
            case BOT_IMAGE_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_image_message, parent, false));
            case BOT_IMAGE_MESSAGE_FULL:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_image_message_full, parent, false));
            case BOT_TEXT_MESSAGE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_text_message, parent, false));
            case BOT_SEQUENCE:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_sequence_message, parent, false));
            default:
                return new BindingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bot_card_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        final ChatMessage chatMessage = chatMessages.get(position);
        UtilsUI.showBotAvatar(holder.rowView, position, botAvatarResource);

        if (chatMessage == null) {
            initBotCommandsView(holder.rowView.findViewById(R.id.container));
            return;
        }

        Logger.e(chatMessage.getMessage());

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

        if (chatMessage.getYoutubeVideo() != null) {
            initVideo(chatMessage.getYoutubeVideo(), holder);
            return;
        }

        if (chatMessage.getLink() != null) {
            initLink(chatMessage.getLink(), holder);
            return;
        }

        if (chatMessage.getBotSequence() != null) {
            initSequenceCarouselMessage(chatMessage.getBotSequence(), holder.rowView);
            return;
        }

        if (chatMessage.getQuote() != null) {
            if (holder.btnNotification != null) {
                holder.btnNotification.setVisibility(View.GONE);
            }
            holder.tvMessage.setText(chatMessage.getQuote().getContent());
            holder.rowView.setOnClickListener(view -> BotQuestionsManager.instance().openMessagePreview(activity, chatMessage.getQuote()));
            return;
        }

        if (chatMessage.getImageMessage() != null) {
            initImageMessage(chatMessage.getImageMessage(), holder);
            return;
        }

        if (chatMessage.getMessage() != null) {
            if (chatMessage.getStep() != null && chatMessage.getStep().getAnimatedGifTranslation() != null) {
                final String gifUrl = String.format(AppConfiguration.GIPHY_URL_TEMPLATE, chatMessage.getStep().getAnimatedGifTranslation().getPath());
                holder.btnNotification.setOnClickListener(view -> GifPreviewDialog.show(activity, gifUrl, 300));
                holder.btnNotification.setVisibility(View.VISIBLE);
            } else {
                if (holder.btnNotification != null) {
                    holder.btnNotification.setVisibility(View.GONE);
                }
            }
            holder.tvMessage.setText(chatMessage.getMessage());
            holder.rowView.setOnClickListener(null);
            return;
        }


        if (chatMessage.getBotMessage() != null) {
            ChatMessage.BotMessage botMessage = chatMessage.getBotMessage();
            holder.tvMessage.setText(botMessage.getContent());
            Glide.with(activity)
                    .load(DataLoader.getImageUrl(activity, botMessage.getImageLink()))
                    .bitmapTransform(new CenterCrop(activity), new RoundedCornersTransformation(activity, 15, 0, RoundedCornersTransformation.CornerType.ALL))
                    .crossFade()
                    .into(holder.ivImage);
            holder.rowView.setOnClickListener(v -> {
                AnalyticsHelper.sendEvent(AnalyticsHelper.Events.ASK_HUGGY_MESSAGE_CLICKED, chatMessage.getBotMessage().getTextId());
                BotQuestionsManager.instance().openMessagePreview(activity, chatMessage.getBotMessage());
            });
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    private void initImageMessage(ImageMessage imageMessage, BindingHolder holder) {
        holder.ivImage.setImageBitmap(null);
        Glide.with(activity).load(DataLoader.getImageUrl(activity, imageMessage.imageUrl)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                double heightCoef;
                if (imageMessage.fullWidth) {
                    heightCoef = (width - Utils.convertDpToPixel(20, activity)) / (resource.getWidth() * 1.0);
                } else {
                    heightCoef = (Utils.convertDpToPixel(200, activity)) / (resource.getWidth() * 1.0);
                }
                int height = (int) (resource.getHeight() * heightCoef);
                holder.ivImage.getLayoutParams().height = height;
                holder.ivImage.setImageBitmap(resource);
            }
        });
        holder.rowView.setOnClickListener(view -> ImagePreviewActivity.start(activity, imageMessage.imageUrl));
    }

    private void initCarouselMessage(CarouselMessage carouselMessage, ViewGroup view) {
        view.removeAllViews();
        View commandsContainerView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_bot_commands, null);
        commandsContainerView.findViewById(R.id.tv_scroll_for_more).setVisibility(View.VISIBLE);
        LinearLayout llCommands = commandsContainerView.findViewById(R.id.container_commands);
        for (BotSequence.CarouselElements carouselElement : carouselMessage.getElements()) {
            addCarouselItem(carouselElement, llCommands);
        }
        view.addView(commandsContainerView);
    }

    private void initCarouselMessage(SuggestionsModel suggestionsModel, ViewGroup view) {
        view.removeAllViews();
        View commandsContainerView = LayoutInflater.from(view.getContext()).inflate(R.layout.item_bot_commands, null);
        commandsContainerView.findViewById(R.id.tv_scroll_for_more).setVisibility(View.VISIBLE);
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
                .load(DataLoader.getImageUrl(activity, carouselElement.getPicturePath()))
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
        if (carouselElement.getTitle() != null && carouselElement.getTitle().equals(".")) {
            menuItem.findViewById(R.id.container_text).setVisibility(View.GONE);
        }
        menuItem.setOnClickListener(view -> ImagePreviewActivity.start(activity, carouselElement.getPicturePath()));
        container.addView(menuItem);
    }

    private void addCarouselItem(DailySuggestion dailySuggestion, LinearLayout container) {
        View menuItem = LayoutInflater.from(container.getContext()).inflate(R.layout.item_command_card_view, null);
        ImageView ivIntentionImage = menuItem.findViewById(R.id.intention_image);
        Glide.with(activity)
                .load(DataLoader.getImageUrl(activity, dailySuggestion.getImageLink()))
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

    private void initSequenceCarouselMessage(BotSequence botSequence, View view) {
        ImageView ivIntentionImage = view.findViewById(R.id.intention_image);
        TextView tvTitle = view.findViewById(R.id.intention_title);
        TextView tvSubtitle = view.findViewById(R.id.intention_subtitle);
        UtilsUI.loadImageRoundedCorners(ivIntentionImage, botSequence.getCarouselElements().getPicturePath());
        if (botSequence.getCarouselElements().getTitle() == null || botSequence.getCarouselElements().getTitle().equals(".")) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(botSequence.getCarouselElements().getTitle());
        }
        if (botSequence.getCarouselElements().getSubtitle() != null) {
            tvSubtitle.setVisibility(View.VISIBLE);
            tvSubtitle.setText(botSequence.getCarouselElements().getSubtitle());
        } else {
            tvSubtitle.setVisibility(View.GONE);
        }
        ((TextView) view.findViewById(R.id.tv_command_label)).setText(botSequence.getLabel());
        view.findViewById(R.id.container).setOnClickListener(view1 -> ImagePreviewActivity.start(activity, botSequence.getCarouselElements().getPicturePath()));
    }

    private void initVideo(final YoutubeVideo youtubeVideo, BindingHolder holder) {
        holder.tvMessage.setText(youtubeVideo.label);
        UtilsUI.loadImageCenterCrop(holder.ivImage, String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg", youtubeVideo.id));
        holder.rowView.setOnClickListener(view -> {

        });
    }

    private void initBotCommandsView(FrameLayout container) {
        botCommandsView = new BotCommandsView(activity, container);
    }

    private void initLink(final ChatMessage.Link link, BindingHolder holder) {
        TextView tvTitle = holder.rowView.findViewById(R.id.tv_title);
        TextView tvSubTitle = holder.rowView.findViewById(R.id.tv_subtitle);
        if (link.link.getImage() != null) {
            holder.ivImage.setVisibility(View.VISIBLE);
            UtilsUI.loadImageCenterCrop(holder.ivImage, link.link.getImage().getPath());
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
        final GifImageView ivImage = container.findViewById(R.id.iv_image);
        final ProgressBar progressBar = container.findViewById(R.id.progress_bar);

        double heightCoef;
        if (gifImage.isFullWidth()) {
            heightCoef = (width - Utils.convertDpToPixel(20, activity)) / (Integer.parseInt(gifImage.width) * 1.0);
        } else {
            heightCoef = (width - Utils.convertDpToPixel(60, activity)) / (Integer.parseInt(gifImage.width) * 1.0);
        }

        int height = (int) (Integer.parseInt(gifImage.height) * heightCoef);
        if (height > MAX_CARD_HEIGHT) {
            height = MAX_CARD_HEIGHT;
        }
        ivImage.getLayoutParams().height = height;
        UtilsUI.loadGifImage(ivImage, gifImageUrl, progressBar);
        container.setOnClickListener(view -> GifPreviewActivity.openGifPreview(activity, gifImageUrl, gifImage.id));
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {

        private TextView tvMessage;
        private ImageView ivImage;
        private View rowView;
        private View btnNotification;

        public BindingHolder(View rowView) {
            super(rowView);
            this.rowView = rowView;
            tvMessage = rowView.findViewById(R.id.tv_message);
            ivImage = rowView.findViewById(R.id.iv_image);
            btnNotification = rowView.findViewById(R.id.iv_notification);
        }
    }

}
