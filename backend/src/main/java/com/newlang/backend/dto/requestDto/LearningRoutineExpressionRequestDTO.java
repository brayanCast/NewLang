package com.newlang.backend.dto.requestDto;

import com.newlang.backend.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LearningRoutineExpressionRequestDTO {

    @NotNull(message = "el ID de la expresión es obligatorio")
    private Long idExpression;

    private Status status;
    private LocalDateTime lastReviewedDate;
    private LocalDateTime nextReviewedDate;
    private short masteryLevel;

    public LearningRoutineExpressionRequestDTO() {
    }

    public LearningRoutineExpressionRequestDTO(Long idExpression, Status status, LocalDateTime lastReviewedDate,
                                               LocalDateTime nextReviewedDate, short masteryLevel) {
        this.idExpression = idExpression;
        this.status = status;
        this.lastReviewedDate = lastReviewedDate;
        this.nextReviewedDate = nextReviewedDate;
        this.masteryLevel = masteryLevel;
    }

    public @NotNull(message = "el ID de la expresión es obligatorio") Long getIdExpression() {
        return idExpression;
    }

    public void setIdExpression(@NotNull(message = "el ID de la expresión es obligatorio") Long idExpression) {
        this.idExpression = idExpression;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getLastReviewedDate() {
        return lastReviewedDate;
    }

    public void setLastReviewedDate(LocalDateTime lastReviewedDate) {
        this.lastReviewedDate = lastReviewedDate;
    }

    public LocalDateTime getNextReviewedDate() {
        return nextReviewedDate;
    }

    public void setNextReviewedDate(LocalDateTime nextReviewedDate) {
        this.nextReviewedDate = nextReviewedDate;
    }

    public short getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(short masteryLevel) {
        this.masteryLevel = masteryLevel;
    }
}
