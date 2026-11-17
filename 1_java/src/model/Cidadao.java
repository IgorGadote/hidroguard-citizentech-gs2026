package model;

public class Cidadao {
    private int idCidadao;
    private String nome;
    private String cpf;
    private String telefone;
    private boolean possuiMobilidadeReduzida;
    private RegiaoRisco regiaoRisco;

    public Cidadao() {
    }

    public Cidadao(int idCidadao, String nome, String cpf, String telefone, boolean possuiMobilidadeReduzida, RegiaoRisco regiaoRisco) {
        this.idCidadao = idCidadao;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.possuiMobilidadeReduzida = possuiMobilidadeReduzida;
        this.regiaoRisco = regiaoRisco;
    }

    public int getIdCidadao() {
        return idCidadao;
    }

    public void setIdCidadao(int idCidadao) {
        this.idCidadao = idCidadao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isPossuiMobilidadeReduzida() {
        return possuiMobilidadeReduzida;
    }

    public void setPossuiMobilidadeReduzida(boolean possuiMobilidadeReduzida) {
        this.possuiMobilidadeReduzida = possuiMobilidadeReduzida;
    }

    public RegiaoRisco getRegiaoRisco() {
        return regiaoRisco;
    }

    public void setRegiaoRisco(RegiaoRisco regiaoRisco) {
        this.regiaoRisco = regiaoRisco;
    }

    @Override
    public String toString() {
        String mobilidade = possuiMobilidadeReduzida ? "SIM (Prioritário)" : "NÃO";
        String nomeBairro = (regiaoRisco != null) ? regiaoRisco.getNomeBairro() : "Sem Região";
        return String.format("ID: %d | Nome: %s | CPF: %s | Tel: %s | Mobilidade Reduzida: %s | Bairro: %s",
                idCidadao, nome, cpf, telefone, mobilidade, nomeBairro);
    }
}
