package com.newlang.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "learning_routine")
public class LearningRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoutine;

    @Column(nullable = false)
    private String nameRoutine;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", referencedColumnName = "id_user")
    private User idUser;
}
