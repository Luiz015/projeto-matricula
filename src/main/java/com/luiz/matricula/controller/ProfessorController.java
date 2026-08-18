package com.luiz.matricula.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String homeProf(Model model) {
        List<Curso> cursos = cursoRepository.findAll();
        

        model.addAttribute("cursos", cursos);

        return "home-prof";
    }
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
        return "formAtualizaCurso";
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
        return "redirect:/home-prof";
    }

    @PostMapping("/curso/excluir/{id}")
    public String excluirCurso(@PathVariable Long id){
        cursoRepository.deleteById(id);
        return "redirect:/home-prof";
    }


    @GetMapping("/oferta")
    public String formOferta(){
        return "formOferta";
    }

    @PostMapping("/oferta")
    public String criarOferta(OfertaDisc oferta, Model model){
        if (ofertaDiscRepository.findByProfessorAndDisciplina(oferta.getProfessor(),oferta.getDisciplina())!=null) {
            model.addAttribute("mensagem", "Oferta ja existe!");
            return "formOferta";
        }

        ofertaDiscRepository.save(oferta);
        model.addAttribute("oferta", oferta);
        return "home-prof";
    }

    @GetMapping("/oferta/atualizar/{id}")
    public String formAtualizarOferta(@PathVariable Long id, Model model){
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

        return "formAtualizaOferta";
    }

    @PostMapping("/oferta/atualizar")
    public String atualizarOferta(OfertaDisc oferta, Model model) {

        Optional<OfertaDisc> ofertaBanco =
                ofertaDiscRepository.findById(oferta.getId());

        if (ofertaBanco.isEmpty()) {
            model.addAttribute("mensagem", "Oferta não encontrada");
            return "redirect:/home-prof";
        }

        OfertaDisc ofertaOriginal = ofertaBanco.get();

        OfertaDisc ofertaDuplicada =
                ofertaDiscRepository.findByProfessorAndDisciplina(
                        oferta.getProfessor(),
                        oferta.getDisciplina()
                );

        if (ofertaDuplicada != null &&
            !ofertaDuplicada.getId().equals(ofertaOriginal.getId())) {

            model.addAttribute("mensagem", "Já existe uma oferta com esse professor e disciplina");
            return "formAtualizaOferta";
        }

        ofertaOriginal.setProfessor(oferta.getProfessor());
        ofertaOriginal.setDisciplina(oferta.getDisciplina());

        ofertaDiscRepository.save(ofertaOriginal);

        return "redirect:/home-prof";
    }

        @GetMapping("/disciplina")
        public String formDisciplina(){
            return "formDisciplina";
        }

        @PostMapping("/disciplina")
        public String criarDisciplina(Disciplina disciplina, Model model){
            if (disciplinaRepository.findByNome(disciplina.getNome())!= null) {
                model.addAttribute("mensagem", "Disciplina ja existe!");
                return "formDisciplina";
            }

            disciplinaRepository.save(disciplina);
            model.addAttribute("disciplina", disciplina);
            return "home-prof";
        }

    @GetMapping("/disciplina/atualizar/{id}")
    public String formAtualizarDisc(@PathVariable Long id, Model model){
        Optional<Disciplina> discBanco = disciplinaRepository.findById(id);
        if (discBanco.isEmpty()) {
            model.addAttribute("mensagem", "Disciplina não encontrado");
            return "redirect:/home-prof";
        }
        model.addAttribute("disciplina", discBanco.get());
        return "formAtualizaDisciplina";
    }

    @PostMapping("/disciplina/atualizar")
    public String atualizarDisciplina(Disciplina disciplina, Model model){
        Optional<Disciplina> discBanco = disciplinaRepository.findById(disciplina.getIdDisciplina());
        if(discBanco.isPresent()){

            Disciplina disc2 = discBanco.get();
            disc2.setNome(disciplina.getNome());
            disc2.setCargaHoraria(disciplina.getCargaHoraria());
            disc2.setArea(disciplina.getArea());

            disciplinaRepository.save(disc2);
            return "home-prof";
        }
        model.addAttribute("mensagem", "Disciplina não encontrada");
        return "redirect:/home-prof";
    }
    
    @PostMapping("/disciplina/excluir/{id}")
    public String excluirDisciplina(@PathVariable Long id){
        disciplinaRepository.deleteById(id);
        return "redirect:/home-prof";
    }

    
}
