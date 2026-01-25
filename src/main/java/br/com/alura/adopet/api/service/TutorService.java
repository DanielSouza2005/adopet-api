package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    public void cadastrar(Tutor tutor) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(tutor.getTelefone());
        boolean emailJaCadastrado = repository.existsByEmail(tutor.getEmail());

        if (telefoneJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse telefone.");
        }

        if (emailJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse email.");
        }

        repository.save(tutor);
    }

    public void atualizar(Tutor tutor) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(tutor.getTelefone());
        boolean emailJaCadastrado = repository.existsByEmail(tutor.getEmail());

        if (telefoneJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse telefone.");
        }

        if (emailJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse email.");
        }

        repository.save(tutor);
    }
}
