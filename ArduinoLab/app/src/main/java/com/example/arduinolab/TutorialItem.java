package com.example.arduinolab;

import java.io.Serializable;

// We implement Serializable so we can pass this object between activities
public class TutorialItem implements Serializable {

    int id;
    String category;
    String title;
    String description;
    String imageName;
    String pinConnection;
    String sampleCode;

    public TutorialItem(String category, String title, String description, String imageName, String pinConnection, String sampleCode) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.pinConnection = pinConnection;
        this.sampleCode = sampleCode;
    }

    public TutorialItem(int id, String category, String title, String description, String imageName, String pinConnection, String sampleCode) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.description = description;
        this.imageName = imageName;
        this.pinConnection = pinConnection;
        this.sampleCode = sampleCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPinConnection() {
        return pinConnection;
    }

    public void setPinConnection(String pinConnection) {
        this.pinConnection = pinConnection;
    }

    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
}
