package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculadoraProbabilidadeAdocaoTest {

    @Test
    @DisplayName("Probabilidade alta para gatos jovens com peso baixo")
    void probabilidadeAltaCenario1() {
        //IDADE 4 ANOS E 4KG - DEVERIA RETORNAR ALTA

        //ARRANGE
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo Feliz",
                "9499999999",
                "abrigofeliz@gmail.com.br"
        ));

        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siamês",
                4,
                "Cinza",
                4.0f
        ), abrigo);

        //ACT
        CalculadoraProbabilidadeAdocao calc = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidadeAdocao = calc.calcular(pet);

        //ASSERT
        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidadeAdocao);
    }

    @Test
    @DisplayName("Probabilidade média para gatos idosos com peso baixo")
    void probabilidadeMediaCenario1() {
        //IDADE 15 ANOS E 4KG - DEVERIA RETORNAR MEDIA

        //ARRANGE
        Abrigo abrigo = new Abrigo(new CadastroAbrigoDto(
                "Abrigo Feliz",
                "9499999999",
                "abrigofeliz@gmail.com.br"
        ));

        Pet pet = new Pet(new CadastroPetDto(
                TipoPet.GATO,
                "Miau",
                "Siamês",
                15,
                "Cinza",
                4.0f
        ), abrigo);

        //ACT
        CalculadoraProbabilidadeAdocao calc = new CalculadoraProbabilidadeAdocao();
        ProbabilidadeAdocao probabilidadeAdocao = calc.calcular(pet);

        //ASSERT
        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidadeAdocao);
    }
}