package model;

public class SensorMonitoramento {
    private int idSensor;
    private String codigoSerie;
    private String tipoSensor;
    private double leituraAtualMm;
    private String statusOperacional;
    private RegiaoRisco regiaoRisco;

    public SensorMonitoramento() {
    }

    public SensorMonitoramento(int idSensor, String codigoSerie, String tipoSensor, double leituraAtualMm, String statusOperacional, RegiaoRisco regiaoRisco) {
        this.idSensor = idSensor;
        this.codigoSerie = codigoSerie;
        this.tipoSensor = tipoSensor;
        this.leituraAtualMm = leituraAtualMm;
        this.statusOperacional = statusOperacional;
        this.regiaoRisco = regiaoRisco;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public String getCodigoSerie() {
        return codigoSerie;
    }

    public void setCodigoSerie(String codigoSerie) {
        this.codigoSerie = codigoSerie;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public double getLeituraAtualMm() {
        return leituraAtualMm;
    }

    public void setLeituraAtualMm(double leituraAtualMm) {
        this.leituraAtualMm = leituraAtualMm;
    }

    public String getStatusOperacional() {
        return statusOperacional;
    }

    public void setStatusOperacional(String statusOperacional) {
        this.statusOperacional = statusOperacional;
    }

    public RegiaoRisco getRegiaoRisco() {
        return regiaoRisco;
    }

    public void setRegiaoRisco(RegiaoRisco regiaoRisco) {
        this.regiaoRisco = regiaoRisco;
    }

    @Override
    public String toString() {
        String nomeBairro = (regiaoRisco != null) ? regiaoRisco.getNomeBairro() : "Sem Região";
        return String.format("ID: %d | Série: %s | Tipo: %s | Leitura: %.2f mm | Status: %s | Bairro: %s",
                idSensor, codigoSerie, tipoSensor, leituraAtualMm, statusOperacional, nomeBairro);
    }
}