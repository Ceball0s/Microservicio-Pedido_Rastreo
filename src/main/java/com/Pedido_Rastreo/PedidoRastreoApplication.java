package com.Pedido_Rastreo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class PedidoRastreoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedidoRastreoApplication.class, args);
	}

}
