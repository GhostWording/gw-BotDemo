package com.ghostwording.chatbot.chatbot;

import android.os.Handler;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghostwording.chatbot.ChatBotApplication;
import com.ghostwording.chatbot.io.DataLoader;
import com.ghostwording.chatbot.model.texts.QuoteLanguageComparator;
import com.ghostwording.chatbot.textimagepreviews.GifPreviewActivity;
import com.ghostwording.chatbot.R;
import com.ghostwording.chatbot.analytics.AnalyticsHelper;
import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.chatbot.model.CarouselMessage;
import com.ghostwording.chatbot.chatbot.model.ChatMessage;
import com.ghostwording.chatbot.chatbot.model.GifImageModel;
import com.ghostwording.chatbot.chatbot.model.ImageMessage;
import com.ghostwording.chatbot.io.ApiClient;
import com.ghostwording.chatbot.io.Callback;
import com.ghostwording.chatbot.io.DataManager;
import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.model.DailySuggestion;
import com.ghostwording.chatbot.model.GifImages;
import com.ghostwording.chatbot.model.GifResponse;
import com.ghostwording.chatbot.model.PopularImages;
import com.ghostwording.chatbot.model.SuggestionsModel;
import com.ghostwording.chatbot.model.YoutubeVideo;
import com.ghostwording.chatbot.model.recipients.Recipient;
import com.ghostwording.chatbot.model.requests.FragmentRequest;
import com.ghostwording.chatbot.model.texts.PopularTexts;
import com.ghostwording.chatbot.model.texts.Quote;
import com.ghostwording.chatbot.textimagepreviews.PickHelper;
import com.ghostwording.chatbot.utils.AppConfiguration;
import com.ghostwording.chatbot.utils.Logger;
import com.ghostwording.chatbot.utils.PrefManager;
import com.ghostwording.chatbot.utils.Utils;
import com.ghostwording.chatbot.utils.UtilsMessages;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

import static com.ghostwording.chatbot.chatbot.SequenceHandler.ContentType.GIF;
import static com.ghostwording.chatbot.chatbot.SequenceHandler.ContentType.IMAGE;
import static com.ghostwording.chatbot.chatbot.SequenceHandler.ContentType.IMAGE_TEXT;
import static com.ghostwording.chatbot.chatbot.SequenceHandler.ContentType.TEXT;

public class SequenceHandler {

    public interface StepTypes {
        String IMAGE = "Image";
        String TEXT = "Text";
        String GIF = "AnimatedGif";
        String ACTION = "Action";
        String VIDEO = "Video";
        String PAUSE = "Pause";
        String LINK = "ShowPersistentLink";
        String WAIT_FOR_USER_INPUT = "WaitForUserInput";
    }

    public interface ContentType {
        String IMAGE_TEXT = "TextImage";
        String TEXT = "Text";
        String IMAGE = "Image";
        String GIF = "Gif";
    }

    public interface Actions {
        String VOTE = "DoVote";
        String SHOW_SURVEY_RESULTS = "ShowSurveyResults";
        String SEND_MESSAGE_TO_RECIPIENT = "MessagesByRecipient";
        String SHOW_CARD = "ShowCards";
        String SHOW_POPULAR_IMAGES = "ShowPopularImageFromTheme";
        String SHOW_POPULAR_TEXTS = "ShowPopularTextFromIntention";
        String SHOW_POPULAR_CARDS = "ShowCardFromIntention";
        String SHOW_CAROUSEL_FROM_LIST = "ShowCarouselFromList";
        String SHOW_CARD_MENU = "ShowCardsWithMenu";
        String SHOW_RANDOM_GIF = "ShowRandomGifFromList";
        String SHOW_CAROUSEL = "ShowCarouselFromIntention";
        String SHOW_TRENDING_CONTENT = "ShowTrendingContent";
        String SHOW_TRENDING_CONTENT_MENU = "ShowTrendingContentWithMenu";
        String SET_USER_PROPERTY = "SetUserProperty";
        String SHOW_ADVERT = "ShowAdvert";
        String RATE_US = "RateUs";
        String REDIRECT = "RedirectTo";
        String HIDE_TYPING_BAR = "TypingBarHide";
        String SHOW_TYPING_BAR = "TypingBarShow";
        String FEEDBACK = "Feedback";
        String EXIT = "Exit";
        String EXECUTE_FRAGMENT = "NativeExecuteFragment";
        String EXECUTE_FRAGMENT2 = "ExecuteFragment";
        String SHOW_TEXT_PROTOTYPE = "ShowTextForPrototype";
        String SHOW_CARD_PROTOTYPE = "ShowCardForPrototype";
        String WAIT_FOR_USER_INPUT = "WaitForUserInput";
    }

