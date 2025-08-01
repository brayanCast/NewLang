package com.newlang.backend.repository;

import com.newlang.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningRoutineExpressionRepository extends JpaRepository<LearningRoutineExpression, LearningRoutineExpressionId> {
    Optional<LearningRoutineExpression> findByLearningRoutineAndExpression(LearningRoutine learningRoutine);
    List<LearningRoutineExpression> findByLearningRoutine(LearningRoutine learningRoutine);
    List<LearningRoutineExpression> findByExpression(Expression expression);
}
