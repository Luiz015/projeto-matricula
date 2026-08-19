package com.luiz.matricula.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Aluno findByProntuario(String prontuario);

}