package com.newlang.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idWord;

    @Column(nullable = false)
    private String englishWord;

    @Column(nullable = false)
    private String spanishWord;

    public Long getIdWord() {
        return idWord;
    }

    public void setIdWord(Long idWord) {
        this.idWord = idWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getSpanishWord() {
        return spanishWord;
    }

    public void setSpanishWord(String spanishWord) {
        this.spanishWord = spanishWord;
    }
}
