package com.newlang.backend.service;

import com.newlang.backend.entity.Expression;
import com.newlang.backend.entity.LearningRoutineExpression;
import com.newlang.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LearningRoutineManagementService {

    @Autowired
    private LearningRoutineRepository learningRoutineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private ExpressionRepository expressionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private LearningRoutineWordRepository learningRoutineWordRepository;

    @Autowired
    private LearningRoutineExpressionRepository learningRoutineExpressionRepository;


}
