package com.newlang.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "level")
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_level")
    private Long idLevel;

    @Column(nullable = false, unique = true)
    private String nameLevel;

    @OneToMany(mappedBy = "level")
    @JsonIgnoreProperties("level")
    private Set<Word> words;

    @OneToMany(mappedBy = "level")
    @JsonIgnoreProperties("level")
    private Set<Expression> expressions;

    public Long getIdLevel() {return idLevel;}

    public void setIdLevel(Long idLevel) {this.idLevel = idLevel;}

    public String getNameLevel() {return nameLevel;}

    public void setNameLevel(String nameLevel) {this.nameLevel = nameLevel;}

    public Set<Word> getWords() {return words;}

    public void setWords(Set<Word> words) {this.words = words;}

    public Set<Expression> getExpressions() {return expressions;}

    public void setExpressions(Set<Expression> expressions) {this.expressions = expressions;}
}
