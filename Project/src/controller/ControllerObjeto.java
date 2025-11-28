package controller;

import java.sql.SQLException;
import java.util.List;

import dao.DaoObjeto;
import model.Aluguel;
import model.Lugar;
import model.Objeto;

public class ControllerObjeto {

    private DaoObjeto dao;

    public ControllerObjeto() {
        dao = new DaoObjeto();
    }
    
    public List<Objeto> buscarObjetosDoAluguel (Aluguel a) throws Exception {
    	
    	return dao.buscarObjetosDoAluguel(a);
    	
    }
    
    public List<Objeto> buscarObjetosDoLugar(Lugar lugar) throws Exception {
    	
    	return dao.buscarObjetosDoLugar(lugar);
    	
    }
    
    public List<Objeto> buscarTodosObjetos() throws Exception {
    	
    	return dao.buscarTodosObjetos();
    	
    }

    public void inserirObjeto(Objeto objeto) throws SQLException, Exception {
    	if (objeto == null) {
            throw new Exception("Objeto inválido.");
        }
        dao.inserir(objeto);
    }

    public void atualizarObjeto(Objeto objeto) throws SQLException, Exception {
    	if (objeto == null) {
            throw new Exception("Objeto inválido.");
        }
        dao.atualizar(objeto);
    }

    public void deletarObjeto(int id) throws SQLException {
        dao.deletar(id);
    }

    public Objeto consultarObjetoPorId(int id) throws SQLException {
        return dao.consultarPorId(id);
    }

}
