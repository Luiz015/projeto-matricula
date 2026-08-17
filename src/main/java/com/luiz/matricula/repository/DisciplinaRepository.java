package com.luiz.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    
}
