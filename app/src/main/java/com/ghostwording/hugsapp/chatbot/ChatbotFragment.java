package com.ghostwording.hugsapp.chatbot;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ghostwording.hugsapp.R;
import com.ghostwording.hugsapp.analytics.AnalyticsHelper;
import com.ghostwording.hugsapp.chatbot.model.BotSequence;
import com.ghostwording.hugsapp.chatbot.model.ChatMessage;
import com.ghostwording.hugsapp.chatbot.model.SearchRequest;
import com.ghostwording.hugsapp.databinding.FragmentChatbotBinding;
import com.ghostwording.hugsapp.io.ApiClient;
import com.ghostwording.hugsapp.io.Callback;
import com.ghostwording.hugsapp.model.DailySuggestion;
import com.ghostwording.hugsapp.model.recipients.Recipient;
import com.ghostwording.hugsapp.model.requests.SequenceRequest;
import com.ghostwording.hugsapp.model.texts.Quote;
import com.ghostwording.hugsapp.utils.AppConfiguration;
import com.ghostwording.hugsapp.utils.Logger;
import com.ghostwording.hugsapp.utils.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ChatbotFragment extends Fragment {

    private ChatAdapter chatAdapter;
    private FragmentChatbotBinding binding;
    private @DrawableRes
    Integer botAvatarResource = R.drawable.ic_huggy_avatar;
    private String botName = AppConfiguration.getBotName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chatbot, container, false);
        binding = DataBindingUtil.bind(rootView);
        chatAdapter = new ChatAdapter((AppCompatActivity) getActivity(), binding.recyclerMenuItems);
        chatAdapter.setAvatarImage(botAvatarResource);
        binding.recyclerMenuItems.setAdapter(chatAdapter);
        binding.recyclerMenuItems.setLayoutManager(new LinearLayoutManager(getActivity()));

        scrollToBottom();

        binding.recyclerMenuItems.post(() -> showBotQuestion());

        return rootView;
    }

    public void scrollToBottom() {
        if (chatAdapter != null) {
            chatAdapter.scrollToBottom();
        }
    }

    private void showPickIntentionsCommands(final Recipient recipient) {
        chatAdapter.getBotCommandsView().showIntentionsCommandForRecipient(recipient, intention -> {
            chatAdapter.addMessage(new ChatMessage(intention.getLabel(), true));
            loadMessageRecommendations(recipient, intention.getIntentionId());
        });
    }

    public void showBotQuestion() {
        if (chatAdapter.getBotCommandsView() == null || getActivity() == null) return;
        //if (AppConfiguration.isTestMode()) {
        //    loadTestSequence();
        //} else {
        loadNextSequenceUsingApi();
        //}
    }

    private void loadTestSequence() {
        chatAdapter.getBotCommandsView().clearCommand();
        new SequenceHandler(botName, chatAdapter, AppConfiguration.getTestSequence(), () -> showBotQuestion()).startStep();
    }

    private void loadNextSequenceUsingApi() {
        chatAdapter.getBotCommandsView().showLoadingView();
        BotSequence preloadedBotSequence = PrefManager.instance().getBotSequence(botName);
        if (!PrefManager.instance().isBotUsed(botName) && preloadedBotSequence != null) {
            showSequence(preloadedBotSequence);
        } else if (!AppConfiguration.isDisabledBotRequests(botName)) {
            ApiClient.getInstance().botService.getNextSequence(new SequenceRequest(botName)).enqueue(new retrofit2.Callback<BotSequence>() {
                @Override
                public void onResponse(Call<BotSequence> call, Response<BotSequence> response) {
                    if (!response.isSuccessful()) {
                        try {
                            if (response.errorBody().string().contains(AppConfiguration.ERROR_NO_SEQUENCES)) {
                                AppConfiguration.disableBotRequests(botName);
                            }
                        } catch (Exception e) {
                            Logger.e(e.toString());
                        }
                    }
                    showSequence(response.body());
                }

                @Override
                public void onFailure(Call<BotSequence> call, Throwable t) {
                    showSequence(null);
                }
            });
        } else {
            showSequence(null);
        }
    }

    private void showSequence(BotSequence botSequence) {
        if (botSequence != null && botSequence.getId() != null) {
            new SequenceHandler(botName, chatAdapter, botSequence, () -> showBotQuestion()).startStep();
            PrefManager.instance().setLastSequenceId(botName, botSequence.getId());
        } else {
            chatAdapter.getBotCommandsView().clearCommand();
            chatAdapter.addMessage(new ChatMessage(BotQuestionsManager.instance().getStringRandomQuestion(), false));
            new SequenceHandler(botName, chatAdapter, AppConfiguration.getDefaultCommands(), () -> showBotQuestion()).startStep();
        }
    }

    private void showBotSuggestionsForUserInput(final String message) {
        chatAdapter.getBotCommandsView().showLoadingView();
        checkSequenceSuggestion(message);
    }

    private void checkSequenceSuggestion(String query) {
        ApiClient.getInstance().testDevService.searchBotSequence(new SearchRequest(query)).enqueue(new Callback<BotSequence>(getActivity(), false) {
            @Override
            public void onDataLoaded(@Nullable BotSequence result) {
                if (result != null) {
                    showSequence(result);
                } else {
                    checkBotSuggestion(query);
                }
            }
        });
    }

    private void checkBotSuggestion(String message) {
        ApiClient.getInstance().botService.getBotSuggestion(message).enqueue(new Callback<List<ChatMessage.BotMessage>>(getActivity()) {
            @Override
            public void onDataLoaded(@Nullable final List<ChatMessage.BotMessage> botMessages) {
                if (botMessages != null) {
                    StringBuilder prototypeIds = new StringBuilder();
                    String delimiter = "";
                    for (ChatMessage.BotMessage botMessage : botMessages) {
                        prototypeIds.append(delimiter);
                        prototypeIds.append(botMessage.getPrototypeId());
                        delimiter = ",";
                    }

                    ApiClient.getInstance().coreApiService.getQuotesFromPrototypes("stickers", prototypeIds.toString()).enqueue(new Callback<List<Quote>>(getActivity()) {
                        @Override
                        public void onDataLoaded(@Nullable List<Quote> result) {
                            if (result != null) {
                                List<ChatMessage.BotMessage> filteredMessages = new ArrayList<>();
                                List<Quote> filteredQuotes = new ArrayList<>();

                                for (Quote quote : result) {
                                    if (quote.getSender().equals("F")) {
                                        continue;
                                    }
                                    filteredQuotes.add(quote);
                                }

                                for (ChatMessage.BotMessage botMessage : botMessages) {
                                    if (BotQuestionsManager.instance().messageContainQuote(botMessage, filteredQuotes)) {
                                        filteredMessages.add(botMessage);
                                    }
                                }

                                final ChatMessage.BotMessage filteredMessage = BotQuestionsManager.instance().getFilteredMessage(filteredMessages);
                                if (filteredMessage != null) {
                                    AnalyticsHelper.setImageTextContext(message);
                                    AnalyticsHelper.sendEvent(AnalyticsHelper.Categories.APP, AnalyticsHelper.Events.HUGGY_REPLY, filteredMessage.getPrototypeId(), filteredMessage.getImageName());
                                    chatAdapter.addMessage(new ChatMessage(filteredMessage));
                                    chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                                        @Override
                                        public void onShowAnotherMessageClicked() {
                                            showBotSuggestionsForUserInput(message);
                                        }

                                        @Override
                                        public void onSendMessageClicked() {
                                            BotQuestionsManager.instance().openMessagePreview((AppCompatActivity) getActivity(), filteredMessage);
                                        }

                                        @Override
                                        public void onChangeTopicClicked() {
                                            showPickIntentionsCommands(BotQuestionsManager.instance().getBotRecipient());
                                        }

                                        @Override
                                        public void onShowQuestionClicked() {
                                            showBotQuestion();
                                        }
                                    }, false);
                                } else {
                                    showBotQuestion();
                                }
                            } else {
                                showBotQuestion();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadMessageRecommendations(final Recipient recipient, final String intentionId) {
        chatAdapter.getBotCommandsView().showLoadingView();
        ApiClient.getInstance().suggestionsService.getDailySuggestions(recipient.getId(), intentionId, "F").enqueue(new Callback<List<DailySuggestion>>(getActivity()) {
            @Override
            public void onDataLoaded(@Nullable List<DailySuggestion> result) {
                if (result != null && result.size() > 0) {
                    final ChatMessage.BotMessage botMessage = BotQuestionsManager.instance().getFilteredRecommendation(result);
                    if (botMessage != null) {
                        chatAdapter.addMessage(new ChatMessage(botMessage));
                        chatAdapter.getBotCommandsView().showMessageCommands(new BotCommandsView.MessageCommandsListener() {
                            @Override
                            public void onShowAnotherMessageClicked() {
                                loadMessageRecommendations(recipient, intentionId);
                            }

                            @Override
                            public void onSendMessageClicked() {
                                BotQuestionsManager.instance().openMessagePreview((AppCompatActivity) getActivity(), botMessage);
                            }

                            @Override
                            public void onChangeTopicClicked() {
                                showPickIntentionsCommands(recipient);
                            }

                            @Override
                            public void onShowQuestionClicked() {
                                showBotQuestion();
                            }
                        }, true);
                    } else {
                        showBotQuestion();
                    }
                } else {
                    showBotQuestion();
                }
            }
        });
    }

}
