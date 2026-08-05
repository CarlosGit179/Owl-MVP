🦉 Owl

Sistema de gerenciamento de biblioteca desenvolvido em Java com Spring Boot, voltado para o controle de livros, alunos e empréstimos.

O projeto foi desenvolvido como um MVP (Minimum Viable Product), priorizando regras de negócio, organização da aplicação, persistência de dados e testes antes da inclusão de recursos adicionais de infraestrutura e segurança.

📖 Sobre o projeto

O Owl é uma API REST para gerenciamento das operações fundamentais de uma biblioteca.

O sistema permite cadastrar e consultar livros e alunos, administrar empréstimos e devoluções e controlar automaticamente a disponibilidade dos livros.

O projeto utiliza uma arquitetura em camadas, separando responsabilidades entre controllers, services e repositories.

✨ Funcionalidades
Cadastro de livros
Consulta de livros
Atualização de livros
Exclusão de livros
Cadastro de alunos
Consulta de alunos
Atualização de alunos
Exclusão de alunos
Realização de empréstimos
Devolução de livros
Consulta de empréstimos
Consulta de empréstimos por aluno
Consulta de empréstimos por livro
Consulta de empréstimos por status
Controle automático da disponibilidade dos livros
📋 Regras de negócio

O MVP implementa regras para preservar a consistência das operações de empréstimo.

Um livro precisa estar disponível para ser emprestado.
Um aluno pode possuir apenas um empréstimo em andamento por vez.
Ao realizar um empréstimo, o livro passa automaticamente para indisponível.
Ao realizar a devolução, o livro volta a ficar disponível.
A data do empréstimo é registrada automaticamente.
A previsão de devolução é definida para um mês após a realização do empréstimo.
Operações envolvendo registros inexistentes são rejeitadas pela aplicação.
🛠️ Tecnologias
Java 21
Spring Boot
Spring Web
Spring Data JPA
Hibernate
PostgreSQL
Maven
JUnit 5
Mockito
🏗️ Arquitetura

O Owl utiliza uma arquitetura em camadas para separar as responsabilidades da aplicação.

Cliente / Postman
       │
       ▼
  Controller
       │
       ▼
    Service
       │
       ▼
  Repository
       │
       ▼
  PostgreSQL
Controller

Responsável por receber as requisições HTTP e disponibilizar os endpoints da API.

Service

Responsável pela lógica da aplicação e aplicação das regras de negócio.

Repository

Responsável pelo acesso e persistência dos dados utilizando Spring Data JPA.

PostgreSQL

Responsável pelo armazenamento persistente dos dados.

📦 Modelo de domínio

As principais entidades do sistema são:

Aluno
  │
  │
  ▼
Emprestimo
  │
  │
  ▼
Livro
Livro

Representa um livro disponível no acervo da biblioteca.

Entre suas informações estão título, autor, ISBN, editora, ano de publicação, categoria e disponibilidade.

Aluno

Representa o aluno cadastrado no sistema e apto a realizar empréstimos.

Empréstimo

Representa a relação entre um aluno e um livro durante determinado período.

Armazena informações referentes à realização do empréstimo, previsão de devolução e seu status.

📂 Estrutura do projeto

A aplicação está organizada seguindo a separação de responsabilidades:

src/
├── main/
│   ├── java/
│   │   └── ...
│   │       ├── controller/
│   │       ├── entity/
│   │       ├── repository/
│   │       └── service/
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    └── java/
        └── ...
🚀 Como executar
Pré-requisitos

Para executar o projeto localmente é necessário possuir:

Java 21+
Maven
PostgreSQL
1. Clone o repositório
git clone <URL-DO-REPOSITORIO>

Entre na pasta:

cd Owl
2. Configure o PostgreSQL

Configure as credenciais do banco no arquivo:

src/main/resources/application.properties

Exemplo:

spring.datasource.url=jdbc:postgresql://localhost:5432/owl
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA

O Hibernate é responsável pela criação/atualização das estruturas necessárias no banco durante a execução da aplicação, conforme a configuração do projeto.

3. Execute
mvn spring-boot:run

Por padrão, a aplicação será iniciada na porta:

8080
🔗 API

A aplicação disponibiliza endpoints REST para gerenciamento de:

Livros

Operações de cadastro, consulta, atualização e exclusão de livros.

Alunos

Operações de cadastro, consulta, atualização e exclusão de alunos.

Empréstimos

Operações relacionadas à realização, consulta e devolução de empréstimos.

🧪 Testes

O projeto possui testes automatizados para validar o comportamento da camada de serviços e suas principais regras de negócio.

Os testes foram desenvolvidos utilizando:

JUnit 5
Mockito

São verificados tanto cenários de execução esperada quanto comportamentos relacionados às regras de negócio e tratamento de operações inválidas.

Para executar os testes:

mvn test
🗺️ Roadmap

O desenvolvimento inicial foi concentrado na construção e validação do MVP.

Possíveis evoluções incluem:

Infraestrutura

Containerização com Docker

Docker Compose para aplicação e PostgreSQL

Pipeline de CI/CD

API e documentação

Swagger / OpenAPI

Documentação detalhada dos endpoints

Tratamento global e padronizado de exceções

Segurança

Spring Security

Autenticação com JWT

Controle de acesso e permissões

Domínio

Reserva de livros

Histórico avançado de empréstimos

Controle de atrasos

Sistema de multas

Notificações de devolução

Arquitetura

Avaliação da evolução do monólito conforme o crescimento da aplicação

Separação de serviços quando houver necessidade arquitetural

📌 Status

MVP concluído.

O núcleo funcional da aplicação — gerenciamento de livros, alunos e empréstimos — encontra-se implementado e testado.

As funcionalidades apresentadas no Roadmap representam possíveis evoluções posteriores e não fazem parte do escopo do MVP atual.
