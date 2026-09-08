package com.luiz.matricula.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.matricula.model.Disciplina;
import com.luiz.matricula.model.OfertaDisc;
import com.luiz.matricula.model.Professor;

public interface OfertaDiscRepository extends JpaRepository<OfertaDisc,Long> {
    
    OfertaDisc findByProfessorAndDisciplina(Professor professor, Disciplina disciplina);
    List<OfertaDisc> findByProfessor(Professor professor);
}
