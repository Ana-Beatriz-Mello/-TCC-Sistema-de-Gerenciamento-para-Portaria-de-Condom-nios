package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Aluguel;
import model.Morador;
import model.Objeto;
import model.Lugar;
import util.Conexao;

public class DaoAluguel {

    private Connection conexao;

    public DaoAluguel() {
    	
        try {
        	
            conexao = Conexao.getConnection();
            
        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
        
    }
    
    public boolean checarAluguelNoMesmoHorario (Aluguel a) throws Exception {
    	
    	// Essa query vai checar se já tem algum aluguel cadastrado no mesmo local e horário
    	// que o aluguel informado na chamada da função
    	
    	String sql = " SELECT * FROM aluguel " +
    				 " WHERE id_lugar = ? " +
    				 " AND (hora_comeco,hora_fim_previsto) " +
    				 " OVERLAPS (?::TIMESTAMP, ?::TIMESTAMP)";


    	try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
    		stmt.setInt(1, a.getLugar().getId());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(a.getHoraComeco()));
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(a.getHoraFimPrevisto()));
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
            	return true;
            }
            else {
            	return false;
            }
            
    	}
            
    }
    
    public List<int[]> objetosPerdidos () throws Exception {
    	/* 
    	 * Esse método retorna uma list contendo vetores relacionando os objetos
    	 * que não foram devolvidos com os aluguéis que não os devovleram
    	*/
    	
    	String sql = " SELECT DISTINCT ON (x.id_objeto) x.id_objeto, x.id_aluguel " +   
                	 " FROM 	           aluguel a, objeto o, aluguel_objetos x " +
                	 " WHERE 		       a.id_aluguel = x.id_aluguel 			  " +
                	 " AND 		           o.id_objeto = x.id_objeto              " +
                	 " AND 		           a.hora_termino IS NOT NULL             " +
                	 " AND                 o.disponivel = FALSE					  " +
                	 " ORDER BY            x.id_objeto, a.hora_termino DESC;      ";
        
    	
        List<int[]> lista = new ArrayList<int[]>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	int[] aux = new int[2];
            	aux[0] = rs.getInt("id_objeto");
            	aux[1] = rs.getInt("id_aluguel");
            	
            	lista.add(aux);
            }
        }
                
    	return lista;
    	
    }
    
    public List<Aluguel> buscarAluguelParaFinalizar () throws Exception {
    	
    	String sql = "SELECT * FROM aluguel WHERE hora_fim_previsto <= CURRENT_DATE + INTERVAL '1 DAY' AND hora_termino IS NULL";
        
    	Aluguel aluguel = null;
    	Morador morador = null;
    	Lugar lugar = null;
    	DaoMorador dm = new DaoMorador();
    	DaoLugar daoLugar = new DaoLugar();
    	
    	
        List<Aluguel> lista = new ArrayList<Aluguel>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	
            	morador = dm.consultarPorId(rs.getInt("id_responsavel"));
            	lugar = daoLugar.consultarPorId(rs.getInt("id_lugar"));
            	
            	aluguel = new Aluguel (
            			morador,
            			lugar,
            			rs.getTimestamp("hora_comeco").toLocalDateTime(),
                        rs.getTimestamp("hora_fim_previsto") != null ? rs.getTimestamp("hora_fim_previsto").toLocalDateTime() : null
            	);
            	aluguel.setId(rs.getInt("id_aluguel"));
            	aluguel.setHoraTermino(rs.getTimestamp("hora_termino") != null ? rs.getTimestamp("hora_termino").toLocalDateTime() : null);
            	lista.add(aluguel);
            }
        }
                
    	return lista;
    	
    	
    }

    public List<Aluguel> buscarAluguelPorMes (Lugar lugar, LocalDateTime mes) throws Exception {
    	
    	String sql = "SELECT * FROM aluguel WHERE id_lugar = ? AND DATE_TRUNC('month', hora_comeco) = DATE_TRUNC('month', ?::timestamp);";
       
    	Aluguel aluguel = null;
    	Morador morador = null;
    	DaoMorador dm = new DaoMorador();
    	
    	
        List<Aluguel> lista = new ArrayList<Aluguel>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, lugar.getId());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(mes));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	
            	morador = dm.consultarPorId(rs.getInt("id_responsavel"));
            	
            	aluguel = new Aluguel (
            			morador,
            			lugar,
            			rs.getTimestamp("hora_comeco").toLocalDateTime(),
                        rs.getTimestamp("hora_fim_previsto") != null ? rs.getTimestamp("hora_fim_previsto").toLocalDateTime() : null
            	);
            	aluguel.setId(rs.getInt("id_aluguel"));
            	aluguel.setHoraTermino(rs.getTimestamp("hora_termino") != null ? rs.getTimestamp("hora_termino").toLocalDateTime() : null);
            	lista.add(aluguel);
            }
        }
                
    	return lista;
    }

    // Será chamado quando o condômino finaliza o aluguel, devolvendo os objetos que pegou emprestado
    public void finalizarAluguel(Aluguel aluguel, List<Objeto> objetosDevolvidos) throws Exception {
        String sqlUpdateAluguel = "UPDATE aluguel SET hora_termino = ? WHERE id_aluguel = ?";
        
        // Só atualiza objetos se eles existirem para o aluguel que está sendo finalizado
        if (objetosDevolvidos == null || !objetosDevolvidos.isEmpty()) {
        	String sqlUpdateObjeto  = "UPDATE objeto SET disponivel = TRUE WHERE id_objeto = ?";
        	
        	try (PreparedStatement stmtObjeto  = conexao.prepareStatement(sqlUpdateObjeto)) {
        			// Libera disponibilidade dos objetos devolvidos
                    for (Objeto obj : objetosDevolvidos) {
                        stmtObjeto.setInt(1, obj.getId());
                        stmtObjeto.executeUpdate();
                        obj.setDisponivel(true);
                    }
        	}
    }
        
        
        // Finaliza o aluguel
        try (
            PreparedStatement stmtAluguel = conexao.prepareStatement(sqlUpdateAluguel);
        ) {
            // Finaliza o aluguel no model (captura a hora atual automaticamente)
            aluguel.finalizar();

            // Atualiza a hora de término no banco
            stmtAluguel.setTimestamp(1, Timestamp.valueOf(aluguel.getHoraTermino()));
            stmtAluguel.setInt(2, aluguel.getId());
            stmtAluguel.executeUpdate();
            
            }
        }

    
    // Será chamado quando um condômino for buscar as "chaves" do lugar e
    // os objetos que for usar
    public void adicionarObjetosAoAluguel(int idAluguel, List<Objeto> objetos) throws Exception {
        String sqlInsert = "INSERT INTO aluguel_objetos (id_aluguel, id_objeto) VALUES (?, ?)";
        String sqlUpdate = "UPDATE objeto SET disponivel = FALSE WHERE id_objeto = ?";
     
        try (
            PreparedStatement stmtInsert = conexao.prepareStatement(sqlInsert);
            PreparedStatement stmtUpdate = conexao.prepareStatement(sqlUpdate)
        ) {
            for (Objeto obj : objetos) {
                
                stmtInsert.setInt(1, idAluguel);
                stmtInsert.setInt(2, obj.getId());
                stmtInsert.executeUpdate();

                // Atualiza disponibilidade do objeto
                stmtUpdate.setInt(1, obj.getId());
                stmtUpdate.executeUpdate();
            }
        }
    }
    
    public List<Aluguel> buscarAlugueisDoDia () throws Exception {
    	
    	String sql = "SELECT * FROM aluguel WHERE DATE(hora_comeco) = CURRENT_DATE AND hora_termino IS NULL;";
        
    	Aluguel aluguel = null;
    	Morador morador = null;
    	Lugar lugar = null;
    	DaoMorador dm = new DaoMorador();
    	DaoLugar dl = new DaoLugar();   	
    	
        List<Aluguel> lista = new ArrayList<Aluguel>();

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
            	
            	morador = dm.consultarPorId(rs.getInt("id_responsavel"));
            	lugar = dl.consultarPorId(rs.getInt("id_lugar"));
            	
            	aluguel = new Aluguel (
            			morador,
            			lugar,
            			rs.getTimestamp("hora_comeco").toLocalDateTime(),
                        rs.getTimestamp("hora_fim_previsto") != null ? rs.getTimestamp("hora_fim_previsto").toLocalDateTime() : null
            	);
            	aluguel.setId(rs.getInt("id_aluguel"));
            	aluguel.setHoraTermino(rs.getTimestamp("hora_termino") != null ? rs.getTimestamp("hora_termino").toLocalDateTime() : null);
            	lista.add(aluguel);
            }
        }
                
    	return lista;
    	
    }
    
    
    // Operações básicas
    public void inserir(Aluguel a) throws Exception {
        String sql = "INSERT INTO aluguel (id_responsavel, id_lugar, hora_comeco, hora_fim_previsto, hora_termino) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, a.getResponsavel().getId());
            stmt.setInt(2, a.getLugar().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(a.getHoraComeco()));
            
            if (a.getHoraFimPrevisto() != null)
                stmt.setTimestamp(4, Timestamp.valueOf(a.getHoraFimPrevisto()));
            else
                stmt.setNull(4, java.sql.Types.TIMESTAMP);

            if (a.getHoraTermino() != null)
                stmt.setTimestamp(5, Timestamp.valueOf(a.getHoraTermino()));
            else
                stmt.setNull(5, java.sql.Types.TIMESTAMP);

            stmt.executeUpdate();
        }
        
    }

    public Aluguel buscarPorId(int id) throws Exception {
        String sql = "SELECT * FROM aluguel WHERE id_aluguel = ?";
        Aluguel aluguel = null;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                DaoMorador daoMorador = new DaoMorador();
                Morador responsavel = daoMorador.consultarPorId(rs.getInt("id_responsavel"));

                DaoLugar daoLugar = new DaoLugar();
                Lugar lugar = daoLugar.consultarPorId(rs.getInt("id_lugar"));

                aluguel = new Aluguel(
                    responsavel,
                    lugar,
                    rs.getTimestamp("hora_comeco").toLocalDateTime(),
                    rs.getTimestamp("hora_fim_previsto") != null ? rs.getTimestamp("hora_fim_previsto").toLocalDateTime() : null
                );
                aluguel.setId(rs.getInt("id_aluguel"));
                aluguel.setHoraTermino(rs.getTimestamp("hora_termino") != null ? rs.getTimestamp("hora_termino").toLocalDateTime() : null);
            }
        }

        return aluguel;
    }

    public void atualizar(Aluguel a) throws Exception {
        String sql = "UPDATE aluguel SET id_responsavel = ?, id_lugar = ?, hora_comeco = ?, hora_fim_previsto = ?, hora_termino = ? " +
                     "WHERE id_aluguel = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, a.getResponsavel().getId());
            stmt.setInt(2, a.getLugar().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(a.getHoraComeco()));

            if (a.getHoraFimPrevisto() != null)
                stmt.setTimestamp(4, Timestamp.valueOf(a.getHoraFimPrevisto()));
            else
                stmt.setNull(4, java.sql.Types.TIMESTAMP);

            if (a.getHoraTermino() != null)
                stmt.setTimestamp(5, Timestamp.valueOf(a.getHoraTermino()));
            else
                stmt.setNull(5, java.sql.Types.TIMESTAMP);

            stmt.setInt(6, a.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws Exception {

        String sql = "DELETE FROM aluguel WHERE id_aluguel = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
