package com.newlang.backend.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LearningRoutineRequestDTO {

    @NotBlank(message = "El nombre de la rutina no puede estar vacío")
    private String nameRoutine;

    @NotNull(message = "El ID del usuario es obligatorio para la rutina de aprendizaje")
    private Long userId;

    @NotNull(message = "El ID de la categoría es obligatorio para la rutina de aprendizaje")
    private Long categoryId;

    @NotNull(message = "El ID de la categoría es obligatorio para la rutina de aprendizaje")
    private Long LevelId;

    public LearningRoutineRequestDTO() {
    }

    public LearningRoutineRequestDTO(String nameRoutine, Long userId, Long levelId, Long categoryId) {
        this.nameRoutine = nameRoutine;
        this.userId = userId;
        LevelId = levelId;
        this.categoryId = categoryId;
    }

    public @NotBlank(message = "El nombre de la rutina no puede estar vacío") String getNameRoutine() {
        return nameRoutine;
    }

    public void setNameRoutine(@NotBlank(message = "El nombre de la rutina no puede estar vacío") String nameRoutine) {
        this.nameRoutine = nameRoutine;
    }

    public @NotNull(message = "El ID del usuario es obligatorio para la rutina de aprendizaje") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "El ID del usuario es obligatorio para la rutina de aprendizaje") Long userId) {
        this.userId = userId;
    }

    public @NotNull(message = "El ID de la categoría es obligatorio para la rutina de aprendizaje") Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull(message = "El ID de la categoría es obligatorio para la rutina de aprendizaje") Long categoryId) {
        this.categoryId = categoryId;
    }

    public @NotNull(message = "El ID de la categoría es obligatorio para la rutina de aprendizaje") Long getLevelId() {
        return LevelId;
    }

    public void setLevelId(@NotNull(message = "El ID de la categoría es obligatorio para la rutina de aprendizaje") Long levelId) {
        LevelId = levelId;
    }
}
