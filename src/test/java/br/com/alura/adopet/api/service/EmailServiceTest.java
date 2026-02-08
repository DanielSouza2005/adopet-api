package br.com.alura.adopet.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService service;

    @Mock
    private JavaMailSender emailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> emailCaptor;

    @Test
    void deveriaEnviarEmailComDadosCorretos() {
        // ARRANGE
        String remetente = "no-reply@adopet.com";
        String destinatario = "usuario@gmail.com";
        String assunto = "Bem-vindo!";
        String corpo = "Seu cadastro foi realizado com sucesso.";

        //ACT
        service.enviarEmail(remetente, destinatario, assunto, corpo);

        //ASSERT
        BDDMockito.then(emailSender).should().send(emailCaptor.capture());
        SimpleMailMessage emailEnviado = emailCaptor.getValue();

        Assertions.assertEquals(remetente, emailEnviado.getFrom());
        assertNotNull(emailEnviado.getTo());
        Assertions.assertEquals(destinatario, emailEnviado.getTo()[0]);
        Assertions.assertEquals(assunto, emailEnviado.getSubject());
        Assertions.assertEquals(corpo, emailEnviado.getText());
    }

    @Test
    void deveriaLancarExcecaoQuandoFalharEnvioDeEmail() {
        // ARRANGE
        doThrow(new MailSendException("Erro ao enviar"))
                .when(emailSender)
                .send(any(SimpleMailMessage.class));

        // ACT + ASSERT
        assertThrows(
                MailSendException.class,
                () -> service.enviarEmail("a", "b", "c", "d")
        );
    }


}