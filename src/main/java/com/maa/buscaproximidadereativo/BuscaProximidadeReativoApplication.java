package com.maa.buscaproximidadereativo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.blockhound.BlockHound;

@SpringBootApplication
public class BuscaProximidadeReativoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuscaProximidadeReativoApplication.class, args);
		
		BlockHound.install();
	}

}