    public static abstract class SequenceListener {
        protected abstract void onSequenceEnd(boolean exit);

        protected void hideTypingBar() {

        }

        protected void showTypingBar() {

        }

    }

    public interface CommandListener {
        void onCommandChoose(BotSequence selectedCommand);
    }

    private ChatAdapter chatAdapter;
    private BotSequence rootSequence;
    private BotSequence currentSequence;
    private final BotSequence masterSequence;
    private SequenceListener sequenceListener;

    private int currentStep;
    private int currentLevel = 0;
    private Handler handler = new Handler();
    private Handler autoSelectHandler = new Handler();
    private Handler userInputWaitHandler = new Handler();
    private String botName;

    public SequenceHandler(String botName, ChatAdapter chatAdapter, BotSequence botSequence, SequenceListener sequenceListener) {
        this.chatAdapter = chatAdapter;
        this.rootSequence = botSequence;
        this.currentSequence = rootSequence;
        this.masterSequence = botSequence;
        this.sequenceListener = sequenceListener;
        this.botName = botName;

        AnalyticsHelper.setScreenName(masterSequence.getLocation());
        AnalyticsHelper.setImageTextContext(masterSequence.getId());
        String sequenceText = null;
        if (botSequence.getSteps() != null && botSequence.getSteps().size() > 0) {
            sequenceText = botSequence.getSteps().get(botSequence.getSteps().size() - 1).getLabel();
        }
        AnalyticsHelper.sendEvent(masterSequence.getMasterGroup(), AnalyticsHelper.Events.HUGGY_SEQUENCE_START, botSequence.getId(), sequenceText);
    }

    public void startStep() {
        if (chatAdapter.getBotCommandsView() != null) {
            chatAdapter.getBotCommandsView().showLoadingView();
        }
        handler.postDelayed(() -> doStep(), 1000);
    }

    private void doStep() {
        if (currentSequence.getSteps() == null || currentStep >= currentSequence.getSteps().size()) {
            onStepsEnd();
            return;
        }

        if (chatAdapter.getBotCommandsView() != null) {
            chatAdapter.getBotCommandsView().clearCommand();
        }

        BotSequence.Step step = currentSequence.getSteps().get(currentStep);
        currentStep++;

        switch (step.getType()) {
            case StepTypes.VIDEO:
                handleVideoStep(step);
                break;
            case StepTypes.WAIT_FOR_USER_INPUT:
                waitForUserInput(step);
                break;
            case StepTypes.ACTION:
                handleActionStep(step);
                break;
            case StepTypes.IMAGE:
                handleImageStep(step);
                break;
            case StepTypes.TEXT:
                chatAdapter.addMessage(new ChatMessage(step.getLabel(), false));
                startStep();
                break;
            case StepTypes.GIF:
                handleGifStep(step);
                break;
            case StepTypes.PAUSE:
                handlePauseStep(step);
                break;
            case StepTypes.LINK:
                chatAdapter.addMessage(new ChatMessage(new ChatMessage.Link(step)));
                startStep();
                break;
            default:
                startStep();
        }
    }

