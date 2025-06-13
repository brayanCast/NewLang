package com.newlang.backend.service;

import com.newlang.backend.entity.Expression;
import com.newlang.backend.exceptions.ExpressionAlreadyExistException;
import com.newlang.backend.exceptions.ExpressionNotFoundException;
import com.newlang.backend.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExpressionManagementService {

    @Autowired
    private ExpressionRepository expressionRepository;

    // Función para guardar la información de la expresión, validando que esta no exista
    public Expression saveExpression(Expression expression){
        Optional<Expression> existingExpressionByText = expressionRepository.findByEnglishExpression(expression.getEnglishExpression());

        if(existingExpressionByText.isPresent()){
            throw new ExpressionAlreadyExistException("Expression already exist");
        } else {
            return expressionRepository.save(expression);
        }
    }

    public List<Expression> getAllExpression(){
        return expressionRepository.findAll();
    }

    public Optional<Expression> getExpressionById(Long id){
        return expressionRepository.findById(id);
    }

    public Expression getByExpression(String expression){
        return expressionRepository.findByEnglishExpression(expression.toLowerCase())
                .or(() -> expressionRepository.findBySpanishExpression(expression.toLowerCase()))
                .orElseThrow(() -> new ExpressionNotFoundException("Expression has not been encountered"));
    }

    public Expression updateExpression(Long id, Expression updatedExpression){
        return expressionRepository.findById(id)
                .map(existingExpression -> {
                    existingExpression.setEnglishExpression(updatedExpression.getEnglishExpression());
                    existingExpression.setSpanishExpression(updatedExpression.getSpanishExpression());
                    return expressionRepository.save(existingExpression);
                }).orElseThrow(() -> new ExpressionNotFoundException("Expression has not been encountered"));
    }

    public void deleteExpression(String expression){
        Optional<Expression> foundExpression = expressionRepository.findByEnglishExpression(expression.toLowerCase());
        foundExpression = foundExpression.or(() -> expressionRepository.findBySpanishExpression(expression.toLowerCase()));

        foundExpression.ifPresentOrElse(
                expressionToDelete -> expressionRepository.deleteById(expressionToDelete.getIdExpression()),
                () -> { throw new ExpressionNotFoundException("expression " + expression + " has not been encountered");
                }
        );
    }

    public List<Expression> getSuggestions(String query) {
        if(query == null || query.isEmpty()){
            return List.of();
        }
        int maxSuggestionsPerService = 5;

        List<Expression> englishMatches = expressionRepository.findByEnglishExpressionStartingWithIgnoreCase(query.toLowerCase());
        List<Expression> spanishMatches = expressionRepository.findBySpanishExpressionStartingWithIgnoreCase(query.toLowerCase());

        Set<Expression> uniqueExpressions = new java.util.LinkedHashSet<>();

        uniqueExpressions.addAll(englishMatches);
        uniqueExpressions.addAll(spanishMatches);

        return uniqueExpressions.stream()
                .limit(maxSuggestionsPerService)
                .collect(Collectors.toList());
    }
}
