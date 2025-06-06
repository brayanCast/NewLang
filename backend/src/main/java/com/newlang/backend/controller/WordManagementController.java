package com.newlang.backend.controller;


import com.newlang.backend.entity.Word;
import com.newlang.backend.exceptions.WordAlreadyExistException;
import com.newlang.backend.exceptions.WordNotFoundException;
import com.newlang.backend.service.WordManagementService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class WordManagementController {

    @Autowired
    private WordManagementService wordManagementService;

    @PostMapping
    public ResponseEntity<?> createWord(@RequestBody Word word){
        try {
            return new ResponseEntity<>(wordManagementService.saveWord(word), HttpStatus.CREATED);
        }catch (WordAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping
    public ResponseEntity<?> updateEnglishWord(@PathVariable Long id, @RequestBody Word word){
        try {
            return ResponseEntity.ok(wordManagementService.updateEnglishWord(id, word));
        }catch (WordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Word>> getAllWords(){
        List<Word> words = wordManagementService.getAllWords();
        return new ResponseEntity<>(words, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Word> getWordById(@PathVariable Long id){
        Optional<Word> word = wordManagementService.getWordById(id);
        return word.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<Word> getByWord(@RequestParam("word") String word){
        Optional<Word> wordText = wordManagementService.getByWord(word);
        return wordText.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByWord(@PathVariable String word){
        try {
            wordManagementService.deleteWord(word);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (WordNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
