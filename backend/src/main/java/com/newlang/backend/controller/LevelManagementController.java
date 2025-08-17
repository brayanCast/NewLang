package com.newlang.backend.controller;

import com.newlang.backend.dto.responseDto.LevelResponseDTO;
import com.newlang.backend.entity.Level;
import com.newlang.backend.exceptions.LevelAlreadyExistException;
import com.newlang.backend.exceptions.LevelNotFoundException;
import com.newlang.backend.service.LevelManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/level")
public class LevelManagementController {

    @Autowired
    private LevelManagementService levelManagementService;

    @PostMapping("/admin/create")
    public ResponseEntity<?> createLevel(@RequestBody Level level) {
        try {
            Level newLevel = levelManagementService.saveLevel(level.getNameLevel());
            return new ResponseEntity<>(newLevel, HttpStatus.CREATED);
        } catch (LevelAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/update-level/{id}")
    public ResponseEntity<?> updateLevel(@PathVariable("id") Long id, @RequestBody Level level) {
        try {
            return ResponseEntity.ok(levelManagementService.updateLevel(id, level));
        } catch (LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/get-all-levels")
    public ResponseEntity<List<LevelResponseDTO>> getAllLevels(){
        List<Level> levels = levelManagementService.getAllLevels();
        List<LevelResponseDTO> levelDtos = levels.stream()
                .map(level -> new LevelResponseDTO(level.getIdLevel(), level.getNameLevel()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(levelDtos, HttpStatus.OK);
    }

    @GetMapping("/auth/get-level/{id}")
    public ResponseEntity<LevelResponseDTO> getLevelById(@PathVariable("id") Long id) {
        return levelManagementService.getLevelById(id)
                .map(level -> new LevelResponseDTO(level.getIdLevel(), level.getNameLevel()))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/admin/delete-level/{id}")
    public ResponseEntity<?> deleteLevelById(@PathVariable Long id) {
        try {
            levelManagementService.deleteLevelById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
