package controller;

import dao.DaoEncomenda;
import model.Encomenda;
import model.Morador;

import java.util.ArrayList;

public class ControllerEncomenda {

    private DaoEncomenda dao;

    public ControllerEncomenda() {
        dao = new DaoEncomenda();
    }
    
    public ArrayList<Encomenda> buscarEncomendasDeMorador (String cpf) throws Exception {
    	
    	return dao.buscarEncomendasDeMorador(cpf);
    	
    }
    
    public ArrayList<Encomenda> buscarEncomendasDeUmLote (int lote) throws Exception {
    	
    	return dao.buscarEncomendasDeUmLote(lote);
    	
    }
    
    public void receberEncomenda(Encomenda encomenda, Morador m) throws Exception {
    	
    	dao.receberEncomenda(encomenda, m);
    	
    }

    public void incluirEncomenda(Encomenda encomenda) throws Exception {
        if (encomenda == null) {
            throw new Exception("Encomenda inválida!");
        }

        if (encomenda.getCodRastreio() == null || encomenda.getCodRastreio().trim().isEmpty()) {
            throw new Exception("Código de rastreio não pode ser vazio!");
        }

        dao.inserir(encomenda);
    }

    public void atualizarEncomenda(Encomenda encomenda) throws Exception {
        if (encomenda == null || encomenda.getId() == 0) {
            throw new Exception("Encomenda inválida para atualização!");
        }

        dao.atualizar(encomenda);
    }

    public void excluirEncomenda(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido!");
        }

        dao.excluir(id);
    }

    public Encomenda buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido!");
        }

        return dao.buscarPorId(id);
    }

}
