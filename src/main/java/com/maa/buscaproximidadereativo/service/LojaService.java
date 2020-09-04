package com.maa.buscaproximidadereativo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import com.google.maps.errors.ApiException;
import com.maa.buscaproximidadereativo.model.Loja;
import com.maa.buscaproximidadereativo.repository.LojaRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LojaService {

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

		List<Double> coordenadas = null;

		try {
			coordenadas = geolocalizacaoService.obterCoordenadasDo(endereco);
		} catch (ApiException | InterruptedException | IOException e) {

			e.printStackTrace();
		}

		Point point = new Point(coordenadas.get(0), coordenadas.get(1));

		Distance distancia = new Distance(raio, Metrics.KILOMETERS);

		return repo.findByLocationNear(point, distancia);
	}

	public Mono<Loja> addLoja(Loja loja) {
		return repo.save(loja);

	}
}