    private void onStepsEnd() {
        currentStep = 0;
        if (currentSequence.getCommands() != null) {
            for (BotSequence botSequence : currentSequence.getCommands()) {
                if (botSequence != null && botSequence.getAutoSelect() != null) {
                    autoSelectHandler.postDelayed(() -> handleCommand(botSequence), botSequence.getAutoSelect().getMs());
                }
            }
            chatAdapter.setCommands(currentSequence, this::handleCommand);
            chatAdapter.scrollToBottom();
        } else if (currentSequence.getLinksToFragment() != null) {
            showFragment(currentSequence.getLinksToFragment());
        } else if (currentSequence.getLinksTo() != null) {
            rootSequence = currentSequence.getLinksTo();
            currentSequence = currentSequence.getLinksTo();
            startStep();
        } else {
            sequenceListener.onSequenceEnd(false);
        }
    }

    private void executeFragmentStep(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        DataLoader.instance().loadSequenceById(step.getParameters().getFragmentPath()).subscribe(botSequence -> new SequenceHandler(botName, chatAdapter, botSequence, new SequenceHandler.SequenceListener() {
            @Override
            protected void onSequenceEnd(boolean exit) {
                if (exit) {
                    sequenceListener.onSequenceEnd(true);
                } else {
                    startStep();
                }
            }
        }).startStep(), throwable -> startStep());
    }

    private void showFragment(BotSequence.LinksToFragment linksToFragment) {
        chatAdapter.getBotCommandsView().showLoadingView();
        DataLoader.instance().loadSequenceById(linksToFragment.getFragmentPath()).subscribe(botSequence -> {
            rootSequence = botSequence;
            currentSequence = botSequence;
            startStep();
        }, throwable -> sequenceListener.onSequenceEnd(false));
    }

    public void handleCommand(BotSequence command) {
        autoSelectHandler.removeCallbacksAndMessages(null);
        PrefManager.instance().setBotUsed(botName);
        currentLevel++;
        if (command == null) {
            chatAdapter.addMessage(new ChatMessage(chatAdapter.getActivity().getString(R.string.skip_bot), true));
            sequenceListener.onSequenceEnd(false);
            return;
        }

        AnalyticsHelper.setIsLast(command.getType().equals("Leaf"));
        AnalyticsHelper.sendEvent(masterSequence.getMasterGroup(), AnalyticsHelper.Events.HUGGY_COMMAND_SELECTED, command.getId());
        AnalyticsHelper.sendEvent(masterSequence.getMasterGroup(), AnalyticsHelper.Events.HUGGY_SEQUENCE_NEXT, command.getId(), masterSequence.getId(), String.valueOf(currentLevel));

        if (command.getCarouselElements() != null) {
            if (currentSequence.isDisplayLargeCard()) {
                chatAdapter.addMessage(new ChatMessage(new ImageMessage(command.getCarouselElements().getPicturePath(), true)));
                chatAdapter.addMessage(new ChatMessage(command.getLabel(), true));
            } else if (currentSequence.isDisplayTextOnly()) {
                chatAdapter.addMessage(new ChatMessage(command.getLabel(), true));
            } else {
                chatAdapter.addMessage(new ChatMessage(command, true));
            }
        } else {
            chatAdapter.addMessage(new ChatMessage(command.getLabel(), true));
        }
        rootSequence = currentSequence;
        currentSequence = command;
        if (currentSequence.getSteps() == null || currentStep >= currentSequence.getSteps().size()) {
            onStepsEnd();
        } else {
            startStep();
        }
    }

