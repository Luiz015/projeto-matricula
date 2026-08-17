package com.luiz.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula,Long> {
    
}
