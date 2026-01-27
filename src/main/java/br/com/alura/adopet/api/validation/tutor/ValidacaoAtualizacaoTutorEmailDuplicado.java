package br.com.alura.adopet.api.validation.tutor;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoAtualizacaoTutorEmailDuplicado implements ValidacaoAtualizacaoTutor {

    @Autowired
    private TutorRepository repository;

    @Override
    public void validar(AtualizacaoTutorDto atualizacaoTutorDto) {
        boolean emailJaCadastrado = repository.existsByEmail(atualizacaoTutorDto.email());

        if (emailJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse email.");
        }
    }
}
