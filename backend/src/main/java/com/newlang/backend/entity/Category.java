    package com.newlang.backend.entity;
    
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.NoArgsConstructor;
    
    import java.util.Set;
    
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "category")
    public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_category")
        private Long idCategory;
    
        @Column(nullable = false, unique = true)
        private String nameCategory;
    
        @OneToMany(mappedBy = "category")
        @JsonIgnoreProperties("category")
        private Set<Word> words;
    
        @OneToMany(mappedBy = "category")
        @JsonIgnoreProperties("category")
        private Set<Expression> expressions;
    
        public Long getIdCategory() {return idCategory;}
    
        public void setIdCategory(Long idCategory) {this.idCategory = idCategory;}
    
        public String getNameCategory() {return nameCategory;}
    
        public void setNameCategory(String nameCategory) {this.nameCategory = nameCategory;}
    
        public Set<Word> getWords() {return words;}
    
        public void setWords(Set<Word> words) {this.words = words;}
    
        public Set<Expression> getExpressions() {return expressions;}
    
        public void setExpressions(Set<Expression> expressions) {this.expressions = expressions;}
    }
