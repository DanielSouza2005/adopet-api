package br.com.alura.adopet.api.validation.tutor;

import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidacaoCadastroTutorDadosDuplicadosTest {

    @InjectMocks
    private ValidacaoCadastroTutorDadosDuplicados validacao;

    @Mock
    private TutorRepository repository;

    @Mock
    private CadastroTutorDto dto;

    @Test
    void deveriaPermitirCadastrarTutorSemDadosDuplicados() {
        //ARRANGE
        BDDMockito
                .given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email()))
                .willReturn(false);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaPermitirCadastrarTutorComDadosDuplicados() {
        //ARRANGE
        BDDMockito
                .given(repository.existsByTelefoneOrEmail(dto.telefone(), dto.email()))
                .willReturn(true);

        //ACT + ASSERT
        Assertions.assertThrows(CadastrarTutorValidacaoException.class, () -> validacao.validar(dto));
    }
}