package com.newlang.backend.service;

import com.newlang.backend.entity.Expression;
import com.newlang.backend.entity.Word;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final  WordManagementService wordManagementService;
    private final ExpressionManagementService expressionManagementService;

    public SearchService(WordManagementService wordManagementService, ExpressionManagementService expressionManagementService) {
        this.wordManagementService = wordManagementService;
        this.expressionManagementService = expressionManagementService;
    }

    public List<String> searchAll(String query) {
        if(query == null || query.trim().isEmpty()) {
            return List.of();
        }



        Set<String> uniqueSuggestion = new java.util.LinkedHashSet<>();

        int maxTotalSuggestions = 10;

        List<Word> wordMatches = wordManagementService.getSuggestions(query.toLowerCase());
        for(Word word : wordMatches) {
            boolean englishWordMatches = word.getEnglishWord() != null && word.getEnglishWord().toLowerCase()
                    .startsWith(query.toLowerCase());
            boolean spanishWordMatches = word.getSpanishWord() != null && word.getSpanishWord().toLowerCase()
                    .startsWith(query.toLowerCase());

            if (englishWordMatches) {
                uniqueSuggestion.add(word.getEnglishWord());
            }

            if (spanishWordMatches) {
                uniqueSuggestion.add(word.getSpanishWord());
            }
        }

        List<Expression> expressionMatches = expressionManagementService.getSuggestions(query.toLowerCase());
        for(Expression expression : expressionMatches) {
             boolean englishExpressionMatches = expression.getEnglishExpression() != null && expression.getEnglishExpression()
                     .toLowerCase().startsWith(query.toLowerCase());

             boolean spanishExpressionMatches = expression.getSpanishExpression() != null && expression.getSpanishExpression()
                     .toLowerCase().startsWith(query.toLowerCase());

             if (englishExpressionMatches) {
                 uniqueSuggestion.add(expression.getEnglishExpression());
             }

             if (spanishExpressionMatches) {
                 uniqueSuggestion.add(expression.getSpanishExpression());
             }
        }

        return uniqueSuggestion.stream()
                .limit(maxTotalSuggestions)
                .collect(Collectors.toList());
    }
}
