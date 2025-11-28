package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import controller.ControllerFuncionario;
import model.Funcionario;
import util.RoundedBorder;

public class ViewFuncionario extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JLabel lblNome = new JLabel("Nome:");
    private JLabel lblCpf = new JLabel("CPF:");
    private JLabel lblTelefone = new JLabel("Telefone:");

    private JTextField txtNome = new JTextField();
    private JFormattedTextField txtCpf;
    private JTextField txtTelefone = new JTextField();

    private JCheckBox cbAtivo;

    private RoundedBorder btnSalvar = new RoundedBorder("Salvar",15);
    private RoundedBorder btnAtualizar = new RoundedBorder("Atualizar",15);
    private RoundedBorder btnVoltar = new RoundedBorder("Voltar",15);

    private String idUser = null;
    private int tipoUser = 999;
    private Funcionario funcionarioConsultado = null;

    public ViewFuncionario(String idUser, int tipoUser, Funcionario funcionarioConsultado) {
    	setBackground(new Color(175,111,191));
    	setTitle("Cadastro de Funcionários");
        this.idUser = idUser;
        this.tipoUser = tipoUser;
        this.funcionarioConsultado = funcionarioConsultado;
        
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
			JOptionPane.showMessageDialog(ViewFuncionario.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(250, 250, 450, 280);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        setResizable(false); // Padrão para os cadastros simples

        arrumarLabels(lblNome);
        lblNome.setBounds(30, 37, 97, 27);
        contentPane.add(lblNome);

        arrumarLabels(lblCpf);
        lblCpf.setBounds(30, 75, 97, 33);
        contentPane.add(lblCpf);

        arrumarLabels(lblTelefone);
        lblTelefone.setBounds(30, 125, 97, 25);
        contentPane.add(lblTelefone);


        
        arrumarTextfields(txtNome);
        txtNome.setBounds(160, 37, 264, 20);
        contentPane.add(txtNome);

        arrumarTextfields(txtCpf);
        txtCpf.setBounds(160, 81, 200, 20);
        contentPane.add(txtCpf);

        
        
        arrumarTextfields(txtTelefone);
        txtTelefone.setBounds(160, 127, 200, 20);
        contentPane.add(txtTelefone);

        cbAtivo = new JCheckBox("Ativo");
        cbAtivo.setBackground(Color.WHITE);
        cbAtivo.setFont(new Font("Candara Light", Font.BOLD, 15));
        cbAtivo.setBounds(30, 10, 97, 23);
        cbAtivo.setSelected(true);
        contentPane.add(cbAtivo);

        
        btnSalvar.setBounds(30, 200, 100, 25);
        contentPane.add(btnSalvar);

        
        btnAtualizar.setBounds(30, 200, 100, 25);
        btnAtualizar.setVisible(false);
        contentPane.add(btnAtualizar);

        btnVoltar.setBounds(324, 200, 100, 25);
        contentPane.add(btnVoltar);

        if (this.funcionarioConsultado != null) {
            preencherCampos(funcionarioConsultado);
            btnSalvar.setVisible(false);
            btnAtualizar.setVisible(true);
        }

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    salvarFuncionario();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ViewFuncionario.this, "Erro ao salvar: " + ex.getMessage());
                }
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    atualizarFuncionario();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ViewFuncionario.this, "Erro ao atualizar: " + ex.getMessage());
                }
            }
        });

        btnVoltar.addActionListener(e -> dispose());
    }

    private void salvarFuncionario() throws Exception {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String telefone = txtTelefone.getText();

        Funcionario f = new Funcionario(nome, cpf, telefone);
        f.setAtivo(cbAtivo.isSelected());

        ControllerFuncionario control = new ControllerFuncionario();
        control.incluirFuncionario(f);
        
        JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
        dispose();
    }

    private void atualizarFuncionario() throws Exception {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String telefone = txtTelefone.getText();

        Funcionario f = new Funcionario(nome, cpf, telefone);
        f.setAtivo(cbAtivo.isSelected());
        f.setId(ViewFuncionario.this.funcionarioConsultado.getId());

        ControllerFuncionario control = new ControllerFuncionario();
        control.atualizarFuncionario(f);

        JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
        dispose();
    }

    private void preencherCampos(Funcionario f) {
        txtNome.setText(f.getNome());
        txtCpf.setText(f.getCpf());
        txtTelefone.setText(f.getTelefone());
        cbAtivo.setSelected(f.isAtivo());
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
