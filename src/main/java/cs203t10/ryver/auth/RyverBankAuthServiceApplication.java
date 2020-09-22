package cs203t10.ryver.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RyverBankAuthServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RyverBankAuthServiceApplication.class, args);
	}
}
