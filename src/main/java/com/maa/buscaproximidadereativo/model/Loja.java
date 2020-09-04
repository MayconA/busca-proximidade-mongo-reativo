package com.maa.buscaproximidadereativo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "lojas")
public class Loja {
	
	@Id
	private String id;

	private String apelido;
	private String codLoja;
	
	private GeoJsonPoint location;

}
