package com.luiz.matricula.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luiz.matricula.model.Aluno;
import com.luiz.matricula.repository.OfertaDiscRepository;
import com.luiz.matricula.repository.MatriculaRepository;
import com.luiz.matricula.model.Curso;
import com.luiz.matricula.model.Matricula;
import com.luiz.matricula.model.OfertaDisc;
import com.luiz.matricula.repository.AlunoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private OfertaDiscRepository ofertaDiscRepository;

    @Autowired 
    private MatriculaRepository matriculaRepository;

    @GetMapping("/alunos")
    public String listarAlunos(Model model) {

        List<Aluno> alunos = alunoRepository.findAll();

        model.addAttribute("alunos", alunos);

        return "alunos";
    }

    @GetMapping("/aluno")
    public String formAluno(Model model) {

        model.addAttribute("aluno", new Aluno());

        return "formAluno";
    }

    @PostMapping("/aluno")
    public String criarAluno(Aluno aluno, Model model) {

        if (alunoRepository.findByProntuario(aluno.getProntuario()) != null) {
            model.addAttribute("mensagem", "Prontuário já cadastrado!");
            return "formAluno";
        }

        alunoRepository.save(aluno);

        return "redirect:/alunos";
    }

    @GetMapping("/aluno/atualizar/{id}")
    public String formAtualizarAluno(@PathVariable Long id, Model model) {

        Optional<Aluno> alunoBanco = alunoRepository.findById(id);

        if (alunoBanco.isEmpty()) {
            model.addAttribute("mensagem", "Aluno não encontrado!");
            return "redirect:/alunos";
        }

        model.addAttribute("aluno", alunoBanco.get());

        return "formAtualizarAluno";
    }

    @PostMapping("/aluno/atualizar")
    public String atualizarAluno(Aluno aluno, Model model) {

        Optional<Aluno> alunoBanco = alunoRepository.findById(aluno.getId());

        if (alunoBanco.isPresent()) {

            Aluno aluno2 = alunoBanco.get();

            aluno2.setNome(aluno.getNome());
            aluno2.setCpf(aluno.getCpf());
            aluno2.setEmail(aluno.getEmail());
            aluno2.setDtNascimento(aluno.getDtNascimento());
            aluno2.setProntuario(aluno.getProntuario());
            aluno2.setSenha(aluno.getSenha());

            alunoRepository.save(aluno2);

            return "redirect:/alunos";
        }

        model.addAttribute("mensagem", "Aluno não encontrado!");

        return "redirect:/alunos";
    }

    @PostMapping("/aluno/excluir/{id}")
    public String excluirAluno(@PathVariable Long id) {

        alunoRepository.deleteById(id);

        return "redirect:/alunos";
    }

    @PostMapping("/matricula")
    public String matricular(
            @RequestParam Long idOfertaDisc,
            HttpSession session,
            Model model) {

        List<OfertaDisc> ofertas = ofertaDiscRepository.findAll();
        model.addAttribute("ofertas", ofertas);

        OfertaDisc oferta = ofertaDiscRepository
                .findById(idOfertaDisc)
                .orElse(null);

        if (oferta == null) {
            model.addAttribute("mensagem", "Oferta não encontrada!");
            return "home-aluno";
        }

        Aluno aluno = (Aluno) session.getAttribute("aluno");

        if (aluno == null) {
            return "redirect:/login";
        }

        if (matriculaRepository.existsByAlunoAndOfertaDisc(aluno, oferta)) {
            model.addAttribute("mensagem", "Você já está matriculado nesta disciplina!");

            return "home-aluno";
        }

        Curso curso = oferta.getDisciplina().getCurso();

        Matricula matricula = new Matricula();

        matricula.setAluno(aluno);
        matricula.setOfertaDisc(oferta);
        matricula.setCurso(curso);

        matriculaRepository.save(matricula);

        return "redirect:/home-aluno?mensagem=Matr%C3%ADcula%20realizada%20com%20sucesso!";
    }
}