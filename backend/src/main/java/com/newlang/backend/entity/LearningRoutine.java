package com.newlang.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "learning_routine")
public class LearningRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoutine;

    @Column(nullable = false)
    private String nameRoutine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_level", nullable = false)
    private Level level;

    @OneToMany(mappedBy = "learningRoutine", cascade = CascadeType.ALL)
    private List<LearningRoutineWord> learningRoutineWords;

    @OneToMany(mappedBy = "learningRoutine", cascade = CascadeType.ALL)
    private List<LearningRoutineExpression> learningRoutineExpressions;

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

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<LearningRoutineWord> getLearningRoutineWords() {
        return learningRoutineWords;
    }

    public void setLearningRoutineWords(List<LearningRoutineWord> learningRoutineWords) {
        this.learningRoutineWords = learningRoutineWords;
    }

    public List<LearningRoutineExpression> getLearningRoutineExpressions() {
        return learningRoutineExpressions;
    }

    public void setLearningRoutineExpressions(List<LearningRoutineExpression> learningRoutineExpressions) {
        this.learningRoutineExpressions = learningRoutineExpressions;
    }
}
