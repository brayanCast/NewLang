package com.newlang.backend.repository;

import com.newlang.backend.entity.Category;
import com.newlang.backend.entity.Level;
import com.newlang.backend.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    Optional<Word> findByEnglishWord(String englishWord);
    Optional<Word> findBySpanishWord(String spanishWord);

    List<Word> findByEnglishWordStartingWithIgnoreCase(String englishWord);
    List<Word> findBySpanishWordStartingWithIgnoreCase(String spanishWord);

    Optional<Word> findByEnglishWordAndSpanishWord(String englishWord, String spanishWord);

    List<Word> findByCategory(Category category);
    List<Word> findByLevel(Level level);
    List<Word> findByCategoryAndLevel(Category category, Level level);
}
