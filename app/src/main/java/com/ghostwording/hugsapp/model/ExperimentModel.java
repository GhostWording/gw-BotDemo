package com.ghostwording.hugsapp.model;

import com.google.gson.annotations.Expose;

//Generated code
public class ExperimentModel {

    @Expose
    private String ExperimentId;
    @Expose
    private Integer VariationId;
    @Expose
    private String Area;

    /**
     * @return The ExperimentId
     */
    public String getExperimentId() {
        return ExperimentId;
    }

    /**
     * @param ExperimentId The ExperimentId
     */
    public void setExperimentId(String ExperimentId) {
        this.ExperimentId = ExperimentId;
    }

    /**
     * @return The VariationId
     */
    public Integer getVariationId() {
        return VariationId;
    }

    /**
     * @param VariationId The VariationId
     */
    public void setVariationId(Integer VariationId) {
        this.VariationId = VariationId;
    }

    /**
     * @return The Area
     */
    public String getArea() {
        return Area;
    }

    /**
     * @param Area The Area
     */
    public void setArea(String Area) {
        this.Area = Area;
    }

}
