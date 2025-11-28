package controller;

import java.util.List;

import dao.DaoFuncionario;
import model.Funcionario;

public class ControllerFuncionario {
    
    private DaoFuncionario dao;

    public ControllerFuncionario() {
        dao = new DaoFuncionario();
    }
   
    public List<Funcionario> buscarTodosFuncionarios() throws Exception {
    	
    	return dao.buscarTodosFuncionarios();    	
    	
    }

    public void incluirFuncionario(Funcionario funcionario) throws Exception {
        if (funcionario == null) {
            throw new Exception("Funcionário inválido!");
        }

        dao.inserir(funcionario);
    }

    public void atualizarFuncionario(Funcionario funcionario) throws Exception {
        if (funcionario == null || funcionario.getId() <= 0) {
            throw new Exception("Funcionário inválido para atualização!");
        }

        dao.atualizar(funcionario);
    }

    public void excluirFuncionario(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido!");
        }

        dao.excluir(id);
    }

    public Funcionario buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido!");
        }

        return dao.buscarPorId(id);
    }

    // Buscar por CPF
    public Funcionario buscarPorCpf(String cpf) throws Exception {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new Exception("CPF não informado!");
        }

        return dao.consultarPorCpf(cpf);
    }
}
