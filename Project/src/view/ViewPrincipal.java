package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.ControllerFuncionario;
import controller.ControllerMorador;
import model.Funcionario;
import model.Lugar;
import model.Morador;
import model.Objeto;
import util.RoundedBorder;
import view.dialogues.SelecaoFuncionario;
import view.dialogues.SelecaoLugar;
import view.dialogues.SelecaoMorador;
import view.dialogues.SelecaoObjeto;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class ViewPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private String idUser = null;
	private int tipoUser = 999;
		
	private JPanel contentPane;
	
	private final JButton btnCadastrarLugar = new RoundedBorder("Cadastrar Lugar",15);
	private final JButton btnCadastrarEncomenda = new RoundedBorder("Cadastrar Encomenda",15);
	private final JButton btnEntregarEncomenda = new RoundedBorder("Entregar Encomenda",15);
	private final JButton btnConsultarFuncionario = new RoundedBorder("Consultar Funcionário",15);
	private final JButton btnCadastrarMorador = new RoundedBorder("Cadastrar Morador",15);
	private final JButton btnCadastrarFuncionario = new RoundedBorder("Cadastrar Funcionário",15);
	private final JButton btnCadastrarObjeto = new RoundedBorder("Cadastrar Objeto",15);
	private final JButton btnConsultarLugar = new RoundedBorder("Consultar Lugar",15);
	private final JButton btnConsultarObjeto = new RoundedBorder("Consultar Objeto",15);
	private final JButton btnAluguel = new RoundedBorder("Aluguel de Espaços",15);
	private final JButton btnConsultarMorador = new RoundedBorder("Consultar Morador",15);
	private final JButton btnFinalizarAluguel = new RoundedBorder("Finalizar Aluguel",15);
	private final JButton btnObjetosNaoDevolvidos = new RoundedBorder("Objetos Pendentes",15);
	private final JButton btnEmprestar = new RoundedBorder("Emprestar Objetos",15);
	private final JButton btnAtribuirPermissao = new RoundedBorder("Atribuir Permissão",15);

	// Utilizar APENAS para testes rápidos
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewPrincipal frame = new ViewPrincipal("181.884.727-27",1);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	public ViewPrincipal(String idUser, int tipoUser) {
		/* Se:
    	 * tipoUsuario = 0 -> Morador
    	 * tipoUsuario = 1 -> Funcionario
    	 */
		
		// Informações da sessão (CPF do usuário, tipo de usuário)
		
		this.tipoUser = tipoUser;
		this.idUser = idUser;	
		
		if (this.idUser == null || this.tipoUser == 999) {
    		JOptionPane.showMessageDialog(this, "Erro na autenticação do usuário!", "Erro", JOptionPane.INFORMATION_MESSAGE);
    		dispose();
    	}
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(590, 680));
		this.setTitle("Principal");
		
		arrumarBotao(btnCadastrarLugar);
		arrumarBotao(btnCadastrarEncomenda);
		arrumarBotao(btnEntregarEncomenda);
		arrumarBotao(btnConsultarFuncionario);
		arrumarBotao(btnCadastrarMorador);
		arrumarBotao(btnCadastrarFuncionario);
		arrumarBotao(btnCadastrarObjeto);
		arrumarBotao(btnConsultarLugar);
		arrumarBotao(btnConsultarObjeto);
		arrumarBotao(btnAluguel);
		arrumarBotao(btnConsultarMorador);
		arrumarBotao(btnFinalizarAluguel);
		arrumarBotao(btnObjetosNaoDevolvidos);
		arrumarBotao(btnEmprestar);
		arrumarBotao(btnAtribuirPermissao);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		// Definindo ícones do sistema
		ImageIcon icon = null;
		try {	
			icon = new ImageIcon(("IconePequeno.png"));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(ViewPrincipal.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
		
		getContentPane().setBackground(new Color(255, 253, 207));
		contentPane.setBackground(new Color(255, 253, 207));
		setContentPane(contentPane);
		
		btnCadastrarMorador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewMorador viewMorador = new ViewMorador(ViewPrincipal.this.idUser,ViewPrincipal.this.tipoUser, null);
				viewMorador.setVisible(true);
				
			}
		});
	
		btnCadastrarMorador.setBounds(10, 11, 123, 45);
		contentPane.add(btnCadastrarMorador);

		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		btnCadastrarFuncionario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewFuncionario viewFuncionario = new ViewFuncionario(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, null);
				viewFuncionario.setVisible(true);
				
			}
		});
		
		
		btnCadastrarFuncionario.setBounds(10, 126, 123, 45);
		contentPane.add(btnCadastrarFuncionario);
		btnCadastrarLugar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewLugar viewLugar = new ViewLugar(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, null);
				viewLugar.setVisible(true);
				
			}
		});
		btnCadastrarLugar.setBounds(143, 11, 129, 45);
		
		contentPane.add(btnCadastrarLugar);
		

		btnCadastrarObjeto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewObjeto viewObjeto = new ViewObjeto(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, null);
				viewObjeto.setVisible(true);
				
			}
		});
		btnCadastrarObjeto.setBounds(143, 126, 129, 45);
		contentPane.add(btnCadastrarObjeto);
		btnCadastrarEncomenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewEncomenda viewEncomenda = new ViewEncomenda(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, null);
				viewEncomenda.setVisible(true);
				
			}
		});
		btnCadastrarEncomenda.setBounds(282, 11, 139, 45);
		
		contentPane.add(btnCadastrarEncomenda);
		btnEntregarEncomenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewEntregarEncomenda viewEntregarEncomenda = new ViewEntregarEncomenda(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser);
				viewEntregarEncomenda.setVisible(true);
				
			}
		});
		btnEntregarEncomenda.setBounds(282, 67, 139, 45);
		
		contentPane.add(btnEntregarEncomenda);
		
		
		btnConsultarMorador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
					Morador moradorSelecionado = null;
			        SelecaoMorador dialog = new SelecaoMorador(ViewPrincipal.this, true);
			        dialog.setVisible(true);
			        moradorSelecionado = dialog.getMoradorSelecionado();
			        ViewMorador consulta = new ViewMorador(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, moradorSelecionado);
			        consulta.setVisible(true);
			        
			}
		});
		btnConsultarMorador.setBounds(10, 67, 123, 45);
		contentPane.add(btnConsultarMorador);
			
		btnConsultarFuncionario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
				Funcionario funcionarioSelecionado = null;
		        SelecaoFuncionario dialog = new SelecaoFuncionario(ViewPrincipal.this);
		        dialog.setVisible(true);
		        funcionarioSelecionado = dialog.getFuncionarioSelecionado();
		        ViewFuncionario consulta = new ViewFuncionario(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, funcionarioSelecionado);
		        consulta.setVisible(true);
				
			}
		});
		btnConsultarFuncionario.setBounds(10, 187, 123, 45);
		contentPane.add(btnConsultarFuncionario);
		btnConsultarLugar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Lugar lugarSelecionado = null;
		        SelecaoLugar dialog = new SelecaoLugar(ViewPrincipal.this);
		        dialog.setVisible(true);
		        lugarSelecionado = dialog.getLugarSelecionado();
		        ViewLugar consulta = new ViewLugar(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, lugarSelecionado);
		        consulta.setVisible(true);

			}
		});
		btnConsultarLugar.setBounds(143, 67, 129, 45);
		
		contentPane.add(btnConsultarLugar);
		btnConsultarObjeto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Objeto objetoSelecionado = null;
		        SelecaoObjeto dialog = new SelecaoObjeto(ViewPrincipal.this);
		        dialog.setVisible(true);
		        objetoSelecionado = dialog.getObjetoSelecionado();
		        ViewObjeto consulta = new ViewObjeto(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser, objetoSelecionado);
		        consulta.setVisible(true);
				
			}
		});
		btnConsultarObjeto.setBounds(143, 187, 129, 45);
		
		contentPane.add(btnConsultarObjeto);
		
		
		btnAluguel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewAluguel viewAluguel;
				try {
					viewAluguel = new ViewAluguel(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser);
					viewAluguel.setVisible(true);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(ViewPrincipal.this, "Erro ao carregar a tela!", "Erro", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				
			}
		});
		btnAluguel.setBounds(282, 126, 139, 45);
		contentPane.add(btnAluguel);
		
		
		btnEmprestar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewEmprestarObjetos eo = new ViewEmprestarObjetos(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser);
				eo.setVisible(true);
				
			}
		});
		btnEmprestar.setBounds(282, 187, 139, 45);
		contentPane.add(btnEmprestar);
		btnFinalizarAluguel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewFinalizarAluguel vfa = new ViewFinalizarAluguel(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser);
				vfa.setVisible(true);
				
			}
		});
		btnFinalizarAluguel.setBounds(431, 187, 123, 45);
		
		contentPane.add(btnFinalizarAluguel);
		
		
		btnAtribuirPermissao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewAtribuirPermissao a = new ViewAtribuirPermissao(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser);
				a.setVisible(true);
				
			}
		});
		
		btnAtribuirPermissao.setBounds(431, 126, 123, 45);
		contentPane.add(btnAtribuirPermissao);
		
		
		btnObjetosNaoDevolvidos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ViewObjetosNaoDevolvidos a = new ViewObjetosNaoDevolvidos(ViewPrincipal.this.idUser, ViewPrincipal.this.tipoUser);
				a.setVisible(true);
				
			}
		});
		btnObjetosNaoDevolvidos.setBounds(431, 67, 123, 45);
		contentPane.add(btnObjetosNaoDevolvidos);
		
		// NOVO
		JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0;  
        contentPane.add(criarCabecalho(), gbc);
        

        // Criar colunas
        JPanel colunaEsquerda = criarColuna(new Font("Candara Light", Font.BOLD, 15), new Font("Candara Light", Font.BOLD, 15), new Color(0, 68, 255), Color.WHITE);
        JPanel colunaDireita = criarColunaDireita(new Font("Candara Light", Font.BOLD, 15), new Font("Candara Light", Font.BOLD, 15), new Color(0, 255, 26), Color.WHITE);

        gbc.gridwidth = 1;
        gbc.weighty = 1;
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(colunaEsquerda, gbc);

        gbc.gridx = 1;
        contentPane.add(colunaDireita, gbc);
        
        //
		
		if (tipoUser == 0) {
			// MORADOR
			btnCadastrarMorador.setVisible(false);
			btnCadastrarFuncionario.setVisible(false);
			btnCadastrarLugar.setVisible(false);
			btnCadastrarObjeto.setVisible(false);
			btnCadastrarEncomenda.setVisible(false);
			
			btnEntregarEncomenda.setVisible(true); // Montagem da tela se baseia no tipo de usuário
			btnEntregarEncomenda.setText("Minhas Encomendas");
			
			btnConsultarFuncionario.setVisible(false);
			btnAluguel.setVisible(true);
			btnConsultarMorador.setVisible(false);
			btnConsultarObjeto.setVisible(false);
			btnConsultarLugar.setVisible(false);
			btnFinalizarAluguel.setVisible(false);
			btnEmprestar.setVisible(false);
			btnAtribuirPermissao.setVisible(false);
			btnObjetosNaoDevolvidos.setVisible(false);
		}
		else if (tipoUser == 1) {
			// FUNCIONARIO
			btnCadastrarMorador.setVisible(true);
			btnCadastrarFuncionario.setVisible(true);
			btnCadastrarLugar.setVisible(true);
			btnCadastrarObjeto.setVisible(true);
			btnCadastrarEncomenda.setVisible(true);
			btnEntregarEncomenda.setVisible(true);
			btnConsultarFuncionario.setVisible(true);
			btnAluguel.setVisible(true);
			btnConsultarMorador.setVisible(true);
			btnConsultarObjeto.setVisible(true);
			btnConsultarLugar.setVisible(true);
			btnFinalizarAluguel.setVisible(true);
			btnEmprestar.setVisible(true);
			btnAtribuirPermissao.setVisible(true);
			btnObjetosNaoDevolvidos.setVisible(true);
		}
		
	}
	
	private JPanel criarColuna(Font fonteTitulo, Font fonteBotao, Color destaque, Color fundo) {
        JPanel coluna = new JPanel();
        coluna.setBackground(fundo);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));

        if (tipoUser == 1) {
        	
	        coluna.add(criarSecao("Funcionários", new JButton[]{
	            btnCadastrarFuncionario,
	            btnConsultarFuncionario
	        }));
	
	        coluna.add(criarSecao("Moradores", new JButton[]{
	            btnCadastrarMorador,
	            btnConsultarMorador,
	            btnAtribuirPermissao
	        }));
	        
        }
	
	        coluna.add(criarSecao("Encomendas", new JButton[]{
	            btnCadastrarEncomenda,
	            btnEntregarEncomenda
	        }));
	        
        return coluna;
    }

	private JPanel criarColunaDireita(Font fonteTitulo, Font fonteBotao, Color destaque, Color fundo) {
        JPanel coluna = new JPanel();
        coluna.setBackground(fundo);
        coluna.setLayout(new BoxLayout(coluna, BoxLayout.Y_AXIS));

        if (tipoUser == 1) {
        	
	        coluna.add(criarSecao("Lugares", new JButton[]{
	            btnCadastrarLugar,
	            btnConsultarLugar
	        }));
	
	        coluna.add(criarSecao("Objetos", new JButton[]{
	            btnCadastrarObjeto,
	            btnConsultarObjeto,
	            btnObjetosNaoDevolvidos
	        }));
	        
        }

        coluna.add(criarSecao("Aluguéis", new JButton[]{
            btnAluguel,
            btnEmprestar,
            btnFinalizarAluguel
        }));

        return coluna;
    }
	
	private JPanel criarSecao(String titulo, JButton[] botoes) {
        JPanel secao = new JPanel(new GridBagLayout());
        secao.setBackground(Color.WHITE);
        secao.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                titulo,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Candara Light", Font.BOLD, 20),
                Color.DARK_GRAY
        ));        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        gbc.fill = GridBagConstraints.NONE;

        for (JButton botao : botoes) {
            secao.add(botao, gbc);
            gbc.gridy++;
        }
        
        if (tipoUser == 0) {
        	secao.setMaximumSize(new Dimension(300,300));
        	secao.setSize(new Dimension(300,300));
        	secao.setPreferredSize(new Dimension(300,300));
        	secao.setMinimumSize(new Dimension(300,300));
        }
        else if (tipoUser == 1) {
        	secao.setMaximumSize(new Dimension(300,170));
        	secao.setSize(new Dimension(300,170));
        	secao.setPreferredSize(new Dimension(300,170));
        	secao.setMinimumSize(new Dimension(300,170));
        }
        
        return secao;
        
    }

	public void arrumarBotao (JButton j) {
		
		j.setMinimumSize(new Dimension(250, 30));
		j.setMaximumSize(new Dimension(250, 30));
    	j.setSize(new Dimension(250, 30));
    	j.setPreferredSize(new Dimension(250, 30));
		
	}
	
	public JPanel criarCabecalho() {
		JPanel cab = new JPanel();
	    cab.setBackground(new Color(175, 111, 191));  
	    cab.setBorder(new EmptyBorder(20, 10, 20, 10));

	    String userName = null;
	    
	    if (tipoUser == 0) {
	    	ControllerMorador m = new ControllerMorador();
	    	try {
				userName = m.buscarPorCpf(idUser).getNome();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(ViewPrincipal.this, "Erro ao buscar usuário!", "Erro", JOptionPane.INFORMATION_MESSAGE);
				//e.printStackTrace();
			}
	    }
	    else if (tipoUser == 1) {
	    	ControllerFuncionario f = new ControllerFuncionario();
	    	try {
				userName = f.buscarPorCpf(idUser).getNome();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(ViewPrincipal.this, "Erro ao buscar usuário!", "Erro", JOptionPane.INFORMATION_MESSAGE);
				//e.printStackTrace();
			}
	    }
	    
	    JLabel titulo = new JLabel("Usuário: " + userName, JLabel.CENTER);
	    titulo.setFont(new Font("Candara Light", Font.BOLD, 30));
	    titulo.setForeground(new Color(255,255,255)); 

	    cab.setLayout(new BorderLayout());
	    cab.add(titulo, BorderLayout.CENTER);

	    return cab;
    }
	
}
