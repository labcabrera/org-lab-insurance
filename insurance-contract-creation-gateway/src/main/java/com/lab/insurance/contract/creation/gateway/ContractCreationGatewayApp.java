package com.lab.insurance.contract.creation.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.integration.annotation.IntegrationComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@IntegrationComponentScan
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableSwagger2
public class ContractCreationGatewayApp {

	public static void main(String[] args) {
		SpringApplication.run(ContractCreationGatewayApp.class, args);
	}

}