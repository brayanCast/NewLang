package com.newlang.backend.service;

import com.newlang.backend.entity.Expression;
import com.newlang.backend.exceptions.ExpressionAlreadyExistException;
import com.newlang.backend.exceptions.ExpressionNotFoundException;
import com.newlang.backend.repository.ExpressionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class ExpressionManagementService {

    @Autowired
    private ExpressionRepository expressionRepository;

    // Función para guardar la información de la expresión, validando que esta no exista
    public Expression saveExpression(Expression expression){
        Optional<Expression> existingExpressionByText = expressionRepository.findByEnglishExpression(expression.getEnglishExpression());

        if(existingExpressionByText.isPresent()){
            throw new ExpressionAlreadyExistException("Expression already exist");
        } else {
            return expressionRepository.save(expression);
        }
    }

    public List<Expression> getAllExpression(){
        return expressionRepository.findAll();
    }

    public Optional<Expression> getExpressionById(Long id){
        return expressionRepository.findById(id);
    }

    public Expression getByExpression(String expression){
        return expressionRepository.findByEnglishExpression(expression.toLowerCase())
                .or(() -> expressionRepository.findBySpanishExpression(expression.toLowerCase()))
                .orElseThrow(() -> new ExpressionNotFoundException
                        ("Expression has not been encountered"));
    }
}
