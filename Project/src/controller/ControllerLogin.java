package controller;

import dao.DaoFuncionario;
import dao.DaoMorador;
import model.Funcionario;
import model.Morador;
import util.GestorHash;

public class ControllerLogin {
	
	// private ViewLogin view = null;
    private DaoFuncionario daoFuncionario;
    private DaoMorador daoMorador;
    private String senhaGenerica = "Condominio@gestao";
    
    public ControllerLogin() {};
    
    public int autenticarMorador(String cpf, String senha) {

    	/* RETORNOS:
    	 * 0 - Login não autenticado
    	 * 1 - Autenticado com sucesso
    	 * 2 - Primeiro acesso detectado
    	 * 999 - Ocorreu algum erro
    	 */
    	
    	GestorHash hash = new GestorHash();
    	try {
			senhaGenerica = hash.gerarHashSHA256(senhaGenerica);
		} catch (Exception e) {
			e.printStackTrace();
			return 999;
		}
    	daoMorador = new DaoMorador();
    	String senhaUsuario = null;
    	Morador m = null;
		try {
				
			m = daoMorador.consultarPorCpf(cpf);
			senhaUsuario = daoMorador.buscarUsuarioRelacionado(m);
			senha = hash.gerarHashSHA256(senha);
				
		} catch (Exception e) {
				
			e.printStackTrace();
			return 999;
				
		}
		
		if (senha == null || senhaUsuario == null)
			
			return 999;
		
    	if (!m.isAtivo()) {
    			
    		return 0;
    			
    	}
    	
    	else if (senha.compareTo(senhaUsuario) == 0 && senha.compareTo(senhaGenerica) == 0)  {
    		
    		return 2;
    		
    	}
    	
    	else if (senha.compareTo(senhaUsuario) == 0) {
    		
    		return 1;
    		
    	}
    		
    	else {
    			
    		return 0;
    			
    	}
    		    		
  
	}
    
    public int autenticarFuncionario(String cpf, String senha) {
    	
    	/* RETORNOS:
    	 * 0 - Login não autenticado
    	 * 1 - Autenticado com sucesso
    	 * 2 - Primeiro acesso detectado
    	 * 999 - Ocorreu algum erro
    	 */
    	
    	GestorHash hash = new GestorHash();
    	try {
			senhaGenerica = hash.gerarHashSHA256(senhaGenerica);
		} catch (Exception e) {
			e.printStackTrace();
			return 999;
		}
    	daoFuncionario = new DaoFuncionario();
    	String senhaUsuario = null;
    	Funcionario f = null;
		try {
				
			f = daoFuncionario.consultarPorCpf(cpf);
			senhaUsuario = daoFuncionario.buscarUsuarioRelacionado(f);
			senha = hash.gerarHashSHA256(senha);
				
		} catch (Exception e) {
				
			e.printStackTrace();
			return 999;
				
		}
		
		if (senha == null || senhaUsuario == null)
			
			return 999;
		
    	if (senha.compareTo(senhaUsuario) == 0 && senha.compareTo(senhaGenerica) == 0) {
    			
    		return 2;
    			
    	}
    	
    	else if (senha.compareTo(senhaUsuario) == 0) {
    		
    		return 1;
    		
    	}
    		
    	else {
    			
    		return 0;
    			
    	}
    	
    }
    
    public boolean primeiroAcesso(String user, String senha, int tipoUsuario) {
    	/* Se:
    	 * tipoUsuario = 0 -> Morador
    	 * tipoUsuario = 1 -> Funcionario
    	 */
    	
    	GestorHash hash = new GestorHash();
    	
    	if (tipoUsuario == 0) {
    		daoMorador = new DaoMorador();
    		try {
				Morador m = daoMorador.consultarPorCpf(user);
				senha = hash.gerarHashSHA256(senha);
				if (daoMorador.setSenhaDeUsuario(m, senha)) {
					return true;
				}
				else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	else if(tipoUsuario == 1) {
    		daoFuncionario = new DaoFuncionario();
    		try {
    			Funcionario f = daoFuncionario.consultarPorCpf(user);
    			senha = hash.gerarHashSHA256(senha);
    			if (daoFuncionario.setSenhaDeUsuario(f, senha)) {
    				return true;
    			}
    			else {
    				return false;
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	}
    	
    	return false;
   }
    
    
    	
}


