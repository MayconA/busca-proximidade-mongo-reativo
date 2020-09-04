package com.maa.buscaproximidadereativo.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.maa.buscaproximidadereativo.model.Loja;

import reactor.core.publisher.Flux;

public interface LojaRepository extends ReactiveMongoRepository<Loja, String> {
	
	Flux<Loja> findByLocationNear(Point point, Distance distance);

}
