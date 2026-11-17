package main;

import model.AlertaEmergencia;
import model.Cidadao;
import model.RegiaoRisco;
import model.SensorMonitoramento;
import service.HidroGuardService;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final HidroGuardService service = new HidroGuardService();

    public static void main(String[] args) {
        int opcao = -1;
        System.out.println("==================================================================");
        System.out.println("   HIDROGUARD - SISTEMA COMUNITÁRIO DE ALERTA DE ENCHENTES");
        System.out.println("   Global Solution 2026/2 - CitizenTech (ODS 11 & ODS 13)");
        System.out.println("==================================================================");

        while (opcao != 0) {
            exibirMenuPrincipal();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1:
                        menuRegioes();
                        break;
                    case 2:
                        menuCidadaos();
                        break;
                    case 3:
                        menuSensores();
                        break;
                    case 4:
                        menuAlertas();
                        break;
                    case 5:
                        consultarResgatePrioritario();
                        break;
                    case 0:
                        System.out.println("\nEncerrando o sistema HidroGuard. Até logo!");
                        break;
                    default:
                        System.out.println("\n[ERRO] Opção inválida! Digite um número entre 0 e 5.");
                }
            } catch (Exception e) {
                System.out.println("\n[ATENÇÃO - VALIDAÇÃO] " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n------------------------- MENU PRINCIPAL -------------------------");
        System.out.println("1. Gerenciar Regiões de Risco (Cadastrar, Listar, Buscar, Atualizar, Remover)");
        System.out.println("2. Gerenciar Cidadãos (Cadastrar, Listar, Buscar, Atualizar, Remover)");
        System.out.println("3. Gerenciar Sensores e Simular Leitura Pluviométrica");
        System.out.println("4. Gerenciar Alertas de Emergência");
        System.out.println("5. Listar Cidadãos com Mobilidade Reduzida por Região (Apoio Defesa Civil)");
        System.out.println("0. Sair");
        System.out.println("------------------------------------------------------------------");
    }

    // SUBMENU 1: REGIÕES

    private static void menuRegioes() {
        System.out.println("\n--- GERENCIAR REGIÕES DE RISCO ---");
        System.out.println("1. Cadastrar nova região");
        System.out.println("2. Listar todas as regiões");
        System.out.println("3. Buscar região por ID");
        System.out.println("4. Atualizar região");
        System.out.println("5. Remover região (Desafio Extra)");
        int sub = lerInteiro("Opção: ");

        switch (sub) {
            case 1:
                String bairro = lerTexto("Nome do Bairro: ");
                String cidade = lerTexto("Cidade: ");
                double nivel = lerDouble("Nível crítico de chuva (mm): ");
                int pop = lerInteiro("População estimada: ");
                RegiaoRisco nova = service.cadastrarRegiao(bairro, cidade, nivel, pop);
                System.out.println("[SUCESSO] Região cadastrada: " + nova);
                break;
            case 2:
                List<RegiaoRisco> lista = service.listarRegioes();
                System.out.println("\n--- LISTA DE REGIÕES DE RISCO ---");
                for (RegiaoRisco r : lista) {
                    System.out.println(r);
                }
                break;
            case 3:
                int idBusca = lerInteiro("Informe o ID da região: ");
                RegiaoRisco encontrada = service.buscarRegiaoPorId(idBusca);
                if (encontrada != null) {
                    System.out.println("Região localizada -> " + encontrada);
                } else {
                    System.out.println("[AVISO] Nenhuma região encontrada com o ID " + idBusca);
                }
                break;
            case 4:
                int idAtu = lerInteiro("Informe o ID da região a atualizar: ");
                String novoBairro = lerTexto("Novo Nome do Bairro: ");
                String novaCidade = lerTexto("Nova Cidade: ");
                double novoNivel = lerDouble("Novo Nível Crítico (mm): ");
                int novaPop = lerInteiro("Nova População Estimada: ");
                if (service.atualizarRegiao(idAtu, novoBairro, novaCidade, novoNivel, novaPop)) {
                    System.out.println("[SUCESSO] Região atualizada com sucesso!");
                } else {
                    System.out.println("[AVISO] Região não encontrada.");
                }
                break;
            case 5:
                int idRem = lerInteiro("Informe o ID da região a remover: ");
                if (service.removerRegiao(idRem)) {
                    System.out.println("[SUCESSO] Região removida com sucesso!");
                } else {
                    System.out.println("[AVISO] Região não encontrada.");
                }
                break;
            default:
                System.out.println("[ERRO] Opção inválida no submenu.");
        }
    }

    // SUBMENU 2: CIDADÃOS

    private static void menuCidadaos() {
        System.out.println("\n--- GERENCIAR CIDADÃOS ---");
        System.out.println("1. Cadastrar cidadão");
        System.out.println("2. Listar todos os cidadãos");
        System.out.println("3. Buscar cidadão por ID");
        System.out.println("4. Atualizar dados do cidadão");
        System.out.println("5. Remover cidadão (Desafio Extra)");
        int sub = lerInteiro("Opção: ");

        switch (sub) {
            case 1:
                String nome = lerTexto("Nome completo: ");
                String cpf = lerTexto("CPF (ex: 111.222.333-44): ");
                String tel = lerTexto("Telefone / WhatsApp: ");
                boolean mobilidade = lerSimNao("Possui mobilidade reduzida? (S/N): ");
                int idReg = lerInteiro("ID da Região de Risco onde reside: ");
                Cidadao novo = service.cadastrarCidadao(nome, cpf, tel, mobilidade, idReg);
                System.out.println("[SUCESSO] Cidadão cadastrado: " + novo);
                break;
            case 2:
                List<Cidadao> lista = service.listarCidadaos();
                System.out.println("\n--- LISTA DE CIDADÃOS CADASTRADOS ---");
                for (Cidadao c : lista) {
                    System.out.println(c);
                }
                break;
            case 3:
                int idBusca = lerInteiro("Informe o ID do cidadão: ");
                Cidadao c = service.buscarCidadaoPorId(idBusca);
                if (c != null) {
                    System.out.println("Cidadão localizado -> " + c);
                } else {
                    System.out.println("[AVISO] Cidadão não encontrado.");
                }
                break;
            case 4:
                int idAtu = lerInteiro("ID do cidadão a atualizar: ");
                String novoNome = lerTexto("Novo nome completo: ");
                String novoTel = lerTexto("Novo telefone: ");
                boolean novaMob = lerSimNao("Possui mobilidade reduzida? (S/N): ");
                int novaReg = lerInteiro("Novo ID da Região de Risco: ");
                if (service.atualizarCidadao(idAtu, novoNome, novoTel, novaMob, novaReg)) {
                    System.out.println("[SUCESSO] Cadastro do cidadão atualizado!");
                } else {
                    System.out.println("[AVISO] Cidadão não encontrado.");
                }
                break;
            case 5:
                int idRem = lerInteiro("ID do cidadão a remover: ");
                if (service.removerCidadao(idRem)) {
                    System.out.println("[SUCESSO] Cidadão removido da base!");
                } else {
                    System.out.println("[AVISO] Cidadão não encontrado.");
                }
                break;
            default:
                System.out.println("[ERRO] Opção inválida no submenu.");
        }
    }

    //SUBMENU 3: SENSORES

    private static void menuSensores() {
        System.out.println("\n--- GERENCIAR SENSORES DE MONITORAMENTO ---");
        System.out.println("1. Cadastrar novo sensor");
        System.out.println("2. Listar sensores instalados");
        System.out.println("3. Buscar sensor por ID");
        System.out.println("4. Atualizar leitura de chuva em sensor (Gera Alerta Automático se crítico)");
        System.out.println("5. Remover sensor (Desafio Extra)");
        int sub = lerInteiro("Opção: ");

        switch (sub) {
            case 1:
                String serie = lerTexto("Código de Série (ex: SN-PLUV-004): ");
                String tipo = lerTexto("Tipo do Sensor (ex: Pluviômetro Digital): ");
                double leitura = lerDouble("Leitura inicial (mm): ");
                String status = lerTexto("Status Operacional (ATIVO / MANUTENCAO / INATIVO): ");
                int idReg = lerInteiro("ID da Região de Risco: ");
                SensorMonitoramento novo = service.cadastrarSensor(serie, tipo, leitura, status, idReg);
                System.out.println("[SUCESSO] Sensor cadastrado: " + novo);
                break;
            case 2:
                List<SensorMonitoramento> lista = service.listarSensores();
                System.out.println("\n--- LISTA DE SENSORES ---");
                for (SensorMonitoramento s : lista) {
                    System.out.println(s);
                }
                break;
            case 3:
                int idBusca = lerInteiro("Informe o ID do sensor: ");
                SensorMonitoramento s = service.buscarSensorPorId(idBusca);
                if (s != null) {
                    System.out.println("Sensor localizado -> " + s);
                } else {
                    System.out.println("[AVISO] Sensor não encontrado.");
                }
                break;
            case 4:
                int idSensor = lerInteiro("Informe o ID do sensor para nova leitura: ");
                double novaLeitura = lerDouble("Nova leitura registrada (mm): ");
                String resultado = service.atualizarLeituraSensorEVerificarAlerta(idSensor, novaLeitura);
                System.out.println("[RESULTADO DO MONITORAMENTO] " + resultado);
                break;
            case 5:
                int idRem = lerInteiro("Informe o ID do sensor a remover: ");
                if (service.removerSensor(idRem)) {
                    System.out.println("[SUCESSO] Sensor removido!");
                } else {
                    System.out.println("[AVISO] Sensor não encontrado.");
                }
                break;
            default:
                System.out.println("[ERRO] Opção inválida no submenu.");
        }
    }

    // SUBMENU 4: ALERTAS

    private static void menuAlertas() {
        System.out.println("\n--- GERENCIAR ALERTAS DE EMERGÊNCIA ---");
        System.out.println("1. Emitir alerta manual");
        System.out.println("2. Listar histórico de alertas");
        System.out.println("3. Buscar alerta por ID");
        System.out.println("4. Remover alerta (Desafio Extra)");
        int sub = lerInteiro("Opção: ");

        switch (sub) {
            case 1:
                String gravidade = lerTexto("Nível de Gravidade (ATENCAO / ALERTA / EMERGENCIA): ");
                String msg = lerTexto("Mensagem de orientação aos moradores: ");
                int idReg = lerInteiro("ID da Região Afetada: ");
                int idSen = lerInteiro("ID do Sensor de Origem: ");
                AlertaEmergencia novo = service.registrarAlerta(gravidade, msg, idReg, idSen);
                System.out.println("[SUCESSO] Alerta emitido: " + novo);
                break;
            case 2:
                List<AlertaEmergencia> lista = service.listarAlertas();
                System.out.println("\n--- HISTÓRICO DE ALERTAS EMITIDOS ---");
                for (AlertaEmergencia a : lista) {
                    System.out.println(a);
                }
                break;
            case 3:
                int idBusca = lerInteiro("Informe o ID do alerta: ");
                AlertaEmergencia a = service.buscarAlertaPorId(idBusca);
                if (a != null) {
                    System.out.println("Alerta localizado -> " + a);
                } else {
                    System.out.println("[AVISO] Alerta não encontrado.");
                }
                break;
            case 4:
                int idRem = lerInteiro("Informe o ID do alerta a remover: ");
                if (service.removerAlerta(idRem)) {
                    System.out.println("[SUCESSO] Alerta removido!");
                } else {
                    System.out.println("[AVISO] Alerta não encontrado.");
                }
                break;
            default:
                System.out.println("[ERRO] Opção inválida no submenu.");
        }
    }

    // OPÇÃO 5: APOIO RESGATE PRIORITÁRIO 

    private static void consultarResgatePrioritario() {
        int idRegiao = lerInteiro("\nInforme o ID da Região de Risco para listar moradores prioritários: ");
        RegiaoRisco regiao = service.buscarRegiaoPorId(idRegiao);
        if (regiao == null) {
            System.out.println("[AVISO] Região não encontrada.");
            return;
        }
        List<Cidadao> prioritarios = service.listarMoradoresPrioritariosPorRegiao(idRegiao);
        System.out.println("\n--- MORADORES COM MOBILIDADE REDUZIDA EM: " + regiao.getNomeBairro().toUpperCase() + " ---");
        if (prioritarios.isEmpty()) {
            System.out.println("Nenhum morador com mobilidade reduzida registrado nesta região.");
        } else {
            for (Cidadao c : prioritarios) {
                System.out.println(c);
            }
        }
    }

    // MÉTODOS AUXILIARES DE LEITURA SEGURA

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("[ENTRADA INVÁLida] Digite apenas números inteiros válidos.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("[ENTRADA INVÁLIDA] Digite um valor numérico válido (ex: 60.5).");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();
            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("[ENTRADA INVÁLIDA] Este campo não pode ficar em branco.");
        }
    }

    private static boolean lerSimNao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim().toUpperCase();
            if (entrada.equals("S") || entrada.equals("SIM")) {
                return true;
            } else if (entrada.equals("N") || entrada.equals("NAO") || entrada.equals("NÃO")) {
                return false;
            }
            System.out.println("[ENTRADA INVÁLIDA] Responda apenas com 'S' para Sim ou 'N' para Não.");
        }
    }
}