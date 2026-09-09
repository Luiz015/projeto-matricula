package com.luiz.matricula.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Aluno;
import com.luiz.matricula.model.Matricula;
import com.luiz.matricula.model.OfertaDisc;

public interface MatriculaRepository extends JpaRepository<Matricula,Long> {

    Boolean existsByAlunoAndOfertaDisc(Aluno aluno, OfertaDisc oferta);
    List<Matricula> findByAluno(Aluno aluno);
    
}
