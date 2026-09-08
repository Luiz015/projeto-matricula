package com.luiz.matricula.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;

import com.luiz.matricula.model.Curso;
import com.luiz.matricula.model.Disciplina;
import com.luiz.matricula.model.OfertaDisc;
import com.luiz.matricula.model.Professor;
import com.luiz.matricula.repository.CursoRepository;
import com.luiz.matricula.repository.DisciplinaRepository;
import com.luiz.matricula.repository.OfertaDiscRepository;
import com.luiz.matricula.repository.ProfessorRepository;

@Controller
public class ProfessorController {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private OfertaDiscRepository ofertaDiscRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping("/home-prof")
    public String homeProf(Model model, HttpSession session) {
        List<Curso> cursos = cursoRepository.findAll();

        Professor professor = (Professor) session.getAttribute("professor");

        List<OfertaDisc> ofertas = ofertaDiscRepository.findByProfessor(professor);

        model.addAttribute("cursos", cursos);
        model.addAttribute("ofertas", ofertas);

        return "home-prof";
    }

    @GetMapping("/curso")
    public String formCurso() {
        return "formCurso";
    }

    @PostMapping("/curso")
    public String criarCurso(Curso curso, Model model) {
        if (cursoRepository.findByNome(curso.getNome()) != null) {
            model.addAttribute("mensagem", "Curso ja existe!");
            return "formCurso";
        }

        cursoRepository.save(curso);
        model.addAttribute("curso", curso);
        return "redirect:/home-prof";
    }

    @GetMapping("/curso/atualizar/{id}")
    public String formAtualizar(@PathVariable Long id, Model model) {
        Optional<Curso> cursoBanco = cursoRepository.findById(id);
        if (cursoBanco.isEmpty()) {
            model.addAttribute("mensagem", "Curso não encontrado");
            return "redirect:/home-prof";
        }
        model.addAttribute("curso", cursoBanco.get());
        return "formAtualizarCurso";
    }

    @PostMapping("/curso/atualizar")
    public String atualizarCurso(Curso curso, Model model) {
        Optional<Curso> cursoBanco = cursoRepository.findById(curso.getIdCurso());
        if (cursoBanco.isPresent()) {

            Curso curso2 = cursoBanco.get();
            curso2.setNome(curso.getNome());
            curso2.setCargaHoraria(curso.getCargaHoraria());
            curso2.setDuracao(curso.getDuracao());

            cursoRepository.save(curso2);
            return "redirect:/home-prof";
        }
        model.addAttribute("mensagem", "Curso não encontrado");
        return "redirect:/home-prof";
    }

    @PostMapping("/curso/excluir/{id}")
    public String excluirCurso(@PathVariable Long id) {
        cursoRepository.deleteById(id);
        return "redirect:/home-prof";
    }

    @GetMapping("/curso/{id}")
    public String verCurso(@PathVariable Long id, Model model) {
        Optional<Curso> cursoBanco = cursoRepository.findById(id);

        if (cursoBanco.isEmpty()) {
            model.addAttribute("mensagem", "Curso não encontrado");
            return "redirect:/home-prof";
        }

        model.addAttribute("curso", cursoBanco.get());

        return "curso";
    }

    @GetMapping("/oferta")
    public String formOferta(Model model) {

        model.addAttribute("oferta", new OfertaDisc());

        List<Professor> professores = professorRepository.findAll();
        List<Disciplina> disciplinas = disciplinaRepository.findAll();

        model.addAttribute("professores", professores);
        model.addAttribute("disciplinas", disciplinas);

        return "formOferta";
    }

    @PostMapping("/oferta")
    public String criarOferta(OfertaDisc oferta, Model model) {
        if (ofertaDiscRepository.findByProfessorAndDisciplina(oferta.getProfessor(), oferta.getDisciplina()) != null) {
            model.addAttribute("mensagem", "Oferta ja existe!");
            return "formOferta";
        }

        ofertaDiscRepository.save(oferta);
        model.addAttribute("oferta", oferta);
        return "redirect:/home-prof";
    }

