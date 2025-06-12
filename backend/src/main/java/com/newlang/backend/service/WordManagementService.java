package com.newlang.backend.service;

import com.newlang.backend.entity.Word;
import com.newlang.backend.exceptions.WordAlreadyExistException;
import com.newlang.backend.exceptions.WordNotFoundException;
import com.newlang.backend.repository.WordRepository;
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

    // Función para guardar la información de la palabra, validando que esta no exista
    public Word saveWord(Word word) {
        Optional<Word> existingWordByText = wordRepository.findByEnglishWord(word.getEnglishWord());

        if(existingWordByText.isPresent()){
            throw new WordAlreadyExistException("La palabra " + word.getEnglishWord() + " ya existe");
        } else {
            return wordRepository.save(word);
        }
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
                .orElseThrow(() -> new WordNotFoundException("Word has not been encountered"));
    }

    //Función para actualizar la palabra
    public Word updateWord(Long id, Word updatedWord){
        return wordRepository.findById(id)
                .map(existingWord -> {
                    existingWord.setEnglishWord(updatedWord.getEnglishWord());
                    existingWord.setSpanishWord(updatedWord.getSpanishWord());
                    return wordRepository.save(existingWord);
                }).orElseThrow(() -> new WordNotFoundException("Word has not been encountered"));
    }

    //Función para eliminar las palabras, validando si estas existen en la base de datos
    public void deleteWord(String word){
        Optional<Word> foundWord = wordRepository.findByEnglishWord(word.toLowerCase());
        foundWord = foundWord.or(() -> wordRepository.findBySpanishWord(word.toLowerCase()));

         foundWord.ifPresentOrElse(
                 wordToDelete -> wordRepository.deleteById(wordToDelete.getIdWord()),
                 () -> { throw new WordNotFoundException("word " + word + " has not been encountered to delete");
                 }
         );
    }

    public List<String> getSuggestions(String query) {
        if(query == null || query.isEmpty()){
            return List.of();
        }
        int maxSuggestions = 10;

        List<Word> englishMatches = wordRepository.findByEnglishWordStartingWithIgnoreCase(query.toLowerCase());
        List<Word> spanishMatches = wordRepository.findBySpanishWordStartingWithIgnoreCase(query.toLowerCase());

        Set<String> uniqueSuggestion = new java.util.LinkedHashSet<>();

        englishMatches.stream()
                .map(Word::getEnglishWord)
                .forEach(uniqueSuggestion::add);

        spanishMatches.stream()
                .map(Word::getSpanishWord)
                .forEach(uniqueSuggestion::add);

        return uniqueSuggestion.stream()
                .limit(maxSuggestions)
                .collect(Collectors.toList());
    }
}
