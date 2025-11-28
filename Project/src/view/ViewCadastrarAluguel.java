package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import controller.ControllerAluguel;
import model.Aluguel;
import model.Lugar;
import model.Morador;
import util.RoundedBorder;
import view.dialogues.SelecaoMorador;

public class ViewCadastrarAluguel extends JFrame {

    private static final long serialVersionUID = 1L;

    private Lugar lugar;
    private Morador moradorSelecionado;

    private JComboBox<Integer> comboDia, comboMes, comboAno;
    private JSpinner spinHoraInicio;
    private JSpinner spinMinutoInicio;
    private JSpinner spinHoraFim;
    private JSpinner spinMinutoFim;
    private JButton btnSalvar 		     = new RoundedBorder("Salvar", 15);
    private JButton btnSelecionarMorador = new RoundedBorder("Selecionar...", 15);
    
    private JLabel lbData 				 = new JLabel("Data:");
    private JLabel lbInicio 			 = new JLabel("Início:");
    private JLabel lbTermino 			 = new JLabel("Término:");
    private JLabel lbMorador			 = new JLabel("Morador:");

    private String idUser = null;
    private int tipoUser = 999;

    public ViewCadastrarAluguel(String idUser, int tipoUser, Lugar lugar) {

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
			JOptionPane.showMessageDialog(ViewCadastrarAluguel.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//

        this.lugar = lugar;
        setResizable(false);

        setTitle("Cadastrar Aluguel - " + lugar.getNome());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.setBackground(Color.WHITE);

        // DATA
        GridBagConstraints gbcDia = new GridBagConstraints();
        
        GridBagConstraints gbcA = new GridBagConstraints();
        GridBagConstraints gbcB = new GridBagConstraints();
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcA.gridx = 1; gbcA.gridy = 0;
        gbcB.gridx = 2; gbcB.gridy = 0;
        gbcC.gridx = 3; gbcC.gridy = 0;
        gbcA.insets = new Insets(10, 10, 10, 10);
        gbcB.insets = new Insets(10, 10, 10, 10);
        gbcC.insets = new Insets(10, 10, 10, 10);
        
        gbcDia.gridx = 0; gbcDia.gridy = 0;
        gbcDia.insets = new Insets(10, 10, 10, 10);
        form.add(lbData, gbcDia);

        comboDia = new JComboBox<>();
        comboMes = new JComboBox<>();
        comboAno = new JComboBox<>();

        for (int i = 1; i <= 31; i++) comboDia.addItem(i);
        for (int i = 1; i <= 12; i++) comboMes.addItem(i);
        int anoAtual = java.time.Year.now().getValue();
        for (int i = anoAtual; i <= anoAtual + 1; i++) comboAno.addItem(i);
        
        comboDia.setSelectedIndex(LocalDate.now().getDayOfMonth() - 1);
        comboMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

        gbcA.gridx = 1; form.add(comboDia, gbcA);
        gbcB.gridx = 2; form.add(comboMes, gbcB);
        gbcC.gridx = 3; form.add(comboAno, gbcC);

        // HORA INÍCIO
        GridBagConstraints gbcHora = new GridBagConstraints();
        gbcHora.gridx = 0; gbcHora.gridy = 1;
        form.add(lbInicio, gbcHora);
        gbcHora.insets = new Insets(10, 10, 10, 10);

        spinHoraInicio = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        spinMinutoInicio = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));

        gbcHora.gridx = 1; form.add(spinHoraInicio, gbcHora);
        gbcHora.gridx = 2; form.add(new JLabel(":"), gbcHora);
        gbcHora.gridx = 3; form.add(spinMinutoInicio, gbcHora);

        // HORA FIM
        GridBagConstraints gbcFim = new GridBagConstraints();
        gbcFim.gridwidth = 1;
        gbcFim.gridx = 0; gbcFim.gridy = 2;
        form.add(lbTermino, gbcFim);
        gbcFim.insets = new Insets(10, 1, 10, 1);

        spinHoraFim = new JSpinner(new SpinnerNumberModel(9, 0, 23, 1));
        spinMinutoFim = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));

        gbcFim.gridx = 1; form.add(spinHoraFim, gbcFim);
        gbcFim.gridx = 2; form.add(new JLabel(":"), gbcFim);
        gbcFim.gridx = 3; form.add(spinMinutoFim, gbcFim);

        // MORADOR
        GridBagConstraints gbcM = new GridBagConstraints();
        gbcM.gridx = 0; gbcM.gridy = 3;
        gbcM.insets = new Insets(10, 10, 10, 10);
        form.add(lbMorador, gbcM);

        
        gbcM.gridx = 1; gbcM.gridwidth = 3;
        form.add(btnSelecionarMorador, gbcM);
        gbcM.gridwidth = 1;
        
        arrumarLabels(lbData);
        arrumarLabels(lbInicio);
        arrumarLabels(lbTermino);
        arrumarLabels(lbMorador);

        btnSelecionarMorador.addActionListener(e -> {
            SelecaoMorador dialog = new SelecaoMorador(ViewCadastrarAluguel.this, false);
            dialog.setVisible(true);
            moradorSelecionado = dialog.getMoradorSelecionado();

            if (moradorSelecionado != null) {
                btnSelecionarMorador.setText(moradorSelecionado.getNome());
            }
        });

        // BOTÃO SALVAR
        GridBagConstraints gbcSalvar = new GridBagConstraints();
        gbcSalvar.gridx = 0; gbcSalvar.gridy = 4; gbcSalvar.gridwidth = 4;
        gbcSalvar.insets = new Insets(10, 10, 10, 10);
        form.add(btnSalvar, gbcSalvar);

        btnSalvar.addActionListener(e -> salvarAluguel());

        add(form, BorderLayout.CENTER);
    }

    private void salvarAluguel() {
        try {
            int dia = (int) comboDia.getSelectedItem();
            int mes = (int) comboMes.getSelectedItem();
            int ano = (int) comboAno.getSelectedItem();

            int horaInicio = (int) spinHoraInicio.getValue();
            int minutoInicio = (int) spinMinutoInicio.getValue();
            int horaFim = (int) spinHoraFim.getValue();
            int minutoFim = (int) spinMinutoFim.getValue();

            LocalDate data = LocalDate.of(ano, mes, dia);
            LocalDateTime inicio = LocalDateTime.of(data, LocalTime.of(horaInicio, minutoInicio));
            LocalDateTime fim = LocalDateTime.of(data, LocalTime.of(horaFim, minutoFim));

            if (moradorSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Selecione um morador!");
                return;
            }

            Aluguel a = new Aluguel(moradorSelecionado, lugar, inicio, fim);
            ControllerAluguel controller = new ControllerAluguel(); 
            
            // Verifica se já tem luguel marcado no lugar nesse horário inserido
            if (controller.checarAluguelNoMesmoHorario(a)) {
            	JOptionPane.showMessageDialog(this, "Já tem um aluguel marcado nesse horário!");
            	return;
            }
            
            controller.incluirAluguel(a); 
            JOptionPane.showMessageDialog(this, "Aluguel cadastrado com sucesso!");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar aluguel: " + ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    public void arrumarLabels (JLabel l) {
    	
    	l.setForeground(new Color(97, 72, 44));
    	l.setFont(new Font("Candara Light", Font.BOLD, 20));
    	
    }
    
}
