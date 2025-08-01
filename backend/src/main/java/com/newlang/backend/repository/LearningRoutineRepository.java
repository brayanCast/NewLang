package com.newlang.backend.repository;

import com.newlang.backend.entity.Category;
import com.newlang.backend.entity.LearningRoutine;
import com.newlang.backend.entity.Level;
import com.newlang.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningRoutineRepository extends JpaRepository<LearningRoutine, Long> {
    Optional<LearningRoutine> findByNameRoutineAndUser(String nameRoutine, User user);
    List<LearningRoutine> findByUser(User user);
    List<LearningRoutine> findByCategory(Category category);
    List<LearningRoutine> findByLevel(Level level);
}
