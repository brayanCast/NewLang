package com.newlang.backend.repository;

import com.newlang.backend.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

    Optional<Level> findByNameLevel(String name);
}
