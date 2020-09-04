package com.maa.buscaproximidadereativo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maa.buscaproximidadereativo.model.Loja;
import com.maa.buscaproximidadereativo.service.LojaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lojas")
public class LojaController {

	@Autowired
	private LojaService service;

	@GetMapping
	public ResponseEntity<Flux<Loja>> getAllLojas() {

		Flux<Loja> lojas = service.findAll();

		return ResponseEntity.ok(lojas);
	}

	@GetMapping("/proximas")
	public ResponseEntity<Flux<Loja>> getLojasProximas(@RequestParam(name = "lat", required = false) String latitude,
			@RequestParam(name = "long", required = false) String longitude,
			@RequestParam(name = "ender", required = false) String endereco, @RequestParam("raio") double raio) {

		Flux<Loja> lojas = null;

		if ((latitude == null) || (longitude == null)) {
			lojas = service.lojasProximasDoEndereco(endereco, raio);

		} else {
			lojas = service.lojasProximasDaCoordenadas(longitude, latitude, raio);
		}

		return ResponseEntity.ok(lojas);

	}

	@PostMapping
	public ResponseEntity<Mono<Loja>> addLoja(@RequestBody Loja loja) {

		return ResponseEntity.status(HttpStatus.CREATED).body(service.addLoja(loja));

	}

}
