package br.com.alura.adopet.api.validation.abrigo;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.CadastrarAbrigoValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoAbrigoTelefoneDuplicado implements ValidacaoCadastroAbrigo {

    @Autowired
    private AbrigoRepository repository;

    @Override
    public void validar(CadastroAbrigoDto cadastroAbrigoDto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(cadastroAbrigoDto.telefone());

        if (telefoneJaCadastrado) {
            throw new CadastrarAbrigoValidacaoException("Abrigo já cadastrado com esse telefone.");
        }
    }
}
