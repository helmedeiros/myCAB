# myCAB

Taxi cab fleet management.

## Sobre

myCAB e uma central de chamadas de taxi com cadastro de frota, clientes, motoristas e dispatcher.

O sistema permite ao operador receber chamadas, localizar o carro mais proximo e despachar.



## Tecnologia

- Java 7

- Spring 3.0.5 (core, web, mvc, orm)

- Hibernate 3.6 (JPA) sobre H2

- JSP + JSTL para views

- jQuery 1.7 para polling de mensagens

- Maven 3 com Jetty 9 embarcado



## Build

    mvn package



## Testes

    mvn test



## Executando

    mvn jetty:run



A aplicacao sobe em http://localhost:8080 com banco H2 em memoria.



## Uso

1. Acesse / para a tela inicial.

2. Acesse /signup para se cadastrar como motorista.

3. Acesse /operator para a central de chamadas.

4. Acesse /cabs para gerenciar a frota.

5. Acesse /customers para gerenciar clientes.

6. Acesse /cab/{id} para a tela do motorista no veiculo.

7. Acesse /customer/{id} para a tela do cliente.

## Catalogo

O catalogo padrao popula 30 modelos populares no Brasil em 2012, divididos em tres categorias.



### NORMAL

Volkswagen Gol, Fiat Palio, Chevrolet Celta, Ford Fiesta, Renault Sandero, Fiat Uno, Chevrolet Classic, Volkswagen Voyage, Renault Logan, Chevrolet Prisma.



### CONFORTO

Toyota Corolla, Honda Civic, Volkswagen Jetta, Chevrolet Cruze, Ford Focus, Hyundai Elantra, Nissan Sentra, Renault Fluence, Volkswagen Passat, Honda Fit.



### GRANDE

Chevrolet Spin, Fiat Doblo, Volkswagen Touran, Toyota Sienna, Renault Grand Scenic, Citroen C4 Picasso, Peugeot 5008, Kia Carens, Hyundai Tucson, Chevrolet Zafira.



## API de mensagens

Clientes e veiculos consultam a propria fila com GET /api/messages/{kind}/{id} para drenar mensagens nao lidas.

Para apenas a contagem nao lida sem marcar como lida: GET /api/messages/{kind}/{id}/unread.

Para enviar uma mensagem ao destinatario: POST /api/messages/{kind}/{id} com body no formulario.

Os papeis suportados sao CAB, CUSTOMER e OPERATOR.



## Arquitetura

Camadas: model (entidades JPA e value objects), persistence (repositorios), service (regras de negocio), controllers (Spring MVC).

Mensagens entre central, motoristas e clientes circulam pela tabela message com flag de leitura.

O finder de carro mais proximo usa haversine sobre os carros FREE da categoria solicitada.



## Estatisticas

GET /api/stats retorna contadores de frota, despachos ativos, carros prontos e motoristas.



## Testes locais

A bateria roda em poucos segundos com H2 em memoria. Use mvn test antes de cada commit.

## Convencoes

Commits Conventional: feat, fix, refactor, test, build, chore, docs, style.

Codigo sem comentarios; o que precisa de explicacao precisa ser renomeado.

Testes locais (mvn test) precisam passar antes de qualquer commit.



## Seguranca

Senhas de motoristas sao guardadas como hash SHA-256 com salt aleatorio de 16 bytes.

Sessao de motorista ao logar fica em HttpSession com chave driverId.

Mensagens enviadas pela central nao sao apagadas para auditoria; apenas marcadas como lidas.



## Mapa de URLs

- / landing publica

- /signup formulario de motorista

- /login formulario de login do motorista
