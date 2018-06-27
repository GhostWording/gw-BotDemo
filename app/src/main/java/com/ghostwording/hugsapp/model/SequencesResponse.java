package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SequencesResponse {

    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("Version")
    @Expose
    private Integer version;
    @SerializedName("SequenceFiles")
    @Expose
    private List<SequenceFile> sequenceFiles = null;

    public Integer getVersion() {
        return version;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<SequenceFile> getSequenceFiles() {
        return sequenceFiles;
    }

    public void setSequenceFiles(List<SequenceFile> sequenceFiles) {
        this.sequenceFiles = sequenceFiles;
    }

    public List<String> getOrderedSequences() {
        List<String> result = new ArrayList<>();
        Collections.shuffle(sequenceFiles);
        Collections.sort(sequenceFiles, (left, right) -> left.getOrder().compareTo(right.getOrder()));
        for (SequenceFile sequenceFile : sequenceFiles) {
            result.add(sequenceFile.getFile());
        }
        return result;
    }

    public List<String> getAllSequenceFiles() {
        List<String> result = new ArrayList<>();
        for (SequenceFile sequenceFile : sequenceFiles) {
            result.addAll(sequenceFile.getFiles());
        }
        return result;
    }

    public class SequenceFile {
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("file")
        @Expose
        private List<String> files;

        public Integer getOrder() {
            return order;
        }

        public void setOrder(Integer order) {
            this.order = order;
        }

        public String getFile() {
            return files.get(new Random().nextInt(files.size()));
        }

        public List<String> getFiles() {
            return files;
        }
    }

}