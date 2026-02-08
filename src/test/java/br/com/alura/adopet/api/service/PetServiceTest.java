package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService service;

    @Mock
    private PetRepository repository;

    @Test
    void deveriaRetornarPets() {
        //ARRANGE
        Pet pet1 = new Pet();
        Pet pet2 = new Pet();
        List<DadosDetalhesPet> petsDisponiveis = Stream.of(pet1, pet2)
                .map(DadosDetalhesPet::new)
                .toList();

        given(repository.findByAdotadoFalse()).willReturn(List.of(pet1, pet2));

        //ACT
        List<DadosDetalhesPet> petsListados = service.listarTodosDisponiveis();

        //ASSERT
        Assertions.assertEquals(2, petsListados.size());
        then(repository).should().findByAdotadoFalse();
    }

    @Test
    void naoDeveriaRetornarNenhumPet() {
        //ARRANGE
        given(repository.findByAdotadoFalse()
                .stream()
                .map(DadosDetalhesPet::new)
                .toList()
        ).willThrow(EntityNotFoundException.class);

        //ACT + ASSERT
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.listarTodosDisponiveis());
    }
}