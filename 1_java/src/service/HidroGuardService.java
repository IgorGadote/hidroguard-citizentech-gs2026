package service;

import model.AlertaEmergencia;
import model.Cidadao;
import model.RegiaoRisco;
import model.SensorMonitoramento;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HidroGuardService {
    private final List<RegiaoRisco> regioes = new ArrayList<>();
    private final List<Cidadao> cidadaos = new ArrayList<>();
    private final List<SensorMonitoramento> sensores = new ArrayList<>();
    private final List<AlertaEmergencia> alertas = new ArrayList<>();

    private int proximoIdRegiao = 1;
    private int proximoIdCidadao = 1;
    private int proximoIdSensor = 1;
    private int proximoIdAlerta = 1;

    public HidroGuardService() {
        carregarDadosIniciais();
    }

    private void carregarDadosIniciais() {
        RegiaoRisco r1 = cadastrarRegiao("Jardim Pantanal", "São Paulo", 60.0, 45000);
        RegiaoRisco r2 = cadastrarRegiao("Vila Aimoré", "São Paulo", 55.0, 28000);
        RegiaoRisco r3 = cadastrarRegiao("Parque Novo Mundo", "São Paulo", 70.0, 32000);

        cadastrarCidadao("Carlos Eduardo Silva", "123.456.789-01", "(11) 98765-4321", true, r1.getIdRegiao());
        cadastrarCidadao("Maria Fernanda Oliveira", "234.567.890-12", "(11) 97654-3210", false, r1.getIdRegiao());
        cadastrarCidadao("Roberto Almeida Santos", "345.678.901-23", "(11) 96543-2109", true, r2.getIdRegiao());

        SensorMonitoramento s1 = cadastrarSensor("SN-PLUV-001", "Pluviômetro Digital", 68.5, "ATIVO", r1.getIdRegiao());
        SensorMonitoramento s2 = cadastrarSensor("SN-NIV-002", "Sensor Ultrassônico de Nível", 52.0, "ATIVO", r2.getIdRegiao());
        cadastrarSensor("SN-PLUV-003", "Pluviômetro Digital", 15.2, "ATIVO", r3.getIdRegiao());

        registrarAlerta("EMERGENCIA", "Risco iminente de transbordamento. Inicie evacuação preventiva para pontos altos.", r1.getIdRegiao(), s1.getIdSensor());
        registrarAlerta("ALERTA", "Volume de chuva próximo ao limite crítico da bacia. Prepare itens essenciais.", r2.getIdRegiao(), s2.getIdSensor());
    }

    // REGIÃO DE RISCO

    public RegiaoRisco cadastrarRegiao(String nomeBairro, String cidade, double nivelCriticoMm, int populacao) {
        if (nomeBairro == null || nomeBairro.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do bairro não pode ser vazio.");
        }
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da cidade não pode ser vazio.");
        }
        if (nivelCriticoMm <= 0) {
            throw new IllegalArgumentException("O nível crítico de chuva deve ser maior que zero.");
        }
        if (populacao <= 0) {
            throw new IllegalArgumentException("A população estimada deve ser maior que zero.");
        }

        RegiaoRisco nova = new RegiaoRisco(proximoIdRegiao++, nomeBairro.trim(), cidade.trim(), nivelCriticoMm, populacao);
        regioes.add(nova);
        return nova;
    }

    public List<RegiaoRisco> listarRegioes() {
        return new ArrayList<>(regioes);
    }

    public RegiaoRisco buscarRegiaoPorId(int id) {
        for (RegiaoRisco r : regioes) {
            if (r.getIdRegiao() == id) {
                return r;
            }
        }
        return null;
    }

    public boolean atualizarRegiao(int id, String novoBairro, String novaCidade, double novoNivelCritico, int novaPopulacao) {
        RegiaoRisco regiao = buscarRegiaoPorId(id);
        if (regiao == null) {
            return false;
        }
        if (novoBairro == null || novoBairro.trim().isEmpty() || novaCidade == null || novaCidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Bairro e cidade são obrigatórios.");
        }
        if (novoNivelCritico <= 0 || novaPopulacao <= 0) {
            throw new IllegalArgumentException("Nível crítico e população devem ser maiores que zero.");
        }
        regiao.setNomeBairro(novoBairro.trim());
        regiao.setCidade(novaCidade.trim());
        regiao.setNivelCriticoChuvaMm(novoNivelCritico);
        regiao.setPopulacaoEstimada(novaPopulacao);
        return true;
    }

    public boolean removerRegiao(int id) {
        RegiaoRisco regiao = buscarRegiaoPorId(id);
        if (regiao == null) {
            return false;
        }
        for (Cidadao c : cidadaos) {
            if (c.getRegiaoRisco().getIdRegiao() == id) {
                throw new IllegalStateException("Não é possível remover: existem cidadãos vinculados a esta região.");
            }
        }
        for (SensorMonitoramento s : sensores) {
            if (s.getRegiaoRisco().getIdRegiao() == id) {
                throw new IllegalStateException("Não é possível remover: existem sensores vinculados a esta região.");
            }
        }
        return regioes.remove(regiao);
    }

    // CIDADÃO

    public Cidadao cadastrarCidadao(String nome, String cpf, String telefone, boolean mobilidadeReduzida, int idRegiao) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do cidadão é obrigatório.");
        }
        if (cpf == null || cpf.trim().length() < 11) {
            throw new IllegalArgumentException("CPF inválido. Informe ao menos 11 caracteres.");
        }
        for (Cidadao existente : cidadaos) {
            if (existente.getCpf().equalsIgnoreCase(cpf.trim())) {
                throw new IllegalArgumentException("Já existe um cidadão cadastrado com este CPF.");
            }
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone de contato é obrigatório.");
        }
        RegiaoRisco regiao = buscarRegiaoPorId(idRegiao);
        if (regiao == null) {
            throw new IllegalArgumentException("Região de risco não encontrada com o ID informado.");
        }

        Cidadao novo = new Cidadao(proximoIdCidadao++, nome.trim(), cpf.trim(), telefone.trim(), mobilidadeReduzida, regiao);
        cidadaos.add(novo);
        return novo;
    }

    public List<Cidadao> listarCidadaos() {
        return new ArrayList<>(cidadaos);
    }

    public Cidadao buscarCidadaoPorId(int id) {
        for (Cidadao c : cidadaos) {
            if (c.getIdCidadao() == id) {
                return c;
            }
        }
        return null;
    }

    public boolean atualizarCidadao(int id, String novoNome, String novoTelefone, boolean novaMobilidade, int novoIdRegiao) {
        Cidadao cidadao = buscarCidadaoPorId(id);
        if (cidadao == null) {
            return false;
        }
        if (novoNome == null || novoNome.trim().isEmpty() || novoTelefone == null || novoTelefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome e telefone não podem ser vazios.");
        }
        RegiaoRisco novaRegiao = buscarRegiaoPorId(novoIdRegiao);
        if (novaRegiao == null) {
            throw new IllegalArgumentException("Região de risco não encontrada.");
        }
        cidadao.setNome(novoNome.trim());
        cidadao.setTelefone(novoTelefone.trim());
        cidadao.setPossuiMobilidadeReduzida(novaMobilidade);
        cidadao.setRegiaoRisco(novaRegiao);
        return true;
    }

    public boolean removerCidadao(int id) {
        Cidadao cidadao = buscarCidadaoPorId(id);
        if (cidadao == null) {
            return false;
        }
        return cidadaos.remove(cidadao);
    }

    // SENSOR DE MONITORAMENTO

    public SensorMonitoramento cadastrarSensor(String codigoSerie, String tipoSensor, double leituraInicialMm, String status, int idRegiao) {
        if (codigoSerie == null || codigoSerie.trim().isEmpty()) {
            throw new IllegalArgumentException("O código de série do sensor é obrigatório.");
        }
        for (SensorMonitoramento s : sensores) {
            if (s.getCodigoSerie().equalsIgnoreCase(codigoSerie.trim())) {
                throw new IllegalArgumentException("Já existe um sensor com este código de série.");
            }
        }
        if (tipoSensor == null || tipoSensor.trim().isEmpty()) {
            throw new IllegalArgumentException("O tipo do sensor é obrigatório.");
        }
        if (leituraInicialMm < 0) {
            throw new IllegalArgumentException("A leitura de chuva não pode ser negativa.");
        }
        String statusPadrao = (status == null || status.trim().isEmpty()) ? "ATIVO" : status.trim().toUpperCase();
        if (!statusPadrao.equals("ATIVO") && !statusPadrao.equals("MANUTENCAO") && !statusPadrao.equals("INATIVO")) {
            throw new IllegalArgumentException("Status inválido. Use ATIVO, MANUTENCAO ou INATIVO.");
        }
        RegiaoRisco regiao = buscarRegiaoPorId(idRegiao);
        if (regiao == null) {
            throw new IllegalArgumentException("Região de risco não encontrada.");
        }

        SensorMonitoramento novo = new SensorMonitoramento(proximoIdSensor++, codigoSerie.trim(), tipoSensor.trim(), leituraInicialMm, statusPadrao, regiao);
        sensores.add(novo);
        return novo;
    }

    public List<SensorMonitoramento> listarSensores() {
        return new ArrayList<>(sensores);
    }

    public SensorMonitoramento buscarSensorPorId(int id) {
        for (SensorMonitoramento s : sensores) {
            if (s.getIdSensor() == id) {
                return s;
            }
        }
        return null;
    }

    public String atualizarLeituraSensorEVerificarAlerta(int idSensor, double novaLeituraMm) {
        SensorMonitoramento sensor = buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor não encontrado.");
        }
        if (novaLeituraMm < 0) {
            throw new IllegalArgumentException("A leitura em milímetros não pode ser negativa.");
        }
        sensor.setLeituraAtualMm(novaLeituraMm);

        RegiaoRisco regiao = sensor.getRegiaoRisco();
        double limite = regiao.getNivelCriticoChuvaMm();

        if (novaLeituraMm >= limite) {
            AlertaEmergencia alertaGerado = registrarAlerta(
                    "EMERGENCIA",
                    String.format("Leitura de %.2f mm ultrapassou o limite crítico (%.2f mm). Evacuação imediata recomendada!", novaLeituraMm, limite),
                    regiao.getIdRegiao(),
                    sensor.getIdSensor()
            );
            return "ALERTA DE EMERGÊNCIA DISPARADO AUTOMATICAMENTE! (ID Alerta: " + alertaGerado.getIdAlerta() + ")";
        } else if (novaLeituraMm >= limite * 0.8) {
            AlertaEmergencia alertaGerado = registrarAlerta(
                    "ALERTA",
                    String.format("Leitura de %.2f mm atingiu mais de 80%% do limite crítico (%.2f mm). Atenção redobrada!", novaLeituraMm, limite),
                    regiao.getIdRegiao(),
                    sensor.getIdSensor()
            );
            return "AVISO DE ALERTA PREVENTIVO GERADO AUTOMATICAMENTE! (ID Alerta: " + alertaGerado.getIdAlerta() + ")";
        }

        return "Leitura atualizada com sucesso. Volume dentro da faixa de normalidade.";
    }

    public boolean removerSensor(int id) {
        SensorMonitoramento sensor = buscarSensorPorId(id);
        if (sensor == null) {
            return false;
        }
        return sensores.remove(sensor);
    }

    // ALERTA DE EMERGÊNCIA

    public AlertaEmergencia registrarAlerta(String nivelGravidade, String mensagem, int idRegiao, int idSensor) {
        if (nivelGravidade == null || nivelGravidade.trim().isEmpty()) {
            throw new IllegalArgumentException("O nível de gravidade é obrigatório.");
        }
        String nivel = nivelGravidade.trim().toUpperCase();
        if (!nivel.equals("ATENCAO") && !nivel.equals("ALERTA") && !nivel.equals("EMERGENCIA")) {
            throw new IllegalArgumentException("Gravidade inválida. Use ATENCAO, ALERTA ou EMERGENCIA.");
        }
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem de orientação é obrigatória.");
        }
        RegiaoRisco regiao = buscarRegiaoPorId(idRegiao);
        if (regiao == null) {
            throw new IllegalArgumentException("Região de risco não encontrada.");
        }
        SensorMonitoramento sensor = buscarSensorPorId(idSensor);
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor de monitoramento não encontrado.");
        }

        String dataHoraAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        AlertaEmergencia novo = new AlertaEmergencia(proximoIdAlerta++, nivel, mensagem.trim(), dataHoraAtual, regiao, sensor);
        alertas.add(novo);
        return novo;
    }

    public List<AlertaEmergencia> listarAlertas() {
        return new ArrayList<>(alertas);
    }

    public AlertaEmergencia buscarAlertaPorId(int id) {
        for (AlertaEmergencia a : alertas) {
            if (a.getIdAlerta() == id) {
                return a;
            }
        }
        return null;
    }

    public boolean removerAlerta(int id) {
        AlertaEmergencia alerta = buscarAlertaPorId(id);
        if (alerta == null) {
            return false;
        }
        return alertas.remove(alerta);
    }

    public List<Cidadao> listarMoradoresPrioritariosPorRegiao(int idRegiao) {
        List<Cidadao> prioritarios = new ArrayList<>();
        for (Cidadao c : cidadaos) {
            if (c.getRegiaoRisco().getIdRegiao() == idRegiao && c.isPossuiMobilidadeReduzida()) {
                prioritarios.add(c);
            }
        }
        return prioritarios;
    }
}