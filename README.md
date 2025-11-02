🚀 App de Agendamento de Processos (FESTO)
Este é um projeto full-stack que simula uma plataforma de agendamento de processos, inspirado no contexto industrial (FESTO). A aplicação permite que usuários ("Usuários") agendem horários com especialistas ("Profissionais") para iniciar ou acompanhar processos de software, e também monitorem dados de sensores em tempo real.

O projeto é dividido em:

Backend: Uma API RESTful construída com Spring Boot (Java).

Frontend: Um aplicativo móvel (e web) construído com React Native (Expo).

✨ Funcionalidades Principais
Autenticação JWT: Sistema completo de Login e Cadastro com tokens JWT.

Perfis de Usuário: Três níveis de acesso (Administrador, Profissional e Usuário).

Agendamento de Processos: Usuários podem agendar horários com profissionais (antigos "Médicos") com base na "Profissão" (antiga "Especialidade").

Dashboards por Perfil:

Admin: Visualiza estatísticas, gerencia usuários (altera senhas) e agendamentos.

Profissional: Vê seus agendamentos pendentes e pode confirmá-los ou cancelá-los.

Usuário: Vê seus agendamentos e pode criar novos.

Dashboard de Sensores: Uma tela que consome o endpoint /readings do backend para exibir as últimas leituras de múltiplos sensores, atendendo ao requisito da Sprint.

💻 Tecnologias Utilizadas
Backend (API)
Java 17

Spring Boot: Para a estrutura da API RESTful.

Spring Security: Para segurança e autenticação com JWT.

Spring Data JPA (Hibernate): Para persistência de dados.

PostgreSQL: Banco de dados relacional.

Maven: Para gerenciamento de dependências.

Frontend (App)
React Native (Expo)

TypeScript

React Navigation: Para gerenciamento de rotas e navegação.

Context API: Para gerenciamento de estado global (ex: Autenticação).

Styled Components: Para estilização.

Axios (via apiClient): Para as requisições HTTP.

⚙️ Pré-requisitos
Para rodar este projeto, você precisará ter instalado em sua máquina:

Java JDK 17+

Maven 3.x+

Node.js (LTS)

npm ou yarn

npx expo-cli

Uma instância do PostgreSQL rodando (localmente ou em um container).

🚀 Como Executar o Projeto

1. Backend (Spring Boot)
   Configure o Banco de Dados:

Certifique-se de que seu PostgreSQL está rodando.

Crie um banco de dados (ex: postgres).

Abra o arquivo src/main/resources/application.properties.

Configure as propriedades spring.datasource.url, spring.datasource.username e spring.datasource.password com suas credenciais do Postgres. (O padrão atual é postgres/123456).

Delete o Flag de Inicialização (Importante):

Para garantir que o backend popule o banco com os dados atualizados (ex: as novas "Profissões"), delete o arquivo ./data/db_initialized.flag na raiz do projeto backend.

Rode a Aplicação:

Abra a classe ApiMarcacaoConsultasApplication.java.

Inicie a aplicação (pelo "Play" do VS Code ou mvn spring-boot:run).

O backend estará rodando em http://localhost:8080.

2. Frontend (React Native / Expo)
   Instale as Dependências:

Bash

cd pasta-do-frontend
npm install
Configure a URL da API:

Abra o arquivo src/services/api.ts.

Certifique-se de que API_BASE_URL esteja apontando para o seu backend.

Para testes no navegador web ou emulador Android, use: export const API_BASE_URL = "http://localhost:8080";

Para testes no celular físico (Expo Go), use o IP da sua máquina na rede (ex: http://192.168.X.X:8080).

Inicie o App:

Bash

npx expo start
Escaneie o QR Code com o app Expo Go no seu celular, ou aperte w para rodar na web.

🔑 Credenciais de Teste
Você pode usar as seguintes credenciais para testar os diferentes perfis (senha padrão: admin123 ou senha123, dependendo do DataInitializer):

Administrador: admin@clinica.com

Profissional (Ex): carlos.silva@clinica.com

Usuário (Ex): joao.pereira@email.com

As senhas e usuários são criados no arquivo DataInitializer.java do backend.
