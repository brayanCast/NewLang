package com.newlang.backend.dto;

public class SearchResultDto {
    private Long id;
    private String type;
    private String text;
    private String meaning;

    public SearchResultDto(Long id, String type, String text, String meaning) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.meaning = meaning;
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

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }





}



