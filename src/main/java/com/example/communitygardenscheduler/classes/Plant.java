package com.example.communitygardenscheduler.classes;

public class Plant {
    String name;
    String season;
    String plantingInstructions;
    String information;

    /* required for firebase, along with getter/setters provided */
    public Plant() {
    }

    public Plant(String name, String season, String plantingInstructions, String information) {
        this.name = name;
        this.season = season;
        this.plantingInstructions = plantingInstructions;
        this.information = information;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPlantingInstructions() {
        return plantingInstructions;
    }

    public void setPlantingInstructions(String plantingInstructions) {
        this.plantingInstructions = plantingInstructions;
    }

    public String getInformation() { return information; }

    public void setInformation(String information) {
        this.information = information;
    }
}
