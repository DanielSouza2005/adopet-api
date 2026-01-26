package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TutorService {

    @Autowired
    private TutorRepository repository;

    public void cadastrar(CadastroTutorDto cadastroTutorDto) {
        boolean telefoneJaCadastrado = repository.existsByTelefone(cadastroTutorDto.telefone());
        boolean emailJaCadastrado = repository.existsByEmail(cadastroTutorDto.email());

        if (telefoneJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse telefone.");
        }

        if (emailJaCadastrado) {
            throw new CadastrarTutorValidacaoException("Tutor já cadastrado com esse email.");
        }

        Tutor tutor = new Tutor();
        tutor.setNome(cadastroTutorDto.nome());
        tutor.setEmail(cadastroTutorDto.email());
        tutor.setTelefone(cadastroTutorDto.telefone());
        repository.save(tutor);
    }

    public void atualizar(AtualizacaoTutorDto atualizacaoTutorDto) {
        Tutor tutor = repository.getReferenceById(atualizacaoTutorDto.idTutor());
        tutor.setTelefone(atualizacaoTutorDto.telefone());
        tutor.setEmail(atualizacaoTutorDto.email());
    }
}
