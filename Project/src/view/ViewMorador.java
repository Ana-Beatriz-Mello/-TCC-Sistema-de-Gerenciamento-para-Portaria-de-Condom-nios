package view;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import controller.ControllerMorador;
import model.Morador;
import util.RoundedBorder;
import view.dialogues.SelecaoLote;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;

public class ViewMorador extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	
	private JTextField txtNome 			  = new JTextField();
	private JFormattedTextField txtCpf;
    private JTextField txtTelefone  	  = new JTextField();
    private JTextField txtEmail			  = new JTextField();
    private JTextField txtLoteSelecionado = new JTextField();
    
    private JButton btnSalvar			  = new RoundedBorder("Salvar",15);;
    private JButton btnVoltar 			  = new RoundedBorder("Voltar",15);
    private JCheckBox cbAtivo 			  = new JCheckBox("Ativo");
    private JButton btnAtualizar 		  = new RoundedBorder("Salvar",15);
    private JButton btnSelecionarLote 	  = new RoundedBorder("Selecionar",15);
    
    private JLabel lblNome 				  = new JLabel("Nome:");
    private JLabel lblCpf  				  = new JLabel("CPF:");
    private JLabel lblTelefone			  = new JLabel("Telefone:");
	private JLabel lblEmail 			  = new JLabel("Email:");
	private JLabel lblLote 				  = new JLabel("Lote:");
    
	private String idUser 				  = null;
	private int tipoUser 				  = 999;
	private Integer loteSelecionado 	  = null;
	private Morador moradorSelecionado 	  = null;

	public ViewMorador(String idUser, int tipoUser, Morador moradorConsultado) {
		setTitle("Cadastro de Moradores");
		/* Se:
    	 * tipoUsuario = 0 -> Morador
    	 * tipoUsuario = 1 -> Funcionario
    	 */
		// Informações da sessão (CPF do usuário, tipo de usuário)
		this.tipoUser = tipoUser;
		this.idUser = idUser;
		this.moradorSelecionado = moradorConsultado;
		
		if (this.idUser == null || this.tipoUser == 999) {
    		JOptionPane.showMessageDialog(this, "Erro na autenticação do usuário!", "Erro", JOptionPane.INFORMATION_MESSAGE);
    		dispose();
    	}
		
		//
		ImageIcon icon = null;
		try {	
			icon = new ImageIcon(("IconePequeno.png"));
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(ViewMorador.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
		
		try {
			MaskFormatter mascaraCPF = new MaskFormatter("###.###.###-##");
			mascaraCPF.setPlaceholderCharacter(' ');
			txtCpf = new JFormattedTextField(mascaraCPF);
		}
		
		catch (Exception e){
			JOptionPane.showMessageDialog(this, "Erro no carregamento de campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		
		btnAtualizar.setVisible(false);
		cbAtivo.setSelected(true);
		cbAtivo.setBackground(Color.WHITE);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.WHITE);
		
		setResizable(false); // Padrão para os cadastros simples
		
		
		arrumarLabels(lblNome);
		lblNome.setBounds(23, 37, 81, 25);
        getContentPane().add(lblNome);

        arrumarTextfields(txtNome);
        txtNome.setBounds(201, 38, 246, 20);
        getContentPane().add(txtNome);

        arrumarLabels(lblCpf);
        lblCpf.setBounds(23, 75, 81, 34);
        getContentPane().add(lblCpf);

        arrumarTextfields(txtCpf);
        txtCpf.setBounds(201, 82, 97, 20);
        getContentPane().add(txtCpf);

        arrumarLabels(lblTelefone);
        lblTelefone.setBounds(23, 120, 104, 34);
        getContentPane().add(lblTelefone);

        arrumarTextfields(txtTelefone);
        txtTelefone.setBounds(201, 135, 246, 20);
        getContentPane().add(txtTelefone);

        arrumarLabels(lblEmail);
        lblEmail.setBounds(23, 172, 81, 34);
        getContentPane().add(lblEmail);

        arrumarTextfields(txtEmail);
        txtEmail.setBounds(201, 179, 246, 20);
        getContentPane().add(txtEmail);

        arrumarLabels(lblLote);
        lblLote.setBounds(23, 218, 81, 34);
        getContentPane().add(lblLote);

        arrumarTextfields(txtLoteSelecionado);
        txtLoteSelecionado.setBounds(201, 225, 132, 20);
        txtLoteSelecionado.setEditable(false);
        getContentPane().add(txtLoteSelecionado);
        
        
        if (this.moradorSelecionado != null) {
        	
        	preencherCampos(this.moradorSelecionado);
        	if (!this.moradorSelecionado.getAtivo()) {
        		cbAtivo.setSelected(false);
        	}
        	
        	btnSalvar.setVisible(false);
        	btnAtualizar.setVisible(true);
        	
        	
        }
        

        
        btnSelecionarLote.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		SelecaoLote dialog = new SelecaoLote(ViewMorador.this);
                dialog.setVisible(true);

                Integer lote = dialog.getLoteSelecionado();
                if (lote != null) {
                    loteSelecionado = lote;
                    txtLoteSelecionado.setText(String.valueOf(loteSelecionado));
                }
        		
        	}
        });
        btnSelecionarLote.setBounds(343, 224, 104, 23);
        getContentPane().add(btnSelecionarLote);

        btnSalvar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		Integer lote = null;
        		String nome = txtNome.getText();
        		String cpf = txtCpf.getText();
        		String telefone = txtTelefone.getText();
        		String email = txtEmail.getText();

        		try {
        			lote = Integer.parseInt(txtLoteSelecionado.getText());
        		}
        		catch (Exception n) {
        			JOptionPane.showMessageDialog(ViewMorador.this, "Selecione um lote!", "Aviso", JOptionPane.WARNING_MESSAGE);
        			return;
        		}
        		
        		ControllerMorador c = new ControllerMorador();
        		try {
					if (c.buscarPorCpf(cpf) != null) {
						JOptionPane.showMessageDialog(ViewMorador.this, "Já existe um morador com esse CPF!", "Aviso", JOptionPane.WARNING_MESSAGE);
						return;
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(ViewMorador.this, "Ocorreu um erro de conexão com o banco!", "Aviso", JOptionPane.WARNING_MESSAGE);
					return;
				}
        		
        		try {
					Morador m = new Morador(
							nome,
							cpf,
							telefone,
							email,
							lote
					);
					if (cbAtivo.isSelected()) {
						m.setAtivo(true);
					}
					else if (!cbAtivo.isSelected()) {
						m.setAtivo(false);
        			}
					
					ControllerMorador control = new ControllerMorador();
					control.incluirMorador(m);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(ViewMorador.this, "Preencha os campos obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
					e1.printStackTrace();
					return;
				}
        			
        		JOptionPane.showMessageDialog(ViewMorador.this, "Morador criado com sucesso!", "Aviso", JOptionPane.WARNING_MESSAGE);
        		ViewMorador.this.dispose();
        	}
        });
        btnSalvar.setBounds(23, 269, 81, 23);
        getContentPane().add(btnSalvar);
        
        btnVoltar.setBounds(366, 269, 81, 23);
        contentPane.add(btnVoltar);
        btnVoltar.addActionListener(e -> dispose());
        
        cbAtivo.setBounds(23, 7, 97, 23);
        contentPane.add(cbAtivo);
        
        
        btnAtualizar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		String nome = txtNome.getText();
        		String cpf = txtCpf.getText();
        		String telefone = txtTelefone.getText();
        		String email = txtEmail.getText();
        		int lote = Integer.parseInt(txtLoteSelecionado.getText());      		
        		
        		try {
					Morador m = new Morador(
							nome,
							cpf,
							telefone,
							email,
							lote
					);
					if (cbAtivo.isSelected()) {
						m.setAtivo(true);
					}
					else if (!cbAtivo.isSelected()) {
						m.setAtivo(false);
        			}
					m.setId(ViewMorador.this.moradorSelecionado.getId());
					
					ControllerMorador control = new ControllerMorador();
					control.atualizarMorador(m);
					
					if (lote != moradorSelecionado.getIdLote()) {
	        			
						JOptionPane.showMessageDialog(ViewMorador.this, "Como o lote deste morador está sendo alterado, ele perderá\n a permissão de receber encomendas do lote anterior e receberá\n a permissão para o novo lote.", "Aviso", JOptionPane.WARNING_MESSAGE);
	        			control.concederPermissao(moradorConsultado, lote);
	        			control.retirarPermissao(moradorConsultado, moradorSelecionado.getIdLote());
	        			
	        		}
					
					
				} catch (Exception e1) {
					e1.printStackTrace();
					return;
				}
        		
        		JOptionPane.showMessageDialog(ViewMorador.this, "Morador atualizado com sucesso!", "Aviso", JOptionPane.WARNING_MESSAGE);
        		ViewMorador.this.dispose();
        	}
        });
        btnAtualizar.setBounds(23, 269, 81, 23);
        contentPane.add(btnAtualizar);
		
	}
	
	
	
	public Integer getLoteSelecionado() {
        return getLoteSelecionado();
    }
	
	public void preencherCampos(Morador morador) {
		
		 	txtNome.setText(morador.getNome());
	        txtCpf.setText(morador.getCpf());
	        txtTelefone.setText(morador.getTelefone());
	        txtEmail.setText(morador.getEmail());
	        txtLoteSelecionado.setText(String.valueOf(morador.getIdLote()));
	        loteSelecionado = morador.getIdLote();
		
	}
	
	
	public void arrumarLabels (JLabel l) {
    	
    	l.setForeground(new Color(97, 72, 44));
    	l.setFont(new Font("Candara Light", Font.BOLD, 20));
    	
    }
    
    public void arrumarTextfields (JTextField t) {
		
		t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
		t.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(97, 72, 44)));
		
	}
    
    public void arrumarTextfields (JFormattedTextField t) {
		
		t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
		t.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(97, 72, 44)));
		
	}
}
