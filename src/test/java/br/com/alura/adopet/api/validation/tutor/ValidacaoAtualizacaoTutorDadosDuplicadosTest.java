package br.com.alura.adopet.api.validation.tutor;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoAtualizacaoTutorDadosDuplicadosTest {

    @InjectMocks
    private ValidacaoAtualizacaoTutorDadosDuplicados validacao;

    @Mock
    private TutorRepository repository;

    @Mock
    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Test
    void deveriaPermitirAtualizarDadosTutorSemDadosDuplicados() {
        //ARRANGE
        BDDMockito
                .given(repository.existsByTelefoneOrEmail(atualizacaoTutorDto.telefone(), atualizacaoTutorDto.email()))
                .willReturn(false);

        Assertions.assertDoesNotThrow(() -> validacao.validar(atualizacaoTutorDto));
    }

    @Test
    void naoDeveriaPermitirAtualizarDadosTutorComDadosDuplicados() {
        //ARRANGE
        BDDMockito
                .given(repository.existsByTelefoneOrEmail(atualizacaoTutorDto.telefone(), atualizacaoTutorDto.email()))
                .willReturn(true);

        Assertions.assertThrows(CadastrarTutorValidacaoException.class, () -> validacao.validar(atualizacaoTutorDto));
    }

}