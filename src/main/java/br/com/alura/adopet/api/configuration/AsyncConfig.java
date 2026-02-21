package br.com.alura.adopet.api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); //Número mínimo de threads sempre ativas.
        executor.setMaxPoolSize(10); //Número máximo de threads que podem existir ao mesmo tempo.
        executor.setQueueCapacity(100); //Quantas tarefas podem ficar esperando na fila.
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
