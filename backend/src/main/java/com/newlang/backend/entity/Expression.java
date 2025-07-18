package com.newlang.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expression")
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExpression;

    @Column(nullable = false)
    private String englishExpression;

    @Column(nullable = false)
    private String spanishExpression;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_level")
    private Level level;

    public Long getIdExpression() {
        return idExpression;
    }

    public void setIdExpression(Long idExpression) {
        this.idExpression = idExpression;
    }

    public String getEnglishExpression() {
        return englishExpression;
    }

    public void setEnglishExpression(String englishExpression) {
        this.englishExpression = englishExpression;
    }

    public String getSpanishExpression() {
        return spanishExpression;
    }

    public void setSpanishExpression(String spanishExpression) {
        this.spanishExpression = spanishExpression;
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
}
