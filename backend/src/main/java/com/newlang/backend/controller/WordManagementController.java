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

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class WordManagementController {

    @Autowired
    private WordManagementService wordManagementService;

    //Función para crear nuevas palabras en inglés y español
    @PostMapping("/admin/word/create")
    public ResponseEntity<?> createWord(@RequestBody Word word){
        try {
            return new ResponseEntity<>(wordManagementService.saveWord(word), HttpStatus.CREATED);
        }catch (WordAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //Función para actualizar las palabras en inglés y español por el ID
    @PutMapping("/admin/word/update-word/{id}")
    public ResponseEntity<?> updateWord(@PathVariable Long id, @RequestBody Word word){
        try {
            return ResponseEntity.ok(wordManagementService.updateWord(id, word));
        }catch (WordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Función para buscar todas las palabras
    @GetMapping("/auth/word/get-all-words")
    public ResponseEntity<List<Word>> getAllWords(){
        List<Word> words = wordManagementService.getAllWords();
        return new ResponseEntity<>(words, HttpStatus.OK);
    }

    //Función para buscar la palabra por el ID
    @GetMapping("/auth/word/get-word/{id}")
    public ResponseEntity<Word> getWordById(@PathVariable Long id){
        Optional<Word> word = wordManagementService.getWordById(id);
        return word.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //Función para buscar por palabras
    @GetMapping("/auth/word/get-word")
    public ResponseEntity<Word> getByWord(@RequestParam("word") String word){
        Word foundWord = wordManagementService.getByWord(word);
        return new ResponseEntity<>(foundWord, HttpStatus.OK);
    }

    //Función para eliminar las palabras, buscando por palabras
    @DeleteMapping("/admin/word/delete")
    public ResponseEntity<?> deleteByWord(@RequestParam("word") String word){
        try {
            wordManagementService.deleteWord(word);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (WordNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/expression/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam("query") String query) {
        List<String> suggestions = wordManagementService.getSuggestions(query);
        return ResponseEntity.ok(suggestions);
    }
}
