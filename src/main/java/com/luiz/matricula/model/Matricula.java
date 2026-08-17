package com.luiz.matricula.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMatricula;

    @ManyToOne
    @JoinColumn(name = "idOfertaDisc")
    private OfertaDisc ofertaDisc;

    @ManyToOne
    @JoinColumn(name = "idAluno")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "idCurso")
    private Curso curso;
}
