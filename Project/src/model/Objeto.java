package model;

public class Objeto {

    private int id;
    private String nome;
    private boolean disponivel;
    private Lugar lugar;

    public static final int TAM_MAX_NOME = 60;
    public static final int TAM_MIN_NOME = 1;

    public Objeto(String nome, Lugar lugar) throws Exception {
        this.setNome(nome);
        this.setDisponivel(true);
        this.setLugar(lugar);
    }

    
    public int getId() {
        return id;
    }
    
    public String getNomeLugar() {
    	return this.lugar.getNome();
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws Exception {
    	
        if (nome == null || nome.trim().isEmpty() || nome.length() < TAM_MIN_NOME || nome.length() > TAM_MAX_NOME) {
        	
            throw new Exception("Nome do objeto inválido!");
            
        }
        
        this.nome = nome;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) throws Exception {
        if (lugar == null) {
        	
            throw new Exception("Objeto precisa estar vinculado a um Lugar!");
            
        }
        this.lugar = lugar;
    }
}
