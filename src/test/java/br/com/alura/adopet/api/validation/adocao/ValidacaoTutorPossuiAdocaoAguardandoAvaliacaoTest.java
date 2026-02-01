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
class ValidacaoTutorPossuiAdocaoAguardandoAvaliacaoTest {

    @InjectMocks
    private ValidacaoTutorPossuiAdocaoAguardandoAvaliacao validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private SolicitacaoAdocaoDto dto;

    @Test
    void deveriaPermitirAdotarPet() {
        BDDMockito.
                given(adocaoRepository.existsByTutorIdAndStatus(
                        dto.idTutor(),
                        StatusAdocao.AGUARDANDO_AVALIACAO)).
                willReturn(false);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaPermitirAdotarPetComTutorComAdocaoEmAndamento() {
        BDDMockito.
                given(adocaoRepository.existsByTutorIdAndStatus(
                        dto.idTutor(),
                        StatusAdocao.AGUARDANDO_AVALIACAO)).
                willReturn(true);

        Assertions.assertThrows(SolicitacaoAdocaoValidacaoException.class, () -> validacao.validar(dto));
    }
}