    @GetMapping("/oferta/atualizar/{id}")
    public String formAtualizarOferta(@PathVariable Long id, Model model) {
        Optional<OfertaDisc> ofertaBanco = ofertaDiscRepository.findById(id);
        if (ofertaBanco.isEmpty()) {
            model.addAttribute("mensagem", "Oferta não encontrada");
            return "redirect:/home-prof";
        }
        List<Professor> professores = professorRepository.findAll();
        List<Disciplina> disciplinas = disciplinaRepository.findAll();
        model.addAttribute("oferta", ofertaBanco.get());
        model.addAttribute("professores", professores);
        model.addAttribute("disciplinas", disciplinas);

        return "formAtualizarOferta";
    }

    @PostMapping("/oferta/atualizar")
    public String atualizarOferta(OfertaDisc oferta, Model model, HttpSession session) {

        Professor professorLogado = (Professor) session.getAttribute("professor");

        Optional<OfertaDisc> ofertaBanco = ofertaDiscRepository.findById(oferta.getId());

        if (ofertaBanco.isEmpty()) {
            model.addAttribute("mensagem", "Oferta não encontrada");
            return "redirect:/home-prof";
        }

        OfertaDisc ofertaOriginal = ofertaBanco.get();

        if (!ofertaOriginal.getProfessor().getId()
                .equals(professorLogado.getId())) {

            model.addAttribute("mensagem",
                    "Você não pode atualizar uma oferta de outro professor!");

            return "redirect:/home-prof";
        }

        OfertaDisc ofertaDuplicada = ofertaDiscRepository.findByProfessorAndDisciplina(
                professorLogado,
                oferta.getDisciplina());

        if (ofertaDuplicada != null &&
                !ofertaDuplicada.getId().equals(ofertaOriginal.getId())) {

            model.addAttribute("mensagem",
                    "Você já possui essa disciplina vinculada!");

            return "redirect:/home-prof";
        }

        ofertaOriginal.setDisciplina(oferta.getDisciplina());

        ofertaDiscRepository.save(ofertaOriginal);

        return "redirect:/home-prof";
    }

    @GetMapping("/disciplina")
    public String formDisciplina(Model model) {
        model.addAttribute("disciplina", new Disciplina());
        return "formDisciplina";
    }

    @PostMapping("/disciplina")
    public String criarDisciplina(Disciplina disciplina, Model model) {
        if (disciplinaRepository.findByNome(disciplina.getNome()) != null) {
            model.addAttribute("mensagem", "Disciplina ja existe!");
            return "formDisciplina";
        }

        disciplinaRepository.save(disciplina);
        model.addAttribute("disciplina", disciplina);
        return "redirect:/home-prof";
    }

    @GetMapping("/disciplina/atualizar/{id}")
    public String formAtualizarDisc(@PathVariable Long id, Model model) {

        Optional<Disciplina> discBanco = disciplinaRepository.findById(id);

        if (discBanco.isEmpty()) {
            model.addAttribute("mensagem", "Disciplina não encontrada");
            return "redirect:/home-prof";
        }

        List<Curso> cursos = cursoRepository.findAll();

        model.addAttribute("disciplina", discBanco.get());
        model.addAttribute("cursos", cursos);

        return "formAtualizaDisciplina";
    }

    @PostMapping("/disciplina/atualizar")
    public String atualizarDisciplina(Disciplina disciplina, Model model) {
        Optional<Disciplina> discBanco = disciplinaRepository.findById(disciplina.getIdDisciplina());
        if (discBanco.isPresent()) {

            Disciplina disc2 = discBanco.get();
            disc2.setNome(disciplina.getNome());
            disc2.setCargaHoraria(disciplina.getCargaHoraria());
            disc2.setArea(disciplina.getArea());

            disciplinaRepository.save(disc2);
            return "redirect:/home-prof";
        }
        model.addAttribute("mensagem", "Disciplina não encontrada");
        return "redirect:/home-prof";
    }

    @PostMapping("/disciplina/excluir/{id}")
    public String excluirDisciplina(@PathVariable Long id) {
        disciplinaRepository.deleteById(id);
        return "redirect:/home-prof";
    }

    @GetMapping("/oferta/{id}/alunos")
    public String verAlunos(@PathVariable Long id, Model model) {

        Optional<OfertaDisc> ofertaBanco = ofertaDiscRepository.findById(id);

        if (ofertaBanco.isEmpty()) {
            return "redirect:/home-prof";
        }

        OfertaDisc oferta = ofertaBanco.get();

        model.addAttribute("oferta", oferta);
        model.addAttribute("matriculas", oferta.getMatriculas());

        return "alunos-oferta";
    }

}
