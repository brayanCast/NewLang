package com.newlang.backend.controller;

import com.newlang.backend.dto.requestDto.WordRequestDTO;
import com.newlang.backend.dto.responseDto.CategoryResponseDTO;
import com.newlang.backend.dto.responseDto.LevelResponseDTO;
import com.newlang.backend.dto.responseDto.WordResponseDTO;
import com.newlang.backend.entity.Word;
import com.newlang.backend.exceptions.CategoryNotFoundException;
import com.newlang.backend.exceptions.LevelNotFoundException;
import com.newlang.backend.exceptions.WordAlreadyExistException;
import com.newlang.backend.exceptions.WordNotFoundException;
import com.newlang.backend.service.WordManagementService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class WordManagementController {

    @Autowired
    private WordManagementService wordManagementService;

    // ---- Método Auxiliar para mapear Word entity y WordResponseDTO ----
    private WordResponseDTO mapToResponseDTO(Word word) {
        CategoryResponseDTO categoryResponseDTO = null;
        if(word.getCategory() != null) {
            categoryResponseDTO = new CategoryResponseDTO(word.getCategory().getIdCategory(), word.getCategory().getNameCategory());
        }

        LevelResponseDTO levelResponseDTO = null;
        if(word.getLevel() != null) {
            levelResponseDTO = new LevelResponseDTO(word.getLevel().getIdLevel(), word.getLevel().getNameLevel());
        }

        return new WordResponseDTO(word.getIdWord(), word.getEnglishWord(), word.getSpanishWord(), word.getImageUrl(), categoryResponseDTO, levelResponseDTO);
    }

    //Función para crear nuevas palabras en inglés y español
    @PostMapping("/admin/word/create")
    public ResponseEntity<?> createWord(@Valid @RequestBody WordRequestDTO wordRequestDTO){
        try {
            Word newWord = wordManagementService.saveWord(wordRequestDTO);
            return new ResponseEntity<>(mapToResponseDTO(newWord), HttpStatus.CREATED);
        } catch (CategoryNotFoundException | LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (WordAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Función para actualizar las palabras en inglés y español por el ID
    @PutMapping("/admin/word/update-word/{id}")
    public ResponseEntity<?> updateWord(@PathVariable Long id, @Valid @RequestBody WordRequestDTO wordRequestDTO){
        try {
            Word updatedWord = wordManagementService.updateWord(id, wordRequestDTO); //Llamada al servicio DTO
            return ResponseEntity.ok(mapToResponseDTO(updatedWord));
        }catch (WordNotFoundException | CategoryNotFoundException | LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Función para buscar todas las palabras
    @GetMapping("/auth/word/get-all-words")
    public ResponseEntity<List<WordResponseDTO>> getAllWords(){
        List<Word> words = wordManagementService.getAllWords();

        //Mapea cada Word a WordResponseDTO
        List<WordResponseDTO> wordDtos = words.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(wordDtos, HttpStatus.OK);
    }

    // ---- Función para buscar la palabra por el ID ----
    @GetMapping("/auth/word/get-word/{id}")
    public ResponseEntity<?> getWordById(@PathVariable Long id){
        try {
            Word word = wordManagementService.getWordById(id)
                    .orElseThrow(() -> new WordNotFoundException("La palabra no fue encontrada con el ID: " + id));
            return new ResponseEntity<>(mapToResponseDTO(word), HttpStatus.OK);

        } catch (WordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Función para buscar por palabras
    @GetMapping("/auth/word/search")
    public ResponseEntity<?> getByWord(@RequestParam("query") String query){
        try {
            Word foundWord = wordManagementService.getByWord(query);
            return new ResponseEntity<>(mapToResponseDTO(foundWord), HttpStatus.OK);
        } catch (WordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Función para eliminar las palabras, buscando por palabras
    @DeleteMapping("/admin/word/delete")
    public ResponseEntity<?> deleteByWord(@RequestParam("query") String query){
        try {
            wordManagementService.deleteWord(query);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (WordNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/word/delete-by-id/{id}")
    public ResponseEntity<?> deleteWordById(@PathVariable("id") Long id){
        try {
            wordManagementService.deleteWordById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (WordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/word-by-category/{id}")
    public ResponseEntity<List<WordResponseDTO>> getWordsByCategoryId(@PathVariable("id") Long categoryId) {
        List<Word> words = wordManagementService.getWordsByCategory(categoryId);
        List<WordResponseDTO> wordDtos = words.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(wordDtos, HttpStatus.OK);
    }

        @GetMapping("/auth/word-by-level/{id}")
        public ResponseEntity<List<WordResponseDTO>> getWordsByLevelId(@PathVariable("id") Long levelId) {
            List<Word> words = wordManagementService.getWordsByLevel(levelId);
            List<WordResponseDTO> wordDtos = words.stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(wordDtos, HttpStatus.OK);
        }
}
