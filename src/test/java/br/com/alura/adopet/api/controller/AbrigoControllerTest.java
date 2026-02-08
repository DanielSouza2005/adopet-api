package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.DadosDetalhesAbrigo;
import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import br.com.alura.adopet.api.exception.CadastrarAbrigoValidacaoException;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.service.AbrigoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AbrigoController.class)
@AutoConfigureJsonTesters
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService service;

    @Autowired
    private JacksonTester<CadastroAbrigoDto> jsonCadastroAbrigoDto;

    //service.listar()
    @Test
    void deveriaRetornar200AoListarAbrigos() throws Exception {
        //ARRANGE
        DadosDetalhesAbrigo abrigo1 = new DadosDetalhesAbrigo(
                1L,
                "Abrigo",
                "19998885544",
                "teste@gmail.com"
        );

        DadosDetalhesAbrigo abrigo2 = new DadosDetalhesAbrigo(
                2L,
                "Abrigo 2",
                "19998885545",
                "teste@gmail.com.br"
        );

        given(service.listar()).willReturn(List.of(abrigo1, abrigo2));

        //ACT + ASSERT
        mvc.perform(get("/abrigos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Abrigo"))
                .andExpect(jsonPath("$[1].nome").value("Abrigo 2"));
    }

    @Test
    void deveriaRetornar400AoListarNenhumAbrigo() throws Exception {
        //ARRANGE
        given(service.listar()).willReturn(List.of());

        //ACT + ASSERT
        mvc.perform(get("/abrigos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    //service.cadastrar
    @Test
    void deveriaRetornar200AoCadastrarAbrigoComSucesso() throws Exception {
        //ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto(
                "Abrigo",
                "19998885522",
                "email@outlook.com"
        );

        //ACT + ASSERT
        mvc.perform(post("/abrigos")
                        .content(jsonCadastroAbrigoDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Abrigo cadastrado com sucesso."));
    }

    @Test
    void deveriaRetornar400AoCadastrarAbrigoComDadosInvalidos() throws Exception {
        //ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto(
                "",
                "1999888",
                "email@outlook"
        );

        //ACT + ASSERT
        mvc.perform(post("/abrigos")
                        .content(jsonCadastroAbrigoDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaRetornar400AoCadastrarAbrigoDuplicado() throws Exception {
        //ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto(
                "Abrigo",
                "19998885522",
                "email@outlook.com"
        );

        //ACT + ASSERT
        doThrow(new CadastrarAbrigoValidacaoException("Abrigo já cadastrado com esses dados."))
                .when(service)
                .cadastrar(any(CadastroAbrigoDto.class));

        mvc.perform(post("/abrigos")
                        .content(jsonCadastroAbrigoDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Abrigo já cadastrado com esses dados."));
    }

    //service.listarPets()
    @Test
    void deveriaRetornar200AoListarPetsAbrigo() throws Exception {
        //ARRANGE
        DadosDetalhesPet pet1 = new DadosDetalhesPet(
                1L, TipoPet.CACHORRO, "Rex", "SRD", 3
        );

        DadosDetalhesPet pet2 = new DadosDetalhesPet(
                2L, TipoPet.GATO, "Mia", "SRD", 2
        );

        String abrigoId = "1";
        given(service.listarPets(abrigoId)).willReturn(List.of(pet1, pet2));

        //ACT + ASSERT
        mvc.perform(get("/abrigos/{idOuNome}/pets", abrigoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Rex"))
                .andExpect(jsonPath("$[1].nome").value("Mia"));
    }

    @Test
    void deveriaRetornar400AoListarNenhumPetNoAbrigo() throws Exception {
        //ARRANGE
        String abrigoId = "1";
        given(service.listarPets(abrigoId)).willReturn(List.of());

        //ACT + ASSERT
        mvc.perform(get("/abrigos/{idOuNome}/pets", abrigoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    //service.cadastrar
    @Test
    void deveriaRetornar200AoCadastrarPetNoAbrigoComSucesso() throws Exception {
        //ARRANGE
        String abrigoId = "1";
        Long petId = 1L;

        //ACT + ASSERT
        mvc.perform(post("/abrigos/{idOuNome}/pets", petId)
                        .content(abrigoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Pet cadastrado com sucesso."));
    }

    @Test
    void deveriaRetornar400AoCadastrarPetInexistenteNoAbrigoOuAbrigoInexistente() throws Exception {
        //ARRANGE
        String abrigoId = "1";
        Long petId = 1L;

        //ACT + ASSERT
        doThrow(new EntityNotFoundException("O recurso solicitado não foi encontrado."))
                .when(service)
                .cadastrarPet(abrigoId, petId);

        mvc.perform(post("/abrigos/{idOuNome}/pets", petId)
                        .content(abrigoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("O recurso solicitado não foi encontrado."));
    }
}