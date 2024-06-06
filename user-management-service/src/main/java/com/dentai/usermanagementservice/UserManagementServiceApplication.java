package com.dentai.usermanagementservice;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserManagementServiceApplication {

	Logger logger = org.slf4j.LoggerFactory.getLogger(UserManagementServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		logger.info("ModelMapperConfig: modelMapper bean creation");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		return modelMapper;
	}

}
