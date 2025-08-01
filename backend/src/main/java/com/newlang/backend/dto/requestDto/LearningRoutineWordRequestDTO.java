package com.newlang.backend.dto.requestDto;

import com.newlang.backend.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class LearningRoutineWordRequestDTO {

    @NotNull(message = "el ID de la palabra es obligatorio")
    private Long idWord;

    private Status status;
    private LocalDateTime lastReviewedDate;
    private LocalDateTime nextReviewedDate;
    private short masteryLevel;

    public LearningRoutineWordRequestDTO() {
    }

    public LearningRoutineWordRequestDTO(Long idWord, Status status, LocalDateTime nextReviewedDate,
                                         LocalDateTime lastReviewedDate, short masteryLevel) {
        this.idWord = idWord;
        this.status = status;
        this.nextReviewedDate = nextReviewedDate;
        this.lastReviewedDate = lastReviewedDate;
        this.masteryLevel = masteryLevel;
    }

    public @NotNull(message = "el ID de la palabra es obligatorio") Long getIdWord() {
        return idWord;
    }

    public void setIdWord(@NotNull(message = "el ID de la palabra es obligatorio") Long idWord) {this.idWord = idWord;}

    public Status getStatus() {return status;}

    public void setStatus(Status status) {this.status = status;}

    public LocalDateTime getLastReviewedDate() {return lastReviewedDate;}

    public void setLastReviewedDate(LocalDateTime lastReviewedDate) {this.lastReviewedDate = lastReviewedDate;}

    public LocalDateTime getNextReviewedDate() {return nextReviewedDate;}

    public void setNextReviewedDate(LocalDateTime nextReviewedDate) {this.nextReviewedDate = nextReviewedDate;}

    public short getMasteryLevel() {return masteryLevel;}

    public void setMasteryLevel(short masteryLevel) {this.masteryLevel = masteryLevel;}
}
