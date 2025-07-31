package com.newlang.backend.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExpressionRequestDTO {

    @NotBlank(message = "la expresión en inglés no puede estar vacía")
    @Size(max = 255, message = "la expresión en inglés no puede exceder los 255 caracteres")
    private String englishExpression;

    @NotBlank(message = "la expresión en español no puede estar vacía")
    @Size(max = 255, message = "la expresión en español no puede exceder los 255 caracteres")
    private String spanishExpression;

    @Size(max = 500, message = "la url de la imagen no puede exceder los 500 caracteres")
    private String imageUrl;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    @NotNull(message = "El ID del nivel es obligatorio")
    private Long levelId;

    public ExpressionRequestDTO() {
    }

    public ExpressionRequestDTO(String englishExpression, String spanishExpression, String imageUrl, Long categoryId, Long levelId) {
        this.englishExpression = englishExpression;
        this.spanishExpression = spanishExpression;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.levelId = levelId;
    }

    public @NotBlank(message = "la expresión en inglés no puede estar vacía") @Size(max = 255, message = "la expresión en inglés no puede exceder los 255 caracteres") String getEnglishExpression() {
        return englishExpression;
    }

    public void setEnglishExpression(@NotBlank(message = "la expresión en inglés no puede estar vacía") @Size(max = 255, message = "la expresión en inglés no puede exceder los 255 caracteres") String englishExpression) {
        this.englishExpression = englishExpression;
    }

    public @NotBlank(message = "la expresión en español no puede estar vacía") @Size(max = 255, message = "la expresión en español no puede exceder los 255 caracteres") String getSpanishExpression() {
        return spanishExpression;
    }

    public void setSpanishExpression(@NotBlank(message = "la expresión en español no puede estar vacía") @Size(max = 255, message = "la expresión en español no puede exceder los 255 caracteres") String spanishExpression) {
        this.spanishExpression = spanishExpression;
    }

    public @Size(max = 500, message = "la url de la imagen no puede exceder los 500 caracteres") String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Size(max = 500, message = "la url de la imagen no puede exceder los 500 caracteres") String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @NotNull(message = "El ID de la categoría es obligatorio") Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull(message = "El ID de la categoría es obligatorio") Long categoryId) {
        this.categoryId = categoryId;
    }

    public @NotNull(message = "El ID del nivel es obligatorio") Long getLevelId() {
        return levelId;
    }

    public void setLevelId(@NotNull(message = "El ID del nivel es obligatorio") Long levelId) {
        this.levelId = levelId;
    }
}
