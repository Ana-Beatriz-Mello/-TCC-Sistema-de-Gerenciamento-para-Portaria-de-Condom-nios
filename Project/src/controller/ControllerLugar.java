package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import dao.DaoLugar;
import model.Lugar;

public class ControllerLugar {

    private DaoLugar dao;

    public ControllerLugar() {
        dao = new DaoLugar();
    }
    
    public ArrayList<Lugar> buscarTodosLugares() throws SQLException {
    	
    	return dao.buscarTodosLugares();
    	
    }

    public void incluirLugar(Lugar lugar) throws Exception {
        if (lugar == null) {
            throw new Exception("Lugar inválido!");
        }

        if (lugar.getNome() == null || lugar.getNome().trim().isEmpty()) {
            throw new Exception("Nome do lugar não pode ser vazio!");
        }

        dao.inserir(lugar);
    }

    public void atualizarLugar(Lugar lugar) throws Exception {
        if (lugar == null || lugar.getId() == 0) {
            throw new Exception("Lugar inválido para atualização!");
        }

        dao.atualizar(lugar);
    }

    public void excluirLugar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido!");
        }

        dao.deletar(id);
    }

    public Lugar buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido!");
        }

        return dao.consultarPorId(id);
    }

}
