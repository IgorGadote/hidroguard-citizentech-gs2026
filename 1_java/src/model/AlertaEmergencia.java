package model;

public class AlertaEmergencia {
    private int idAlerta;
    private String nivelGravidade;
    private String mensagemOrientacao;
    private String dataHoraEmissao;
    private RegiaoRisco regiaoAfetada;
    private SensorMonitoramento sensorOrigem;

    public AlertaEmergencia() {
    }

    public AlertaEmergencia(int idAlerta, String nivelGravidade, String mensagemOrientacao, String dataHoraEmissao, RegiaoRisco regiaoAfetada, SensorMonitoramento sensorOrigem) {
        this.idAlerta = idAlerta;
        this.nivelGravidade = nivelGravidade;
        this.mensagemOrientacao = mensagemOrientacao;
        this.dataHoraEmissao = dataHoraEmissao;
        this.regiaoAfetada = regiaoAfetada;
        this.sensorOrigem = sensorOrigem;
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public String getNivelGravidade() {
        return nivelGravidade;
    }

    public void setNivelGravidade(String nivelGravidade) {
        this.nivelGravidade = nivelGravidade;
    }

    public String getMensagemOrientacao() {
        return mensagemOrientacao;
    }

    public void setMensagemOrientacao(String mensagemOrientacao) {
        this.mensagemOrientacao = mensagemOrientacao;
    }

    public String getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    public void setDataHoraEmissao(String dataHoraEmissao) {
        this.dataHoraEmissao = dataHoraEmissao;
    }

    public RegiaoRisco getRegiaoAfetada() {
        return regiaoAfetada;
    }

    public void setRegiaoAfetada(RegiaoRisco regiaoAfetada) {
        this.regiaoAfetada = regiaoAfetada;
    }

    public SensorMonitoramento getSensorOrigem() {
        return sensorOrigem;
    }

    public void setSensorOrigem(SensorMonitoramento sensorOrigem) {
        this.sensorOrigem = sensorOrigem;
    }

    @Override
    public String toString() {
        String bairro = (regiaoAfetada != null) ? regiaoAfetada.getNomeBairro() : "Desconhecido";
        String serieSensor = (sensorOrigem != null) ? sensorOrigem.getCodigoSerie() : "Manual";
        return String.format("ID: %d | [%s] %s | Bairro: %s | Sensor: %s | Mensagem: %s",
                idAlerta, nivelGravidade, dataHoraEmissao, bairro, serieSensor, mensagemOrientacao);
    }
}