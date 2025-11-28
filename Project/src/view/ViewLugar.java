package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.ControllerLugar;
import model.Lugar;
import util.RoundedBorder;

public class ViewLugar extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField txtNome 				= new JTextField();
    private JTextArea txtDescricao 			= new JTextArea();
    private JTextField txtMaxHoras 			= new JTextField();
    private JTextField txtPreco 			= new JTextField();
    
    private JScrollPane scrollPane 			= new JScrollPane(txtDescricao);
    
    private JLabel lblDescricao 			= new JLabel("Descrição:");
    private JLabel lblNome 					= new JLabel("Nome:");
    private JLabel lblMaxHoras 				= new JLabel("Max Horas:");
    private JLabel lblPreco 				= new JLabel("Preço:");

    private JButton btnSalvar 				= new RoundedBorder("Salvar",15);
    private JButton btnAtualizar 			= new RoundedBorder("Salvar",15);
    private JButton btnVoltar 				= new RoundedBorder("Voltar",15);

    private Lugar lugarConsultado			= null;
    private String idUser 				    = null;
	private int tipoUser 				    = 999;
    private ControllerLugar controllerLugar = new ControllerLugar();

    public ViewLugar(String idUser, int tipoUser, Lugar lugarConsultado) {
    	setTitle("Cadastro de Lugares");
        this.lugarConsultado = lugarConsultado;
        
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
			JOptionPane.showMessageDialog(ViewLugar.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//

        setResizable(false);
        btnAtualizar.setVisible(false);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 330);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        arrumarLabels(lblNome);
        lblNome.setBounds(23, 32, 81, 23);
        getContentPane().add(lblNome);

        arrumarTextfields(txtNome);
        txtNome.setBounds(201, 32, 160, 20);
        getContentPane().add(txtNome);

        arrumarLabels(lblDescricao);
        lblDescricao.setBounds(23, 169, 119, 34);
        getContentPane().add(lblDescricao);

        arrumarTextfields(txtDescricao);
        txtDescricao.setLineWrap(true);     
        txtDescricao.setWrapStyleWord(true);
        
        // Barra de rolagem da Descrição 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(201, 176, 160, 60);
        getContentPane().add(scrollPane);
        

        arrumarLabels(lblMaxHoras);
        lblMaxHoras.setBounds(23, 113, 104, 45);
        getContentPane().add(lblMaxHoras);

        arrumarTextfields(txtMaxHoras);
        txtMaxHoras.setBounds(201, 125, 160, 20);
        getContentPane().add(txtMaxHoras);

        arrumarLabels(lblPreco);
        lblPreco.setBounds(23, 69, 81, 45);
        getContentPane().add(lblPreco);

        arrumarTextfields(txtPreco);
        txtPreco.setBounds(201, 81, 160, 20);
        getContentPane().add(txtPreco);

        
        btnSalvar.setBounds(23, 257, 81, 23);
        getContentPane().add(btnSalvar);

        btnVoltar.setBounds(280, 257, 81, 23);
        contentPane.add(btnVoltar);

        btnAtualizar.setBounds(23, 257, 81, 23);
        contentPane.add(btnAtualizar);

        // Se veio com lugarConsultado, preenche campos
        if (lugarConsultado != null) {
            preencherCampos(lugarConsultado);
            btnSalvar.setVisible(false);
            btnAtualizar.setVisible(true);
        }

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarLugar();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarLugar();
            }
        });

        btnVoltar.addActionListener(e -> dispose());
    }

    private void preencherCampos(Lugar lugar) {
        txtNome.setText(lugar.getNome());
        txtDescricao.setText(lugar.getDescricao());
        txtMaxHoras.setText(String.valueOf(lugar.getMaxHoras()));
        txtPreco.setText(String.valueOf(lugar.getPreco()));
    }

    private void salvarLugar() {    	
    	try {
            Lugar novoLugar = new Lugar(
                    txtNome.getText(),
                    txtDescricao.getText(),
                    Integer.parseInt(txtMaxHoras.getText()),
                    Integer.parseInt(txtPreco.getText())
            );
            controllerLugar.incluirLugar(novoLugar);

            JOptionPane.showMessageDialog(ViewLugar.this, "Lugar criado com sucesso!", "Aviso", JOptionPane.WARNING_MESSAGE);
            ViewLugar.this.dispose();
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(ViewLugar.this, "Ocorreu um erro! Verifique os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            // ex.printStackTrace();
        }
    }

    private void atualizarLugar() {
        try {
            lugarConsultado.setNome(txtNome.getText());
            lugarConsultado.setDescricao(txtDescricao.getText());
            lugarConsultado.setMaxHoras(Integer.parseInt(txtMaxHoras.getText()));
            lugarConsultado.setPreco(Integer.parseInt(txtPreco.getText()));

            controllerLugar.atualizarLugar(lugarConsultado);

            JOptionPane.showMessageDialog(ViewLugar.this, "Lugar atualizado com sucesso!", "Aviso", JOptionPane.WARNING_MESSAGE);
            ViewLugar.this.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
    
    public void arrumarTextfields (JTextArea t) {
		
		t.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));
		t.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(97, 72, 44)));
		
	}
    
}
