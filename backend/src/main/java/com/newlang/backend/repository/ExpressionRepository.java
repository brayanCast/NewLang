package com.newlang.backend.repository;

import com.newlang.backend.entity.Expression;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpressionRepository extends JpaRepository<Expression, Long> {
    Optional<Expression> findByEnglishExpression(String englishExpression);
    Optional<Expression> findBySpanishExpression(String spanishExpression);

    List<Expression> findByEnglishExpressionStartingWithIgnoreCase(String englishExpression);
    List<Expression> findBySpanishExpressionStartingWithIgnoreCase(String spanishExpression);
}
