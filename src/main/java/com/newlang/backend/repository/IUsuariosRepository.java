package com.newlang.backend.repository;

import com.newlang.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuariosRepository extends JpaRepository<User, Long> {
    //Metodo para poder buscar un usuario mediante su email
    Optional<User> findByEmail(String email);

    //Metodo para ppoder verificar si un usuario existe en la base de datos
    Boolean existByEmail(String email);

}
