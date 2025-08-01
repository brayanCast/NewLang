package com.newlang.backend.entity;

import java.io.Serializable;
import java.util.Objects;

public class LearningRoutineWordId implements Serializable {
    private Long learningRoutine;
    private Long word;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LearningRoutineWordId that = (LearningRoutineWordId) obj;
        return Objects.equals(learningRoutine, that.learningRoutine) &&
                Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(learningRoutine, word);
    }
}
