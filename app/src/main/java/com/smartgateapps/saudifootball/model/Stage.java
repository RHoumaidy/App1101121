package com.smartgateapps.saudifootball.model;

/**
 * Created by Raafat on 19/01/2016.
 */
public class Stage {

    private String name;
    private String url;

    public Stage(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        Stage other = (Stage) o;
        return this.getName().equalsIgnoreCase(other.getName());
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
