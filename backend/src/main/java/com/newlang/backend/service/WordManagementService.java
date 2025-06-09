package com.newlang.backend.service;

import com.newlang.backend.entity.Word;
import com.newlang.backend.exceptions.WordAlreadyExistException;
import com.newlang.backend.exceptions.WordNotFoundException;
import com.newlang.backend.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Word> getByWord(String word){
        return wordRepository.findByEnglishWord(word);
    }

    //Función para actualizar la palabra
    public Word updateEnglishWord(Long id, Word updatedWord){
        return wordRepository.findById(id)
                .map(existingWord -> {
                    existingWord.setEnglishWord(updatedWord.getEnglishWord());
                    return wordRepository.save(existingWord);
                }).orElseThrow(() -> new WordNotFoundException("La palabra no fue encontrada"));
    }

    //Función para eliminar las palabras, validando si estas existen en la base de datos
    public void deleteWord(String word){
        Optional<Word> foundWord = wordRepository.findByEnglishWord(word.toLowerCase());
         foundWord.ifPresentOrElse(
                 wordToDelete -> wordRepository.deleteById(wordToDelete.getIdWord()),
                 () -> { throw new WordNotFoundException("La palabra " + foundWord + "no fue encontrada para eliminar");
                 }
         );
    }
}
