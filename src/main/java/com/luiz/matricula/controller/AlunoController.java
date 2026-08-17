package com.luiz.matricula.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.luiz.matricula.repository.AlunoRepository;

@Controller
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    
}
