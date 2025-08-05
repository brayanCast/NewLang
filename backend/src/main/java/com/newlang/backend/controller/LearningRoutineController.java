package com.newlang.backend.controller;

import com.newlang.backend.dto.requestDto.LearningRoutineExpressionRequestDTO;
import com.newlang.backend.dto.requestDto.LearningRoutineRequestDTO;
import com.newlang.backend.dto.requestDto.LearningRoutineWordRequestDTO;
import com.newlang.backend.dto.responseDto.LearningRoutineExpressionResponseDTO;
import com.newlang.backend.dto.responseDto.LearningRoutineResponseDTO;
import com.newlang.backend.dto.responseDto.LearningRoutineWordResponseDTO;
import com.newlang.backend.exceptions.*;
import com.newlang.backend.service.LearningRoutineManagementService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/auth/routine")
public class LearningRoutineController {

    @Autowired
    private LearningRoutineManagementService learningRoutineManagementService;

    @PostMapping("/create")
    public ResponseEntity<?> createLearningRoutine(@Valid @RequestBody LearningRoutineRequestDTO requestDTO) {
        try{
            LearningRoutineResponseDTO responseDTO = learningRoutineManagementService.createLearningRoutine(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (UserNotFoundByIdException | CategoryNotFoundException | LevelNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LearningRoutineAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-learning-by-id/{id}")
    public ResponseEntity<?> getLearningRoutineById(@PathVariable Long id) {
        try {
            LearningRoutineResponseDTO responseDTO = learningRoutineManagementService.getLearningRoutineById(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (LearningRoutineNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-routine-by-user/{id}")
    public ResponseEntity<List<LearningRoutineResponseDTO>> getLearningRoutineByUserId(@PathVariable Long id) {
        try {
            List<LearningRoutineResponseDTO> responseDTOs = learningRoutineManagementService.getAllLearningRoutinesByUserId(id);
            return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
        } catch (UserNotFoundByIdException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-routine/{id}")
    public ResponseEntity<?> updateLearningRoutine(@PathVariable Long id, @Valid @RequestBody LearningRoutineRequestDTO requestDTO) {
        try {
            LearningRoutineResponseDTO responseDTO = learningRoutineManagementService.updateLearningRoutine(id, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (LearningRoutineNotFoundException | UserNotFoundByIdException | CategoryNotFoundException | LevelNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LearningRoutineAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLearningRoutine(@PathVariable Long id) {
        try {
            learningRoutineManagementService.deleteLearningRoutine(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LearningRoutineNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create-routine/{id}/word")
    public ResponseEntity<?> addWordToRoutine(@PathVariable Long id, @Valid@RequestBody LearningRoutineWordRequestDTO requestDTO) {
        try {
            LearningRoutineWordResponseDTO responseDTO = learningRoutineManagementService.addWordToRoutine(id, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (LearningRoutineNotFoundException | WordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LearningRoutineWordAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-routine/{routineId}/word/{wordId}")
    public ResponseEntity<?> updateWordInRoutine(@PathVariable Long routineId, @PathVariable Long wordId, @Valid @RequestBody LearningRoutineWordRequestDTO requestDTO) {
        try {
            LearningRoutineWordResponseDTO responseDTO = learningRoutineManagementService.updateWordInRoutine(routineId, wordId, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (LearningRoutineNotFoundException | WordNotFoundException | LearningRoutineWordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-routine/{routineId}/word/{wordId}")
    public ResponseEntity<?> removeWordFromRoutine(@PathVariable Long routineId, @PathVariable Long wordId) {
        try {
            learningRoutineManagementService.removeWordFromRoutine(routineId, wordId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LearningRoutineNotFoundException | WordNotFoundException | LearningRoutineWordNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/create-routine/{id}/expression")
    public ResponseEntity<?> addExpressionToRoutine(@PathVariable Long id, @Valid@RequestBody LearningRoutineExpressionRequestDTO requestDTO) {
        try {
            LearningRoutineExpressionResponseDTO responseDTO = learningRoutineManagementService.addExpressionToRoutine(id, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (LearningRoutineNotFoundException | ExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (LearningRoutineExpressionAlreadyExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-routine/{routineId}/expression/{expressionId}")
    public ResponseEntity<?> updateExpressionInRoutine(@PathVariable Long routineId, @PathVariable Long expressionId, @Valid @RequestBody LearningRoutineExpressionRequestDTO requestDTO) {
        try {
            LearningRoutineExpressionResponseDTO responseDTO = learningRoutineManagementService.updateExpressionInRoutine(routineId, expressionId, requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (LearningRoutineNotFoundException | ExpressionNotFoundException | LearningRoutineExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-routine/{routineId}/expression/{expressionId}")
    public ResponseEntity<?> removeExpressionFromRoutine(@PathVariable Long routineId, @PathVariable Long expressionId) {
        try {
            learningRoutineManagementService.removeExpressionFromRoutine(routineId, expressionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (LearningRoutineNotFoundException | ExpressionNotFoundException | LearningRoutineExpressionNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
