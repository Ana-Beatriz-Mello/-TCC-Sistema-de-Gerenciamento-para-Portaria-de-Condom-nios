package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.ControllerEncomenda;
import controller.ControllerFuncionario;
import model.Encomenda;
import model.Funcionario;
import util.RoundedBorder;
import view.dialogues.SelecaoLote;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class ViewEncomenda extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtLoteSelecionado 	= new JTextField();
	private JTextField txtCodIdentificador 	= new JTextField();
	private JTextField txtCodRastreio 		= new JTextField();
	
	private JLabel lbCodIdentificador 		= new JLabel("Código Identificador");
	private JLabel lbCodRastreio 			= new JLabel("Código de Rastreio");
	private JLabel lbLote 					= new JLabel("Lote");
	
	JButton btnVoltar 						= new RoundedBorder("Voltar",15);
	JButton btnSalvar 						= new RoundedBorder("Salvar",15);
	
	private JButton btnSelecionarLote		= new RoundedBorder("Selecionar",15);
	private Integer loteSelecionado;
	
	private String idUser 					= null;
    private int tipoUser 					= 999;
    

	public ViewEncomenda(String idUser, int tipoUser, Encomenda encomenda) {
		setTitle("Cadastro de Encomendas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

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
			JOptionPane.showMessageDialog(ViewEncomenda.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
		
		setContentPane(contentPane);
		setResizable(false);
		contentPane.setLayout(null);
		
		arrumarLabels(lbCodRastreio);
		lbCodRastreio.setBounds(10, 22, 202, 22);
		contentPane.add(lbCodRastreio);
		
		arrumarLabels(lbCodIdentificador);
		lbCodIdentificador.setBounds(10, 75, 202, 22);
		contentPane.add(lbCodIdentificador);
		
		arrumarTextfields(txtLoteSelecionado);
		txtLoteSelecionado.setEditable(false);
		txtLoteSelecionado.setBounds(222, 134, 81, 20);
		contentPane.add(txtLoteSelecionado);
		
		arrumarLabels(lbLote);
		lbLote.setBounds(10, 133, 104, 22);
		contentPane.add(lbLote);
		
		arrumarTextfields(txtCodIdentificador);
		txtCodIdentificador.setBounds(222, 76, 190, 20);
		contentPane.add(txtCodIdentificador);
		txtCodIdentificador.setColumns(10);
		
		arrumarTextfields(txtCodRastreio);
		txtCodRastreio.setColumns(10);
		txtCodRastreio.setBounds(222, 22, 190, 20);
		contentPane.add(txtCodRastreio);
		
		btnVoltar.setBounds(312, 225, 100, 25);
		contentPane.add(btnVoltar);
		
		
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ControllerFuncionario cf = new ControllerFuncionario();
				ControllerEncomenda ce = new ControllerEncomenda();
				
				try {
						Funcionario recebedor = cf.buscarPorCpf(idUser);
						Encomenda encomenda = new Encomenda (
					
								txtCodRastreio.getText(),
								txtCodIdentificador.getText(),
								Integer.parseInt(txtLoteSelecionado.getText()),
								recebedor

						);
						ce.incluirEncomenda(encomenda);
			    }
				catch (Exception ex) {	
					JOptionPane.showMessageDialog(ViewEncomenda.this, "Problema ao cadastrar encomenda! Preencha pelo menos o lote e o código de rastreio.", "Aviso", JOptionPane.WARNING_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(ViewEncomenda.this, "Encomenda incluída com sucesso!", "Aviso", JOptionPane.WARNING_MESSAGE);
				dispose();
			}
		});
		btnSalvar.setBounds(21, 225, 100, 25);
		contentPane.add(btnSalvar);
		
		btnSelecionarLote.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SelecaoLote dialog = new SelecaoLote(ViewEncomenda.this);
                dialog.setVisible(true);

                Integer lote = dialog.getLoteSelecionado();
                if (lote != null) {
                    loteSelecionado = lote;
                    txtLoteSelecionado.setText(String.valueOf(loteSelecionado));
                }
				
			}
		});
		btnSelecionarLote.setBounds(313, 133, 99, 23);
		contentPane.add(btnSelecionarLote);
		
		btnVoltar.addActionListener(e -> dispose());
		
		
		
	}
	
	public Integer getLoteSelecionado() {
        return getLoteSelecionado();
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
