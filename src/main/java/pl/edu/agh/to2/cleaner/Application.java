package pl.edu.agh.to2.cleaner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.edu.agh.to2.cleaner.model.EmbeddingServerClient;

@EnableJpaRepositories(basePackages = "pl.edu.agh.to2.cleaner.repository")
@SpringBootApplication
@Slf4j
public class Application {
    @Autowired
    private Environment environment;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            EmbeddingServerClient.run();
            log.info("\u001B[32mApplication started, running on http://localhost:{}\u001B[0m", environment.getProperty("local.server.port"));
        };
    }


//  TODO: Uncomment before pushing to Bitbucket
//	@EventListener(ContextClosedEvent.class)
//	public void onShutdownEvent() {
//		EmbeddingServerClient.shutdown();
//	}
}
