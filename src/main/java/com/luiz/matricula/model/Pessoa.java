package com.luiz.matricula.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pessoa {
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dtNascimento;
}
