package pl.edu.agh.to2.cleaner;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import pl.edu.agh.to2.cleaner.model.EmbeddingServerClient;

@SpringBootApplication
public class Application {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			EmbeddingServerClient.run();
//		};
//	}
//
//	@EventListener(ContextClosedEvent.class)
//	public void onShutdownEvent() {
//		EmbeddingServerClient.shutdown();
//	}
}