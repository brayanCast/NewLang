package com.newlang.backend.dto.responseDto;

import com.newlang.backend.dto.requestDto.RequestResp;

import java.util.List;

public class LearningRoutineResponseDTO {

    private Long idRoutine;
    private String nameRoutine;
    private RequestResp user;
    private CategoryResponseDTO categoryResponseDTO;
    private LevelResponseDTO levelResponseDTO;
    private List<LearningRoutineWordResponseDTO> learningRoutineWordResponseDTOs;
    private List<LearningRoutineExpressionResponseDTO> learningRoutineExpressionResponseDTOs;

    public LearningRoutineResponseDTO() {
    }

    public LearningRoutineResponseDTO(Long idRoutine, String nameRoutine, RequestResp user,
                                      CategoryResponseDTO categoryResponseDTO, LevelResponseDTO levelResponseDTO,
                                      List<LearningRoutineWordResponseDTO> learningRoutineWordResponseDTOs,
                                      List<LearningRoutineExpressionResponseDTO> learningRoutineExpressionResponseDTOs) {
        this.idRoutine = idRoutine;
        this.nameRoutine = nameRoutine;
        this.user = user;
        this.categoryResponseDTO = categoryResponseDTO;
        this.levelResponseDTO = levelResponseDTO;
        this.learningRoutineWordResponseDTOs = learningRoutineWordResponseDTOs;
        this.learningRoutineExpressionResponseDTOs = learningRoutineExpressionResponseDTOs;
    }

    public Long getIdRoutine() {
        return idRoutine;
    }

    public void setIdRoutine(Long idRoutine) {
        this.idRoutine = idRoutine;
    }

    public String getNameRoutine() {
        return nameRoutine;
    }

    public void setNameRoutine(String nameRoutine) {
        this.nameRoutine = nameRoutine;
    }

    public RequestResp getUser() {
        return user;
    }

    public void setUser(RequestResp user) {
        this.user = user;
    }

    public CategoryResponseDTO getCategoryResponseDTO() {
        return categoryResponseDTO;
    }

    public void setCategoryResponseDTO(CategoryResponseDTO categoryResponseDTO) {
        this.categoryResponseDTO = categoryResponseDTO;
    }

    public LevelResponseDTO getLevelResponseDTO() {
        return levelResponseDTO;
    }

    public void setLevelResponseDTO(LevelResponseDTO levelResponseDTO) {
        this.levelResponseDTO = levelResponseDTO;
    }

    public List<LearningRoutineWordResponseDTO> getLearningRoutineWordResponseDTOs() {
        return learningRoutineWordResponseDTOs;
    }

    public void setLearningRoutineWordResponseDTOs(List<LearningRoutineWordResponseDTO> learningRoutineWordResponseDTOs) {
        this.learningRoutineWordResponseDTOs = learningRoutineWordResponseDTOs;
    }

    public List<LearningRoutineExpressionResponseDTO> getLearningRoutineExpressionResponseDTOs() {
        return learningRoutineExpressionResponseDTOs;
    }

    public void setLearningRoutineExpressionResponseDTOs(List<LearningRoutineExpressionResponseDTO> learningRoutineExpressionResponseDTOs) {
        this.learningRoutineExpressionResponseDTOs = learningRoutineExpressionResponseDTOs;
    }
}
