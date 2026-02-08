package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validation.tutor.ValidacaoAtualizacaoTutorDadosDuplicados;
import br.com.alura.adopet.api.validation.tutor.ValidacaoCadastroTutorDadosDuplicados;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService service;

    @Mock
    private TutorRepository repository;

    @Mock
    private Tutor tutor;

    @Mock
    private ValidacaoCadastroTutorDadosDuplicados validacaoCadastroTutorDadosDuplicados;

    @Mock
    private ValidacaoAtualizacaoTutorDadosDuplicados validacaoAtualizacaoTutorDadosDuplicados;

    @Captor
    private ArgumentCaptor<Tutor> tutorCaptor;

    private CadastroTutorDto cadastroTutorDto;

    private AtualizacaoTutorDto atualizacaoTutorDto;

    @Test
    void deveriaCadastrarTutorComSucesso() {
        //ARRANGE
        this.cadastroTutorDto = new CadastroTutorDto(
                "Teste",
                "19995581144",
                "teste@gmail.com"
        );

        this.tutor = new Tutor(this.cadastroTutorDto);

        //ACT
        this.service.cadastrar(this.cadastroTutorDto);

        //ASSERT
        then(repository).should().save(tutorCaptor.capture());
        Tutor tutorSalvo = tutorCaptor.getValue();

        Assertions.assertEquals(this.tutor, tutorSalvo);
        Assertions.assertEquals(this.tutor.getNome(), tutorSalvo.getNome());
        Assertions.assertEquals(this.tutor.getEmail(), tutorSalvo.getEmail());
        Assertions.assertEquals(this.tutor.getTelefone(), tutorSalvo.getTelefone());
    }

    @Test
    void deveriaChamarValidadoresAoCadastrarTutor() {
        //ARRANGE
        this.cadastroTutorDto = new CadastroTutorDto(
                "Teste",
                "19995581144",
                "teste@gmail.com"
        );

        //ACT
        this.service.cadastrar(this.cadastroTutorDto);

        //ARRANGE
        then(this.validacaoCadastroTutorDadosDuplicados).should().validar(this.cadastroTutorDto);
    }

    @Test
    void deveriaAtualizarTutorComSucesso() {
        //ARRANGE
        this.atualizacaoTutorDto = new AtualizacaoTutorDto(
                1L,
                "19995581144",
                "teste@gmail.com"
        );

        given(repository.getReferenceById(this.atualizacaoTutorDto.idTutor()))
                .willReturn(this.tutor);

        //ACT
        this.service.atualizar(this.atualizacaoTutorDto);

        //ASSERT
        then(repository).should().getReferenceById(this.atualizacaoTutorDto.idTutor());
        then(this.tutor).should().setTelefone(this.atualizacaoTutorDto.telefone());
        then(this.tutor).should().setEmail(this.atualizacaoTutorDto.email());
    }

    @Test
    void deveriaChamarValidadoresAoAtualizarTutor() {
        //ARRANGE
        this.atualizacaoTutorDto = new AtualizacaoTutorDto(
                1L,
                "19995581144",
                "teste@gmail.com"
        );

        given(repository.getReferenceById(this.atualizacaoTutorDto.idTutor()))
                .willReturn(this.tutor);

        //ACT
        this.service.atualizar(this.atualizacaoTutorDto);

        //ARRANGE
        then(this.validacaoAtualizacaoTutorDadosDuplicados).should().validar(this.atualizacaoTutorDto);
    }

}