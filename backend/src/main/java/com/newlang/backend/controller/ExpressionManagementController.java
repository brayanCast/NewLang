package com.newlang.backend.controller;


import com.newlang.backend.entity.Expression;
import com.newlang.backend.exceptions.ExpressionAlreadyExistException;
import com.newlang.backend.exceptions.ExpressionNotFoundException;
import com.newlang.backend.service.ExpressionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
public class ExpressionManagementController {

    @Autowired
    private ExpressionManagementService expressionManagementService;

    @PostMapping("/admin/expression/create")
    public ResponseEntity<?> createExpression(@RequestBody Expression expression){
        try {
            return new ResponseEntity<>(expressionManagementService.saveExpression(expression), HttpStatus.CREATED);
        } catch (ExpressionAlreadyExistException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/admin/expression/update-expression/{id}")
    public ResponseEntity<?> updateExpression(@PathVariable Long id, @RequestBody Expression expression) {
        try {
            return ResponseEntity.ok(expressionManagementService.updateExpression(id, expression));
        } catch (ExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e ) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/expression/get-all-expressions")
    public ResponseEntity<List<Expression>> getAllExpressions(){
        List<Expression> expressions = expressionManagementService.getAllExpression();
        return new ResponseEntity<>(expressions, HttpStatus.OK);
    }

    @GetMapping("/auth/expression/get-expression/{id}")
    public ResponseEntity<Expression> getExpressionById(@PathVariable Long id){
        Optional<Expression> expression = expressionManagementService.getExpressionById(id);
        return expression.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/auth/expression/get-expression")
    public ResponseEntity<Expression> getByExpression(@RequestParam("expression") String expression) {

        Expression foundExpression = expressionManagementService.getByExpression(expression);
        return new ResponseEntity<>(foundExpression, HttpStatus.OK);
    }

    @DeleteMapping("/admin/expression/delete")
    public ResponseEntity<?> deleteExpression(@RequestParam("expression") String expression){
        try {
            expressionManagementService.deleteExpression(expression);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
