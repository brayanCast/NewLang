package com.newlang.backend.dto.responseDto;

public class ExpressionResponseDTO {

    private Long id;
    private String englishExpression;
    private String spanishExpression;
    private String imageUrl;
    private CategoryResponseDTO categoryResponseDTO;
    private LevelResponseDTO levelResponseDTO;

    public ExpressionResponseDTO() {
    }

    public ExpressionResponseDTO(Long id, String englishExpression, String spanishExpression, String imageUrl, CategoryResponseDTO categoryResponseDTO, LevelResponseDTO levelResponseDTO) {
        this.id = id;
        this.englishExpression = englishExpression;
        this.spanishExpression = spanishExpression;
        this.imageUrl = imageUrl;
        this.categoryResponseDTO = categoryResponseDTO;
        this.levelResponseDTO = levelResponseDTO;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getSpanishExpression() {return spanishExpression;}

    public void setSpanishExpression(String spanishExpression) {this.spanishExpression = spanishExpression;}

    public String getEnglishExpression() {return englishExpression;}

    public void setEnglishExpression(String englishExpression) {this.englishExpression = englishExpression;}

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public CategoryResponseDTO getCategoryResponseDTO() {return categoryResponseDTO;}

    public void setCategoryResponseDTO(CategoryResponseDTO categoryResponseDTO) {this.categoryResponseDTO = categoryResponseDTO;}

    public LevelResponseDTO getLevelResponseDTO() {return levelResponseDTO;}

    public void setLevelResponseDTO(LevelResponseDTO levelResponseDTO) {this.levelResponseDTO = levelResponseDTO;}
}
