package com.newlang.backend.dto;

public class LevelResponseDTO {

    private Long idLevel;
    private String nameLevel;

    public LevelResponseDTO() {
    }

    public LevelResponseDTO(Long idLevel, String nameLevel) {
        this.idLevel = idLevel;
        this.nameLevel = nameLevel;
    }

    public Long getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(Long idLevel) {
        this.idLevel = idLevel;
    }

    public String getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(String nameLevel) {
        this.nameLevel = nameLevel;
    }
}
