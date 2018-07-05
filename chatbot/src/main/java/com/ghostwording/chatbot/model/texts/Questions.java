package com.ghostwording.chatbot.model.texts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Questions {

    @SerializedName("Questions")
    @Expose
    private List<Question> questions = new ArrayList<>();

    /**
     * @return The questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * @param questions The Questions
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public class Question {

        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("DefaultImage")
        @Expose
        private String defaultImage;
        @SerializedName("Question")
        @Expose
        private List<Question_> question = new ArrayList<>();
        @SerializedName("Answers")
        @Expose
        private List<Answer> answers = new ArrayList<>();

        public String getLocalizedLabel() {
            for (Question_ questionLabel : question) {
                if (questionLabel.getLanguage().contains(Locale.getDefault().getLanguage())) {
                    return questionLabel.getLabel();
                }
            }
            return "";
        }

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The Id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The defaultImage
         */
        public String getDefaultImage() {
            return defaultImage;
        }

        /**
         * @param defaultImage The DefaultImage
         */
        public void setDefaultImage(String defaultImage) {
            this.defaultImage = defaultImage;
        }

        /**
         * @return The question
         */
        public List<Question_> getQuestion() {
            return question;
        }

        /**
         * @param question The Question
         */
        public void setQuestion(List<Question_> question) {
            this.question = question;
        }

        /**
         * @return The answers
         */
        public List<Answer> getAnswers() {
            return answers;
        }

        /**
         * @param answers The Answers
         */
        public void setAnswers(List<Answer> answers) {
            this.answers = answers;
        }

    }

    public class Label {

        @SerializedName("Language")
        @Expose
        private String language;
        @SerializedName("Label")
        @Expose
        private String label;

        /**
         * @return The language
         */
        public String getLanguage() {
            return language;
        }

        /**
         * @param language The Language
         */
        public void setLanguage(String language) {
            this.language = language;
        }

        /**
         * @return The label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label The Label
         */
        public void setLabel(String label) {
            this.label = label;
        }

    }

    public class Answer {
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("Labels")
        @Expose
        private List<Label> labels = new ArrayList<>();

        public String getLocalizedLabel() {
            for (Label label : labels) {
                if (label.getLanguage().contains(Locale.getDefault().getLanguage())) {
                    return label.getLabel();
                }
            }
            return "";
        }

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The Id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The labels
         */
        public List<Label> getLabels() {
            return labels;
        }

        /**
         * @param labels The Labels
         */
        public void setLabels(List<Label> labels) {
            this.labels = labels;
        }

    }

    public class Question_ {

        @SerializedName("Language")
        @Expose
        private String language;
        @SerializedName("Label")
        @Expose
        private String label;

        /**
         * @return The language
         */
        public String getLanguage() {
            return language;
        }

        /**
         * @param language The Language
         */
        public void setLanguage(String language) {
            this.language = language;
        }

        /**
         * @return The label
         */
        public String getLabel() {
            return label;
        }

        /**
         * @param label The Label
         */
        public void setLabel(String label) {
            this.label = label;
        }

    }

}
