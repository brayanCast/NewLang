package com.newlang.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_word")
    private Long idWord;

    @Column(nullable = false)
    private String englishWord;

    @Column(nullable = false)
    private String spanishWord;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_level")
    private Level level;



    public Long getIdWord() {return idWord;}

    public void setIdWord(Long idWord) {this.idWord = idWord;}

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

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public Category getCategory() {return category;}

    public void setCategory(Category category) {this.category = category;}

    public Level getLevel() {return level;}

    public void setLevel(Level level) {this.level = level;}
}
