package com.maa.buscaproximidadereativo.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import com.maa.buscaproximidadereativo.model.Coordenada;

@Service
public class GeolocalizacaoService {

	public Coordenada obterCoordenadasDo(String endereco) throws ApiException, InterruptedException, IOException {

		GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyBuAX8M8NS6J0bmSbD6g1mShzi51xkHR3Q").build();

		GeocodingApiRequest request = GeocodingApi.newRequest(context).address(endereco);

		GeocodingResult[] results = request.await();
		GeocodingResult resultado = results[0];

		Geometry geometry = resultado.geometry;
		LatLng location = geometry.location;

		return new Coordenada( location.lng, location.lat);
	}

}
