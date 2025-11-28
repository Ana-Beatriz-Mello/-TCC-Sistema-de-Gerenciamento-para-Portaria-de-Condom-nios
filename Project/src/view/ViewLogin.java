package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import controller.ControllerLogin;
import util.RoundedBorder;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

public class ViewLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFormattedTextField txtUser;	
	
	private JPasswordField txtSenha = new JPasswordField();
	private JPasswordField txtConfirmarSenha = new JPasswordField();
	
	private JLabel lConfirmarSenha = new JLabel("Confirmar Senha");
	private JLabel lbUsuario = new JLabel("Usuário");
	private JLabel lSenha = new JLabel("Senha");
	
	private RoundedBorder btnSair = new RoundedBorder("Sair", 15);
	private RoundedBorder btnLogin = new RoundedBorder("Login", 15);
	private final RoundedBorder btnConfirmaSenha = new RoundedBorder("Confirmar Senha", 15);
	
	private JRadioButton rbMorador = new JRadioButton("Morador");
	private JRadioButton rbFuncionario = new JRadioButton("Funcionário");
	

	// ABRIR NO: ControllerExecutar
	
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewLogin frame = new ViewLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/


	
	public ViewLogin() {
		
		setBackground(SystemColor.inactiveCaptionBorder);
		setFont(new Font("Candara Light", Font.BOLD, 15));
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255,255,255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setResizable(false);
		
		//
		ImageIcon icon = null;
		try {	
			icon = new ImageIcon(("IconePequeno.png"));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(ViewLogin.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
		
		try {
			MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
			mascaraCPF.setPlaceholderCharacter(' ');
			txtUser = new JFormattedTextField(mascaraCPF);
		}
		
		catch (Exception e){
			JOptionPane.showMessageDialog(ViewLogin.this, "Erro no carregamento de campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		
		btnConfirmaSenha.setVisible(false);
		
		getContentPane().setLayout(new GridLayout(0, 1));
		
		ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbFuncionario);
        grupo.add(rbMorador);
        
        rbFuncionario.setFont(new Font("Candara Light", Font.BOLD, 15)); 
        rbFuncionario.setBounds(35, 164, 125, 27);
        rbMorador.setFont(new Font("Candara Light", Font.BOLD, 15));
        rbMorador.setBounds(184, 162, 98, 27);
        rbFuncionario.setSelected(true);
        
        rbFuncionario.setBackground(Color.WHITE);
        rbMorador.setBackground(Color.WHITE);
        rbFuncionario.setOpaque(true);
        rbMorador.setOpaque(true);
        
        
        contentPane.add(rbFuncionario);
        contentPane.add(rbMorador);
		

		setContentPane(contentPane);
		contentPane.setLayout(null);
		btnLogin.setFont(new Font("Candara Light", Font.BOLD, 20));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String user = txtUser.getText();
				
				char[] senhaChars = txtSenha.getPassword();
				String senha = new String(senhaChars);
				
				ControllerLogin cLogin = new ControllerLogin();
				int estadoAcesso = 999;
				
				if (rbMorador.isSelected()) {
					estadoAcesso = cLogin.autenticarMorador(user, senha);
					if (estadoAcesso == 1) {
						ViewPrincipal viewPrincipal = new ViewPrincipal(user,0);
						viewPrincipal.setVisible(true);
						ViewLogin.this.dispose();
					}
					else if (estadoAcesso == 0) {
						JOptionPane.showMessageDialog(ViewLogin.this, "Login ou senha incorretos!", "Aviso", JOptionPane.WARNING_MESSAGE);
					}
					else if (estadoAcesso == 2) {
						
						primeiroAcesso();
						
					}
					else {
						JOptionPane.showMessageDialog(ViewLogin.this, "Login ou senha incorretos!", "Aviso", JOptionPane.WARNING_MESSAGE);
						return;
					}
				
					
				} else if (rbFuncionario.isSelected()) {
					estadoAcesso = cLogin.autenticarFuncionario(user, senha);
					if (estadoAcesso == 1) {
						ViewPrincipal viewPrincipal = new ViewPrincipal(user,1);
						viewPrincipal.setVisible(true);
						ViewLogin.this.dispose();
					}
					else if (estadoAcesso == 0) {
						JOptionPane.showMessageDialog(ViewLogin.this, "Login ou senha incorretos!", "Aviso", JOptionPane.WARNING_MESSAGE);
					}
					else if (estadoAcesso == 2) {
						
						primeiroAcesso();
						
					}
					else {
						JOptionPane.showMessageDialog(ViewLogin.this, "Login ou senha incorretos!", "Aviso", JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				
				
			}
		});
		
		
		btnLogin.setBounds(22, 196, 138, 40);
		contentPane.add(btnLogin);
		btnSair.setFont(new Font("Candara Light", Font.BOLD, 20));
		btnSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		
		btnSair.setBounds(262, 196, 125, 41);
		contentPane.add(btnSair);
		
		
		txtUser.setBounds(204, 24, 183, 20);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		
		txtSenha.setBounds(204, 74, 183, 20);
		contentPane.add(txtSenha);
		lSenha.setFont(new Font("Candara Light", Font.BOLD, 20));
		
		
		lSenha.setBounds(22, 72, 125, 26);
		contentPane.add(lSenha);
		lbUsuario.setFont(new Font("Candara Light", Font.BOLD, 20));
		
		
		lbUsuario.setBounds(22, 19, 124, 32);
		contentPane.add(lbUsuario);
		lConfirmarSenha.setFont(new Font("Candara Light", Font.BOLD, 20));
		
		
		lConfirmarSenha.setBounds(22, 116, 150, 31);
		lConfirmarSenha.setVisible(false);
		contentPane.add(lConfirmarSenha);
		
		
		txtConfirmarSenha.setBounds(204, 120, 183, 20);
		txtConfirmarSenha.setVisible(false);
		contentPane.add(txtConfirmarSenha);
		btnConfirmaSenha.setFont(new Font("Candara Light", Font.BOLD, 20));
		btnConfirmaSenha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String user = txtUser.getText();
				
				char[] senhaChars = txtSenha.getPassword();
				String senha1 = new String(senhaChars);
				
				senhaChars = txtConfirmarSenha.getPassword();
				String senha2 = new String(senhaChars);
				
				if (senha2.compareTo(senha1) != 0) {
					
					JOptionPane.showMessageDialog(null, "As senhas inseridas são diferentes! A senha deve ser igual à senha de confirmação!", "Aviso", JOptionPane.WARNING_MESSAGE);
					return;
				}
				else {
				
					if (rbMorador.isSelected()) {
						ControllerLogin cLogin = new ControllerLogin();
						if(cLogin.primeiroAcesso(user, senha1, 0)) {
							JOptionPane.showMessageDialog(ViewLogin.this, "Senha definida com sucesso! Faça login com a nova senha!", "Aviso", JOptionPane.WARNING_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(ViewLogin.this, "Ocorreu um erro!", "Aviso", JOptionPane.WARNING_MESSAGE);
						}
					}
					else if (rbFuncionario.isSelected()) {
						ControllerLogin cLogin = new ControllerLogin();
						if(cLogin.primeiroAcesso(user, senha1, 1)) {
							JOptionPane.showMessageDialog(ViewLogin.this, "Senha definida com sucesso! Faça login com a nova senha!", "Aviso", JOptionPane.WARNING_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(ViewLogin.this, "Ocorreu um erro!", "Aviso", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				
				ViewLogin frame = new ViewLogin();
				frame.setVisible(true);
				ViewLogin.this.dispose();
				
			}});
		btnConfirmaSenha.setBounds(22, 198, 200, 39);
		
		// Arrumando elementos da tela
		
		arrumarBotoes(btnLogin);
		arrumarBotoes(btnSair);
		arrumarTextfields(txtSenha);
		arrumarTextfields(txtConfirmarSenha);
		arrumarTextfields(txtUser);
				
		//
		
		contentPane.add(btnConfirmaSenha);
	}
	
	public void primeiroAcesso() {
		
		lConfirmarSenha.setVisible(true);
		txtConfirmarSenha.setVisible(true);
		txtSenha.setText("");
		rbMorador.setVisible(false);
		rbFuncionario.setVisible(false);
		
		JOptionPane.showMessageDialog(ViewLogin.this, "Primeiro acesso detectado! Defina sua senha para o sistema!", "Aviso", JOptionPane.WARNING_MESSAGE);
		btnLogin.setVisible(false);
		btnConfirmaSenha.setVisible(true);
	}
	
	public void arrumarLabels (JLabel l) {
		
		l.setForeground(new Color(97, 72, 44)); // COR DO TEXTO
		
	}
	
	public void arrumarTextfields (JPasswordField t) {
		
		t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
		t.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(97, 72, 44))); // COR DA BORDA DO BOTÇAO
		
	}
	
	public void arrumarTextfields (JFormattedTextField t) {
		
		t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
		t.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(97, 72, 44)));
		
	}
	
	public void arrumarBotoes (JButton b) {
		
        b.setFont(new Font("Candara Light", Font.BOLD, 20)); 
        b.setFocusPainted(true);
        
	}
	
}
