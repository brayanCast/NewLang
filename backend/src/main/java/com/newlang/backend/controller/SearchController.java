package com.newlang.backend.controller;

import com.newlang.backend.dto.SearchResultDto;
import com.newlang.backend.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/auth/search")
@RestController
public class SearchController {

    private SearchService searchSevice;

    public SearchController(SearchService searchSevice){
        this.searchSevice = searchSevice;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<SearchResultDto>> getSuggestions(@RequestParam("query") String query) {
        List<SearchResultDto> results = searchSevice.searchAll(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/global")
    public ResponseEntity<?> globalSearch(@RequestParam("query") String query) {
        try {
            Object result = searchSevice.globalSearchOne(query);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
