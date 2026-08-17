package com.luiz.matricula.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luiz.matricula.model.Curso;
import com.luiz.matricula.repository.CursoRepository;
import com.luiz.matricula.repository.ProfessorRepository;

@Controller
public class ProfessorController {
    
    @Autowired
    private CursoRepository cursoRepository;



    @GetMapping("/curso")
    public String formCurso(){
        return "formCurso";
    }

    @PostMapping("/curso")
    public String criarCurso(Curso curso,Model model){
        if (cursoRepository.findByNome(curso.getNome())!= null) {
            model.addAttribute("mensagem", "Curso ja existe!");
            return "formCurso";
        }

        cursoRepository.save(curso);
        model.addAttribute("curso", curso);
        return "home-prof";
    }

    @GetMapping("/curso/atualizar/{id}")
    public String formAtualizar(@PathVariable Long id, Model model){
        Optional<Curso> cursoBanco = cursoRepository.findById(id);
        if (cursoBanco.isEmpty()) {
            model.addAttribute("mensagem", "Curso não encontrado");
            return "redirect:/home-prof";
        }
        model.addAttribute("curso", cursoBanco.get());
        return "formAtualizar";
    }

    @PostMapping("/curso/atualizar")
    public String atualizarCurso(Curso curso, Model model){
        Optional <Curso> cursoBanco = cursoRepository.findById(curso.getIdCurso());
        if(cursoBanco.isPresent()){

            Curso curso2 = cursoBanco.get();
            curso2.setNome(curso.getNome());
            curso2.setCargaHoraria(curso.getCargaHoraria());
            curso2.setDuracao(curso.getDuracao());
            
            cursoRepository.save(curso2);
            return "home-prof";
        }
        model.addAttribute("mensagem", "Curso não encontrado");
        return "redirect:/curso/atualizar";
    }

    @PostMapping("/curso/excluir/{id}")
    public String excluirCurso(@PathVariable Long id){
        cursoRepository.deleteById(id);
        return "redirect:/home-prof";
    }


    @GetMapping("/disciplina")
    public String formDisciplina(){
        return "formDisciplina";
    }

    
}
