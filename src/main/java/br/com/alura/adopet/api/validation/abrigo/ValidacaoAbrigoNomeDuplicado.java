package br.com.alura.adopet.api.validation.abrigo;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.CadastrarAbrigoValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidacaoAbrigoNomeDuplicado implements ValidacaoCadastroAbrigo {

    @Autowired
    private AbrigoRepository repository;

    @Override
    public void validar(CadastroAbrigoDto cadastroAbrigoDto) {
        boolean nomeJaCadastrado = repository.existsByNome(cadastroAbrigoDto.nome());

        if (nomeJaCadastrado) {
            throw new CadastrarAbrigoValidacaoException("Abrigo já cadastrado com esse nome.");
        }
    }
}
