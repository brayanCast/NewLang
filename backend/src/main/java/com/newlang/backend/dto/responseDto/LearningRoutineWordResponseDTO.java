package com.newlang.backend.dto.responseDto;

import com.newlang.backend.enums.Status;

import java.time.LocalDateTime;

public class LearningRoutineWordResponseDTO {

    private WordResponseDTO wordResponseDTO;
    private Status status;
    private LocalDateTime lastReviewedDate;
    private LocalDateTime nextReviewedDate;
    private short masteryLevel;

    public LearningRoutineWordResponseDTO() {
    }

    public LearningRoutineWordResponseDTO(WordResponseDTO wordResponseDTO, Status status,
                                          LocalDateTime lastReviewedDate, LocalDateTime nextReviewedDate,
                                          short masteryLevel) {
        this.wordResponseDTO = wordResponseDTO;
        this.status = status;
        this.lastReviewedDate = lastReviewedDate;
        this.nextReviewedDate = nextReviewedDate;
        this.masteryLevel = masteryLevel;
    }

    public WordResponseDTO getWordResponseDTO() {
        return wordResponseDTO;
    }

    public void setWordResponseDTO(WordResponseDTO wordResponseDTO) {
        this.wordResponseDTO = wordResponseDTO;
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
