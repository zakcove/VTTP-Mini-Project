package ssf.miniproject.weatherplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WeatherPlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherPlanApplication.class, args);
	}

}
