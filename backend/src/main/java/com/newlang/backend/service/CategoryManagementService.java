package com.newlang.backend.service;

import com.newlang.backend.entity.Category;
import com.newlang.backend.exceptions.CategoryAlreadyExistException;
import com.newlang.backend.exceptions.CategoryNotFoundException;
import com.newlang.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryManagementService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {return categoryRepository.findAll();}

    public Optional<Category> getCategoryById(Long id){ return categoryRepository.findById(id);}

    public Category saveCategory(String nameCategory) {

        if (categoryRepository.findByNameCategory(nameCategory).isPresent()) {
            throw new CategoryAlreadyExistException("La categoría ya se encuentra registrada");
        }
        Category category = new Category();
        category.setNameCategory(nameCategory);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setNameCategory(updatedCategory.getNameCategory());
                    return categoryRepository.save(existingCategory);
                }).orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada"));
    }

    public void deleteCategoryById(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.deleteById(id);
        } else {
            throw new CategoryNotFoundException("No se encontró el nivel con el ID" + id);
        }
    }
}
