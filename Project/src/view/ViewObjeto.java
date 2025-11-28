package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.ControllerObjeto;
import controller.ControllerLugar;
import model.Lugar;
import model.Objeto;
import util.RoundedBorder;
import view.dialogues.SelecaoLugar;

public class ViewObjeto extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JTextField txtNome 			       = new JTextField();
    private JTextField txtLugarSelecionado     = new JTextField();
    //private JCheckBox cbDisponivel;
    private JButton btnSelecionarLugar 	       = new RoundedBorder("Selecionar",15);
    private JButton btnSalvar 			       = new RoundedBorder("Salvar",15);
    private JButton btnAtualizar 		       = new RoundedBorder("Salvar",15);
    private JButton btnVoltar 			       = new RoundedBorder("Voltar",15);
    
    private JLabel lblNome 				       = new JLabel("Nome do objeto:");
    private JLabel lblLugar 			   	   = new JLabel("Lugar associado:");

    private Integer idLugarSelecionado;
    private Objeto objetoConsultado;
    
    private String idUser					  = null;
	private int tipoUser 					  = 999;

    private ControllerObjeto controllerObjeto;
    private ControllerLugar controllerLugar;

    public ViewObjeto(String idUser, int tipoUser, Objeto objetoConsultado) {
    	setTitle("Cadastro de Objetos");
        this.objetoConsultado = objetoConsultado;
        
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
			JOptionPane.showMessageDialog(ViewObjeto.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
    	

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(250, 250, 500, 280);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setResizable(false); // Padrão para os cadastros simples

        arrumarLabels(lblNome);
        lblNome.setBounds(10, 38, 150, 23);
        contentPane.add(lblNome);

        arrumarTextfields(txtNome);
        txtNome.setBounds(170, 38, 180, 20);
        contentPane.add(txtNome);

        arrumarLabels(lblLugar);
        lblLugar.setBounds(10, 93, 150, 29);
        contentPane.add(lblLugar);

        arrumarTextfields(txtLugarSelecionado);
        txtLugarSelecionado.setBounds(170, 97, 180, 20);
        txtLugarSelecionado.setEditable(false);
        contentPane.add(txtLugarSelecionado);

        btnSelecionarLugar.setBounds(360, 96, 101, 23);
        contentPane.add(btnSelecionarLugar);

        btnSalvar.setBounds(23, 177, 81, 23);
        contentPane.add(btnSalvar);

        btnAtualizar.setBounds(23, 177, 81, 23);
        btnAtualizar.setVisible(false);
        contentPane.add(btnAtualizar);

        btnVoltar.setBounds(380, 177, 81, 23);
        contentPane.add(btnVoltar);
        btnVoltar.addActionListener(e -> dispose());

        
        if (objetoConsultado != null) {
            preencherCampos(objetoConsultado);
            btnSalvar.setVisible(false);
            btnAtualizar.setVisible(true);
        }

        
        btnSelecionarLugar.addActionListener(e -> {
            abrirSelecao();
        });

      
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarObjeto();
            }
        });

       
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarObjeto();
            }
        });
    }

    private void preencherCampos(Objeto obj) {
        txtNome.setText(obj.getNome());
        // cbDisponivel.setSelected(obj.isDisponivel());
        txtLugarSelecionado.setText(obj.getLugar().getNome());
        idLugarSelecionado = obj.getLugar().getId();
    }

    private void salvarObjeto() {
        try {
        	controllerLugar = new ControllerLugar();
            Lugar lugar = controllerLugar.buscarPorId(idLugarSelecionado);
            Objeto obj = new Objeto(txtNome.getText(), lugar);
            
            controllerObjeto = new ControllerObjeto();
            controllerObjeto.inserirObjeto(obj);

            JOptionPane.showMessageDialog(this, "Objeto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao salvar objeto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void abrirSelecao() {
    	
    	SelecaoLugar dialog = new SelecaoLugar(ViewObjeto.this);
        dialog.setVisible(true);

        Integer idLugar = dialog.getIdLugarSelecionado();
        if (idLugar != null) {
            try {
            	controllerLugar = new ControllerLugar();
                Lugar lugar = controllerLugar.buscarPorId(idLugar);
                idLugarSelecionado = idLugar;
                txtLugarSelecionado.setText(lugar.getNome());
            } catch (Exception ex) {
            	JOptionPane.showMessageDialog(this, "Erro ao selecionar o lugar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    	
    }

    private void atualizarObjeto() {
        try {
        	controllerLugar   = new ControllerLugar();
            Lugar lugar = controllerLugar.buscarPorId(idLugarSelecionado);
            objetoConsultado.setNome(txtNome.getText());
            objetoConsultado.setLugar(lugar);

            controllerObjeto = new ControllerObjeto();
            controllerObjeto.atualizarObjeto(objetoConsultado);

            JOptionPane.showMessageDialog(this, "Objeto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            // ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao atualizar objeto!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    public void arrumarBotoes (JButton b) {
    	
    	
    	
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
