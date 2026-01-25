package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService service;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid Tutor tutor) {
        try {
            service.cadastrar(tutor);
            return ResponseEntity.ok().body("Tutor cadastrado com sucesso!");
        } catch (CadastrarTutorValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid Tutor tutor) {
        try {
            service.atualizar(tutor);
            return ResponseEntity.ok().body("Tutor atualizado com sucesso!");
        } catch (CadastrarTutorValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

}
