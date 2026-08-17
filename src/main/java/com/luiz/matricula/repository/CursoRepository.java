package com.luiz.matricula.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Curso;

public interface CursoRepository extends JpaRepository<Curso,Long>{

    Curso findByNome(String nome);
    
}