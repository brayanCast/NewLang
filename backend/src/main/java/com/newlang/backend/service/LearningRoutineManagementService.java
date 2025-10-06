package com.newlang.backend.service;

import com.newlang.backend.dto.requestDto.LearningRoutineExpressionRequestDTO;
import com.newlang.backend.dto.requestDto.LearningRoutineRequestDTO;
import com.newlang.backend.dto.requestDto.LearningRoutineWordRequestDTO;
import com.newlang.backend.dto.requestDto.RequestResp;
import com.newlang.backend.dto.responseDto.*;
import com.newlang.backend.entity.*;
import com.newlang.backend.exceptions.*;
import com.newlang.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

    @Transactional
    public LearningRoutineResponseDTO createLearningRoutine(LearningRoutineRequestDTO learningRoutineRequestDTO, String userEmail) {
        //Busca al usuario logueado con el email
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundByIdException("Usuario logueado no encontrado en la base de datos con el email:" + userEmail));

        Category category = categoryRepository.findById(learningRoutineRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con el ID" + learningRoutineRequestDTO.getCategoryId()));

        Level level = levelRepository.findById(learningRoutineRequestDTO.getLevelId())
                .orElseThrow(() -> new LevelNotFoundException("Nivel no encontrado con el ID " + learningRoutineRequestDTO.getLevelId()));

        if (learningRoutineRepository.findByNameRoutineAndUser(learningRoutineRequestDTO.getNameRoutine(), user).isPresent()) {
            throw new LearningRoutineAlreadyExistException("La Rutina ya está creada con ese nombre para el usuario");
        }

        LearningRoutine routine = new LearningRoutine();
        routine.setNameRoutine(learningRoutineRequestDTO.getNameRoutine());
        routine.setUser(user);
        routine.setCategory(category);
        routine.setLevel(level);

        LearningRoutine savedRoutine = learningRoutineRepository.save(routine);
        return mapRoutineToResponseDTO(savedRoutine);
    }

    @Transactional(readOnly = true)
    public LearningRoutineResponseDTO getLearningRoutineById(Long id) {
        LearningRoutine routine = learningRoutineRepository.findById(id)
                .orElseThrow(() -> new LearningRoutineNotFoundException("La rutina no fue encontrada con el Id :" + id));
        return mapRoutineToResponseDTO(routine);
    }

    @Transactional(readOnly = true)
    public List<LearningRoutineResponseDTO> getAllLearningRoutinesByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException("Usuario no encontrado con el Id: " + id));
        List<LearningRoutine> routines = learningRoutineRepository.findByUser(user);
        return routines.stream()
                .map(this::mapRoutineToResponseDTO)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public RequestResp getAllMyLearningRoutines(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundByIdException("Usuario no encontrado con el email" + userEmail));

        List<LearningRoutine> routines = learningRoutineRepository.findByUser(user);

        List<LearningRoutineResponseDTO> routineDtos = routines.stream()
                .map(this::mapRoutineToResponseDTOWithoutUser)
                .collect(toList());

        RequestResp userDto = new RequestResp(
                user.getIdUser(),
                user.getNameUser(),
                user.getIdNumber(),
                user.getEmail(),
                user.getRole()
        );

        return new RequestResp(userDto, routineDtos);
    }

    @Transactional
    public LearningRoutineResponseDTO updateLearningRoutine(Long id, LearningRoutineRequestDTO requestDTO, String userEmail) {
        LearningRoutine routine = learningRoutineRepository.findById(id)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina de Aprendizaje no encontrada con ID: "+ id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundByIdException("Usuario no encontrado con ID:" + userEmail));

        if (!routine.getUser().getIdUser().equals(user.getIdUser())) {
            throw new UnauthorizedActionException("No tiene permiso para actualizar esta rutina");
        }

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con Id: " + requestDTO.getCategoryId()));

        Level level = levelRepository.findById(requestDTO.getLevelId())
                .orElseThrow(() -> new LevelNotFoundException("Nivel no encontrado con el Id: " + requestDTO.getLevelId()));

        Optional<LearningRoutine> existingRoutineByName = learningRoutineRepository.findByNameRoutineAndUser(requestDTO.getNameRoutine(), user);
        if (existingRoutineByName.isPresent() && !existingRoutineByName.get().getIdRoutine().equals(id)) {
            throw new LearningRoutineAlreadyExistException("Ya existe otra rutina con el nombre: " +requestDTO.getNameRoutine() + " para este usuario");
        }

        routine.setNameRoutine(requestDTO.getNameRoutine());
        routine.setUser(user);
        routine.setCategory(category);
        routine.setLevel(level);

        LearningRoutine updateRoutine = learningRoutineRepository.save(routine);
        return mapRoutineToResponseDTO(updateRoutine);
    }

    @Transactional
    public void deleteLearningRoutine(Long id, String userEmail) {

        LearningRoutine routine = learningRoutineRepository.findById(id)
                        .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina de aprendizaje no encontrada con el id: " + id));

        if (!routine.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedActionException("No tiene permiso para eliminar esta rutina");
        }

        learningRoutineRepository.deleteById(id);
    }

    @Transactional
    public LearningRoutineWordResponseDTO addWordToRoutine(Long routineId, LearningRoutineWordRequestDTO requestDTO) {
        LearningRoutine routine = learningRoutineRepository.findById(routineId)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina no encontrada con el ID: " + routineId));
        Word word = wordRepository.findById(requestDTO.getIdWord())
                .orElseThrow(() -> new WordNotFoundException("Palabra no encontrada con el Id: " + requestDTO.getIdWord()));

        if (learningRoutineWordRepository.findByLearningRoutineAndWord(routine, word).isPresent()) {
            throw new LearningRoutineWordAlreadyExistException("La palabra ya está asociada con esta rutina");
        }

        LearningRoutineWord routineWord = new LearningRoutineWord();
        routineWord.setLearningRoutine(routine);
        routineWord.setWord(word);
        routineWord.setStatus(requestDTO.getStatus());
        routineWord.setLastReviewedUpdate(requestDTO.getLastReviewedDate());
        routineWord.setNextReviewedUpdate(requestDTO.getNextReviewedDate());
        routineWord.setMasteryLevel(requestDTO.getMasteryLevel());

        LearningRoutineWord saveRoutineWord = learningRoutineWordRepository.save(routineWord);
        return mapRoutineWordToResponseDTO(saveRoutineWord);
    }


    @Transactional
    public LearningRoutineWordResponseDTO updateWordInRoutine(Long routineId, Long wordId, LearningRoutineWordRequestDTO requestDTO) {
        LearningRoutine routine = learningRoutineRepository.findById(routineId)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina no encontrada con Id: " + routineId));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new WordNotFoundException("Palabra no encontrada con el Id: " + wordId));

        LearningRoutineWordId id = new LearningRoutineWordId(routine.getIdRoutine(), word.getIdWord());
        LearningRoutineWord routineWord = learningRoutineWordRepository.findById(id)
                .orElseThrow(() -> new LearningRoutineWordNotFoundException("La palabra no está en esta rutina"));

        routineWord.setStatus(requestDTO.getStatus());
        routineWord.setLastReviewedUpdate(requestDTO.getLastReviewedDate());
        routineWord.setNextReviewedUpdate(requestDTO.getNextReviewedDate());
        routineWord.setMasteryLevel(requestDTO.getMasteryLevel());

        LearningRoutineWord updateRoutineWord = learningRoutineWordRepository.save(routineWord);
        return mapRoutineWordToResponseDTO(updateRoutineWord);
    }

    @Transactional
    public void removeWordFromRoutine(Long routineId, Long wordId) {
        LearningRoutine routine = learningRoutineRepository.findById(routineId)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina no encontrada con Id: " + routineId));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new WordNotFoundException("Palabra no encontrada con Id: " + wordId));

        LearningRoutineWordId id = new LearningRoutineWordId(routine.getIdRoutine(), word.getIdWord());
        learningRoutineWordRepository.findById(id)
                 .orElseThrow(() -> new LearningRoutineWordNotFoundException("La palabra no está en esta rutina"));

         learningRoutineWordRepository.deleteById(id);
    }


    //****************************************************************************
    //****************************************************************************
    @Transactional
    public LearningRoutineExpressionResponseDTO addExpressionToRoutine(Long routineId, LearningRoutineExpressionRequestDTO requestDTO) {
        LearningRoutine routine = learningRoutineRepository.findById(routineId)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina no encontrada con el ID: " + routineId));
        Expression expression = expressionRepository.findById(requestDTO.getIdExpression())
                .orElseThrow(() -> new WordNotFoundException("Expresión no encontrada con el Id: " + requestDTO.getIdExpression()));

        if (learningRoutineExpressionRepository.findByLearningRoutineAndExpression(routine, expression).isPresent()) {
            throw new LearningRoutineExpressionNotFoundException("La expresión} ya está asociada con esta rutina");
        }

        LearningRoutineExpression routineExpression= new LearningRoutineExpression();

        routineExpression.setLearningRoutine(routine);
        routineExpression.setExpression(expression);
        routineExpression.setStatus(requestDTO.getStatus());
        routineExpression.setLastReviewedUpdate(requestDTO.getLastReviewedDate());
        routineExpression.setNextReviewedUpdate(requestDTO.getNextReviewedDate());
        routineExpression.setMasteryLevel(requestDTO.getMasteryLevel());

        LearningRoutineExpression saveRoutineExpression = learningRoutineExpressionRepository.save(routineExpression);
        return mapRoutineExpressionToResponseDTO(saveRoutineExpression);
    }


    @Transactional
    public LearningRoutineExpressionResponseDTO updateExpressionInRoutine(Long routineId, Long expressionId, LearningRoutineExpressionRequestDTO requestDTO) {
        LearningRoutine routine = learningRoutineRepository.findById(routineId)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina no encontrada con Id: " + routineId));
        Expression expression = expressionRepository.findById(expressionId)
                .orElseThrow(() -> new WordNotFoundException("Palabra no encontrada con el Id: " + expressionId));

        LearningRoutineExpressionId id = new LearningRoutineExpressionId(routine.getIdRoutine(), expression.getIdExpression());
        LearningRoutineExpression routineExpression = learningRoutineExpressionRepository.findById(id)
                .orElseThrow(() -> new LearningRoutineExpressionNotFoundException("La palabra no está en esta rutina"));

        routineExpression.setStatus(requestDTO.getStatus());
        routineExpression.setLastReviewedUpdate(requestDTO.getLastReviewedDate());
        routineExpression.setNextReviewedUpdate(requestDTO.getNextReviewedDate());
        routineExpression.setMasteryLevel(requestDTO.getMasteryLevel());

        LearningRoutineExpression updateRoutineExpression = learningRoutineExpressionRepository.save(routineExpression);
        return mapRoutineExpressionToResponseDTO(updateRoutineExpression);
    }

    @Transactional
    public void removeExpressionFromRoutine(Long routineId, Long expressionId) {
        LearningRoutine routine = learningRoutineRepository.findById(routineId)
                .orElseThrow(() -> new LearningRoutineNotFoundException("Rutina no encontrada con Id: " + routineId));
        Expression expression = expressionRepository.findById(expressionId)
                .orElseThrow(() -> new ExpressionNotFoundException("Expresión no encontrada con Id: " + expressionId));

        LearningRoutineExpressionId id = new LearningRoutineExpressionId(routine.getIdRoutine(), expression.getIdExpression());
        learningRoutineExpressionRepository.findById(id)
                .orElseThrow(() -> new LearningRoutineExpressionNotFoundException("La expresión no está en esta rutina"));

        learningRoutineExpressionRepository.deleteById(id);
    }

    //***********************************************************************************************
    //***********************************************************************************************


    private LearningRoutineResponseDTO mapRoutineToResponseDTO(LearningRoutine routine) {
        if (routine == null) return null;
        RequestResp requestResp = new RequestResp(routine.getUser().getIdUser(), routine.getUser().getNameUser(),
                routine.getUser().getIdNumber(), routine.getUser().getEmail(), routine.getUser().getRole());

        CategoryResponseDTO categoryDto = new CategoryResponseDTO(routine.getCategory().getIdCategory(), routine.getCategory().getNameCategory());

        LevelResponseDTO levelDto = new LevelResponseDTO(routine.getLevel().getIdLevel(), routine.getLevel().getNameLevel());

        List<LearningRoutineWordResponseDTO> wordDtos = routine.getLearningRoutineWords() != null ?
                routine.getLearningRoutineWords().stream()
                .map(this::mapRoutineWordToResponseDTO)
                .collect(toList()) : null;
        List<LearningRoutineExpressionResponseDTO> expressionDtos = routine.getLearningRoutineExpressions() != null ?
        routine.getLearningRoutineExpressions().stream()
                .map(this::mapRoutineExpressionToResponseDTO)
                .collect(toList()) : null;

        return new LearningRoutineResponseDTO(
                routine.getIdRoutine(),
                routine.getNameRoutine(),
                requestResp,
                categoryDto,
                levelDto,
                wordDtos,
                expressionDtos
        );

    }

    private LearningRoutineWordResponseDTO mapRoutineWordToResponseDTO(LearningRoutineWord routineWord) {
        if (routineWord == null) return null;

        Category category = routineWord.getWord().getCategory();
        CategoryResponseDTO categoryDto = null;
        if (category != null) {
            categoryDto = new CategoryResponseDTO(category.getIdCategory(), category.getNameCategory());
        }

        Level level = routineWord.getWord().getLevel();
        LevelResponseDTO levelDto = null;
        if (level != null) {
            levelDto = new LevelResponseDTO(level.getIdLevel(), level.getNameLevel());
        }


        WordResponseDTO wordDto = new WordResponseDTO(
                routineWord.getWord().getIdWord(),
                routineWord.getWord().getEnglishWord(),
                routineWord.getWord().getSpanishWord(),
                routineWord.getWord().getImageUrl(),
                categoryDto,
                levelDto
        );

        return new LearningRoutineWordResponseDTO(
                wordDto,
                routineWord.getStatus(),
                routineWord.getLastReviewedUpdate(),
                routineWord.getNextReviewedUpdate(),
                routineWord.getMasteryLevel()
        );
    }

    private LearningRoutineResponseDTO mapRoutineToResponseDTOWithoutUser(LearningRoutine routine) {
        if (routine == null) return null;

        CategoryResponseDTO categoryDto = new CategoryResponseDTO(routine.getCategory().getIdCategory(), routine.getCategory().getNameCategory());
        LevelResponseDTO levelDto = new LevelResponseDTO(routine.getLevel().getIdLevel(), routine.getLevel().getNameLevel());

        List<LearningRoutineWordResponseDTO> wordDtos = routine.getLearningRoutineWords() != null ?
                routine.getLearningRoutineWords().stream()
                        .map(this::mapRoutineWordToResponseDTO)
                        .toList() : null;

        List<LearningRoutineExpressionResponseDTO> expressionDtos = routine.getLearningRoutineExpressions() != null ?
                routine.getLearningRoutineExpressions().stream()
                        .map(this::mapRoutineExpressionToResponseDTO)
                        .toList() : null;

        return new LearningRoutineResponseDTO(
                routine.getIdRoutine(),
                routine.getNameRoutine(),
                null,
                categoryDto,
                levelDto,
                wordDtos,
                expressionDtos
        );
    }

    private LearningRoutineExpressionResponseDTO mapRoutineExpressionToResponseDTO(LearningRoutineExpression routineExpression) {
        if (routineExpression == null) return null;

        Category category = routineExpression.getExpression().getCategory();
        CategoryResponseDTO categoryDto = null;
        if (category != null) {
            categoryDto = new CategoryResponseDTO(category.getIdCategory(), category.getNameCategory());
        }

        Level level = routineExpression.getExpression().getLevel();
        LevelResponseDTO levelDto = null;
        if (level != null) {
            levelDto = new LevelResponseDTO(level.getIdLevel(), level.getNameLevel());
        }

        ExpressionResponseDTO expressionDto = new ExpressionResponseDTO(
                routineExpression.getExpression().getIdExpression(),
                routineExpression.getExpression().getEnglishExpression(),
                routineExpression.getExpression().getSpanishExpression(),
                routineExpression.getExpression().getImageUrl(),
                categoryDto,
                levelDto
        );

        return new LearningRoutineExpressionResponseDTO(
                expressionDto,
                routineExpression.getStatus(),
                routineExpression.getLastReviewedUpdate(),
                routineExpression.getNextReviewedUpdate(),
                routineExpression.getMasteryLevel()
        );
    }
}
