package com.newlang.backend.entity;


import com.newlang.backend.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "learning_routine_expression")
@IdClass(LearningRoutineExpressionId.class)
public class LearningRoutineExpression {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_routine", nullable = false)
    private LearningRoutine learningRoutine;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_expression", nullable = false)
    private Expression expression;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "last_reviewed_date")
    private LocalDateTime lastReviewedUpdate;

    @Column(name = "next_reviewed_date")
    private LocalDateTime nextReviewedUpdate;

    @Column(name = "mastery_level", columnDefinition = "TINYINT")
    private short masteryLevel;

    public LearningRoutine getLearningRoutine() {
        return learningRoutine;
    }

    public void setLearningRoutine(LearningRoutine learningRoutine) {
        this.learningRoutine = learningRoutine;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getLastReviewedUpdate() {
        return lastReviewedUpdate;
    }

    public void setLastReviewedUpdate(LocalDateTime lastReviewedUpdate) {
        this.lastReviewedUpdate = lastReviewedUpdate;
    }

    public LocalDateTime getNextReviewedUpdate() {
        return nextReviewedUpdate;
    }

    public void setNextReviewedUpdate(LocalDateTime nextReviewedUpdate) {
        this.nextReviewedUpdate = nextReviewedUpdate;
    }

    public short getMasteryLevel() {
        return masteryLevel;
    }

    public void setMasteryLevel(short masteryLevel) {
        this.masteryLevel = masteryLevel;
    }
}
