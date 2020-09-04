# Teste Busca por Proximidade com Mongo de forma reativa
Teste simples usando Spring Data MongoDB para busca lojas perto de um endereço de forma reativa


## Antes de começar 

- crie um banco de dados "cad-lojas".
- crie um coleção "lojas" com a seguinte estrutura.

	{
		"codLoja": "002",
		"apelido": "Araras",
		"location": { "type": "Point", "coordinates": [-22.351621, -47.386110] }
	}

- No mongo crie o índice:
  - db.lojas.createIndex( { location : "2dsphere" } )
  - Sem ele a consulta não vai funcionar.


	
## Testar

1-testar proximidade por coordenada.

http://localhost:8080/lojas/proximas/?long=-22.407744&lat=-47.560361&raio=2
	
2-testar proximidade por endereço.

http://localhost:8080/lojas/proximas/?ender=R.%20Tr%C3%AAs,%201296%20-%20Centro,%20Rio%20Claro%20-%20SP,%2013504-821&raio=20
	

	
	
	
	
