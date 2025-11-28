package controller;

import java.util.ArrayList;
import java.util.List;

import dao.DaoMorador;
import model.Morador;

public class ControllerMorador {

    private DaoMorador dao = null;

    public ControllerMorador() {
        dao = new DaoMorador();
    }
    
    public void retirarPermissao(Morador morador, int idLote) throws Exception {
    	
    	dao.retirarPermissao(morador, idLote);
    	
    }
    
    public void concederPermissao(Morador morador, int idLote) {
    	
    	dao.concederPermissao(morador, idLote);
    	
    }
    
    public List<Morador> buscarTodosMoradores(boolean todos) throws Exception {
    	
    	return dao.buscarTodosMoradores(todos);
    	
    }
    
    public ArrayList<Integer> lotesComPermissao(Morador morador) {
    	
    	return dao.lotesComPermissao(morador);   	
    	
    }

    public void incluirMorador(Morador morador) throws Exception {

        if (morador.getNome() == null || morador.getNome().isBlank()) {
            throw new Exception("O nome do morador não pode estar vazio.");
        }

        if (morador.getCpf() == null || morador.getCpf().isBlank()) {
            throw new Exception("O CPF não pode estar vazio.");
        }

        dao.inserir(morador);
    }

    public void atualizarMorador(Morador morador) throws Exception {
        if (morador.getCpf() == null || morador.getCpf().isBlank()) {
            throw new Exception("O CPF não pode estar vazio.");
        }
        dao.atualizar(morador);
    }

    public Morador buscarPorCpf(String cpf) throws Exception {
        if (cpf == null || cpf.isBlank()) {
            throw new Exception("Informe um CPF para buscar.");
        }
        return dao.consultarPorCpf(cpf);
    }

    public Morador buscarPorId(int id) throws Exception {
    	
    	return dao.consultarPorId(id);
    	
    }
    
    public void excluirMorador(Morador m) throws Exception {
        dao.deletar(m.getId());
    }
}
