package com.newlang.backend.dto;

import java.util.Objects;

public class SearchResultDto {
    private Long id;
    private String type;
    private String text;

    public SearchResultDto(Long id, String type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
    }

    public SearchResultDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    //Allow to compare objects in HashSet in SearchService class
    @Override
    public boolean equals(Object object){
        if(this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SearchResultDto that = (SearchResultDto) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, text);
    }
}



