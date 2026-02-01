package br.com.alura.adopet.api.validation.tutor;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoAtualizacaoTutorDadosDuplicados {

    @Autowired
    private TutorRepository repository;

    public void validar(AtualizacaoTutorDto atualizacaoTutorDto) {
        boolean dadosJaCadastrados = repository.existsByTelefoneOrEmail(atualizacaoTutorDto.telefone(), atualizacaoTutorDto.email());

        if (dadosJaCadastrados) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esses dados. ");
        }
    }
}
