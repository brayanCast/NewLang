package com.newlang.backend.controller;

import com.newlang.backend.dto.requestDto.ExpressionRequestDTO;
import com.newlang.backend.dto.responseDto.CategoryResponseDTO;
import com.newlang.backend.dto.responseDto.ExpressionResponseDTO;
import com.newlang.backend.dto.responseDto.LevelResponseDTO;
import com.newlang.backend.entity.Expression;
import com.newlang.backend.exceptions.*;
import com.newlang.backend.service.ExpressionManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class ExpressionManagementController {

    @Autowired
    private ExpressionManagementService expressionManagementService;

    // -- Método auxiliar para mapear Expression entity y ExpressionResponseDTO ----
    private ExpressionResponseDTO mapToResponseDTO(Expression expression) {
        CategoryResponseDTO categoryResponseDTO = null;
        if (expression.getCategory() != null) {
            categoryResponseDTO = new CategoryResponseDTO(expression.getCategory().getIdCategory(), expression.getCategory().getNameCategory());
        }

        LevelResponseDTO levelResponseDTO = null;
        if (expression.getLevel() != null) {
            levelResponseDTO = new LevelResponseDTO(expression.getLevel().getIdLevel(), expression.getLevel().getNameLevel());
        }

        return new ExpressionResponseDTO(expression.getIdExpression(), expression.getEnglishExpression(), expression.getSpanishExpression(), expression.getImageUrl(), categoryResponseDTO, levelResponseDTO);
    }

    @PostMapping("/admin/expression/create")
    public ResponseEntity<?> createExpression(@Valid @RequestBody ExpressionRequestDTO expressionRequestDTO){
        try {
            Expression newExpression = expressionManagementService.saveExpression(expressionRequestDTO);
            return new ResponseEntity<>(mapToResponseDTO(newExpression), HttpStatus.CREATED);
        } catch (CategoryNotFoundException | LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ExpressionAlreadyExistException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/expression/update-expression/{id}")
    public ResponseEntity<?> updateExpression(@PathVariable Long id, @Valid @RequestBody ExpressionRequestDTO expressionRequestDTO) {
        try {
            Expression expression = expressionManagementService.updateExpression(id, expressionRequestDTO); //Llama al servicio DTO
            return ResponseEntity.ok(mapToResponseDTO(expression));
        } catch (ExpressionNotFoundException | CategoryNotFoundException | LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/expression/get-all-expressions")
    public ResponseEntity<List<ExpressionResponseDTO>> getAllExpressions(){
        List<Expression> expressions = expressionManagementService.getAllExpression();

        //Mapea cada expression a ExpressionResponseDTO
        List<ExpressionResponseDTO> expressionDtos = expressions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(expressionDtos, HttpStatus.OK);
    }

    // ---- Función para buscar la palabra por el ID ----
    @GetMapping("/auth/expression/get-expression/{id}")
    public ResponseEntity<?> getExpressionById(@PathVariable Long id){
        try {
            Expression expression = expressionManagementService.getExpressionById(id)
                    .orElseThrow(() -> new ExpressionNotFoundException("La expresión no fue encontrada con el ID: " + id));
            return new ResponseEntity<>(mapToResponseDTO(expression), HttpStatus.OK);
        } catch (ExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Función para buscar por la expresión
    @GetMapping("/auth/expression/search")
    public ResponseEntity<?> getByExpression(@RequestParam("query") String query) {
        try {
            Expression foundExpression = expressionManagementService.getByExpression(query);
            return new ResponseEntity<>(mapToResponseDTO(foundExpression), HttpStatus.OK);
        } catch (ExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/admin/expression/delete")
    public ResponseEntity<?> deleteExpression(@RequestParam("query") String query){
        try {
            expressionManagementService.deleteExpression(query);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/expression-by-category/{id}")
    public ResponseEntity<List<ExpressionResponseDTO>> getExpressionsByCategoryId(@PathVariable Long categoryId) {
        List<Expression> expressions = expressionManagementService.getExpressionByCategory(categoryId);
        List<ExpressionResponseDTO> expressionDtos = expressions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(expressionDtos, HttpStatus.OK);
    }

    @GetMapping("/auth/expression-by-level/{id}")
    public ResponseEntity<List<ExpressionResponseDTO>> getExpressionsByLevelId(@PathVariable Long levelId) {
        List<Expression> expressions = expressionManagementService.getExpressionByLevel(levelId);
        List<ExpressionResponseDTO> expressionDtos = expressions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(expressionDtos, HttpStatus.OK);
    }
}
