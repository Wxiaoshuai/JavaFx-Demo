package com.example.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FileInfoEntity {

    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private String code;
    private String category;
    private String name;
    private String owner;
    private String conclusion1;
    private String conclusion2;
    private String conclusion3;
    private String conclusion4;

    public void setFileInfo(int column, String data) {
        switch (column) {
            case 1:
                this.code = data;
                break;
            case 2:
                this.category = data;
                break;
            case 3:
                this.name = data;
                break;
            case 4:
                this.owner = data;
                break;
            case 5:
                this.conclusion1 = data;
                break;
            case 6:
                this.conclusion4 = data;
                break;
            default:
                break;
        }
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getConclusion1() {
        return conclusion1;
    }

    public void setConclusion1(String conclusion1) {
        this.conclusion1 = conclusion1;
    }

    public String getConclusion2() {
        return conclusion2;
    }

    public void setConclusion2(String conclusion2) {
        this.conclusion2 = conclusion2;
    }

    public String getConclusion3() {
        return conclusion3;
    }

    public void setConclusion3(String conclusion3) {
        this.conclusion3 = conclusion3;
    }

    public String getConclusion4() {
        return conclusion4;
    }

    public void setConclusion4(String conclusion4) {
        this.conclusion4 = conclusion4;
    }
}
