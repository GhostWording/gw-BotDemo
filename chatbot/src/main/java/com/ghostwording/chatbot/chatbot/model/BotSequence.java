package com.ghostwording.chatbot.chatbot.model;

import com.ghostwording.chatbot.io.service.PictureService;
import com.ghostwording.chatbot.utils.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BotSequence {

    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("Randomize")
    @Expose
    private String randomize;
    @SerializedName("Commands")
    @Expose
    private List<BotSequence> commands = null;
    @SerializedName("LinksTo")
    @Expose
    private BotSequence linksTo = null;
    @SerializedName("CommandLabel")
    @Expose
    private Label commandLabel;
    @SerializedName("Title")
    @Expose
    private Label title;
    @SerializedName("CommandPicture")
    @Expose
    private String commandPicture = null;
    @SerializedName("Condition")
    @Expose
    private String condition = null;
    @SerializedName("LinksToFragment")
    @Expose
    private LinksToFragment linksToFragment;
    @SerializedName("CarouselElements")
    @Expose
    private CarouselElements carouselElements = null;
    @SerializedName("AutoSelect")
    @Expose
    private AutoSelect autoSelect = null;
    @SerializedName("ElementValue")
    @Expose
    private String elementValue = null;
    @SerializedName("MasterName")
    @Expose
    private String masterName = null;
    @SerializedName("MasterGroup")
    @Expose
    private String masterGroup = null;
    @SerializedName("MasterOrder")
    @Expose
    private Integer masterOrder = null;

    public AutoSelect getAutoSelect() {
        return autoSelect;
    }

    public String getMasterGroup() {
        return masterGroup;
    }

    public String getLocation() {
        return masterName + "-" + masterOrder;
    }

    public String getElementValue() {
        return elementValue;
    }

    private String groupName;

    public CarouselElements getCarouselElements() {
        return carouselElements;
    }

    public String getCondition() {
        return condition;
    }

    public LinksToFragment getLinksToFragment() {
        return linksToFragment;
    }

    public void setLinksToFragment(LinksToFragment linksToFragment) {
        this.linksToFragment = linksToFragment;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCommandPicture() {
        return commandPicture;
    }

    public String getTitle() {
        return Utils.getLocalizedLabel(title);
    }

    public BotSequence getLinksTo() {
        return linksTo;
    }

    public boolean isRandomizeCommands() {
        return randomize != null && randomize.equals("true");
    }

    public String getLabel() {
        return Utils.getLocalizedLabel(commandLabel);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public String getRandomize() {
        return randomize;
    }

    public void setRandomize(String randomize) {
        this.randomize = randomize;
    }

    public List<BotSequence> getCommands() {
        return commands;
    }

    public void setCommands(List<BotSequence> commands) {
        this.commands = commands;
    }

    public class CarouselElements {

        @SerializedName("Title")
        @Expose
        private Label title;
        @SerializedName("Picture")
        @Expose
        private Parameters picture;
        @SerializedName("Image")
        @Expose
        private Parameters image;
        @SerializedName("Subtitle")
        @Expose
        private Label subtitle;

        public String getTitle() {
            return Utils.getLocalizedLabel(title);
        }

        public String getPicturePath() {
            if (image != null) {
                return image.getPath();
            } else {
                return picture.getPath();
            }
        }

        public String getSubtitle() {
            return Utils.getLocalizedLabel(subtitle);
        }

    }

    public class LinksToFragment {
        @SerializedName("FragmentPath")
        @Expose
        private String fragmentPath;
        @SerializedName("Tags")
        @Expose
        private List<String> tags = null;

        public List<String> getTags() {
            return tags;
        }

        public String getFragmentPath() {
            return fragmentPath;
        }

        public void setFragmentPath(String fragmentPath) {
            this.fragmentPath = fragmentPath;
        }
    }

    public class Step {

        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("Label")
        @Expose
        private Label label;
        @SerializedName("Title")
        @Expose
        private Label title;
        @SerializedName("SubTitle")
        @Expose
        private Label subTitle;
        @SerializedName("LinkLabel")
        @Expose
        private Label linkLabel;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("Parameters")
        @Expose
        private Parameters parameters;
        @SerializedName("Image")
        @Expose
        private Parameters image;
        @SerializedName("Rule")
        @Expose
        private String rule;

        public Parameters getImage() {
            return image;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return Utils.getLocalizedLabel(title);
        }

        public String getSubtitle() {
            return Utils.getLocalizedLabel(subTitle);
        }

        public String getLinkLabel() {
            return Utils.getLocalizedLabel(linkLabel);
        }

        public Parameters getParameters() {
            return parameters;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLabel() {
            return Utils.getLocalizedLabel(label);
        }
    }

    public class Label {

        @SerializedName("en")
        @Expose
        private String en;
        @SerializedName("fr")
        @Expose
        private String fr;
        @SerializedName("es")
        @Expose
        private String es;

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public String getFr() {
            return fr;
        }

        public void setFr(String fr) {
            this.fr = fr;
        }

        public String getEs() {
            return es;
        }

        public void setEs(String es) {
            this.es = es;
        }

    }

    public class AutoSelect {

        @SerializedName("ms")
        @Expose
        private Integer ms;

        public Integer getMs() {
            return ms;
        }
    }

    public class Parameters {

        @SerializedName("Display")
        @Expose
        private String display;
        @SerializedName("Source")
        @Expose
        private String source;
        @SerializedName("Path")
        @Expose
        private String path;
        @SerializedName("Mode")
        @Expose
        private String mode;
        @SerializedName("ms")
        @Expose
        private Integer ms;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("property")
        @Expose
        private String property;
        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("IntentionId")
        @Expose
        private String intentionId;
        @SerializedName("Label")
        @Expose
        private Label label;
        @SerializedName("NumberOfCards")
        @Expose
        private Integer numberOfCards;

        public String getImageUrl() {
            if (source.equals("Internal")) {
                return PictureService.HOST_URL + path;
            }
            return path;
        }

        public boolean isFullWidth() {
            return display != null && display.equals("FullScreenWidth");
        }

        public Integer getNumberOfCards() {
            return numberOfCards;
        }

        public String getIntentionId() {
            return intentionId;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return Utils.getLocalizedLabel(label);
        }

        public String getProperty() {
            return property;
        }

        public String getValue() {
            return value;
        }

        public String getTitle() {
            return title;
        }

        public Integer getMs() {
            return ms;
        }

        public String getMode() {
            return mode;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }

}
