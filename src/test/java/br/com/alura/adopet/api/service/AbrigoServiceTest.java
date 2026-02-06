package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.DadosDetalhesAbrigo;
import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.validation.abrigo.ValidacaoAbrigoDuplicado;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService service;

    @Mock
    private Abrigo abrigo;

    @Mock
    private Pet pet;

    @Mock
    private AbrigoRepository repository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private ValidacaoAbrigoDuplicado validacaoAbrigoDuplicado;

    @Captor
    private ArgumentCaptor<Abrigo> abrigoCaptor;

    private CadastroAbrigoDto cadastroAbrigoDto;

    @Test
    void deveriaCadastrarAbrigoComSucesso() {
        //ARRANGE
        this.cadastroAbrigoDto = new CadastroAbrigoDto(
                "Abrigo",
                "19888885555",
                "abrigo@gmail.com"
        );

        this.abrigo = new Abrigo(this.cadastroAbrigoDto);

        //ACT
        this.service.cadastrar(this.cadastroAbrigoDto);

        //ASSERT
        then(repository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();

        Assertions.assertEquals(abrigo, abrigoSalvo);
        Assertions.assertEquals(cadastroAbrigoDto.nome(), abrigoSalvo.getNome());
        Assertions.assertEquals(cadastroAbrigoDto.email(), abrigoSalvo.getEmail());
        Assertions.assertEquals(cadastroAbrigoDto.telefone(), abrigoSalvo.getTelefone());
    }

    @Test
    void deveriaChamarValidadoresAoCadastrarAbrigo() {
        //ARRANGE
        this.cadastroAbrigoDto = new CadastroAbrigoDto(
                "Abrigo",
                "19888885555",
                "abrigo@gmail.com"
        );

        //ACT
        this.service.cadastrar(this.cadastroAbrigoDto);

        //ASSERT
        then(this.validacaoAbrigoDuplicado).should().validar(this.cadastroAbrigoDto);
    }

    @Test
    void deveriaCadastrarPetNoAbrigoComSucessoComIdCorreto() {
        //ARRANGE
        String id = "1";
        Long abrigoId = 1L;
        Long petId = 10L;

        this.abrigo = new Abrigo();
        this.abrigo.setPets(new ArrayList<>());
        this.pet = new Pet();

        given(this.petRepository.getReferenceById(petId)).willReturn(pet);
        given(this.repository.getReferenceById(abrigoId)).willReturn(abrigo);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> {
            this.service.cadastrarPet(id, petId);
        });

        then(repository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();

        Assertions.assertTrue(abrigoSalvo.getPets().contains(pet));
        Assertions.assertEquals(abrigoSalvo, pet.getAbrigo());
        Assertions.assertFalse(pet.getAdotado());
    }

    @Test
    void deveriaLancarEntityNotFoundExceptionAoCadastrarPetQuandoAbrigoNaoExistePorId() {
        //ARRANGE
        String idOuNome = "1";
        Long abrigoId = 1L;
        Long petId = 10L;

        given(petRepository.getReferenceById(petId)).willReturn(new Pet());
        given(repository.getReferenceById(abrigoId)).willThrow(EntityNotFoundException.class);

        //ACT + ASSERT
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            this.service.cadastrarPet(idOuNome, petId);
        });
    }

    @Test
    void deveriaCadastrarPetNoAbrigoComSucessoComNomeCorreto() {
        //ARRANGE
        String idOuNome = "Abrigo";
        Long abrigoId = 1L;
        Long petId = 10L;

        this.abrigo = new Abrigo();
        this.abrigo.setPets(new ArrayList<>());
        this.pet = new Pet();

        given(this.petRepository.getReferenceById(petId)).willReturn(this.pet);
        given(this.repository.findByNome(idOuNome)).willReturn(this.abrigo);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> {
            this.service.cadastrarPet(idOuNome, petId);
        });

        then(repository).should().save(abrigoCaptor.capture());
        Abrigo abrigoSalvo = abrigoCaptor.getValue();

        Assertions.assertTrue(abrigoSalvo.getPets().contains(pet));
        Assertions.assertEquals(abrigoSalvo, pet.getAbrigo());
        Assertions.assertFalse(pet.getAdotado());
    }

    @Test
    void deveriaLancarEntityNotFoundExceptionAoCadastrarPetQuandoAbrigoNaoExistePorNome() {
        //ARRANGE
        String idOuNome = "Abrigo";
        Long petId = 10L;

        given(petRepository.getReferenceById(petId)).willReturn(new Pet());
        given(repository.findByNome(idOuNome)).willThrow(EntityNotFoundException.class);

        //ACT + ASSERT
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            this.service.cadastrarPet(idOuNome, petId);
        });
    }

    @Test
    void deveriaListarAbrigosComSucesso() {
        // ARRANGE
        Abrigo abrigo1 = new Abrigo();
        abrigo1.setNome("Abrigo 1");

        Abrigo abrigo2 = new Abrigo();
        abrigo2.setNome("Abrigo 2");

        given(repository.findAll()).willReturn(List.of(abrigo1, abrigo2));

        // ACT
        List<DadosDetalhesAbrigo> resultado = service.listar();

        // ASSERT
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals("Abrigo 1", resultado.get(0).nome());
        Assertions.assertEquals("Abrigo 2", resultado.get(1).nome());

        then(repository).should().findAll();
    }

    @Test
    void deveriaRetornarListaVaziaQuandoNaoExistirAbrigos() {
        // ARRANGE
        given(repository.findAll()).willReturn(List.of());

        // ACT
        List<DadosDetalhesAbrigo> resultado = service.listar();

        // ASSERT
        Assertions.assertTrue(resultado.isEmpty());
        then(repository).should().findAll();
    }

    @Test
    void deveriaListarPetsDoAbrigoPorId() {
        // ARRANGE
        String idOuNome = "1";
        Long abrigoId = 1L;

        this.abrigo = new Abrigo();

        Pet pet1 = new Pet();
        Pet pet2 = new Pet();

        abrigo.setPets(List.of(pet1, pet2));

        given(repository.getReferenceById(abrigoId)).willReturn(abrigo);

        // ACT
        List<DadosDetalhesPet> resultado = service.listarPets(idOuNome);

        // ASSERT
        Assertions.assertEquals(2, resultado.size());
        then(repository).should().getReferenceById(abrigoId);
    }

    @Test
    void deveriaLancarExcecaoAoListarPetsDeAbrigoInexistentePorId() {
        //ARRANGE
        given(repository.getReferenceById(1L)).willThrow(EntityNotFoundException.class);

        //ACT + ASSERT
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                service.listarPets("1")
        );
    }

    @Test
    void deveriaListarPetsDoAbrigoPorNome() {
        // ARRANGE
        String nomeAbrigo = "Abrigo Feliz";

        this.abrigo = new Abrigo();

        Pet pet1 = new Pet();
        Pet pet2 = new Pet();

        abrigo.setPets(List.of(pet1, pet2));

        given(repository.findByNome(nomeAbrigo)).willReturn(abrigo);

        // ACT
        List<DadosDetalhesPet> resultado = service.listarPets(nomeAbrigo);

        // ASSERT
        Assertions.assertEquals(2, resultado.size());
        then(repository).should().findByNome(nomeAbrigo);
    }

    @Test
    void deveriaLancarExcecaoAoListarPetsDeAbrigoInexistentePorNome() {
        //ARRANGE
        given(repository.findByNome("Nome")).willThrow(EntityNotFoundException.class);

        //ACT + ASSERT
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                service.listarPets("Nome")
        );
    }

}