package com.newlang.backend.service;

import com.newlang.backend.dto.requestDto.WordRequestDTO;
import com.newlang.backend.entity.Category;
import com.newlang.backend.entity.Level;
import com.newlang.backend.entity.Word;
import com.newlang.backend.exceptions.*;
import com.newlang.backend.repository.CategoryRepository;
import com.newlang.backend.repository.LevelRepository;
import com.newlang.backend.repository.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WordManagementService {

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LevelRepository levelRepository;

    // ------- Función para guardar la información de la palabra, validando que esta no exista ------
    @Transactional
    public Word saveWord(WordRequestDTO wordRequestDTO) {
        //Valida que la palabra en inglés o en español no haya sido ya creada
        if(wordRepository.findByEnglishWordAndSpanishWord(wordRequestDTO.getEnglishWord(),
                wordRequestDTO.getSpanishWord()).isPresent()) {
            throw new WordAlreadyExistException("La palabra " + wordRequestDTO.getEnglishWord() + "/"
                    + wordRequestDTO.getSpanishWord()+ " ya existe");
        }

        //Obtener y validar que la categoría exista
        Category category = categoryRepository.findById(wordRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada con ID " + wordRequestDTO.getCategoryId()));

        //Obtener y validar que el nivel exista
        Level level = levelRepository.findById(wordRequestDTO.getLevelId())
                .orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado con ID " + wordRequestDTO.getLevelId()));

        //Crea el objeto newWord y guarda los la palabra en inglés y español, la categoría y el nivel (guarda la entidad Word)
        Word newWord = new Word();
        newWord.setEnglishWord(wordRequestDTO.getEnglishWord());
        newWord.setSpanishWord(wordRequestDTO.getSpanishWord());
        newWord.setImageUrl(wordRequestDTO.getImageUrl());
        newWord.setCategory(category);
        newWord.setLevel(level);

        return wordRepository.save(newWord);
    }

    //Función para listar todas las palabras
    public List<Word> getAllWords(){
        return wordRepository.findAll();
    }

    //Función para buscar la palabra por el ID
    public Optional<Word> getWordById(Long id){
        return wordRepository.findById(id);
    }

    //Función para hacer la búsqueda por la palabra
    public Word getByWord(String word){
        return wordRepository.findByEnglishWord(word.toLowerCase())
                .or(() -> wordRepository.findBySpanishWord(word.toLowerCase()))
                .orElseThrow(() -> new WordNotFoundException("La palabra no fue encontrada"));
    }

    // ------- Función para actualizar la palabra (usando wordRequestDTO) -------
    @Transactional
    public Word updateWord(Long id, WordRequestDTO wordRequestDTO){
        //Valida por el ID que la palabra exista
        Word existingWord = wordRepository.findById(id)
                .orElseThrow(() -> new WordNotFoundException("La palabra no fue encontrada con el ID: " + id));

        //Valida por el ID que la categoría exista
        Category newCategory = categoryRepository.findById(wordRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada con el ID " + wordRequestDTO.getCategoryId()));

        //Valida por el Id que el nivel exista
        Level newLevel = levelRepository.findById(wordRequestDTO.getLevelId())
                .orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado con el ID " + wordRequestDTO.getLevelId()));

        //Actualiza y guarda los nuevos datos de la entidad Word
        existingWord.setEnglishWord(wordRequestDTO.getEnglishWord());
        existingWord.setSpanishWord(wordRequestDTO.getSpanishWord());
        existingWord.setImageUrl(wordRequestDTO.getImageUrl());
        existingWord.setCategory(newCategory);
        existingWord.setLevel(newLevel);

        return wordRepository.save(existingWord);
    }

    // ----Función para eliminar las palabras, validando si estas existen en la base de datos ----
    public void deleteWord(String word){
        Optional<Word> foundWord = wordRepository.findByEnglishWord(word.toLowerCase());
        foundWord = foundWord.or(() -> wordRepository.findBySpanishWord(word.toLowerCase()));

         foundWord.ifPresentOrElse(
                 wordToDelete -> wordRepository.deleteById(wordToDelete.getIdWord()),
                 () -> { throw new WordNotFoundException("La palabra " + word + "no fue encontrada para eliminar");
                 }
         );
    }

    // ---- Función para traer la lista de palabras asociadas a las letras que se vayan digitando ----
    public List<Word> getSuggestions(String query) {

        //Valida que la consulta sea vacía, tariga una lista
        if(query == null || query.isEmpty()) {
            return List.of();
        }

        int maxSuggestionsPerService = 5; //Definición de la cantidad de sugrenecias máximas por la palabra

        List<Word> englishMatches = wordRepository.findByEnglishWordStartingWithIgnoreCase(query.toLowerCase());
        List<Word> spanishMatches = wordRepository.findBySpanishWordStartingWithIgnoreCase(query.toLowerCase());

        Set<Word> uniqueWords = new java.util.LinkedHashSet<>();

        //Añade la lista de palabras en inglés y español que coinciden
        uniqueWords.addAll(englishMatches);
        uniqueWords.addAll(spanishMatches);

        /*Retorna la lista de palabras teniendo en cuenta el límite de palabras
        definido en la variable "maxSuggestionsPerService"
        */
        return uniqueWords.stream()
                .limit(maxSuggestionsPerService)
                .collect(Collectors.toList());
    }

    // ---- Función para buscar la lista de palabras por la categoría ----
    public List<Word> getWordsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("La categoría no fue encontrada con el ID" + categoryId));

        return wordRepository.findByCategory(category);
    }

    // ---- Función para buscar la lista de palabras por el nivel ----
    public List<Word> getWordsByLevel(Long levelId) {
        Level level = levelRepository.findById(levelId)
                .orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado con el ID " + levelId));

        return wordRepository.findByLevel(level);
    }

}
