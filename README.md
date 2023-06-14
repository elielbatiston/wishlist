# Wishlist

Serviço HTTP para as funcionalidades de Wishlist do cliente.
Esse sistema está programado para aceitar as linguagens **pt_BR** e **un_US**.

- [Wishlist](#wishlist) 
   - [1 - Primeiros passos](#primeiros-passos) 
   - [2 - Documentação da API](#documentação-da-api) 
   - [3 - Endereços da aplicação](#endereços-da-aplicação) 
   - [4 - Como testar a aplicação](#como-testar-a-aplicação)
   - [5 - Teste de cobertura](#teste-de-cobertura)

## Primeiros passos

**Passo 1:** Faça o clone do projeto no diretório de sua preferência.

```shell
git clone https://github.com/elielbatiston/wishlist
```

**Passo 2:** Vá para o terminal, acesse o diretório que você clonou o projeto e execute o comando abaixo:

```shell
docker-compose -f ./docker-compose-build.yml up -d
```

***Atenção**: A aplicação está programado para responder na porta 8080 e a 27018. Caso estas portas na sua máquina estejam 
ocupadas com outras aplicações, antes de subir os containers, favor editar o docker-compose-build.yml e o arquivo Dockerfile 
mudando as portas do container.*

## Documentação da API

1. __Documentação da API:__ [http://localhost:8080/wishlist-backend/v3/api-docs](http://localhost:8080/wishlist-backend/v3/api-docs)
2. __Documentação Visual da API:__ [http://localhost:8080/wishlist-backend/swagger-ui.html](http://localhost:8080/wishlist-backend/swagger-ui.html)

## Endereços da aplicação

Após subir o container, os seguintes endereços estarão disponíveis.

1. __BackEnd:__ [http://localhost:8080/wishlist-backend](http://localhost:8080/wishlist-backend)
2. __Documentação API-DOCS:__ [http://localhost:8080/wishlist-backend/docs/v3/api-docs](http://localhost:8080/wishlist-backend/docs/v3/api-docs)
3. __Documentação Swagger-UI:__ [http://localhost:8080/wishlist-backend/swagger-ui.html](http://localhost:8080/wishlist-backend/swagger-ui.html)

## Como testar a aplicação

Com o container da aplicação no ar, você poderá consumir a API das seguintes formas:

1. __Documentação Swagger-UI:__ [Acesse a documentação da API](http://localhost:8080/wishlist-backend/swagger-ui.html) 
e utilize dela para a realização dos testes.
2. __Através do curl:__ Utilize os comandos curl abaixo para consumir a API.
   - **Adicionar um produto na Wishlist do cliente:**
```shell
curl --location --request POST 'localhost:8080/wishlist-backend/wishlist' \
--header 'Accept-Language: pt_BR' \
--data-raw '{
   "customer": {
      "id": "C1",
      "name": "Customer 1"
   },
   "product": {
      "id": "P1",
      "name": "Product 1",
      "price": 15.98
   }
}'
```
   - **Remover um produto da Wishlist do cliente:**
```shell
curl --location --request DELETE 'localhost:8080/wishlist-backend/wishlist/C1/product/P1' \
--header 'Accept-Language: pt_BR'
```

   - **Consultar todos os produtos da Wishlist do cliente:**
```shell
curl --location --request GET 'localhost:8080/wishlist-backend/wishlist/C1' \
--header 'Accept-Language: pt_BR' \
```

   - **Consultar um determinado produto na Wishlist do cliente:**
```shell
curl --location --request GET 'localhost:8080/wishlist-backend/wishlist/C1/P1' \
--header 'Accept-Language: pt_BR'
```
   
***OBSERVAÇÃO:** Os comandos acima precisaram que o curl esteja instalado em sua máquina.*

3. __Arquivo do Postman:__ Arquivo do postman para importar e consumir a API. 
[O arquivo pode ser baixado aqui](https://github.com/elielbatiston/wishlist/blob/main/wishlist.postman_collection.json)

## Teste de cobertura

Foi utilizado o jacoco para a geração do teste de cobertura do código. Execute o comando abaixo para gerar o relatório. 
```shell
mvn -B clean jacoco:prepare-agent verify jacoco:report
``` 

<p align="center"><img src="https://github.com/elielbatiston/wishlist/blob/main/src/main/resources/medias/coverage.png"></p>

