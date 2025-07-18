package com.newlang.backend.dto.responseDto;



public class WordResponseDTO {

    private Long id;
    private String englishWord;
    private String spanishWord;

    private CategoryResponseDTO categoryResponseDTO;
    private LevelResponseDTO levelResponseDTO;

    public WordResponseDTO() {
    }

    public WordResponseDTO(Long id, String englishWord, String spanishWord, CategoryResponseDTO categoryResponseDTO,
                           LevelResponseDTO levelResponseDTO) {
        this.id = id;
        this.englishWord = englishWord;
        this.spanishWord = spanishWord;
        this.categoryResponseDTO = categoryResponseDTO;
        this.levelResponseDTO = levelResponseDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getSpanishWord() {
        return spanishWord;
    }

    public void setSpanishWord(String spanishWord) {
        this.spanishWord = spanishWord;
    }

    public LevelResponseDTO getLevelResponseDTO() {
        return levelResponseDTO;
    }

    public void setLevelResponseDTO(LevelResponseDTO levelResponseDTO) {
        this.levelResponseDTO = levelResponseDTO;
    }

    public CategoryResponseDTO getCategoryResponseDTO() {
        return categoryResponseDTO;
    }

    public void setCategoryResponseDTO(CategoryResponseDTO categoryResponseDTO) {
        this.categoryResponseDTO = categoryResponseDTO;
    }
}


