package com.ghostwording.chatbot.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

public class RecyclerViewModel {

    public final ObservableBoolean isDataLoading = new ObservableBoolean(false);
    public final ObservableBoolean isLoadFailed = new ObservableBoolean(false);
    public final ObservableField<String> errorText = new ObservableField<>("");

}
