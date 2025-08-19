package com.newlang.backend.service;

import com.newlang.backend.dto.requestDto.ExpressionRequestDTO;
import com.newlang.backend.entity.Category;
import com.newlang.backend.entity.Expression;
import com.newlang.backend.entity.Level;
import com.newlang.backend.exceptions.CategoryNotFoundException;
import com.newlang.backend.exceptions.ExpressionAlreadyExistException;
import com.newlang.backend.exceptions.ExpressionNotFoundException;
import com.newlang.backend.exceptions.LevelNotFoundException;
import com.newlang.backend.repository.CategoryRepository;
import com.newlang.backend.repository.ExpressionRepository;
import com.newlang.backend.repository.LevelRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LevelRepository levelRepository;

    // ---- Función para guardar la información de la expresión, validando que esta no exista ----
    @Transactional
    public Expression saveExpression(ExpressionRequestDTO expressionRequestDTO){
        if(expressionRepository.findByEnglishExpressionAndSpanishExpression
                (expressionRequestDTO.getEnglishExpression(), expressionRequestDTO.getSpanishExpression())
                .isPresent()) {
            throw new ExpressionAlreadyExistException("La expresión " + expressionRequestDTO.getEnglishExpression()
                    + "/" + expressionRequestDTO.getSpanishExpression() + "ya existe");
        }

        Category category = categoryRepository.findById(expressionRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada con ID " + expressionRequestDTO.getCategoryId()));

        Level level = levelRepository.findById(expressionRequestDTO.getLevelId())
                .orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado con ID " + expressionRequestDTO.getLevelId()));

        Expression newExpression = new Expression();
        newExpression.setEnglishExpression(expressionRequestDTO.getEnglishExpression());
        newExpression.setSpanishExpression(expressionRequestDTO.getSpanishExpression());
        newExpression.setImageUrl(expressionRequestDTO.getImageUrl());
        newExpression.setCategory(category);
        newExpression.setLevel(level);

        return expressionRepository.save(newExpression);
    }

    // ---- Función que trae todas las expresiones creadas ----
    public List<Expression> getAllExpression(){return expressionRepository.findAll(); }

    // ---- Función para consultar las expresiones por el ID ----
    public Optional<Expression> getExpressionById(Long id){return expressionRepository.findById(id); }

    // ---- Función para encontrar la expresión por el nombre ya sea en inglés o español ----
    public Expression getByExpression(String expression){
        return expressionRepository.findByEnglishExpression(expression.toLowerCase())
                .or(() -> expressionRepository.findBySpanishExpression(expression.toLowerCase()))
                .orElseThrow(() -> new ExpressionNotFoundException("La expresión no fue encontrada"));
    }

    // ---- Función para actualizar las expresiones por el ID ----
    public Expression updateExpression(Long id, ExpressionRequestDTO expressionRequestDTO){
        Expression existingExpression = expressionRepository.findById(id)
                .orElseThrow(() -> new ExpressionNotFoundException("La expresión no fue encontrada con el ID: " + id));

        Category newCategory = categoryRepository.findById(expressionRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada con el ID " + expressionRequestDTO.getCategoryId()));

        Level newLevel = levelRepository.findById(expressionRequestDTO.getLevelId())
                .orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado con el ID " + expressionRequestDTO.getLevelId()));

        existingExpression.setEnglishExpression(expressionRequestDTO.getEnglishExpression());
        existingExpression.setSpanishExpression(expressionRequestDTO.getSpanishExpression());
        existingExpression.setSpanishExpression(expressionRequestDTO.getImageUrl());
        existingExpression.setCategory(newCategory);
        existingExpression.setLevel(newLevel);

        return expressionRepository.save(existingExpression);
    }

    public void deleteExpression(String expression){
        Optional<Expression> foundExpression = expressionRepository.findByEnglishExpression(expression.toLowerCase());
        foundExpression = foundExpression.or(() -> expressionRepository.findBySpanishExpression(expression.toLowerCase()));

        foundExpression.ifPresentOrElse(
                expressionToDelete -> expressionRepository.deleteById(expressionToDelete.getIdExpression()),
                () -> { throw new ExpressionNotFoundException("La expresión " + expression + " no fue encontrada");
                }
        );
    }

    public void deleteExpressionById(Long id) {
        if(expressionRepository.findById(id).isPresent()) {
            expressionRepository.deleteById(id);
        } else {
            throw new ExpressionNotFoundException("La expresión no existe o ya fue eliminada");
        }
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

    public List<Expression> getExpressionByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada con el ID " + categoryId));

        return expressionRepository.findByCategory(category);
    }

    public List<Expression> getExpressionByLevel(Long levelId) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado con el ID " + levelId));

        return expressionRepository.findByLevel(level);
    }
}
