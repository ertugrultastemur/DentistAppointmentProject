package com.dentai.authenticationservice;

import com.dentai.authenticationservice.client.RetrieveMessageErrorDecoder;
import feign.codec.ErrorDecoder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}



	@Bean
	public ErrorDecoder errorDecoder() {
		return new RetrieveMessageErrorDecoder();
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
