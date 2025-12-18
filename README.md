# Serviço de Cálculo de Frete – Microserviço Reutilizável

Este projeto implementa um microserviço reutilizável para cálculo de frete, desenvolvido como parte da disciplina **Reuso de Software**. A solução foi projetada seguindo princípios de **arquitetura orientada a serviços (SOA)**, com foco em **extensibilidade, desacoplamento e reutilização**, utilizando padrões de projeto consolidados.

---

##  Visão Geral

O microserviço é responsável por calcular o custo e o prazo de entrega de um frete com base em:

- CEP de origem e destino
- Dimensões e peso do produto
- Transportadora selecionada
- Modalidade de entrega
- Serviços adicionais (ex: seguro, entrega rápida)

A aplicação expõe uma **API REST**, podendo ser consumida por diferentes sistemas, como e-commerces, marketplaces e sistemas logísticos.

---

## ️ Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller**: expõe os endpoints REST
- **Service**: orquestra o fluxo de cálculo
- **Strategy**: encapsula regras específicas de cada transportadora
- **Factory**: instancia dinamicamente a estratégia adequada
- **Decorator**: adiciona funcionalidades extras ao cálculo (serviços adicionais)

Essa abordagem garante baixo acoplamento e facilita a reutilização do serviço em diferentes contextos.

---

## ️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Validation**
- **Resilience4j** (Circuit Breaker e Retry)
- **Maven**
- **Postman / Insomnia** (testes da API)

---

##  Como Executar o Projeto

###  Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- Java JDK 17 ou superior
- Maven 3.8+
- Git

---

###  Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/freight-service.git
cd freight-service
```
---

## ️ Executando a Aplicação

Utilize o Maven para iniciar o microserviço:

```bash
mvn spring-boot:run
```
Após a inicialização, a aplicação estará disponível em:
http://localhost:8080

##  Endpoint Principal: Calcular Frete

**Descrição:** Realiza o cálculo de frete considerando dimensões, peso, distância e serviços adicionais, selecionando a estratégia mais adequada ou a solicitada explicitamente.

* **Método:** `POST`
* **URL:** `/api/freight/calculate`
* **Headers:** `Content-Type: application/json`

###  Exemplo de Requisição

```json
{
  "zipCodeOrigin": "01001000",
  "zipCodeDestination": "20040002",
  "weight": 12.0,
  "length": 50,
  "height": 30,
  "width": 40,
  "carrier": "AZUL_EXPRESS",
  "deliveryMode": "FAST",
  "extraServices": [
    "INSURANCE"
  ],
  "declaredValue": 800.00,
  "discount": 0.0
}
```

### Exemplo de Resposta

```json
{
"totalCost": 189.50,
"carrier": "AZUL_EXPRESS",
"deliveryMode": "FAST",
"minDeliveryDays": 2,
"maxDeliveryDays": 2,
"deliveryDate": "2025-05-20",
"insuranceCost": "16.00",
"message": "Cálculo realizado com sucesso"
}
```

### Reutilização do Serviço
O microserviço foi projetado para ser consumido como uma "caixa-preta" por qualquer aplicação capaz de realizar chamadas HTTP (E-commerce, ERPs, Apps Mobile).

A adição de novas transportadoras ou serviços extras pode ser feita sem alterar o contrato da API.

Internamente, novas funcionalidades são acopladas através de novas implementações da interface FreightStrategy ou novos Decoradores.

Essa característica reforça o foco do projeto nos princípios de Open/Closed Principle e Reuso de Software.

### Resiliência
O serviço utiliza a biblioteca Resilience4j para implementar padrões de tolerância a falhas, garantindo alta disponibilidade:

Circuit Breaker: Monitora chamadas externas (como o cálculo de distância entre CEPs). Se a taxa de erro subir, o circuito abre para prevenir falhas em cascata.

Fallback: Em caso de indisponibilidade de serviços externos ou lentidão excessiva, um mecanismo de fallback é acionado automaticamente, retornando uma estimativa segura para manter o funcionamento do sistema de vendas.