    private void handleActionStep(BotSequence.Step step) {
        switch (step.getName().trim()) {
            case Actions.EXIT:
                sequenceListener.onSequenceEnd(true);
                break;
            case Actions.EXECUTE_FRAGMENT:
            case Actions.EXECUTE_FRAGMENT2:
                executeFragmentStep(step);
                break;
            case Actions.VOTE:
                doVoteAction();
                break;
            case Actions.WAIT_FOR_USER_INPUT:
                waitForUserInput(step);
                break;
            case Actions.SHOW_TEXT_PROTOTYPE:
                showTextFromPrototype(step);
                break;
            case Actions.SHOW_CARD_PROTOTYPE:
                showCardPrototype(step);
                break;
            case Actions.HIDE_TYPING_BAR:
                sequenceListener.hideTypingBar();
                startStep();
                break;
            case Actions.SHOW_TYPING_BAR:
                sequenceListener.showTypingBar();
                startStep();
                break;
            case Actions.REDIRECT:
                ChatBotApplication.instance().getRedirectionManager().handleRedirectAction(chatAdapter.getActivity(), step);
                startStep();
                break;
            case Actions.SHOW_SURVEY_RESULTS:
                showSurveyResults();
                break;
            case Actions.SEND_MESSAGE_TO_RECIPIENT:
                sendMessageToRecipient(step);
                break;
            case Actions.SHOW_CARD:
                handleShowCardAction(step, false);
                break;
            case Actions.SHOW_RANDOM_GIF:
                handleGifRandomCardAction(step);
                break;
            case Actions.SHOW_POPULAR_IMAGES:
                handlePopularImagesAction(step);
                break;
            case Actions.SHOW_POPULAR_TEXTS:
                handlePopularTextsAction(step);
                break;
            case Actions.SHOW_CAROUSEL:
                handleCarousel(step);
                break;
            case Actions.SHOW_CAROUSEL_FROM_LIST:
                handleCarouselList(step);
                break;
            case Actions.SHOW_POPULAR_CARDS:
                handlePopularCardsAction(step);
                break;
            case Actions.SHOW_CARD_MENU:
                handleShowCardAction(step, true);
                break;
            case Actions.SHOW_TRENDING_CONTENT:
                handleTrendingContentAction(step, false);
                break;
            case Actions.SHOW_TRENDING_CONTENT_MENU:
                handleTrendingContentAction(step, true);
                break;
            case Actions.SHOW_ADVERT:
                showAdvert();
                break;
            case Actions.SET_USER_PROPERTY:
                setUserProperty(step.getParameters().getProperty(), step.getParameters().getValue());
                break;
            case Actions.RATE_US:
                Utils.rateManually(chatAdapter.getActivity());
                startStep();
                break;
            case Actions.FEEDBACK:
                AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.FEEDBACK, step.getParameters().getSequenceId(), step.getParameters().getFeedbackValue());
                startStep();
                break;
            default:
                startStep();
        }
    }

    private void waitForUserInput(BotSequence.Step step) {
        AppConfiguration.setWaitForInput(true);
        ChatBotApplication.getUserInputSubject().subscribe(s -> startStep());
        if (step.getAutoSkip() != null) {
            userInputWaitHandler.postDelayed(() -> {
                if (AppConfiguration.isWaitingForInput()) {
                    AppConfiguration.setWaitForInput(false);
                    startStep();
                }
            }, step.getAutoSkip().getMs());
        }
    }

    private void handleGifRandomCardAction(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().configService.getGiffsByPathNewFormat(step.getParameters().getPath()).enqueue(new Callback<GifImages>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable GifImages result) {
                if (result != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String delimiter = "";
                    for (GifImages.Image id : result.getImages()) {
                        stringBuilder.append(delimiter + id.getPath());
                        delimiter = ",";
                    }
                    ApiClient.getInstance().giffyService.getGifByIds(stringBuilder.toString()).enqueue(new Callback<GifResponse>(chatAdapter.getActivity()) {
                        @Override
                        public void onDataLoaded(@Nullable GifResponse result) {
                            if (result != null) {
                                final GifResponse.GifImage gifImage = result.getData().get(new Random().nextInt(result.getData().size()));
                                chatAdapter.addMessage(new ChatMessage(new GifImageModel(gifImage)));
                            }
                            startStep();
                        }
                    });
                } else {
                    startStep();
                }
            }
        });
    }

    private void handleCarouselList(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().configService.getCarouselItems(step.getParameters().getPath()).enqueue(new Callback<CarouselMessage>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable CarouselMessage carouselMessage) {
                if (carouselMessage != null) {
                    chatAdapter.addMessage(new ChatMessage(carouselMessage));
                }
                startStep();
            }
        });
    }

    private void handlePopularCardsAction(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().ideasService.getIdeasForIntention(step.getParameters().getIntentionId()).enqueue(new Callback<List<DailySuggestion>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<DailySuggestion> result) {
                if (result != null && result.size() > 0) {
                    final ChatMessage.BotMessage botMessage = BotQuestionsManager.instance().getFilteredRecommendation(result);
                    if (botMessage != null) {
                        chatAdapter.addMessage(new ChatMessage(botMessage));
                    }
                }
                startStep();
            }
        });
    }

    private void handlePopularImagesAction(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().botSuggestionsService.getSuggestedImages(step.getParameters().getPath()).enqueue(new Callback<List<PopularImages>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<PopularImages> result) {
                if (result != null) {
                    String picture = BotQuestionsManager.instance().getFilteredPictureFromPopularPictures(result);
                    if (picture != null) {
                        chatAdapter.addMessage(new ChatMessage(new ImageMessage(picture, false)));
                    }
                }
                startStep();
            }
        });
    }

    private void handleCarousel(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().ideasService.getIdeasForIntention(step.getParameters().getIntentionId()).enqueue(new Callback<List<DailySuggestion>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<DailySuggestion> result) {
                if (result != null) {
                    chatAdapter.addMessage(new ChatMessage(new SuggestionsModel(result, step.getParameters().getNumberOfCards())));
                }
                startStep();
            }
        });
    }

    private void handlePopularTextsAction(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().botSuggestionsService.getSuggestedTexts(step.getParameters().getIntentionId()).enqueue(new Callback<List<PopularTexts>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<PopularTexts> popularTexts) {
                if (popularTexts != null) {
                    Quote quote = BotQuestionsManager.instance().getFilteredTextPopular(popularTexts);
                    if (quote != null) {
                        chatAdapter.addMessage(new ChatMessage(quote));
                    }
                }
                startStep();
            }
        });
    }

    private void handleTrendingContentAction(BotSequence.Step step, boolean showMenu) {
        switch (step.getParameters().getType()) {
            case IMAGE_TEXT:
                showImageTextSuggestion(showMenu);
                break;
            case GIF:
                showGifSuggestion(showMenu);
                break;
            case TEXT:
                showTextSuggestion(showMenu);
                break;
            default:
                startStep();
        }
    }

    private void handleShowCardAction(BotSequence.Step step, boolean showMenu) {
        switch (step.getParameters().getType()) {
            case IMAGE_TEXT:
                showImageTextSuggestion(BotQuestionsManager.instance().getBotRecipient(), step.getParameters().getId(), showMenu);
                break;
            case TEXT:
                showTextSuggestion(step.getParameters().getId(), showMenu);
                break;
            case IMAGE:
                showImageSuggestion(step.getParameters().getId(), showMenu);
                break;
            case GIF:
                showGifSuggestion(step.getParameters().getPath(), showMenu);
                break;
            default:
                startStep();
        }
    }

    private void setUserProperty(String property, String value) {
        PrefManager.instance().setUserAnswer(property, value);
        startStep();
    }

    private void showAdvert() {
        AppConfiguration.setAdEnabled(true);
        chatAdapter.getBotCommandsView().clearCommand();
        handler.postDelayed(() -> startStep(), 2000);
    }

    private void doVoteAction() {
        AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.SURVEY, AnalyticsHelper.Events.SURVEY_CHOICE, rootSequence.getId(), currentSequence.getId());
        PrefManager.instance().setUserAnswer(rootSequence.getId(), currentSequence.getId());
        chatAdapter.getBotCommandsView().showLoadingView();
        DataManager.vote(rootSequence, currentSequence).subscribe(success -> startStep());
    }

    private void showSurveyResults() {
        chatAdapter.getBotCommandsView().showLoadingView();
        DataManager.getSurveyResult(rootSequence, currentSequence).subscribe(voteMessage -> {
            chatAdapter.getBotCommandsView().clearCommand();
            if (voteMessage != null) {
                chatAdapter.addMessage(new ChatMessage(voteMessage.getCountVotes(), false));
                chatAdapter.getBotCommandsView().showLoadingView();
                handler.postDelayed(() -> {
                    chatAdapter.addMessage(new ChatMessage(voteMessage.getPercentOverall(), false));
                    showVoteMessageAdditionalResults(voteMessage);
                }, 1000);
            }
        }, throwable -> {
            for (int i = 0; i < throwable.getStackTrace().length; i++) {
                Logger.e(throwable.getStackTrace()[i].toString());
            }
            startStep();
        });
    }

    private void showVoteMessageAdditionalResults(final ChatMessage.VoteMessage voteMessage) {
        chatAdapter.getBotCommandsView().showLoadingView();
        handler.postDelayed(() -> {
            chatAdapter.getBotCommandsView().clearCommand();
            if (voteMessage.isShowGenderPercent()) {
                chatAdapter.addMessage(new ChatMessage(voteMessage.getPercentGender(), false));
                voteMessage.setPercentGender(voteMessage.percentageOverAll);
            } else if (voteMessage.isShowCountryPercent()) {
                chatAdapter.addMessage(new ChatMessage(voteMessage.getPercentCountry(), false));
                voteMessage.setPercentCountry(voteMessage.percentageOverAll);
            } else if (voteMessage.isShowMaturityPercent()) {
                chatAdapter.addMessage(new ChatMessage(voteMessage.getPercentMaturity(), false));
                voteMessage.setPercentMaturity(voteMessage.percentageOverAll);
            } else {
                startStep();
                return;
            }
            showVoteMessageAdditionalResults(voteMessage);
        }, 1000);
    }

    private void sendMessageToRecipient(BotSequence.Step step) {
        final Recipient recipient = new Recipient();
        recipient.setId(step.getParameters().getPath());
        recipient.setRelationTypeTag(step.getParameters().getSource());
        PickHelper.with(chatAdapter.getActivity()).pickIntention(Utils.LOVE_RELATION_TAG).subscribe(intention -> showImageTextSuggestion(recipient, intention.getIntentionId(), true));
    }

    private void handlePauseStep(BotSequence.Step step) {
        if (step.getParameters().getMode().equals("Wait")) {
            chatAdapter.getBotCommandsView().showLoadingView();
            handler.postDelayed(() -> startStep(), step.getParameters().getMs());
        } else {
            startStep();
        }
    }

    private void handleVideoStep(BotSequence.Step step) {
        chatAdapter.addMessage(new ChatMessage(new YoutubeVideo(step.getParameters().getPath(), step.getParameters().getTitle())));
        startStep();
    }

    private void handleImageStep(BotSequence.Step step) {
        chatAdapter.addMessage(new ChatMessage(new ImageMessage(step.getParameters().getImageUrl(), step.getParameters().isFullWidth())));
        startStep();
    }

    private void handleGifStep(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        if (step.getParameters().getSource().equals("Web")) {
            GifImageModel gifImageModel = new GifImageModel(step.getParameters().getPath());
            chatAdapter.addMessage(new ChatMessage(gifImageModel));
            chatAdapter.getBotCommandsView().clearCommand();
            startStep();
        } else {
            ApiClient.getInstance().giffyService.getGifByIds(step.getParameters().getPath()).enqueue(new Callback<GifResponse>(chatAdapter.getActivity()) {
                @Override
                public void onDataLoaded(@Nullable GifResponse result) {
                    if (result != null && result.getData().size() > 0) {
                        GifResponse.GifImage gifImage = result.getData().get(0);
                        GifImageModel gifImageModel = new GifImageModel(gifImage);
                        gifImageModel.setFullWidth(step.getParameters().isFullWidth());
                        chatAdapter.addMessage(new ChatMessage(gifImageModel));
                    }
                    chatAdapter.getBotCommandsView().clearCommand();
                    startStep();
                }
            });
        }
    }

    private void showPickIntentionsCommands(final Recipient recipient) {
        chatAdapter.getBotCommandsView().showIntentionsCommandForRecipient(recipient, intention -> {
            chatAdapter.addMessage(new ChatMessage(intention.getLabel(), true));
            showImageTextSuggestion(recipient, intention.getIntentionId(), true);
        });
    }

    private void showImageTextSuggestion(final Recipient recipient, final String intentionId, final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().suggestionsService.getDailySuggestions(recipient.getId(), intentionId, "F").enqueue(new Callback<List<DailySuggestion>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<DailySuggestion> result) {
                if (result != null && result.size() > 0) {
                    final ChatMessage.BotMessage botMessage = BotQuestionsManager.instance().getFilteredRecommendation(result);
                    if (botMessage != null) {
                        chatAdapter.addMessage(new ChatMessage(botMessage));
                        if (showMenu) {
                            chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                                @Override
                                public void onShowAnotherMessageClicked() {
                                    showImageTextSuggestion(recipient, intentionId, showMenu);
                                }

                                @Override
                                public void onSendMessageClicked() {
                                    BotQuestionsManager.instance().openMessagePreview(chatAdapter.getActivity(), botMessage);
                                }

                                @Override
                                public void onChangeTopicClicked() {
                                    showPickIntentionsCommands(recipient);
                                }

                                @Override
                                public void onShowQuestionClicked() {
                                    startStep();
                                }
                            }, true);
                        } else {
                            startStep();
                        }
                    } else {
                        startStep();
                    }
                } else {
                    startStep();
                }
            }
        });
    }

    private void showImageTextSuggestion(final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().suggestionsService.getDailySuggestions(AppConfiguration.getDeviceId()).enqueue(new Callback<List<DailySuggestion>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<DailySuggestion> result) {
                if (result != null && result.size() > 0) {
                    final ChatMessage.BotMessage botMessage = BotQuestionsManager.instance().getFilteredRecommendation(result);
                    if (botMessage != null) {
                        chatAdapter.addMessage(new ChatMessage(botMessage));
                        if (showMenu) {
                            chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                                @Override
                                public void onShowAnotherMessageClicked() {
                                    showImageTextSuggestion(showMenu);
                                }

                                @Override
                                public void onSendMessageClicked() {
                                    BotQuestionsManager.instance().openMessagePreview(chatAdapter.getActivity(), botMessage);
                                }

                                @Override
                                public void onChangeTopicClicked() {
                                    showPickIntentionsCommands(BotQuestionsManager.instance().getBotRecipient());
                                }

                                @Override
                                public void onShowQuestionClicked() {
                                    startStep();
                                }
                            }, true);
                        } else {
                            startStep();
                        }
                    } else {
                        startStep();
                    }
                }
            }
        });
    }

    private void showGifSuggestion(final String gifPath, final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        DataManager.requestTrendingGifs(gifPath).subscribe(gifResponse -> {
            if (gifResponse != null) {
                final GifResponse.GifImage gifImage = gifResponse.getData().get(new Random().nextInt(gifResponse.getData().size()));
                chatAdapter.addMessage(new ChatMessage(new GifImageModel(gifImage)));
                if (showMenu) {
                    chatAdapter.getBotCommandsView().showGifCommands(new BotCommandsView.GifCommandsListener() {
                        @Override
                        public void showAnother() {
                            showGifSuggestion(gifPath, showMenu);
                        }

                        @Override
                        public void send() {
                            GifPreviewActivity.openGifPreview(chatAdapter.getActivity(), gifImage.getImages().getFixedHeight().getUrl(), gifImage.getId());
                        }
                    });
                } else {
                    startStep();
                }
            } else {
                startStep();
            }
        });
    }

    private void showGifSuggestion(final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        DataManager.requestTrendingGifs().subscribe(gifResponse -> {
            if (gifResponse != null) {
                final GifResponse.GifImage gifImage = gifResponse.getData().get(new Random().nextInt(gifResponse.getData().size()));
                chatAdapter.addMessage(new ChatMessage(new GifImageModel(gifImage)));
                if (showMenu) {
                    chatAdapter.getBotCommandsView().showGifCommands(new BotCommandsView.GifCommandsListener() {
                        @Override
                        public void showAnother() {
                            showGifSuggestion(showMenu);
                        }

                        @Override
                        public void send() {
                            GifPreviewActivity.openGifPreview(chatAdapter.getActivity(), gifImage.getImages().getFixedHeight().getUrl(), gifImage.getId());
                        }
                    });
                } else {
                    startStep();
                }
            } else {
                startStep();
            }
        });
    }

    private void showTextSuggestion(final String intentionId, final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().popularService.getPopularTexts(AppConfiguration.getAppAreaId(), intentionId).enqueue(new Callback<List<PopularTexts>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<PopularTexts> popularTexts) {
                if (popularTexts != null) {
                    final Quote quote = BotQuestionsManager.instance().getFilteredTextPopular(popularTexts);
                    if (quote != null) {
                        chatAdapter.addMessage(new ChatMessage(quote));
                        if (showMenu) {
                            chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                                @Override
                                public void onShowAnotherMessageClicked() {
                                    showTextSuggestion(intentionId, showMenu);
                                }

                                @Override
                                public void onSendMessageClicked() {
                                    BotQuestionsManager.instance().openMessagePreview(chatAdapter.getActivity(), quote);
                                }

                                @Override
                                public void onChangeTopicClicked() {

                                }

                                @Override
                                public void onShowQuestionClicked() {
                                    startStep();
                                }
                            }, false);
                        } else {
                            startStep();
                        }
                    } else {
                        startStep();
                    }
                }
            }
        });
    }

    private void showTextSuggestion(final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().popularService.getMostPopularTexts(AppConfiguration.getAppAreaId()).enqueue(new Callback<List<PopularTexts.Text>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<PopularTexts.Text> popularTexts) {
                if (popularTexts != null) {
                    Quote quote = BotQuestionsManager.instance().getFilteredText(popularTexts);
                    if (quote != null) {
                        chatAdapter.addMessage(new ChatMessage(quote));
                        if (showMenu) {
                            chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                                @Override
                                public void onShowAnotherMessageClicked() {
                                    showTextSuggestion(showMenu);
                                }

                                @Override
                                public void onSendMessageClicked() {
                                    BotQuestionsManager.instance().openMessagePreview(chatAdapter.getActivity(), quote);
                                }

                                @Override
                                public void onChangeTopicClicked() {

                                }

                                @Override
                                public void onShowQuestionClicked() {
                                    startStep();
                                }
                            }, false);
                        } else {
                            startStep();
                        }
                    } else {
                        startStep();
                    }
                }
            }
        });
    }

    private void showImageSuggestion(final String imagePath, final boolean showMenu) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().pictureService.getPicturesByPath(imagePath).enqueue(new Callback<List<String>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<String> result) {
                if (result != null) {
                    String picture = BotQuestionsManager.instance().getFilteredPicture(result);
                    if (picture != null) {
                        final String imageUrl = PictureService.HOST_URL + picture;
                        chatAdapter.addMessage(new ChatMessage(new ImageMessage(imageUrl, false)));
                        if (showMenu) {
                            chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                                @Override
                                public void onShowAnotherMessageClicked() {
                                    showImageSuggestion(imagePath, showMenu);
                                }

                                @Override
                                public void onSendMessageClicked() {
                                    ImagePreviewActivity.start(chatAdapter.getActivity(), imageUrl);
                                }

                                @Override
                                public void onChangeTopicClicked() {

                                }

                                @Override
                                public void onShowQuestionClicked() {
                                    startStep();
                                }
                            }, false);
                        } else {
                            startStep();
                        }
                    } else {
                        startStep();
                    }
                }
            }
        });
    }

    private void showTextFromPrototype(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().coreApiService.getQuotesFromRealizations(AppConfiguration.getAppAreaId(), step.getParameters().getPrototypeId()).enqueue(new Callback<List<Quote>>(chatAdapter.getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<Quote> result) {
                if (result != null && result.size() > 0) {
                    result = UtilsMessages.filterQuotes(result, PrefManager.instance().getSelectedGender(), false);
                    Collections.sort(result, new QuoteLanguageComparator());
                    chatAdapter.addMessage(new ChatMessage(result.get(0)));
                }
                startStep();
            }
        });
    }

    private void showCardPrototype(BotSequence.Step step) {
        chatAdapter.getBotCommandsView().showLoadingView();
        DataManager.loadImageCardFromPrototype(step).subscribe(botMessage -> {
            chatAdapter.addMessage(new ChatMessage(botMessage));
            startStep();
        }, throwable -> startStep());
    }

}