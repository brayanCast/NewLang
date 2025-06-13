package com.newlang.backend.controller;

import com.newlang.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/auth/search")
@RestController
public class SearchController {

    @Autowired
    private SearchService searchSevice;

    public SearchController(SearchService searchSevice){
        this.searchSevice = searchSevice;
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam("query") String query) {
        List<String> results = searchSevice.searchAll(query);
        return ResponseEntity.ok(results);
    }
}
