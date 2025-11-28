package model;

public class Funcionario {
	
	private int 	id;
	private String 	nome;
	private String 	cpf;
	private String 	telefone;
	private boolean ativo;
	
	private static final int TAM_MAX_NOME = 100;
	private static final int TAM_MIN_NOME = 3;
	
	
public Funcionario ( String nome, String cpf, String tel ) throws Exception {
		
		this.setNome(nome);
		this.setCpf(cpf);
		this.setTelefone(tel);
		this.setAtivo(true);
		
}

public String getNome() {
	return nome;
}


public void setNome(String nome) throws Exception {
	if (nome == null || nome == "" || nome.length() < TAM_MIN_NOME || nome.length() > TAM_MAX_NOME) {
		
		throw new Exception ("Tamanho do nome é muito pequeno ou muito grande!");
		
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
	
	cpf = cpf.trim();
    cpf = cpf.replaceAll("\\s+", "");
	
	if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
        throw new Exception("CPF deve estar no formato XXX.XXX.XXX-XX");
    }
	
	this.cpf = cpf;
}

public void setAtivo(boolean a) {
	this.ativo = a;
}

public String getTelefone() {
	return telefone;
}

public boolean isAtivo() {
	return ativo;
}

public void setTelefone(String telefone) {
	this.telefone = telefone;
}

public void setId(int id) {
	this.id = id;
}


public int getId () {
	return id;
}

}