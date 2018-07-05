package com.ghostwording.chatbot.model;


import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThemeResponse {

    @SerializedName("Themes")
    @Expose
    private List<Theme> themes = null;

    public List<Theme> getThemes() {
        return themes;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }

    public static class Theme {

        @SerializedName("Path")
        @Expose
        private Path path;
        @SerializedName("DefaultImage")
        @Expose
        private DefaultImage defaultImage;
        @SerializedName("Labels")
        @Expose
        private BotSequence.Label labels = null;

        public String getLabel() {
            return Utils.getLocalizedLabel(labels);
        }

        public String getPath() {
            return path.getLink();
        }

        public String getSource() {
            return path.getSource();
        }

        public String getImagePath() {
            if (defaultImage.path.startsWith("http")) {
                return defaultImage.path;
            } else {
                return PictureService.HOST_URL + defaultImage.path;
            }
        }
    }

    public class DefaultImage {

        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("Path")
        @Expose
        private String path;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

    public class Path {

        @SerializedName("Source")
        @Expose
        private String source;
        @SerializedName("Link")
        @Expose
        private String link;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

    }

}



