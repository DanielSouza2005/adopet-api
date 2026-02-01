package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validation.adocao.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Adocao adocao;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto solicitacaoAdocaoDto;

    private AprovacaoAdocaoDto aprovacaoAdocaoDto;

    private ReprovacaoAdocaoDto reprovacaoAdocaoDto;

    @Captor
    private ArgumentCaptor<Adocao> adocaoCaptor;

    @Mock
    private LocalDateTime data;

    @Test
    void deveriaSalvarAdocaoAoSolicitar() {
        //ARRANGE
        this.solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(
                10L,
                20L,
                "Motivo Qualquer"
        );

        given(petRepository.getReferenceById(solicitacaoAdocaoDto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        service.solicitar(solicitacaoAdocaoDto);

        //ASSERT
        then(repository).should().save(adocaoCaptor.capture());
        Adocao adocaoSalva = adocaoCaptor.getValue();

        assertEquals(pet, adocaoSalva.getPet());
        assertEquals(tutor, adocaoSalva.getTutor());
        assertEquals(solicitacaoAdocaoDto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    void deveriaChamarValidadoresDeAdocaoAoSolicitar() {
        //ARRANGE
        this.solicitacaoAdocaoDto = new SolicitacaoAdocaoDto(
                10L,
                20L,
                "Motivo Qualquer"
        );

        given(petRepository.getReferenceById(solicitacaoAdocaoDto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(solicitacaoAdocaoDto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        validacoes.add(validador1);
        validacoes.add(validador2);

        //ACT
        service.solicitar(solicitacaoAdocaoDto);

        //ASSERT
        then(validador1).should().validar(solicitacaoAdocaoDto);
        then(validador2).should().validar(solicitacaoAdocaoDto);
    }

    @Test
    void deveriaSalvarAdocaoComoAprovadaAoAprovar() {
        //ARRANGE
        this.aprovacaoAdocaoDto = new AprovacaoAdocaoDto(10L);
        this.adocao = new Adocao(tutor, pet, "Motivo Qualquer");

        given(repository.getReferenceById(aprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(tutor.getNome()).willReturn("João");
        given(tutor.getEmail()).willReturn("joao@email.com.br");

        given(pet.getNome()).willReturn("Rex");
        given(pet.getAbrigo()).willReturn(abrigo);

        given(abrigo.getNome()).willReturn("Abrigo Feliz");

        //ACT
        service.aprovar(aprovacaoAdocaoDto);

        //ASSERT
        assertEquals(StatusAdocao.APROVADO, adocao.getStatus());
    }

    @Test
    void deveriaSalvarAdocaoComoReprovadaAoReprovar() {
        //ARRANGE
        this.reprovacaoAdocaoDto = new ReprovacaoAdocaoDto(
                10L,
                "Justificativa qualquer."
        );
        this.adocao = new Adocao(tutor, pet, "Motivo Qualquer");

        given(repository.getReferenceById(reprovacaoAdocaoDto.idAdocao())).willReturn(adocao);
        given(tutor.getNome()).willReturn("João");
        given(tutor.getEmail()).willReturn("joao@email.com.br");

        given(pet.getNome()).willReturn("Rex");
        given(pet.getAbrigo()).willReturn(abrigo);

        given(abrigo.getNome()).willReturn("Abrigo Feliz");

        //ACT
        service.reprovar(reprovacaoAdocaoDto);

        //ASSERT
        assertEquals(StatusAdocao.REPROVADO, adocao.getStatus());
    }

}