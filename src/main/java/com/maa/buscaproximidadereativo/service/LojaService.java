package com.maa.buscaproximidadereativo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.google.maps.errors.ApiException;
import com.maa.buscaproximidadereativo.model.Coordenada;
import com.maa.buscaproximidadereativo.model.Loja;
import com.maa.buscaproximidadereativo.repository.LojaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class LojaService {

	// esta aqui somente devido aos metodos de exemplos v1
	private Coordenada coordenadas = null;

	@Autowired
	private LojaRepository repo;

	@Autowired
	private GeolocalizacaoService geolocalizacaoService;

	public Flux<Loja> findAll() {

		return repo.findAll();
	}

	public Flux<Loja> lojasProximasDaCoordenadas(String longitude, String latitude, double raio) {

		Point point = new Point(Double.valueOf(longitude), Double.valueOf(latitude));

		Distance distancia = new Distance(raio, Metrics.KILOMETERS);

		return repo.findByLocationNear(point, distancia);
	}

	public Flux<Loja> lojasProximasDoEndereco(String endereco, double raio) {

		// wrapper do código bloqueante
		Mono<Coordenada> coord = Mono.fromCallable(() -> geolocalizacaoService.obterCoordenadasDo(endereco))
				.subscribeOn(Schedulers.elastic());

		Distance distancia = new Distance(raio, Metrics.KILOMETERS);

		return coord
				.flux()
				.map(coo -> new Point(coo.getLatitude(), coo.getLongitude()))
				.flatMap(po -> repo.findByLocationNear(po, distancia));

	}

	public Mono<Loja> addLoja(Loja loja) {
		return repo.save(loja);

	}

	/*-------------------------------código alternativos, não são usados*/
	public Flux<Loja> lojasProximasDoEnderecoV0(String endereco, double raio) {
		/* Versão original que o blockhound indicava que era bloqueante */

		Coordenada coordenadas = null;

		/* codigo bloqueante */
		try {
			coordenadas = geolocalizacaoService.obterCoordenadasDo(endereco);
		} catch (ApiException | InterruptedException | IOException e) {

			e.printStackTrace();
		}

		Distance distancia = new Distance(raio, Metrics.KILOMETERS);

		Point point = new Point(coordenadas.getLatitude(), coordenadas.getLongitude());

		return repo.findByLocationNear(point, distancia);
	}

	public Flux<Loja> lojasProximasDoEnderecoV1(String endereco, double raio) {

		/*
		 * não bloqueante, mas usa uma forma não recomendada para obter o valor das
		 * coordendas do mono
		 */

		// wrapper do código bloqueante
		Mono<Coordenada> coord = Mono.fromCallable(() -> geolocalizacaoService.obterCoordenadasDo(endereco))
				.subscribeOn(Schedulers.elastic());

		// alternativa 1 para extrair dados do mono
		// mas dai tive que fazer coordenadas como atributo da classe
		// e nada garante que vou ter o valor nesse momento
		coord.subscribe(value -> coordenadas = value);

		Distance distancia = new Distance(raio, Metrics.KILOMETERS);

		Point point = new Point(coordenadas.getLatitude(), coordenadas.getLongitude());

		return repo.findByLocationNear(point, distancia);
	}

	public Flux<Loja> lojasProximasDoEnderecoV3_1(String endereco, double raio) {

		/** não bloqueante, mas cada passo esta separado para ficar mais didatico */

		// wrapper do código bloqueante
		Mono<Coordenada> coord = Mono.fromCallable(() -> geolocalizacaoService.obterCoordenadasDo(endereco))
				.subscribeOn(Schedulers.elastic());

		Flux<Coordenada> coord2 = coord.flux();

		Flux<Point> ponto = coord2.map(coo -> new Point(coo.getLatitude(), coo.getLongitude()));

		Distance distancia = new Distance(raio, Metrics.KILOMETERS);

		Flux<Loja> lojas = ponto.flatMap(po -> repo.findByLocationNear(po, distancia));

		return lojas;
	}
}
