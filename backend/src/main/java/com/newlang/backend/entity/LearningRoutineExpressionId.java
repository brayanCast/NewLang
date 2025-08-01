package com.newlang.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class LearningRoutineExpressionId implements Serializable {
    private Long learningRoutine;
    private Long expression;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LearningRoutineExpressionId that = (LearningRoutineExpressionId) obj;
        return Objects.equals(learningRoutine, that.learningRoutine) &&
                Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(learningRoutine, expression);
    }
}
