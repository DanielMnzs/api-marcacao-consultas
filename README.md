# api-marcacao-consultas

Murilo Pomin rm:99683
Gabriel Taboada rm:97957
Daniel Menezes rm:551398
Luiz Augusto Melki rm:552053
Pedro Martins rm:98663

API para Marcação de Consultas
Este projeto é uma API RESTful desenvolvida em Java com Spring Boot, projetada para gerenciar o agendamento de consultas em uma clínica. Ela permite o cadastro e autenticação de usuários (pacientes, médicos e administradores), gerenciamento de especialidades, e a marcação e visualização de consultas.

Tecnologias Utilizadas
Java 17+

Spring Boot: Framework principal para a construção da aplicação.

Spring Security: Utilizado para a autenticação e autorização, com implementação de JSON Web Tokens (JWT).

Spring Data JPA (Hibernate): Para a persistência de dados e comunicação com o banco.

H2 Database: Banco de dados em memória e persistente em arquivo, ideal para desenvolvimento e testes.

Lombok: Para reduzir a verbosidade do código em models e DTOs.

Maven: Gerenciador de dependências e build do projeto.

Como Executar o Projeto
Pré-requisitos:

Java JDK 17 ou superior instalado.

Maven instalado.

Clone o repositório:

Bash

git clone <url-do-seu-repositorio>
cd <pasta-do-projeto>
Execute a aplicação:

Você pode rodar diretamente pela sua IDE (IntelliJ, Eclipse, etc.), localizando a classe principal ApiMarcacaoConsultasApplication e executando-a.

Ou pode executar via Maven no terminal:

Bash

.\mvnw spring-boot:run
Acesso à Aplicação:

A API estará disponível em http://localhost:8080.

O console do banco de dados H2 pode ser acessado em http://localhost:8080/h2-console. Utilize as seguintes credenciais para conectar:

Driver Class: org.h2.Driver

JDBC URL: jdbc:h2:file:./data/consultas_db

User Name: sa

Password: (deixe em branco)

Inicialização dos Dados:

Na primeira vez que a aplicação é executada, o banco de dados é populado com dados de teste, incluindo um usuário administrador, médicos, pacientes e especialidades, para facilitar a utilização imediata da API.

Usuário Admin:

Email: admin@clinica.com

Senha: admin123

Estrutura dos Endpoints da API
A seguir estão os principais endpoints disponíveis na API. Rotas que não estão marcadas como "Público" exigem um token de autenticação Bearer no cabeçalho Authorization.

Autenticação
POST /usuarios/login (Público)

Autentica um usuário e retorna um token JWT. O corpo da requisição deve conter email e senha.

POST /usuarios (Público)

Cadastra um novo usuário no sistema.

Usuários
GET /usuarios

Lista todos os usuários cadastrados.

GET /usuarios/{id}

Busca um usuário específico pelo seu ID.

GET /usuarios/medicos

Lista todos os usuários do tipo "MEDICO". Pode ser filtrado pela especialidade com um parâmetro de query (ex: /usuarios/medicos?especialidade=Cardiologia).

GET /usuarios/me

Retorna os dados do usuário que está autenticado (através do token).

PUT /usuarios/{id}

Atualiza os dados de um usuário.

DELETE /usuarios/{id}

Exclui um usuário do sistema.

Consultas
GET /consultas

Lista todas as consultas agendadas.

POST /consultas

Cria uma nova consulta.

GET /consultas/{id}

Busca uma consulta pelo seu ID.

DELETE /consultas/{id}

Exclui (cancela) uma consulta.

Especialidades
GET /especialidades

Lista todas as especialidades médicas cadastradas.
