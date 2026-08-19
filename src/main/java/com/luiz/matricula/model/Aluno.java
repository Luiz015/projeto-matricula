package com.luiz.matricula.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "aluno")
public class Aluno extends Pessoa{

    
    @Column(name = "prontuario")
    private String prontuario;

    @Column(name = "senha")
    private String senha;

    @OneToMany(mappedBy = "aluno")
    private List<Matricula> matriculas;

}