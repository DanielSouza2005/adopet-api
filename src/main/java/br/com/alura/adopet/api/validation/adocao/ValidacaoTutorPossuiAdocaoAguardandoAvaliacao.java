package br.com.alura.adopet.api.validation.adocao;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.SolicitacaoAdocaoValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoTutorPossuiAdocaoAguardandoAvaliacao implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void validar(SolicitacaoAdocaoDto solicitacaoAdocaoDto) {
        boolean tutorPossuiAdocaoAguardandoAvaliacao = adocaoRepository.existsByTutorIdAndStatus(
                solicitacaoAdocaoDto.idTutor(),
                StatusAdocao.AGUARDANDO_AVALIACAO);

        if (tutorPossuiAdocaoAguardandoAvaliacao) {
            throw new SolicitacaoAdocaoValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
        }
    }
}
