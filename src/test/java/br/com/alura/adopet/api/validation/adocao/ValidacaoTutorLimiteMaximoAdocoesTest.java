package br.com.alura.adopet.api.validation.adocao;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.SolicitacaoAdocaoValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorLimiteMaximoAdocoesTest {

    @InjectMocks
    private ValidacaoTutorLimiteMaximoAdocoes validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void deveriaPermitirAdocaoComTutorCom4Adocoes() {
        //ARRANGE
        BDDMockito.given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO))
                .willReturn(4);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaPermitirAdocaoComTutorCom5Adocoes() {
        //ARRANGE
        BDDMockito.given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO))
                .willReturn(5);

        //ACT + ASSERT
        Assertions.assertThrows(SolicitacaoAdocaoValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaPermitirAdocaoComTutorCom6Adocoes() {
        //ARRANGE
        BDDMockito.given(adocaoRepository.countByTutorIdAndStatus(dto.idTutor(), StatusAdocao.APROVADO))
                .willReturn(6);

        //ACT + ASSERT
        Assertions.assertThrows(SolicitacaoAdocaoValidacaoException.class, () -> validacao.validar(dto));
    }


}