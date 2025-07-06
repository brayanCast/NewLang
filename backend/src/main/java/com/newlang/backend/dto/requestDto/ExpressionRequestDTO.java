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

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    @NotNull(message = "El ID del nivel es obligatorio")
    private Long levelId;

    public ExpressionRequestDTO() {
    }

    public ExpressionRequestDTO(String englishExpression, String spanishExpression, Long categoryId, Long levelId) {
        this.englishExpression = englishExpression;
        this.spanishExpression = spanishExpression;
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
