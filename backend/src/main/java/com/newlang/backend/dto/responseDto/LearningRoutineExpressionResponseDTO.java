package com.newlang.backend.dto.responseDto;

import com.newlang.backend.enums.Status;

import java.time.LocalDateTime;

public class LearningRoutineExpressionResponseDTO {

    private ExpressionResponseDTO expressionResponseDTO;
    private Status status;
    private LocalDateTime lastReviewedDate;
    private LocalDateTime nextReviewedDate;
    private short masteryLevel;

    public LearningRoutineExpressionResponseDTO() {
    }

    public LearningRoutineExpressionResponseDTO(ExpressionResponseDTO expressionResponseDTO, Status status,
                                                LocalDateTime lastReviewedDate, LocalDateTime nextReviewedDate,
                                                short masteryLevel) {
        this.expressionResponseDTO = expressionResponseDTO;
        this.status = status;
        this.lastReviewedDate = lastReviewedDate;
        this.nextReviewedDate = nextReviewedDate;
        this.masteryLevel = masteryLevel;
    }

    public ExpressionResponseDTO getExpressionResponseDTO() {
        return expressionResponseDTO;
    }

    public void setExpressionResponseDTO(ExpressionResponseDTO expressionResponseDTO) {
        this.expressionResponseDTO = expressionResponseDTO;
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
