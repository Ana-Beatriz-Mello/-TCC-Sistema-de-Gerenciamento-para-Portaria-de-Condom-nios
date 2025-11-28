package controller;

import java.time.LocalDateTime;
import java.util.List;

import dao.DaoAluguel;
import model.Aluguel;
import model.Lugar;
import model.Objeto;

public class ControllerAluguel {

    private DaoAluguel dao;

    public ControllerAluguel() {
        dao = new DaoAluguel();
    }
    
    public boolean checarAluguelNoMesmoHorario (Aluguel a) throws Exception {
    	
    	return dao.checarAluguelNoMesmoHorario(a);
    	
    }
    
    public List<int[]> objetosPerdidos () throws Exception {
    	
    	return dao.objetosPerdidos();
    	
    }
    
    public List<Aluguel> buscarAluguelParaFinalizar () throws Exception {
    	
    	return dao.buscarAluguelParaFinalizar();
    	
    }
    
    public List<Aluguel> buscarAlugueisDoDia () throws Exception {
    	
    	return dao.buscarAlugueisDoDia();
    	
    }

    public List<Aluguel> buscarAluguelPorMes (Lugar lugar, LocalDateTime mes) throws Exception {
    	
    	return dao.buscarAluguelPorMes(lugar, mes);
    }
      
    public void incluirAluguel(Aluguel aluguel) throws Exception {
        if (aluguel == null) {
            throw new Exception("Aluguel inválido.");
        }

        dao.inserir(aluguel);

    }
    
    public void incluirObjetos(Aluguel aluguel, List<Objeto> objetos) throws Exception {
    	
    	if (aluguel == null) {
            throw new Exception("Aluguel inválido.");
        }

        dao.adicionarObjetosAoAluguel(aluguel.getId(), objetos);
    	
    }

    public void atualizarAluguel(Aluguel aluguel) throws Exception {
        if (aluguel == null || aluguel.getId() <= 0) {
            throw new Exception("Aluguel inválido para atualização.");
        }
        dao.atualizar(aluguel);
    }

    public void finalizarAluguel(Aluguel aluguel, List<Objeto> objetosDevolvidos) throws Exception {
        if (aluguel == null || aluguel.getId() <= 0) {
            throw new Exception("Aluguel inválido para finalização.");
        }
        dao.finalizarAluguel(aluguel, objetosDevolvidos);
    }

    public void excluirAluguel(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido.");
        }
        dao.excluir(id);
    }

    public Aluguel buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido.");
        }
        return dao.buscarPorId(id);
    }
}
