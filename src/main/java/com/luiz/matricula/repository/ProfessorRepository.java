package com.luiz.matricula.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Professor;

public interface ProfessorRepository extends JpaRepository<Professor,Long> {

    Professor findByProntuario(String prontuario);
}
