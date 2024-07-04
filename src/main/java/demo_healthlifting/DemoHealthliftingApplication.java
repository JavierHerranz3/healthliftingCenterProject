package demo_healthlifting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class DemoHealthliftingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoHealthliftingApplication.class, args);
	}

}
