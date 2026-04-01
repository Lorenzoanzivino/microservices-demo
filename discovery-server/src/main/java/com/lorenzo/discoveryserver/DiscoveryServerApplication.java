package it.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //dashboard su http://localhost:8070/
public class MmQuizEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmQuizEurekaServerApplication.class, args);
	}

}
