package com.newlang.backend.service;

import com.newlang.backend.dto.SearchResultDto;
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

                    if (expression.getEnglishExpression() != null && expression.getSpanishExpression().
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
}
