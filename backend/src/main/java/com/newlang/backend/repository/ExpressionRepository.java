package com.newlang.backend.repository;

import com.newlang.backend.entity.Category;
import com.newlang.backend.entity.Expression;
import com.newlang.backend.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpressionRepository extends JpaRepository<Expression, Long> {
    Optional<Expression> findByEnglishExpression(String englishExpression);
    Optional<Expression> findBySpanishExpression(String spanishExpression);

    List<Expression> findByEnglishExpressionStartingWithIgnoreCase(String englishExpression);
    List<Expression> findBySpanishExpressionStartingWithIgnoreCase(String spanishExpression);

    Optional<Expression> findByEnglishExpressionAndSpanishExpression(String englishExpression, String spanishExpression);

    List<Expression> findByCategory(Category category);
    List<Expression> findByLevel(Level level);
    List<Expression> findByCategoryAndLevel(Category category, Level level);
}
