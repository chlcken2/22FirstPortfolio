package HelloMyTeam.Hellomyteam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

	@Value("${env}")
	private String env;

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		application.addListeners(new ApplicationPidFileWriter());
		application.run(args);

	}

	@PostConstruct
	private void start() {
		System.out.println("now-env = " + env);
	}
}
