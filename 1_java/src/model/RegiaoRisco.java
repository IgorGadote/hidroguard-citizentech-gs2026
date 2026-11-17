package model;

public class RegiaoRisco {
    private int idRegiao;
    private String nomeBairro;
    private String cidade;
    private double nivelCriticoChuvaMm;
    private int populacaoEstimada;

    public RegiaoRisco() {
    }

    public RegiaoRisco(int idRegiao, String nomeBairro, String cidade, double nivelCriticoChuvaMm, int populacaoEstimada) {
        this.idRegiao = idRegiao;
        this.nomeBairro = nomeBairro;
        this.cidade = cidade;
        this.nivelCriticoChuvaMm = nivelCriticoChuvaMm;
        this.populacaoEstimada = populacaoEstimada;
    }

    public int getIdRegiao() {
        return idRegiao;
    }

    public void setIdRegiao(int idRegiao) {
        this.idRegiao = idRegiao;
    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public void setNomeBairro(String nomeBairro) {
        this.nomeBairro = nomeBairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public double getNivelCriticoChuvaMm() {
        return nivelCriticoChuvaMm;
    }

    public void setNivelCriticoChuvaMm(double nivelCriticoChuvaMm) {
        this.nivelCriticoChuvaMm = nivelCriticoChuvaMm;
    }

    public int getPopulacaoEstimada() {
        return populacaoEstimada;
    }

    public void setPopulacaoEstimada(int populacaoEstimada) {
        this.populacaoEstimada = populacaoEstimada;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Bairro: %s (%s) | Limite Crítico: %.2f mm | População: %d hab.",
                idRegiao, nomeBairro, cidade, nivelCriticoChuvaMm, populacaoEstimada);
    }
}