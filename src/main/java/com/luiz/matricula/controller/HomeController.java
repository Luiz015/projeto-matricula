package com.luiz.matricula.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luiz.matricula.model.Aluno;
import com.luiz.matricula.model.OfertaDisc;
import com.luiz.matricula.model.Professor;
import com.luiz.matricula.repository.AlunoRepository;
import com.luiz.matricula.repository.OfertaDiscRepository;
import com.luiz.matricula.repository.ProfessorRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    String msg;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private OfertaDiscRepository ofertaDiscRepository;

    @GetMapping("/")
    public String paginaInicial() {
        return "HomePage";
    }

    @GetMapping("/login")
    public String abrirLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String prontuario,
            @RequestParam String senha,
            HttpSession httpSession,
            Model model) {

        Aluno alunoBanco = alunoRepository.findByProntuario(prontuario);
        if (alunoBanco != null) {
            if (alunoBanco.getSenha().equals(senha)) {
                httpSession.setAttribute("aluno", alunoBanco);
                List<OfertaDisc> ofertas = ofertaDiscRepository.findAll();

                model.addAttribute("ofertas", ofertas);
                return "home-aluno";
            }
            model.addAttribute("mensagem", "Senha Incorreta");
            return "login";
        }

        Professor professorBanco = professorRepository.findByProntuario(prontuario);
        if (professorBanco != null) {
            if (professorBanco.getSenha().equals(senha)) {
                httpSession.setAttribute("professor", professorBanco);
                return "redirect:/home-prof";
            }
            model.addAttribute("mensagem", "Senha Incorreta");
            return "login";
        }

        model.addAttribute("mensagem", "Prontuario não cadastrado");
        return "login";

    }

}
