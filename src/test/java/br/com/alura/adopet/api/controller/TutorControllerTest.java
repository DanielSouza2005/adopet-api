package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.CadastrarTutorValidacaoException;
import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TutorController.class)
@AutoConfigureJsonTesters
class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService service;

    @Autowired
    private JacksonTester<CadastroTutorDto> jsonCadastroTutorDto;

    @Autowired
    private JacksonTester<AtualizacaoTutorDto> jsonAtualizacaoTutorDto;

    @Test
    void deveriaRetornar200AoCadastrarTutorComSucesso() throws Exception {
        //ARRANGE
        CadastroTutorDto dto = new CadastroTutorDto(
                "Tutor",
                "19998856622",
                "email@gmail.com"
        );

        //ACT + ASSERT
        mvc.perform(post("/tutores")
                        .content(jsonCadastroTutorDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Tutor cadastrado com sucesso!"));
    }

    @Test
    void deveriaRetornar400AoCadastrarTutorComErros() throws Exception {
        //ARRANGE
        CadastroTutorDto dto = new CadastroTutorDto(
                "",
                "19999",
                "email@gmail"
        );

        //ACT + ASSERT
        mvc.perform(post("/tutores")
                        .content(jsonCadastroTutorDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaRetornar400AoCadastrarTutorQuandoTutorJaEstaCadastrado() throws Exception {
        //ARRANGE
        CadastroTutorDto dto = new CadastroTutorDto(
                "Tutor",
                "19998856622",
                "email@gmail.com"
        );

        doThrow(new CadastrarTutorValidacaoException("Tutor já cadastrado com esses dados."))
                .when(service)
                .cadastrar(any(CadastroTutorDto.class));

        //ACT + ASSERT
        mvc.perform(post("/tutores")
                        .content(jsonCadastroTutorDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tutor já cadastrado com esses dados."));
    }

    @Test
    void deveriaRetornar200AoAtualizarTutorComSucesso() throws Exception {
        //ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(
                1L,
                "19998856622",
                "email@gmail.com"
        );

        //ACT + ASSERT
        mvc.perform(put("/tutores")
                        .content(jsonAtualizacaoTutorDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Tutor atualizado com sucesso!"));
    }

    @Test
    void deveriaRetornar400AoAtualizarTutorComErros() throws Exception {
        //ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(
                1L,
                "19999",
                "email@gmail"
        );

        //ACT + ASSERT
        mvc.perform(put("/tutores")
                        .content(jsonAtualizacaoTutorDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveriaRetornar400AoAtualizarTutorQuandoTutorJaEstaCadastrado() throws Exception {
        //ARRANGE
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(
                1L,
                "19998856622",
                "email@gmail.com"
        );

        doThrow(new CadastrarTutorValidacaoException("Tutor já cadastrado com esses dados."))
                .when(service)
                .atualizar(any(AtualizacaoTutorDto.class));

        //ACT + ASSERT
        mvc.perform(put("/tutores")
                        .content(jsonAtualizacaoTutorDto.write(dto).getJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tutor já cadastrado com esses dados."));
    }
}