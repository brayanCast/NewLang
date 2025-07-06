package com.newlang.backend.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WordRequestDTO {

    @NotBlank(message = "la palabra en inglés no puede estar vacía")
    @Size(max = 255, message = "la palabra en inglés no puede exceder los 255 caracteres")
    private String englishWord;

    @NotBlank(message = "la palabra en español no puede estar vacía")
    @Size(max = 255, message = "la palabra en español no puede exceder los 255 caracteres")
    private String spanishWord;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;

    @NotNull(message = "El ID del nivel es obligatorio")
    private Long levelId;

    public WordRequestDTO() {
    }

    public WordRequestDTO(String englishWord, String spanishWord, Long categoryId, Long levelId) {
        this.englishWord = englishWord;
        this.spanishWord = spanishWord;
        this.categoryId = categoryId;
        this.levelId = levelId;
    }

    public @NotBlank(message = "la palabra en inglés no puede estar vacía")
    @Size(max = 255, message = "la palabra en inglés no puede exceder los 255 caracteres") String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(@NotBlank(message = "la palabra en inglés no puede estar vacía")
                               @Size(max = 255, message = "la palabra en inglés no puede exceder los 255 caracteres") String englishWord) {
        this.englishWord = englishWord;
    }

    public @NotBlank(message = "la palabra en español no puede estar vacía")
    @Size(max = 255, message = "la palabra en español no puede exceder los 255 caracteres") String getSpanishWord() {
        return spanishWord;
    }

    public void setSpanishWord(@NotBlank(message = "la palabra en español no puede estar vacía")
                               @Size(max = 255, message = "la palabra en español no puede exceder los 255 caracteres") String spanishWord) {
        this.spanishWord = spanishWord;
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
