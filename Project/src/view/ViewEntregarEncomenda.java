package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import controller.ControllerEncomenda;
import model.Encomenda;
import model.Morador;
import util.RoundedBorder;
import view.dialogues.SelecaoMorador;
import view.dialogues.SelecaoLote;

public class ViewEntregarEncomenda extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JButton btnSelecionarMorador;
    private JButton btnSelecionarLote;
    private JButton btnConfirmar;
    private JCheckBox btnSelecionarTodas;
    private JPanel panelEncomendas;
    private JScrollPane scrollPane = new JScrollPane();

    private Morador moradorSelecionado;
    private Integer loteSelecionado;
    
    private String idUser = null;
    private int tipoUser = 999;

    private ControllerEncomenda controllerEncomenda = new ControllerEncomenda();

    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private List<Encomenda> encomendasDisponiveis = new ArrayList<>();
    
    private JTextArea lbMoradorSelecionado = new JTextArea("");
    private JTextArea lbLoteSelecionado = new JTextArea("");
 
    public ViewEntregarEncomenda(String idUser, int tipoUser) {
        setTitle("Entregar Encomendas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 500);

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
			JOptionPane.showMessageDialog(ViewEntregarEncomenda.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
        
       
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);
        
        setResizable(false);

        if (tipoUser == 1) { // FUNCIONÁRIO
        	
        	// Botão para selecionar morador
        
	        btnSelecionarMorador = new RoundedBorder("Selecionar Morador",15);
	        btnSelecionarMorador.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		abrirSelecaoMorador();
	        	}
	        });
	        btnSelecionarMorador.setBounds(20, 11, 180, 25);
	        contentPane.add(btnSelecionarMorador);
	        
	        // Botão para selecionar lotes
	
	        btnSelecionarLote = new RoundedBorder("Selecionar Lote",15);
	        btnSelecionarLote.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		abrirSelecaoLote();
	        	}
	        });
	        btnSelecionarLote.setBounds(220, 11, 180, 25);
	        btnSelecionarLote.setEnabled(false);
	        contentPane.add(btnSelecionarLote);
	
	        // Botão para selecionar todas as encomendas
	        
	        btnSelecionarTodas = new JCheckBox("Selecionar Todas");
	        btnSelecionarTodas.setBackground(Color.WHITE);
	        btnSelecionarTodas.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		selecionarTodas();
	        	}
	        });
	        btnSelecionarTodas.setBounds(420, 11, 140, 25);
	        btnSelecionarTodas.setEnabled(false);
	        contentPane.add(btnSelecionarTodas);
	        
	        // Botão de confimar
	        
	        btnConfirmar = new RoundedBorder("Confirmar Recebimento",15);
	        btnConfirmar.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {
	        		confirmarRecebimento();
	        	}
	        });
	        btnConfirmar.setBounds(200, 420, 200, 30);
	        btnConfirmar.setEnabled(false);
	        contentPane.add(btnConfirmar);
	        
        }
        
        panelEncomendas = new JPanel();
        panelEncomendas.setLayout(new BoxLayout(panelEncomendas, BoxLayout.Y_AXIS));


        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setColumnHeaderView(panelEncomendas);
        scrollPane.setBounds(20, 147, 540, 259);
        contentPane.add(scrollPane);
        
        
        lbMoradorSelecionado.setBounds(20, 44, 195, 101);
        contentPane.add(lbMoradorSelecionado);
        lbLoteSelecionado.setBackground(new Color(255, 255, 255));
        
        
        lbLoteSelecionado.setBounds(225, 44, 307, 101);
        contentPane.add(lbLoteSelecionado);
        
        if (tipoUser == 0) { // MORADOR
        
        	visualizarEncomendas();
        	lbMoradorSelecionado.setText("Usuário:\n" + idUser);
        	lbLoteSelecionado.setBounds(lbLoteSelecionado.getX(), 40, lbLoteSelecionado.getWidth(), lbLoteSelecionado.getHeight());
        	lbMoradorSelecionado.setBounds(lbMoradorSelecionado.getX(), 40, lbMoradorSelecionado.getWidth(), lbMoradorSelecionado.getHeight());
        }
        
        arrumarTextAreas(lbLoteSelecionado);
        arrumarTextAreas(lbMoradorSelecionado);
        
    }

    private void abrirSelecaoMorador() {
        SelecaoMorador dialog = new SelecaoMorador(this, false);
        dialog.setVisible(true);
        moradorSelecionado = dialog.getMoradorSelecionado();
        if (moradorSelecionado != null) {
            btnSelecionarLote.setEnabled(true);
            lbMoradorSelecionado.setText(moradorSelecionado.getNome());
            
            
            lbLoteSelecionado.setText("");
            
            panelEncomendas.removeAll();
            panelEncomendas.revalidate();
            panelEncomendas.repaint();
            
            JOptionPane.showMessageDialog(this, "Morador selecionado: " + moradorSelecionado.getNome());
        }
    }

    private void abrirSelecaoLote() {
        if (moradorSelecionado == null) return;

        SelecaoLote dialog = new SelecaoLote(this, moradorSelecionado.getId());
        dialog.setVisible(true);
        loteSelecionado = dialog.getLoteSelecionado();

        if (loteSelecionado != null) {
            carregarEncomendas();
            lbLoteSelecionado.setText(Integer.toString(loteSelecionado));
        }
    }

    private void visualizarEncomendas() {
    	try {
			encomendasDisponiveis = controllerEncomenda.buscarEncomendasDeMorador(idUser);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro ao carregar encomendas!");
			return;
		}
    	
    	lbLoteSelecionado.setText("Quantidade de Encomendas:\n" + encomendasDisponiveis.size());
    	
    	lbLoteSelecionado.setBounds(lbLoteSelecionado.getX(), 5, lbLoteSelecionado.getWidth(), lbLoteSelecionado.getHeight());
    	lbMoradorSelecionado.setBounds(lbMoradorSelecionado.getX(), 10, lbMoradorSelecionado.getWidth(), lbMoradorSelecionado.getHeight());
    	
    	for (Encomenda e : encomendasDisponiveis) {
    	    JPanel card = new JPanel();
    	    card.setLayout(new BorderLayout());
    	    card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	    card.setBackground(Color.WHITE);
    	    card.setMaximumSize(new Dimension(500, 80));

    	    // Informações da encomenda
    	    JLabel lblInfo = new JLabel("<html><b>Código Identificador: " + e.getCodIdentificador() + "</b><br/>"
    	            + "Código de Rastreio: " + e.getCodRastreio() + "<br/>Lote: " + e.getDestinatario() + "</html>");
    	    arrumarLabels(lblInfo);
    	    lblInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
    	    card.add(lblInfo, BorderLayout.CENTER);

    	    panelEncomendas.add(Box.createVerticalStrut(5));
    	    panelEncomendas.add(card);
    	}
    	
    	
    	panelEncomendas.revalidate();
        panelEncomendas.repaint();
        scrollPane.setViewportView(panelEncomendas);
        
    }
    
    private void carregarEncomendas() {
        try {
            panelEncomendas.removeAll();
            
            if (tipoUser == 1) { // FUNCIONÁRIO
            	encomendasDisponiveis = controllerEncomenda.buscarEncomendasDeUmLote(loteSelecionado);
            	checkBoxes.clear();
        	}

            for (Encomenda e : encomendasDisponiveis) {
                JPanel card = new JPanel();
                card.setLayout(new BorderLayout());
                card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                card.setBackground(Color.WHITE);
                card.setMaximumSize(new Dimension(500, 70));

                JCheckBox check = new JCheckBox();
                checkBoxes.add(check);
                card.add(check, BorderLayout.WEST);

                JLabel lblInfo = new JLabel("<html><b>Código Identificador: " + e.getCodIdentificador() + "</b><br/>Código de Rastreio: " + e.getCodRastreio() + "</html>");
                arrumarLabels(lblInfo);
                lblInfo.setBorder(new EmptyBorder(10, 10, 10, 10));
                card.add(lblInfo, BorderLayout.CENTER);

                panelEncomendas.add(Box.createVerticalStrut(5));
                panelEncomendas.add(card);
            }

            panelEncomendas.revalidate();
            panelEncomendas.repaint();
            scrollPane.setViewportView(panelEncomendas);
            
            btnSelecionarTodas.setEnabled(true);
            btnConfirmar.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar encomendas!");
        }
    }

    private void selecionarTodas() {
    	if (btnSelecionarTodas.isSelected())
    		for (JCheckBox cb : checkBoxes) cb.setSelected(true);
    	else
    		for (JCheckBox cb : checkBoxes) cb.setSelected(false);
    }

    private void confirmarRecebimento() {

    	// Checa se alguma encomenda foi selecionada
    	boolean flag = false;
    	for (int i = 0; i < checkBoxes.size(); i++) {
    		if (checkBoxes.get(i).isSelected()) {
                flag = true;
            }
    	}
    	
    	if (!flag) {
    		JOptionPane.showMessageDialog(this, "Nenhuma encomenda selecionada!");
        	return;
    	}
    	
    	try {
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    controllerEncomenda.receberEncomenda(encomendasDisponiveis.get(i), moradorSelecionado);
                }
            }
            
            	JOptionPane.showMessageDialog(this, "Encomendas registradas como recebidas!");
            	dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar recebimento!");
        }
    }
    
    public void arrumarLabels (JLabel l) {
    	
    	l.setForeground(new Color(97, 72, 44));
    	l.setFont(new Font("Candara Light", Font.BOLD, 20));
    	
    }
    
    public void arrumarTextAreas (JTextArea t) {
    	
    	t.setForeground(new Color(97, 72, 44));
    	t.setFont(new Font("Candara Light", Font.BOLD, 20));
    	t.setOpaque(false);
    	t.setFocusable(false);
    	t.setLineWrap(true);        
        t.setWrapStyleWord(true);  
        t.setEditable(false);       
        t.setOpaque(false);           
        t.setBorder(null);        
        t.setVisible(true);
        t.setCursor(Cursor.getDefaultCursor());
    	((DefaultCaret) t.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
    	
    }
    
}
