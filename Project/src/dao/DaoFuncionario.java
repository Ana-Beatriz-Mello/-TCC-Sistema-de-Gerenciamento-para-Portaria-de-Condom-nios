package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.Funcionario;
import util.Conexao;
import util.GestorHash;

public class DaoFuncionario {


	private Connection conexao;
    
    public DaoFuncionario() {
    	
        try {
        	conexao = Conexao.getConnection();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }
    
    public List<Funcionario> buscarTodosFuncionarios() throws Exception {
    	
    	List<Funcionario> lista = new ArrayList<Funcionario>();
    	Funcionario funcionario = null;
    	String sql = null;
    	
    	sql = "SELECT * FROM funcionario";
    	
    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
    		
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()) {
    			funcionario = new Funcionario(
				    rs.getString("nome"),
				    rs.getString("cpf_funcionario"),
				    rs.getString("telefone")
				);
    			funcionario.setId(rs.getInt("id_funcionario"));
    			funcionario.setAtivo(rs.getBoolean("ativo"));
    	       	lista.add(funcionario);
       	
    		}
    	}
    	
    	return lista;
    	
    }
    
    
    public boolean setSenhaDeUsuario(Funcionario f, String senha) {
    	
   	 String sql = "UPDATE usuario SET senha_hash = ? WHERE id_funcionario = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, senha);
            stmt.setInt(2, f.getId());
            stmt.executeUpdate();
            return true;
        }
        catch (Exception e) {
       	 e.printStackTrace();
       	 return false;
        }
   	
   	
   }
    
    public String buscarUsuarioRelacionado(Funcionario funcionario) {
    	String senha = null;
    	
    	if (funcionario == null)
    		return null;
    	
    	String sql = "SELECT senha_hash FROM usuario WHERE id_funcionario = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        	stmt.setInt(1, funcionario.getId());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            senha = rs.getString("senha_hash");
        }
		catch (Exception e) {
			return null;
		}
        
        return senha;
    	
    }
    
	
    public void inserir(Funcionario f) throws Exception {
    	
    	if (consultarPorCpf(f.getCpf()) != null) {
    		throw new Exception("Já existe um funcionário com esse CPF.");
    	}
    	
        String sql = "INSERT INTO funcionario (nome, cpf_funcionario, telefone, ativo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getTelefone());
            stmt.setBoolean(4, f.isAtivo());
            stmt.executeUpdate();

        }
        
        // Refaz o objeto funcionário com o Id gerado no banco de dados
        f = consultarPorCpf (f.getCpf());
        
        // Todo funcionário recebe um usuário
        sql = "INSERT INTO usuario (id_funcionario, senha_hash) VALUES (?, ?)";
        
        // Pegando o hash para a senha de primeiro acesso
        String senhaGenerica = "Condominio@gestao";
        GestorHash hash = new GestorHash();
        String senhaHash = hash.gerarHashSHA256(senhaGenerica);
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, f.getId());
            stmt.setString(2, senhaHash);
            stmt.executeUpdate();
        }
        
    }
    
    
    public Funcionario consultarPorCpf(String cpf) throws Exception {
        String sql = "SELECT id_funcionario, nome, cpf_funcionario, telefone " +
                     "FROM funcionario WHERE cpf_funcionario = ?";
        Funcionario funcionario = null;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                funcionario = new Funcionario(
                		rs.getString("nome"),
                        rs.getString("cpf_funcionario"),
                        rs.getString("telefone")
                );
                funcionario.setId(rs.getInt("id_funcionario"));
            }
        }
        return funcionario; // retorna null se não encontrar por que funcionario é inicializado como null
    }



    public Funcionario buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM funcionario WHERE id_funcionario = ?";
        Funcionario f = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                f = new Funcionario(
                        rs.getString("nome"),
                        rs.getString("cpf_funcionario"),
                        rs.getString("telefone")
                );
                f.setId(rs.getInt("id_funcionario"));
                f.setAtivo(rs.getBoolean("ativo"));
            }
        }

        return f;
    }

    public void atualizar(Funcionario f) throws Exception {
        String sql = "UPDATE funcionario SET nome = ?, cpf_funcionario = ?, telefone = ?, ativo = ? WHERE id_funcionario = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getCpf());
            stmt.setString(3, f.getTelefone());
            stmt.setBoolean(4, f.isAtivo());
            stmt.setInt(5, f.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM funcionario WHERE id_funcionario = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}