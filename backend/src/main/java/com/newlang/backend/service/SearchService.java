package com.newlang.backend.service;

import com.newlang.backend.dto.SearchResultDto;
import com.newlang.backend.dto.responseDto.GlobalSearchResponseDTO;

import com.newlang.backend.entity.Expression;
import com.newlang.backend.entity.Word;
import com.newlang.backend.exceptions.ExpressionNotFoundException;
import com.newlang.backend.exceptions.WordNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SearchService {

    private final  WordManagementService wordManagementService;
    private final ExpressionManagementService expressionManagementService;

    public SearchService(WordManagementService wordManagementService, ExpressionManagementService expressionManagementService) {
        this.wordManagementService = wordManagementService;
        this.expressionManagementService = expressionManagementService;
    }

    public List<SearchResultDto> searchAll(String query) {
        if(query == null || query.trim().isEmpty()) {
            return List.of();
        }

        int maxTotalSuggestions = 10;

        List<SearchResultDto> wordSuggestions = wordManagementService.getSuggestions(query.toLowerCase()).stream()
                .flatMap(word -> {List<SearchResultDto> dtos = new ArrayList<>();
                    //validation where english word is not null, get id word, type of word, english word and meaning in spanish
                    if (word.getEnglishWord() != null && word.getEnglishWord().toLowerCase().startsWith(query.toLowerCase())) {
                        dtos.add(new SearchResultDto(word.getIdWord(), "word", word.getEnglishWord()));
                    }

                    if (word.getSpanishWord() != null && word.getSpanishWord().toLowerCase().startsWith(query.toLowerCase())) {
                        dtos.add(new SearchResultDto(word.getIdWord(), "word", word.getSpanishWord()));
                    }

                    return dtos.stream();
                })
                .toList(); //Collect all word DTOs in a list

        List<SearchResultDto> expressionSuggestions = expressionManagementService.getSuggestions(query.toLowerCase()).stream()
                .flatMap(expression -> {List<SearchResultDto> dtos = new ArrayList<>();

                    if (expression.getEnglishExpression() != null && expression.getEnglishExpression().
                            toLowerCase().startsWith(query.toLowerCase())){
                        dtos.add(new SearchResultDto(expression.getIdExpression(), "expression",
                                expression.getEnglishExpression()));
                    }

                    if (expression.getSpanishExpression() != null && expression.getSpanishExpression().
                            toLowerCase().startsWith(query.toLowerCase())){
                        dtos.add(new SearchResultDto(expression.getIdExpression(), "expression",
                                expression.getSpanishExpression()));
                    }

                    return dtos.stream();
                })
                .toList();

        return Stream.concat(wordSuggestions.stream(), expressionSuggestions.stream())
                .collect(Collectors.toCollection(LinkedHashSet::new))
                .stream()
                .limit(maxTotalSuggestions)
                .collect(Collectors.toList());
    }

    public Object globalSearchOne(String query) throws Exception {
        Word foundWord = null;
        try {
            foundWord = wordManagementService.getByWord(query);
            return new GlobalSearchResponseDTO("word", foundWord);
        } catch (WordNotFoundException e) {
            // No se encontró una palabra, ahora busca una expresión
        }

        Expression foundExpression = null;
        try {
            foundExpression = expressionManagementService.getByExpression(query);
            return new GlobalSearchResponseDTO("expression", foundExpression);
        } catch (ExpressionNotFoundException e) {
            // Si la búsqueda de la expresión también falla, lanza la excepción
            throw new Exception("No se encuentran resultados para la búsqueda: " + query);
        }
    }
}
