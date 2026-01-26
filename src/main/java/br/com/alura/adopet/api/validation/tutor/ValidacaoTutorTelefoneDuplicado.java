package br.com.alura.adopet.api.validation.tutor;

import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoTutorTelefoneDuplicado implements ValidacaoCadastroTutor {

    @Autowired
    private TutorRepository repository;

    public void validar(CadastroTutorDto cadastroTutorDto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(cadastroTutorDto.telefone());

        if (telefoneJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse telefone.");
        }
    }
}
