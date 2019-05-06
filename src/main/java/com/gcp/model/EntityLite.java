package com.gcp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class EntityLite implements Serializable {

    @JsonProperty("description")
    private String description;

    @JsonProperty("boundingVertices")
    private List<VertexLite> vertexList;

    @JsonProperty("confidence")
    private Float confidence;

    public EntityLite(){}

    public EntityLite(String description, List<VertexLite> vertexList) {
        this.description = description;
        this.vertexList = vertexList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VertexLite> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<VertexLite> vertexList) {
        this.vertexList = vertexList;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "/ "+this.getDescription() + "/" +
                 this.getConfidence() +" /" +
                 this.getVertexList();
    }
}
