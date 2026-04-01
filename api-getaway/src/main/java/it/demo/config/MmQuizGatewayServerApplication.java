package it.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.config.conditional.ConditionalOnEnabledGlobalFilter;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@PropertySource("classpath*:/*.properties") //se si vogliono aggiugere politiche di routing personalizzate
@RefreshScope
public class MmQuizGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MmQuizGatewayServerApplication.class, args);
	}

}
