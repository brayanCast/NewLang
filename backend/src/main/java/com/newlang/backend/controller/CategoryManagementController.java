package com.newlang.backend.controller;

import com.newlang.backend.dto.CategoryResponseDTO;
import com.newlang.backend.entity.Category;
import com.newlang.backend.exceptions.CategoryAlreadyExistException;
import com.newlang.backend.exceptions.CategoryNotFoundException;
import com.newlang.backend.service.CategoryManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/admin/category")
public class CategoryManagementController {

    @Autowired
    private CategoryManagementService categoryManagementService;

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category newCategory = categoryManagementService.saveCategory(category.getNameCategory());
            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
        } catch (CategoryAlreadyExistException e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryManagementService.updateCategory(id, category));
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-categories")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<Category> categories = categoryManagementService.getAllCategories();
        List<CategoryResponseDTO> categoryDtos = categories.stream()
                .map(category -> new CategoryResponseDTO(category.getIdCategory(), category.getNameCategory()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @GetMapping("/get-category/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") Long id) {
        return categoryManagementService.getCategoryById(id)
                .map(category -> new CategoryResponseDTO(category.getIdCategory(), category.getNameCategory()))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
