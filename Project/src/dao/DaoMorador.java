package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Morador;
import util.Conexao;
import util.GestorHash;

public class DaoMorador {
	
	
    private Connection conexao;
    
    public DaoMorador() {
    	
        try {
        	conexao = Conexao.getConnection();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }
    
    public List<Morador> buscarTodosMoradores (boolean todos) throws Exception {
    	
    	List<Morador> lista = new ArrayList<Morador>();
    	Morador morador = null;
    	String sql = null;
    	
    	if (todos) {
    		sql = "SELECT * " +
				  "FROM morador";
    	}
    	else {
    		sql = "SELECT * " +
    			  "FROM morador WHERE ativo = true";
    	}
    	
    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
    		
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()) {
    			morador = new Morador(
				    rs.getString("nome"),
				    rs.getString("cpf_morador"),
				    rs.getString("telefone"),
				    rs.getString("email"),
				    rs.getInt("fk_lote")
				);
    			morador.setId(rs.getInt("id_morador"));
    	       	morador.setAtivo(rs.getBoolean("ativo"));
    	       	lista.add(morador);
       	
       }
   }
   return lista;
    	
    }
    
    // Retorna lista de lotes aos quais um certo morador tem permissão de buscar encomendas
    public ArrayList<Integer> lotesComPermissao (Morador m) {
    	
    	if (m == null)
    		return null;
    	
    	ArrayList<Integer> lista = new ArrayList<Integer>();
    	
    	String sql = "SELECT fk_lote FROM lotexmorador WHERE fk_morador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        	stmt.setInt(1, m.getId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	
            	lista.add(rs.getInt("fk_lote"));
            	
            }
            
        }
		catch (Exception e) {
			e.printStackTrace();
		}
        
        return lista;
    	
    	
    }
    
    public boolean setSenhaDeUsuario(Morador morador, String senha) {
    	
    	 String sql = "UPDATE usuario SET senha_hash = ? WHERE id_morador = ?";
         try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
             stmt.setString(1, senha);
             stmt.setInt(2, morador.getId());
             stmt.executeUpdate();
             return true;
         }
         catch (Exception e) {
        	 e.printStackTrace();
        	 return false;
         }
    	
    	
    }
    
    // Busca a senha correspondente ao morador com um CPF x
    public String buscarUsuarioRelacionado(Morador morador) {
    	String senha = null;
    	
    	if (morador == null)
    		return null;
    	
    	String sql = "SELECT senha_hash FROM usuario WHERE id_morador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        	stmt.setInt(1, morador.getId());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            senha = rs.getString("senha_hash");
        }
		catch (Exception e) {
			e.printStackTrace();
		}
        
        return senha;
    	
    }
	
    // Essa função permite que um morador tenha permissão de receber encomendas para
    // um certo lote
    public void concederPermissao(Morador morador, int idLote) {
    	
    	String sql = "INSERT INTO lotexmorador (fk_lote, fk_morador) VALUES (?, ?)";
    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, idLote);
            stmt.setInt(2, morador.getId());
            stmt.executeUpdate();
        }
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void retirarPermissao(Morador morador, int idLote) throws Exception {
    	// Excessão agora é feita na própria tela
    	/*if (idLote == morador.getIdLote()) {
    		throw new Exception ("Não pode retirar a permissão de um morador.");
    	}*/ 
    		
    	String sql = "DELETE FROM lotexmorador WHERE fk_lote = ? AND fk_morador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        	stmt.setInt(1, idLote);
            stmt.setInt(2, morador.getId());
            stmt.executeUpdate();
    	}
    }
    
    // Retorna todos os lotes no sistema
    public List<Integer> buscaLotes() {
    	List<Integer> lotes = new ArrayList<Integer>();
    	
    	String sql = "SELECT id_lote FROM lote";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
        	ResultSet rs = stmt.executeQuery();
            
        	while (rs.next()) {
                lotes.add(rs.getInt("id_lote"));
            }
        	
        	
        }
		catch (Exception e) {
			e.printStackTrace();
		}
        
        return lotes;
        
    }
    
    // Pega o número do lote de um morador
    public String buscarNumLote(Morador morador) {
    	
    	String sql = "SELECT id_lote FROM lote WHERE id_morador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, morador.getId());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString("id_lote");
        }
		catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
        
    }

    
    
    
    
    
    // OPERAÇÕES BÁSICAS
    public void inserir(Morador morador) throws SQLException, Exception {
    	
    	if (consultarPorCpf(morador.getCpf()) != null) {
            throw new Exception("Já existe um morador com esse CPF.");
        }
    	
        String sql = "INSERT INTO morador (nome, cpf_morador, telefone, email, ativo, fk_lote) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, morador.getNome());
            stmt.setString(2, morador.getCpf());
            stmt.setString(3, morador.getTelefone());
            stmt.setString(4, morador.getEmail());
            stmt.setBoolean(5, morador.getAtivo());
            stmt.setInt(6, morador.getIdLote());
            stmt.executeUpdate();
        }
        
        // Refaz o objeto morador com o Id gerado no banco de dados
        morador = consultarPorCpf (morador.getCpf());
        
        // Todo morador possui permissão para buscar encomendas do lote onde mora
        concederPermissao(morador, morador.getIdLote());
        
        // Todo morador recebe um usuário
        sql = "INSERT INTO usuario (id_morador, senha_hash) VALUES (?, ?)";
        
        // Pegando o hash para a senha de primeiro acesso
        String senhaGenerica = "Condominio@gestao";
        GestorHash hash = new GestorHash();
        String senhaHash = hash.gerarHashSHA256(senhaGenerica);
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, morador.getId());
            stmt.setString(2, senhaHash);
            stmt.executeUpdate();
        }
        
        
    }


    public void atualizar(Morador morador) throws SQLException {
        String sql = "UPDATE morador SET nome = ?, cpf_morador = ?, telefone = ?, email = ?, ativo = ?, fk_lote = ? WHERE id_morador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, morador.getNome());
            stmt.setString(2, morador.getCpf());
            stmt.setString(3, morador.getTelefone());
            stmt.setString(4, morador.getEmail());
            stmt.setBoolean(5, morador.isAtivo());
            stmt.setInt(6, morador.getIdLote());
            stmt.setInt(7, morador.getId());
            stmt.executeUpdate();
            
        }
    }

    public Morador consultarPorCpf(String cpf) throws Exception {
        String sql = "SELECT id_morador, nome, cpf_morador, telefone, email, ativo, fk_lote " +
                     "FROM morador WHERE cpf_morador = ?";
        Morador morador = null;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	morador = new Morador(
					    rs.getString("nome"),
					    rs.getString("cpf_morador"),
					    rs.getString("telefone"),
					    rs.getString("email"),
					    rs.getInt("fk_lote")
					);
            	morador.setId(rs.getInt("id_morador"));
            	morador.setAtivo(rs.getBoolean("ativo"));
            }
        }
        return morador; // Retornará null se não existir, pois foi inicializado como null
    }
 
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM morador WHERE id_morador = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    public Morador consultarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM morador WHERE id_morador = ?";
        Morador morador = null;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
				try {
					morador = new Morador(
					    rs.getString("nome"),
					    rs.getString("cpf_morador"),
					    rs.getString("telefone"),
					    rs.getString("email"),
					    rs.getInt("fk_lote")
					);
	                morador.setId(rs.getInt("id_morador"));
	                morador.setAtivo(rs.getBoolean("ativo"));
				} catch (SQLException e) {
					
					e.printStackTrace();
					
				} catch (Exception e) {
				
					e.printStackTrace();
				}
				
                return morador;
            }
            
        }
        
        return null;
        
    }
    
}
