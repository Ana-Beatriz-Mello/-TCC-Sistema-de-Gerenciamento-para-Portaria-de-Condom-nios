package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Lugar;
import util.Conexao;

public class DaoLugar {

    private Connection conexao;
    
    public DaoLugar() {
    	
        try {
        	conexao = Conexao.getConnection();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }
    
    public ArrayList<Lugar> buscarTodosLugares() throws SQLException {
    	
    	ArrayList<Lugar> lugares = new ArrayList<Lugar>();
    	String sql = "SELECT * FROM lugar";
        Lugar lugar = null;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	try {
					lugar = new Lugar(
					rs.getString("nome"),
					rs.getString("descricao"),
					rs.getInt("max_horas"),
					rs.getInt("preco")
            );
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
                lugar.setId(rs.getInt("id_lugar"));
                lugares.add(lugar);
            }
        }
        return lugares;
    	
    }

    public void inserir(Lugar lugar) throws SQLException {
        String sql = "INSERT INTO lugar (nome, descricao, max_horas, preco) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, lugar.getNome());
            stmt.setString(2, lugar.getDescricao());
            stmt.setInt(3, lugar.getMaxHoras());
            stmt.setInt(4, lugar.getPreco());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Lugar lugar) throws SQLException {
        String sql = "UPDATE lugar SET nome = ?, descricao = ?, max_horas = ?, preco = ? WHERE id_lugar = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, lugar.getNome());
            stmt.setString(2, lugar.getDescricao());
            stmt.setInt(3, lugar.getMaxHoras());
            stmt.setInt(4, lugar.getPreco());
            stmt.setInt(5, lugar.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM lugar WHERE id_lugar = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Lugar consultarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM lugar WHERE id_lugar = ?";
        Lugar lugar = null;
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	try {
					lugar = new Lugar(
					rs.getString("nome"),
					rs.getString("descricao"),
					rs.getInt("max_horas"),
					rs.getInt("preco")
            );
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
                lugar.setId(rs.getInt("id_lugar"));
                return lugar;
            }
        }
        return null;
    }
}