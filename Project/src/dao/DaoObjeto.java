package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Aluguel;
import model.Lugar;
import model.Objeto;
import util.Conexao;

public class DaoObjeto {
	
    private Connection conexao;
    
    public DaoObjeto() {
    	
        try {
        	conexao = Conexao.getConnection();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }
    
    public List<Objeto> buscarObjetosDoAluguel (Aluguel a) throws Exception {
    	
    	List<Objeto> lista = new ArrayList<Objeto>();
    	
    	Objeto objeto = null;
    	String sql = null;
    	DaoLugar dao = new DaoLugar();
    	
    	sql = "SELECT * FROM objeto, aluguel_objetos WHERE id_aluguel = ? AND objeto.id_objeto = aluguel_objetos.id_objeto";
    	
    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
    		
    		stmt.setInt(1, a.getId());
    		ResultSet rs = stmt.executeQuery();
    		
    		while (rs.next()) {
    			
    			Lugar lugar = dao.consultarPorId(rs.getInt("fk_lugar"));
    			
    			objeto = new Objeto(
				    rs.getString("nome"),
				    lugar
				);
    			
    			objeto.setId(rs.getInt("id_objeto"));
    			objeto.setDisponivel(rs.getBoolean("disponivel"));
    	       	lista.add(objeto);
       	
    		}
    	}
    	
    	return lista;
    	
    }
    
    public List<Objeto> buscarObjetosDoLugar(Lugar lugar) throws Exception {
    	
    	List<Objeto> lista = new ArrayList<Objeto>();
    	
    	Objeto objeto = null;
    	String sql = null;
    	DaoLugar dao = new DaoLugar();
    	
    	sql = "SELECT * FROM objeto WHERE fk_lugar = ? AND disponivel = true";
    	
    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
    		
    		stmt.setInt(1, lugar.getId());
    		ResultSet rs = stmt.executeQuery();
    		
    		while (rs.next()) {
    			
    			lugar = dao.consultarPorId(rs.getInt("fk_lugar"));
    			
    			objeto = new Objeto(
				    rs.getString("nome"),
				    lugar
				);
    			
    			objeto.setId(rs.getInt("id_objeto"));
    			objeto.setDisponivel(rs.getBoolean("disponivel"));
    	       	lista.add(objeto);
       	
    		}
    	}
    	
    	return lista;
    }
    

    public List<Objeto> buscarTodosObjetos() throws Exception {
    	
    	List<Objeto> lista = new ArrayList<Objeto>();
    	
    	Objeto objeto = null;
    	String sql = null;
    	Lugar lugar = null;
    	DaoLugar dao = new DaoLugar();

    	sql = "SELECT * FROM objeto";
    	
    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
    		
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()) {
    			
    			lugar = dao.consultarPorId(rs.getInt("fk_lugar"));
    			
    			objeto = new Objeto(
				    rs.getString("nome"),
				    lugar
				);
    			
    			objeto.setId(rs.getInt("id_objeto"));
    			objeto.setDisponivel(rs.getBoolean("disponivel"));
    	       	lista.add(objeto);
       	
    		}
    	}
    	
    	return lista;
    }


    public void inserir(Objeto objeto) throws SQLException {
        String sql = "INSERT INTO objeto (nome, disponivel, fk_lugar) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, objeto.getNome());
            stmt.setBoolean(2, objeto.isDisponivel());
            stmt.setInt(3, objeto.getLugar().getId());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Objeto objeto) throws SQLException {
        String sql = "UPDATE objeto SET nome = ?, disponivel = ?, fk_lugar = ? WHERE id_objeto = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, objeto.getNome());
            stmt.setBoolean(2, objeto.isDisponivel());
            stmt.setInt(3, objeto.getLugar().getId());
            stmt.setInt(4, objeto.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM objeto WHERE id_objeto = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Objeto consultarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM objeto WHERE id_objeto = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	
            	// Recupera objeto Lugar
            	DaoLugar daoL = new DaoLugar();
            	Lugar lugar = daoL.consultarPorId(rs.getInt("fk_lugar"));
            	
            	
                Objeto objeto = null;
				try {
					objeto = new Objeto(rs.getString("nome"),lugar);
					objeto.setId(rs.getInt("id_objeto"));
					objeto.setDisponivel(rs.getBoolean("disponivel"));
					
				} catch (SQLException e) {
					e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

                return objeto;
            }
            
        }
        
        return null;
    }
}
