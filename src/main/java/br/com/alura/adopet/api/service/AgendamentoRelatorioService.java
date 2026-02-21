package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.DadosDetalhesPet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoRelatorioService {

    @Autowired
    private PetService petService;

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String emailHost;

    @Scheduled(cron = "0 0 6 1 * *") //MENSAL NO PRIMEIRO DIA DO MES ÀS 6H
    public void enviarEmailRelatorioPetsDisponiveis() {
        List<DadosDetalhesPet> petsDisponiveis = petService.listarTodosDisponiveis();

        if (petsDisponiveis.isEmpty()) {
            return;
        }

        StringBuilder corpo = new StringBuilder();

        corpo.append("Olá!\n\n");
        corpo.append("Segue a lista de pets disponíveis para adoção:\n\n");

        for (DadosDetalhesPet pet : petsDisponiveis) {
            corpo.append("🐾 Nome: ").append(pet.nome()).append("\n");
            corpo.append("   Tipo: ").append(pet.tipo()).append("\n");
            corpo.append("   Raça: ").append(pet.raca()).append("\n");
            corpo.append("   Idade: ").append(pet.idade()).append(" ano(s)\n");
            corpo.append("---------------------------------------\n");
        }

        corpo.append("\nAtenciosamente,\nEquipe Adopet");

        emailService.enviarEmail(
                "adopet@email.com.br",
                this.emailHost,
                "Relatório de Pets Disponíveis",
                corpo.toString()
        );
    }
}
