package com.newlang.backend.repository;

import com.newlang.backend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    //Metodo para buscar un rol por su nombre en la base de datos
    Optional<Role> findByName(String nameRole);
}
