package model;

public class Morador {

    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private boolean ativo;
    private int idLote;

    public static final int TAM_MAX_NOME = 100;
    public static final int TAM_MIN_NOME = 3;
    public static final int TAM_MAX_EMAIL = 100;

    public Morador(String nome, String cpf, String telefone, String email, int idLote) throws Exception {
        this.setNome(nome);
        this.setCpf(cpf);
        this.setTelefone(telefone);
        this.setEmail(email);
        this.setIdLote(idLote);
    }

    public boolean getAtivo() {
    	return ativo;
    }
  
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws Exception {
        if (nome == null || nome.trim().isEmpty() 
                || nome.length() < TAM_MIN_NOME 
                || nome.length() > TAM_MAX_NOME) {
            throw new Exception("Tamanho do nome é inválido!");
        }
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new Exception("O CPF não pode ser nulo ou vazio!");
        }

        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new Exception("CPF deve estar no formato XXX.XXX.XXX-XX");
        }

        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) throws Exception {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new Exception("Telefone não pode ser vazio!");
        }
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        this.email = email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) throws Exception {
        this.idLote = idLote;
    }
}