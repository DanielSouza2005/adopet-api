package br.com.alura.adopet.api.validation.abrigo;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.exception.CadastrarAbrigoValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoAbrigoDuplicadoTest {

    @InjectMocks
    private ValidacaoAbrigoDuplicado validacao;

    @Mock
    private AbrigoRepository repository;

    @Mock
    private CadastroAbrigoDto dto;

    @Test
    void deveriaPermitirCadastrarAbrigoSemDadosDuplicados() {
        //ARRANGE
        BDDMockito
                .given(repository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email()))
                .willReturn(false);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaPermitirCadastrarAbrigoComDadosDuplicados() {
        //ARRANGE
        BDDMockito
                .given(repository.existsByNomeOrTelefoneOrEmail(dto.nome(), dto.telefone(), dto.email()))
                .willReturn(true);

        //ACT + ASSERT
        Assertions.assertThrows(CadastrarAbrigoValidacaoException.class, () -> validacao.validar(dto));
    }

}