package com.newlang.backend.service;


import com.newlang.backend.entity.Level;
import com.newlang.backend.exceptions.LevelAlreadyExistException;
import com.newlang.backend.exceptions.LevelNotFoundException;
import com.newlang.backend.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LevelManagementService {

    @Autowired
    private LevelRepository levelRepository;

    public Level saveLevel(String nameLevel) {
        if (levelRepository.findByNameLevel(nameLevel).isPresent()) {
            throw new LevelAlreadyExistException("El nivel escrito ya existe");
        }
        Level level = new Level();
        level.setNameLevel(nameLevel);
        return levelRepository.save(level);
    }

    //Actualiza los nombres de los niveles
    //IMPORTANTE manejar para que no permita que el nombre de los niveles sea igual a otros ya creados
    public Level updateLevel (Long id, Level updatedLevel) {


        return levelRepository.findById(id)
                .map(existingLevel -> {
                    existingLevel.setNameLevel(updatedLevel.getNameLevel());
                    return levelRepository.save(existingLevel);
                }).orElseThrow(() -> new LevelNotFoundException("El nivel no fue encontrado"));
    }

    public List<Level> getAllLevels() {return levelRepository.findAll();}

    public Optional<Level> getLevelById(Long id) {
        return levelRepository.findById(id);
    }
}
