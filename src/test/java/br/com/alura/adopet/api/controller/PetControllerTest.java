package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService service;

    @Test
    void deveriaRetornar200AoListarPets() throws Exception {
        //ARRANGE
        DadosDetalhesPet pet1 = new DadosDetalhesPet(
                1L, TipoPet.CACHORRO, "Rex", "SRD", 3
        );
        DadosDetalhesPet pet2 = new DadosDetalhesPet(
                2L, TipoPet.GATO, "Mia", "SRD", 2
        );

        given(service.listarTodosDisponiveis())
                .willReturn(List.of(pet1, pet2));

        //ACT + ASSERT
        mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Rex"))
                .andExpect(jsonPath("$[1].nome").value("Mia"));
    }

    @Test
    void deveriaRetornar200EListaVaziaQuandoNaoHouverPets() throws Exception {
        //ARRANGE
        given(service.listarTodosDisponiveis())
                .willReturn(List.of());

        //ACT + ASSERT
        mvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}