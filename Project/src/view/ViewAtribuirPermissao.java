package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.ControllerMorador;
import model.Morador;
import util.RoundedBorder;
import view.dialogues.SelecaoLote;
import view.dialogues.SelecaoMorador;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewAtribuirPermissao extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtLoteSelecionado 		= new JTextField();
    private JTextField txtMoradorSelecionado 	= new JTextField();
    
    private JButton btnSelecionarLote 			= new RoundedBorder("Selecionar Lote",15);
    private JButton btnSelecionarMorador 		= new RoundedBorder("Selecionar Morador",15);
    private JButton btnExecutar 				= new RoundedBorder("Atribuir Permissão",15); // botão que muda de função (atribuir/retirar)
    private JButton btnAlternarModo 			= new RoundedBorder("Alternar para Retirar Permissão",15); 
    
    private JLabel lblModo 						= new JLabel("Modo atual: Atribuir permissão");
    private JLabel lblMorador 					= new JLabel("Morador:");
    private JLabel lblLote 						= new JLabel("Lote:");

    private Morador moradorSelecionado 			= null;
    private Integer loteSelecionado 			= null;

    private boolean modoAtribuir 				= true; // true = atribuir, false = retirar

    private String idUser 						= null;
    private int tipoUser 						= 999;

    public ViewAtribuirPermissao(String idUser, int tipoUser) {
    	getContentPane().setBackground(new Color(255, 255, 255));

        this.idUser = idUser;
        this.tipoUser = tipoUser;

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
			JOptionPane.showMessageDialog(ViewAtribuirPermissao.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//

        setTitle("Gerenciar Permissões");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        GridBagConstraints gbcModo = new GridBagConstraints();
        gbcModo.gridx = 0;
        gbcModo.gridy = 0;
        gbcModo.gridwidth = 3;
        gbcModo.insets = new Insets(10, 10, 20, 10);
        gbcModo.anchor = GridBagConstraints.CENTER;
        contentPanel.add(lblModo, gbcModo);
        
                // ====== Morador ======
                GridBagConstraints gbcLabelMorador = new GridBagConstraints();
                gbcLabelMorador.gridx = 0;
                gbcLabelMorador.gridy = 2;
                gbcLabelMorador.insets = new Insets(10, 10, 10, 10);
                gbcLabelMorador.anchor = GridBagConstraints.EAST;
                contentPanel.add(lblMorador, gbcLabelMorador);
                
                        
                        btnSelecionarMorador.addActionListener(new ActionListener() {
                        	public void actionPerformed(ActionEvent e) {
                        		abrirSelecaoMorador();
                        	}
                        });
                        
                
                txtMoradorSelecionado.setEditable(false);
                GridBagConstraints gbcTxtMorador = new GridBagConstraints();
                gbcTxtMorador.gridx = 1;
                gbcTxtMorador.gridy = 2;
                gbcTxtMorador.weightx = 1.0;
                gbcTxtMorador.fill = GridBagConstraints.HORIZONTAL;
                gbcTxtMorador.insets = new Insets(10, 10, 10, 10);
                contentPanel.add(txtMoradorSelecionado, gbcTxtMorador);
                GridBagConstraints gbcBtnMorador = new GridBagConstraints();
                gbcBtnMorador.gridx = 2;
                gbcBtnMorador.gridy = 2;
                gbcBtnMorador.insets = new Insets(10, 10, 10, 10);
                contentPanel.add(btnSelecionarMorador, gbcBtnMorador);
        
                // LOTE
                GridBagConstraints gbcLabelLote = new GridBagConstraints();
                gbcLabelLote.gridx = 0;
                gbcLabelLote.gridy = 3;
                gbcLabelLote.insets = new Insets(10, 10, 10, 10);
                gbcLabelLote.anchor = GridBagConstraints.EAST;
                contentPanel.add(lblLote, gbcLabelLote);
        
                
                btnSelecionarLote.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		abrirSelecaoLote();
                	}
                });
                
                
                txtLoteSelecionado.setEditable(false);
                GridBagConstraints gbcTxtLote = new GridBagConstraints();
                gbcTxtLote.gridx = 1;
                gbcTxtLote.gridy = 3;
                gbcTxtLote.weightx = 1.0;
                gbcTxtLote.fill = GridBagConstraints.HORIZONTAL;
                gbcTxtLote.insets = new Insets(10, 10, 10, 10);
                contentPanel.add(txtLoteSelecionado, gbcTxtLote);
                GridBagConstraints gbcBtnLote = new GridBagConstraints();
                gbcBtnLote.gridx = 2;
                gbcBtnLote.gridy = 3;
                gbcBtnLote.insets = new Insets(10, 10, 10, 10);
                contentPanel.add(btnSelecionarLote, gbcBtnLote);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 100, 10));
        panelBotoes.setBackground(new Color(255, 255, 255));

        
        btnExecutar.setEnabled(false);
        btnAlternarModo.setLocation(50,btnAlternarModo.getY());

        

        panelBotoes.add(btnExecutar);
        panelBotoes.add(btnAlternarModo);

        getContentPane().add(panelBotoes, BorderLayout.SOUTH);
        
        arrumarLabels(lblModo);
        arrumarLabels(lblMorador);
        arrumarLabels(lblLote);

        // Eventos
        btnExecutar.addActionListener(e -> executarAcao());
        btnAlternarModo.addActionListener(e -> alternarModo());
        
        
    }

    private void abrirSelecaoLote() {
    	SelecaoLote selecao = null;
    	if (modoAtribuir) {
    		selecao = new SelecaoLote(this);
    	}
    	else {
    		if (moradorSelecionado != null || txtMoradorSelecionado.getText() != "" || txtMoradorSelecionado.getText() != null) {
    			selecao = new SelecaoLote(this, moradorSelecionado.getId());
    		}
    		else {
    			JOptionPane.showMessageDialog(this, "Selecione um morador para retirar suas permissões!");
    			return;
    		}
    	}
    	selecao.setVisible(true);
        Integer lote = selecao.getLoteSelecionado();
        if (lote != null) {
            loteSelecionado = lote;
            txtLoteSelecionado.setText("Lote " + loteSelecionado);
            verificarCampos();
        }
    }

    private void abrirSelecaoMorador() {
    	SelecaoMorador selecao = new SelecaoMorador(this, true);
    	selecao.setVisible(true);
        moradorSelecionado = selecao.getMoradorSelecionado();
        if (moradorSelecionado != null) {
            txtMoradorSelecionado.setText(moradorSelecionado.getNome());
            verificarCampos();
        }
    }

    private void verificarCampos() {
        btnExecutar.setEnabled(loteSelecionado != null && moradorSelecionado != null);
    }

    private void alternarModo() {
        modoAtribuir = !modoAtribuir;

        if (modoAtribuir) {
            lblModo.setText("Modo atual: Atribuir permissão");
            lblModo.setForeground(new Color(0, 128, 0));
            btnExecutar.setText("Atribuir Permissão");
            btnAlternarModo.setText("Alternar para Retirar Permissão");
        } else {
            lblModo.setText("Modo atual: Retirar permissão");
            lblModo.setForeground(new Color(180, 0, 0));
            btnExecutar.setText("Retirar Permissão");
            btnAlternarModo.setText("Alternar para Atribuir Permissão");
        }
        txtMoradorSelecionado.setText("");
        txtLoteSelecionado.setText("");
        loteSelecionado = null;
        moradorSelecionado = null;
        btnExecutar.setEnabled(false);
    }

    private void executarAcao() {
        if (loteSelecionado == null || moradorSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione o lote e o morador!");
            return;
        }

        try {
            ControllerMorador controller = new ControllerMorador();

            if (modoAtribuir) {
                controller.concederPermissao(moradorSelecionado, loteSelecionado);
                JOptionPane.showMessageDialog(this, "Permissão atribuída com sucesso!");
            } else {
            	if (loteSelecionado == moradorSelecionado.getIdLote()) {
            		
            		JOptionPane.showMessageDialog(ViewAtribuirPermissao.this, "Não pode retirar a permissão de um morador para o lote no qual mora.", "Erro", JOptionPane.WARNING_MESSAGE);
            		return;
            		
            	}
                controller.retirarPermissao(moradorSelecionado, loteSelecionado);
                JOptionPane.showMessageDialog(this, "Permissão retirada com sucesso!");
            }

            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao processar permissão: " + e.getMessage());
        }
    }
    
    public void arrumarLabels (JLabel l) {
    	
    	l.setForeground(new Color(97, 72, 44));
    	l.setFont(new Font("Candara Light", Font.BOLD, 20));
    	
    }
}
