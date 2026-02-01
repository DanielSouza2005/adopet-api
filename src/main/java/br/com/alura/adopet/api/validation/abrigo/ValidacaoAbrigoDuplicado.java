package br.com.alura.adopet.api.validation.abrigo;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoAbrigoDuplicado {

    @Autowired
    private AbrigoRepository repository;

    public void validar(CadastroAbrigoDto cadastroAbrigoDto) {
        boolean dadosJaCadastrados = repository.existsByNomeOrTelefoneOrEmail(cadastroAbrigoDto.nome(), cadastroAbrigoDto.telefone(), cadastroAbrigoDto.email());

        if (dadosJaCadastrados) {
            throw new CadastrarTutorValidacaoException("Abrigo já cadastrado com esses dados.");
        }
    }
}
