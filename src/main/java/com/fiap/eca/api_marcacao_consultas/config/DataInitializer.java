package com.fiap.eca.api_marcacao_consultas.config;

import com.fiap.eca.api_marcacao_consultas.model.Consulta;
import com.fiap.eca.api_marcacao_consultas.model.Especialidade;
import com.fiap.eca.api_marcacao_consultas.model.Usuario;
import com.fiap.eca.api_marcacao_consultas.repository.ConsultaRepository;
import com.fiap.eca.api_marcacao_consultas.repository.EspecialidadeRepository;
import com.fiap.eca.api_marcacao_consultas.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PersistenceContext
    private EntityManager entityManager;

    // Nome do arquivo que será usado como flag para indicar que a inicialização já
    // foi feita
    private static final String INIT_FLAG_FILE = "./data/db_initialized.flag";

    @Bean
    @Transactional
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            EspecialidadeRepository especialidadeRepository,
            ConsultaRepository consultaRepository) {

        return args -> {
            // Verifica se o arquivo de flag existe
            File flagFile = new File(INIT_FLAG_FILE);

            if (flagFile.exists()) {
                System.out.println("Banco de dados já foi inicializado anteriormente. Pulando inicialização.");
                return;
            }

            // Limpar o banco de dados antes de inserir novos dados
            System.out.println("Limpando banco de dados...");
            consultaRepository.deleteAllInBatch();
            usuarioRepository.deleteAllInBatch();
            especialidadeRepository.deleteAllInBatch();

            // Resetar sequências de ID (específico para H2)
            try {
                entityManager.createNativeQuery("ALTER TABLE consultas ALTER COLUMN id RESTART WITH 1").executeUpdate();
                entityManager.createNativeQuery("ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 1").executeUpdate();
                entityManager.createNativeQuery("ALTER TABLE especialidades ALTER COLUMN id RESTART WITH 1")
                        .executeUpdate();
            } catch (Exception e) {
                System.out.println(
                        "Aviso: Não foi possível resetar as sequências de ID. Isso é normal na primeira execução.");
            }

            System.out.println("Inicializando banco de dados com dados de exemplo...");

            // MUDANÇA 1: Trocando as especialidades pelas profissões
            List<Especialidade> especialidades = new ArrayList<>();
            String[] nomesEspecialidades = {
                    "Engenheiro de Software", "Arquiteto", "Conferente", "Mesário", "Operador de Máquina",
                    "Assistente de Operação", "QA", "DevOps", "Assistente Administrativo", "Auxiliar de Produção"
            };

            for (String nome : nomesEspecialidades) {
                Especialidade especialidade = new Especialidade();
                especialidade.setNome(nome);
                especialidades.add(especialidade);
            }

            especialidadeRepository.saveAll(especialidades);
            System.out.println("Profissões (Especialidades) criadas: " + especialidades.size());

            // Criando usuário admin
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@clinica.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setTipo("ADMIN");
            usuarioRepository.save(admin);
            System.out.println("Administrador criado");

            // MUDANÇA 2: Trocando os nomes e especialidades dos "médicos"
            // (Vamos manter o 'tipo' como "MEDICO" pro app continuar funcionando)
            List<Usuario> medicos = new ArrayList<>();
            Map<String, Usuario> medicosPorEspecialidade = new HashMap<>();

            String[][] dadosMedicos = {
                    // Removemos o "Dr."/"Dra." e atualizamos a profissão
                    { "Carlos Silva", "carlos.silva@clinica.com", "senha123", "MEDICO", "Engenheiro de Software" },
                    { "Ana Oliveira", "ana.oliveira@clinica.com", "senha123", "MEDICO", "Arquiteto" },
                    { "Roberto Santos", "roberto.santos@clinica.com", "senha123", "MEDICO", "Conferente" },
                    { "Juliana Costa", "juliana.costa@clinica.com", "senha123", "MEDICO", "Mesário" },
                    { "Marcelo Lima", "marcelo.lima@clinica.com", "senha123", "MEDICO", "Operador de Máquina" },
                    { "Patricia Mendes", "patricia.mendes@clinica.com", "senha123", "MEDICO", "Assistente de Operação" },
                    { "Ricardo Ferreira", "ricardo.ferreira@clinica.com", "senha123", "MEDICO", "QA" },
                    { "Camila Rodrigues", "camila.rodrigues@clinica.com", "senha123", "MEDICO", "DevOps" },
                    { "Felipe Alves", "felipe.alves@clinica.com", "senha123", "MEDICO", "Assistente Administrativo" },
                    { "Beatriz Santos", "beatriz.santos@clinica.com", "senha123", "MEDICO", "Auxiliar de Produção" }
            };

            for (String[] dados : dadosMedicos) {
                Usuario medico = new Usuario();
                medico.setNome(dados[0]);
                medico.setEmail(dados[1]);
                medico.setSenha(passwordEncoder.encode(dados[2]));
                medico.setTipo(dados[3]); // Mantém "MEDICO"
                medico.setEspecialidade(dados[4]); // Agora é a profissão
                medicos.add(medico);
                medicosPorEspecialidade.put(dados[4], medico);
            }

            usuarioRepository.saveAll(medicos);
            System.out.println("Usuários (tipo Médico) criados: " + medicos.size());

            // Criando pacientes
            List<Usuario> pacientes = new ArrayList<>();
            String[][] dadosPacientes = {
                    { "João Pereira", "joao.pereira@email.com", "senha123", "PACIENTE" },
                    { "Maria Souza", "maria.souza@email.com", "senha123", "PACIENTE" },
                    { "Pedro Almeida", "pedro.almeida@email.com", "senha123", "PACIENTE" },
                    { "Lucia Ferreira", "lucia.ferreira@email.com", "senha123", "PACIENTE" },
                    { "Fernando Gomes", "fernando.gomes@email.com", "senha123", "PACIENTE" },
                    { "Camila Dias", "camila.dias@email.com", "senha123", "PACIENTE" },
                    { "Rafael Martins", "rafael.martins@email.com", "senha123", "PACIENTE" },
                    { "Amanda Rocha", "amanda.rocha@email.com", "senha123", "PACIENTE" },
                    { "Bruno Castro", "bruno.castro@email.com", "senha123", "PACIENTE" },
                    { "Carla Mendes", "carla.mendes@email.com", "senha123", "PACIENTE" }
            };

            for (String[] dados : dadosPacientes) {
                Usuario paciente = new Usuario();
                paciente.setNome(dados[0]);
                paciente.setEmail(dados[1]);
                paciente.setSenha(passwordEncoder.encode(dados[2]));
                paciente.setTipo(dados[3]);
                pacientes.add(paciente);
            }

            usuarioRepository.saveAll(pacientes);
            System.out.println("Pacientes criados: " + pacientes.size());

            // MUDANÇA 3: Atualizando as consultas de exemplo para usarem as novas profissões
            List<Consulta> consultas = new ArrayList<>();

            // Dados para consultas: paciente, profissão, data/hora, observação
            Object[][] dadosConsultas = {
                    { 0, "Engenheiro de Software", LocalDateTime.now().plusDays(2).withHour(9).withMinute(0),
                            "Revisão de código" },
                    { 1, "Arquiteto", LocalDateTime.now().plusDays(3).withHour(14).withMinute(30),
                            "Discussão de novo projeto" },
                    { 2, "Conferente", LocalDateTime.now().plusDays(4).withHour(10).withMinute(15),
                            "Verificação de estoque" },
                    { 3, "Mesário", LocalDateTime.now().plusDays(5).withHour(16).withMinute(0),
                            "Treinamento de urna" },
                    { 4, "Operador de Máquina", LocalDateTime.now().plusDays(7).withHour(11).withMinute(45),
                            "Manutenção preventiva" },
                    { 5, "Assistente de Operação", LocalDateTime.now().plusDays(8).withHour(8).withMinute(30),
                            "Revisão de planilhas" },
                    { 6, "QA", LocalDateTime.now().plusDays(9).withHour(15).withMinute(0),
                            "Planejamento de testes" },
                    { 7, "DevOps", LocalDateTime.now().plusDays(10).withHour(13).withMinute(15),
                            "Configuração de pipeline" },
                    { 8, "Assistente Administrativo", LocalDateTime.now().plusDays(12).withHour(17).withMinute(30),
                            "Organização de arquivos" },
                    { 9, "Auxiliar de Produção", LocalDateTime.now().plusDays(14).withHour(10).withMinute(0),
                            "Acompanhamento de linha" }
            };

            // Criando consultas com pacientes, "médicos" e profissões correspondentes
            for (Object[] dados : dadosConsultas) {
                int pacienteIndex = (int) dados[0];
                String especialidadeNome = (String) dados[1];
                LocalDateTime dataHora = (LocalDateTime) dados[2];
                String observacao = (String) dados[3];

                Consulta consulta = new Consulta();
                consulta.setUsuario(pacientes.get(pacienteIndex));
                consulta.setMedico(medicosPorEspecialidade.get(especialidadeNome));
                consulta.setEspecialidade(especialidadeNome);
                consulta.setDataHora(dataHora);
                consulta.setStatus("AGENDADA");
                consulta.setObservacao(observacao);

                consultas.add(consulta);
            }

            consultaRepository.saveAll(consultas);
            System.out.println("Consultas criadas: " + consultas.size());

            // Criar o arquivo de flag para indicar que a inicialização foi concluída
            criarArquivoFlag();

            System.out.println("Inicialização de dados concluída com sucesso!");
        };
    }

    /**
     * Cria um arquivo de flag para indicar que a inicialização do banco de dados
     * foi concluída
     */
    private void criarArquivoFlag() {
        try {
            // Garantir que o diretório existe
            Path diretorio = Paths.get("./data");
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }

            // Criar o arquivo de flag
            Path flagPath = Paths.get(INIT_FLAG_FILE);
            Files.createFile(flagPath);
            Files.write(flagPath, ("Banco de dados inicializado em: " +
                    java.time.LocalDateTime.now().toString()).getBytes());

            System.out.println("Arquivo de flag criado. O banco não será reinicializado nas próximas execuções.");
        } catch (IOException e) {
            System.err.println("Aviso: Não foi possível criar o arquivo de flag: " + e.getMessage());
            System.err.println("O banco de dados pode ser reinicializado na próxima execução.");
        }
    }
}