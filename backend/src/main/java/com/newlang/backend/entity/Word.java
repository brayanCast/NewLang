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
    private String word;
    private String wordStatus;

    public Long getIdWord() {
        return idWord;
    }

    public void setIdWord(Long idWord) {
        this.idWord = idWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordStatus() {
        return wordStatus;
    }

    public void setWordStatus(String wordStatus) {
        this.wordStatus = wordStatus;
    }
}
