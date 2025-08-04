package com.newlang.backend.repository;

import com.newlang.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningRoutineExpressionRepository extends JpaRepository<LearningRoutineExpression, LearningRoutineExpressionId> {
    Optional<LearningRoutineExpression> findByLearningRoutineAndExpression(LearningRoutine learningRoutine, Expression expression);
    List<LearningRoutineExpression> findByLearningRoutine(LearningRoutine learningRoutine);
    List<LearningRoutineExpression> findByExpression(Expression expression);
}
