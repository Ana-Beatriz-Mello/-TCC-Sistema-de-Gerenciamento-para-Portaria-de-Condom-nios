package model;

public class Lugar {

    private int id;
    private String nome;
    private String descricao;
    private int maxHoras;
    private int preco;

    public static final int TAM_MAX_NOME = 100;
    public static final int TAM_MIN_NOME = 3;
    public static final int TAM_MAX_DESCRICAO = 1000;

    public Lugar(String nome, String descricao, int maxHoras, int preco) throws Exception {
        this.setNome(nome);
        this.setDescricao(descricao);
        this.setMaxHoras(maxHoras);
        this.setPreco(preco);
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
        if (nome == null || nome.trim().isEmpty() || nome.length() < TAM_MIN_NOME || nome.length() > TAM_MAX_NOME) {
        	
            throw new Exception("Nome do lugar inválido!");
            
        }
        
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) throws Exception {
        if (descricao != null && descricao.length() > TAM_MAX_DESCRICAO) {
            throw new Exception("Descrição muito longa! Máximo " + TAM_MAX_DESCRICAO + " caracteres.");
        }
        this.descricao = descricao;
    }

    public int getMaxHoras() {
        return maxHoras;
    }

    public void setMaxHoras(int maxHoras) throws Exception {
        if (maxHoras <= 0) {
            throw new Exception("Máximo de horas deve ser maior que zero!");
        }
        this.maxHoras = maxHoras;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) throws Exception {
        if (preco < 0) {
            throw new Exception("Preço não pode ser negativo!");
        }
        this.preco = preco;
    }
}
