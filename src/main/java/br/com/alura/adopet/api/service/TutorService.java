package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validation.tutor.ValidacaoAtualizacaoTutorDadosDuplicados;
import br.com.alura.adopet.api.validation.tutor.ValidacaoCadastroTutorDadosDuplicados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    @Autowired
    ValidacaoCadastroTutorDadosDuplicados validacaoCadastroTutor;

    @Autowired
    ValidacaoAtualizacaoTutorDadosDuplicados validacaoAtualizacaoTutor;

    public void cadastrar(CadastroTutorDto cadastroTutorDto) {
        validacaoCadastroTutor.validar(cadastroTutorDto);

        Tutor tutor = new Tutor();
        tutor.setNome(cadastroTutorDto.nome());
        tutor.setEmail(cadastroTutorDto.email());
        tutor.setTelefone(cadastroTutorDto.telefone());
        repository.save(tutor);
    }

    public void atualizar(AtualizacaoTutorDto atualizacaoTutorDto) {
        validacaoAtualizacaoTutor.validar(atualizacaoTutorDto);

        Tutor tutor = repository.getReferenceById(atualizacaoTutorDto.idTutor());
        tutor.setTelefone(atualizacaoTutorDto.telefone());
        tutor.setEmail(atualizacaoTutorDto.email());
    }
}
