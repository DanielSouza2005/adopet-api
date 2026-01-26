package br.com.alura.adopet.api.validation;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.SolicitacaoAdocaoValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoTutorLimiteMaximoAdocoes implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void validar(SolicitacaoAdocaoDto solicitacaoAdocaoDto) {
        int quantidadeDeAdocoesAprovadasDoTutor = adocaoRepository.countByTutorIdAndStatus(
                solicitacaoAdocaoDto.idTutor(),
                StatusAdocao.APROVADO);

        if (quantidadeDeAdocoesAprovadasDoTutor >= 5) {
            throw new SolicitacaoAdocaoValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
        }
    }
}
