package com.newlang.backend.repository;

import com.newlang.backend.entity.LearningRoutine;
import com.newlang.backend.entity.LearningRoutineWord;
import com.newlang.backend.entity.LearningRoutineWordId;
import com.newlang.backend.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningRoutineWordRepository extends JpaRepository<LearningRoutineWord, LearningRoutineWordId> {
    Optional<LearningRoutineWord> findByLearningRoutineAndWord(LearningRoutine learningRoutine, Word word);
    List<LearningRoutineWord> findByLearningRoutine(LearningRoutine learningRoutine);
    List<LearningRoutineWord> findByWord(Word word);
}
