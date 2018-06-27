package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VotingCounters {

    @SerializedName("counterNames")
    @Expose
    private List<String> counterNames;

    public VotingCounters() {

    }

    public VotingCounters(String counterName) {
        counterNames = new ArrayList<>();
        counterNames.add(counterName);
    }

    public VotingCounters(List<String> counterNames) {
        this.counterNames = counterNames;
    }

    public class VotingCounter {

        @SerializedName("counterName")
        @Expose
        private String counterName;
        @SerializedName("value")
        @Expose
        private Integer value;

        public String getCounterName() {
            return counterName;
        }

        public void setCounterName(String counterName) {
            this.counterName = counterName;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

    }

}
