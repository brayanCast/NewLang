package com.newlang.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
}
