package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import model.Encomenda;
import model.Funcionario;
import model.Morador;
import util.Conexao;

public class DaoEncomenda {
	
	
    private Connection conexao;
    
    public DaoEncomenda() {
    	
        try {
        	conexao = Conexao.getConnection();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    	
    }
	
    public void receberEncomenda(Encomenda e, Morador m) throws Exception {
    	
    	e.receber(m);
    	atualizar(e);
    	
    }
    
    public ArrayList<Encomenda> buscarEncomendasDeMorador (String cpf) throws Exception {
    	
    	ArrayList<Encomenda> lista = new ArrayList<Encomenda>();
    	
    	
    	String sql = "SELECT * FROM encomenda WHERE id_recebido IS NULL AND id_destinatario IN (SELECT lotexmorador.fk_lote FROM lotexmorador, morador WHERE morador.id_morador = lotexmorador.fk_morador AND cpf_morador = ?)";

    	Encomenda e = null;
    	Morador morador = null;
    	LocalDateTime horaBuscada = null;

    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

    		stmt.setString(1, cpf);
    		ResultSet rs = stmt.executeQuery();          

    		while (rs.next()) {
       	
    			// Recupera o objeto funcionário para o constructor
    			DaoFuncionario daoFunc = new DaoFuncionario();
    			Funcionario recebedor = daoFunc.buscarPorId(rs.getInt("id_recebedor"));
           
    			// Recupera o objeto morador que recebeu
    			if (rs.getInt("id_recebido") != 0) {
    				DaoMorador daoMorador = new DaoMorador();
    				morador = daoMorador.consultarPorId(rs.getInt("id_recebido"));
    			}
           
    			// Recupera hora do recebimento
    			if (rs.getTimestamp("recolhimento") != null) 
    				horaBuscada = rs.getTimestamp("recolhimento").toLocalDateTime();

           
    			e = new Encomenda(
    				 rs.getString("cod_rastreio"),
                     rs.getString("cod_identificador"),
                     rs.getInt("id_destinatario"),
                     recebedor
    			);
    			e.setId(rs.getInt("id_encomenda"));
    			e.setRecebidoPor(morador);
    			e.setRecebimento(rs.getTimestamp("recebimento").toLocalDateTime());
    			e.setRecolhimento(horaBuscada);
           
    			lista.add(e);
    			
    		}
    		
    	}
    	
    	return lista;
    	
    }
	
    public ArrayList<Encomenda> buscarEncomendasDeUmLote (int lote) throws Exception {
    	
    	ArrayList<Encomenda> lista = new ArrayList<Encomenda>();
    	
    	
    	String sql = "SELECT id_encomenda, cod_rastreio, cod_identificador, recebimento, " +
                "id_recebedor, id_destinatario, id_recebido, recolhimento " +
                "FROM encomenda WHERE id_destinatario = ? AND recolhimento IS NULL";
    	Encomenda e = null;
    	Morador morador = null;
    	LocalDateTime horaBuscada = null;

    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

    		stmt.setInt(1, lote);
    		ResultSet rs = stmt.executeQuery();          

    		while (rs.next()) {
       	
    			// Recupera o objeto funcionário para o constructor
    			DaoFuncionario daoFunc = new DaoFuncionario();
    			Funcionario recebedor = daoFunc.buscarPorId(rs.getInt("id_recebedor"));
           
    			// Recupera o objeto morador que recebeu
    			if (rs.getInt("id_recebido") != 0) {
    				DaoMorador daoMorador = new DaoMorador();
    				morador = daoMorador.consultarPorId(rs.getInt("id_recebido"));
    			}
           
    			// Recupera hora do recebimento
    			if (rs.getTimestamp("recolhimento") != null) 
    				horaBuscada = rs.getTimestamp("recolhimento").toLocalDateTime();

           
    			e = new Encomenda(
    				 rs.getString("cod_rastreio"),
                     rs.getString("cod_identificador"),
                     rs.getInt("id_destinatario"),
                     recebedor
    			);
    			e.setId(rs.getInt("id_encomenda"));
    			e.setRecebidoPor(morador);
    			e.setRecebimento(rs.getTimestamp("recebimento").toLocalDateTime());
    			e.setRecolhimento(horaBuscada);
           
    			lista.add(e);
    			
    		}
    		
    	}
    		
    	return lista;
    	
    }
	
	// Operações básicas
    public void inserir(Encomenda e) throws Exception {
        String sql = "INSERT INTO encomenda (cod_rastreio, cod_identificador, recebimento, " +
                     "id_recebedor, id_destinatario) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, e.getCodRastreio());
            stmt.setString(2, e.getCodIdentificador());
            stmt.setTimestamp(3, Timestamp.valueOf(e.getRecebimento()));
            stmt.setInt(4, e.getRecebedor().getId());
            stmt.setInt(5, e.getDestinatario());
            stmt.executeUpdate();
        }
    }

    public Encomenda buscarPorId(int id) throws Exception {
        String sql = "SELECT id_encomenda, cod_rastreio, cod_identificador, recebimento, " +
                     "id_recebedor, id_destinatario, id_recebido, recolhimento " +
                     "FROM encomenda WHERE id_encomenda = ?";
        Encomenda e = null;
        Morador morador = null;
        LocalDateTime horaBuscada = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();          

            if (rs.next()) {
            	
            	// Recupera o objeto funcionário para o constructor
            	DaoFuncionario daoFunc = new DaoFuncionario();
                Funcionario recebedor = daoFunc.buscarPorId(rs.getInt("id_recebedor"));
                
                // Recupera o objeto morador que recebeu
                if (rs.getInt("id_recebido") != 0) {
                	DaoMorador daoMorador = new DaoMorador();
                	morador = daoMorador.consultarPorId(rs.getInt("id_recebido"));
                }
                
                // Recupera hora do recebimento
                if (rs.getTimestamp("recolhimento") != null) 
                	horaBuscada = rs.getTimestamp("recolhimento").toLocalDateTime();

                
                e = new Encomenda(
                        rs.getString("cod_rastreio"),
                        rs.getString("cod_identificador"),
                        rs.getInt("id_destinatario"),
                        recebedor
                );
                e.setId(rs.getInt("id_encomenda"));
                e.setRecebidoPor(morador);
                e.setRecebimento(rs.getTimestamp("recebimento").toLocalDateTime());
                e.setRecolhimento(horaBuscada);
            }
        }

        return e;
    }

    public void atualizar(Encomenda e) throws Exception {
        String sql = "UPDATE encomenda SET cod_rastreio = ?, cod_identificador = ?, recebimento = ?, " +
                     "id_recebedor = ?, id_destinatario = ?, id_recebido = ?, recolhimento = ? " +
                     "WHERE id_encomenda = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, e.getCodRastreio());
            stmt.setString(2, e.getCodIdentificador());
            stmt.setTimestamp(3, Timestamp.valueOf(e.getRecebimento()));
            stmt.setInt(4, e.getRecebedor().getId());
            stmt.setInt(5, e.getDestinatario());

            if (e.getRecebidoPor().getId() != 0)
                stmt.setInt(6, e.getRecebidoPor().getId());
            else
                stmt.setNull(6, java.sql.Types.INTEGER);

            if (e.getRecolhimento() != null)
                stmt.setTimestamp(7, Timestamp.valueOf(e.getRecolhimento()));
            else
                stmt.setNull(7, java.sql.Types.TIMESTAMP);

            stmt.setInt(8, e.getId());

            stmt.executeUpdate();
        }
    }
    
    public void excluir(int id) throws Exception {
        String sql = "DELETE FROM encomenda WHERE id_encomenda = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
