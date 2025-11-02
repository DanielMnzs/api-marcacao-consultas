Murilo Pomin rm:99683
Gabriel Taboada rm:97957
Daniel Menezes rm:551398
Luiz Augusto Melki rm:552053
Pedro Martins rm:98663

# Hub FESTO - Aplicativo de Agendamento

Este é um projeto fullstack (React Native + Spring Boot) para um sistema de agendamento de serviços de software. A plataforma permite que usuários agendem horários com profissionais de TI (como Engenheiros de Software, QAs, DevOps) e administradores gerenciem a plataforma.

## ✨ Funcionalidades Principais

- **Sistema de Autenticação:** Login e Cadastro de novos usuários.
- **Controle de Perfil (Roles):** O sistema se divide em 3 tipos de usuários:
  - **Usuário (Paciente):** Pode ver seus agendamentos, criar novos agendamentos e gerenciar seu perfil.
  - **Profissional (Médico):** Pode ver o painel com os agendamentos destinados a ele.
  - **Administrador:** Tem um painel com estatísticas de uso e um módulo de gerenciamento de todos os usuários da plataforma.
- **Agendamento de Serviços:** O usuário pode selecionar uma data, horário e um profissional com base na sua "especialidade" (ex: Engenheiro de Software, QA).

## 🚀 Tecnologias Utilizadas

### Backend (API)

- **Java 17+**
- **Spring Boot:** Framework principal para a API REST.
- **Spring Security:** Para autenticação e autorização via **JWT** (JSON Web Tokens).
- **Spring Data JPA (Hibernate):** Para persistência de dados.
- **H2 Database (em modo arquivo):** Banco de dados leve para desenvolvimento e testes.

### Frontend

- **React Native (Expo):** Para a interface do usuário (web e mobile).
- **TypeScript:** Para tipagem estática e código mais robusto.
- **React Navigation:** Para o gerenciamento de rotas e telas.
- **Styled Components:** Para estilização dos componentes.
- **Context API:** Para gerenciamento de estado global (ex: Autenticação).

## 🏁 Como Rodar o Projeto

Este projeto é dividido em duas partes: `backend` (API) e `frontend` (App).

### 1. Backend (API Spring Boot)

O backend é responsável por servir os dados e gerenciar a autenticação.

1.  Abra o projeto backend (ex: `api-marcacao-consultas`) na sua IDE Java (IntelliJ, VS Code, etc.).
2.  Inicie o projeto. O Spring Boot irá rodar o servidor, por padrão, na porta **`8080`**.
3.  O backend usa um banco H2 que salva os dados em arquivos locais.

> **⚠️ Atenção: Resetando o Banco de Dados**
> O backend possui um inicializador de dados (`DataInitializer.java`) que popula o banco com usuários e profissões de exemplo **apenas na primeira vez** que ele é executado.
>
> Se você precisar "resetar" o banco para que os dados de exemplo sejam criados novamente, você precisa:
>
> 1.  Parar o servidor backend.
> 2.  Encontrar a pasta `data` que foi criada no **diretório onde você executou o Java** (ex: `C:\Users\Daniel\Documents\this\this\data`).
> 3.  **Apagar** essa pasta `data`.
> 4.  Iniciar o backend novamente.

### 2. Frontend (App React Native)

O frontend é o aplicativo que o usuário vê e interage.

1.  Abra a pasta do projeto frontend no seu terminal.
2.  Instale as dependências:
    ```bash
    npm install
    ```
3.  Verifique se o `API_BASE_URL` no arquivo `src/services/api.ts` está apontando para o seu backend (ex: `http://localhost:8080`).
4.  Inicie o servidor de desenvolvimento do Expo:
    ```bash
    npm start
    ```
5.  O Expo abrirá uma aba no navegador. Pressione a tecla `w` para iniciar o aplicativo no modo "Web".
6.  O frontend provavelmente irá rodar na porta **`8081`** (se a `8080` já estiver em uso pelo backend).

## 🔑 Credenciais de Teste

Você pode usar os seguintes usuários (criados pelo `DataInitializer.java`) para testar os diferentes perfis:

- **Administrador:**

  - **Email:** `admin@clinica.com`
  - **Senha:** `admin123`

- **Profissional (Exemplo):**

  - **Email:** `carlos.silva@clinica.com` (Engenheiro de Software)
  - **Senha:** `senha123`

- **Usuário (Exemplo):Pos**
  - **Email:** `joao.pereira@email.com`
  - **Senha:** `senha123`
