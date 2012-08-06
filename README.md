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

introduce catalog readme section

explain catalog defaults
