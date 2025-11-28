package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import controller.ControllerAluguel;
import controller.ControllerLugar;
import model.Aluguel;
import model.Lugar;
import util.RoundedBorder;

import java.awt.Color;
import java.awt.Cursor;

public class ViewAluguel extends JFrame {

	private static final long serialVersionUID = 1L;
	
	
	private JPanel panelLugares;
    private JPanel panelCalendario;
    private JComboBox<String> comboMes;
    private JComboBox<Integer> comboAno;
    private Lugar lugarSelecionado;
	
	
	private String idUser = null;
    private int tipoUser = 999;


	public ViewAluguel(String idUser, int tipoUser) throws SQLException {
		setBackground(new Color(255, 255, 255));

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
			JOptionPane.showMessageDialog(ViewAluguel.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//
		
		this.setMinimumSize(new Dimension(600, 400));
		
		
		setTitle("Aluguel de Lugares");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Painel lateral (lugares)
        panelLugares = new JPanel();
        panelLugares.setBackground(new Color(255, 255, 255));
        panelLugares.setLayout(new BoxLayout(panelLugares, BoxLayout.Y_AXIS));
        JScrollPane scrollLugares = new JScrollPane(panelLugares);
        scrollLugares.setPreferredSize(new Dimension(300, 0));
        getContentPane().add(scrollLugares, BorderLayout.WEST);

        // Painel principal (calendário)
        panelCalendario = new JPanel(new BorderLayout());
        panelCalendario.setBackground(new Color(255, 255, 255));
        getContentPane().add(panelCalendario, BorderLayout.CENTER);

        carregarLugares();
    }

	private void carregarLugares() throws SQLException {
	    ControllerLugar controller = new ControllerLugar();
	    List<Lugar> lugares = controller.buscarTodosLugares();

	    panelLugares.removeAll(); // limpa antes de recarregar

	    for (Lugar lugar : lugares) {
	        JPanel card = new JPanel(new BorderLayout());
	        TitledBorder border = BorderFactory.createTitledBorder(lugar.getNome());
	        border.setTitleFont(new Font("Candara Light", Font.BOLD, 16));
	        card.setBorder(border);
	        
	        card.setBackground(Color.WHITE);
	        
	        // Informações do lugar
	        JTextArea info = new JTextArea();
	        info.setText(lugar.getDescricao() + "\nPreço: " + lugar.getPreco() + "\nHoras Máximas: " + lugar.getMaxHoras());	        
	        arrumarTextAreas(info);
	        card.add(info);
	        
	        JScrollPane scrollPane = new JScrollPane(info);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Mostra a barra de rolagem quando necessário
	        scrollPane.setPreferredSize(new Dimension(260, 100)); // Definir uma altura preferencial para o scroll
	        
	        // Adicionar o JScrollPane no painel principal (card)
	        card.add(scrollPane, BorderLayout.CENTER);
	        
	        // Aumenta a altura dos cartões
	        card.setPreferredSize(new Dimension(260, 160));
	        card.setMaximumSize(new Dimension(260, 160));
	        card.setAlignmentX(LEFT_ALIGNMENT);

	        // Painel de botões
	        JPanel botoes = new JPanel();
	        botoes.setBackground(new Color(247, 230, 252));
	        RoundedBorder btnVerReservas = new RoundedBorder("Ver reservas",15);
	        
	        botoes.add(btnVerReservas);
	        if (this.tipoUser == 1) {
	        	RoundedBorder btnMarcar = new RoundedBorder("Marcar horário",15);
	        	botoes.add(btnMarcar);
	        	btnMarcar.addActionListener(e -> abrirAgendamento(lugar));
	        }
	          
	        card.add(botoes, BorderLayout.SOUTH);

	        // Ações dos botões
	        btnVerReservas.addActionListener(e -> {
	            try {
	                mostrarCalendario(lugar);
	            } catch (Exception e1) {
	                e1.printStackTrace();
	            }
	        });
	        

	        // adiciona o card ao painel de lugares
	        panelLugares.add(card);
	        panelLugares.add(Box.createVerticalStrut(8)); // espaçamento entre os cards
	    }

	    panelLugares.revalidate();
	    panelLugares.repaint();
	}


    private void mostrarCalendario(Lugar lugar) throws Exception {
        this.lugarSelecionado = lugar;
        panelCalendario.removeAll();

        // Topo com mês/ano
        JPanel topo = new JPanel();
        String[] meses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                           "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
        comboMes = new JComboBox<>(meses);
        comboAno = new JComboBox<>();
        int anoAtual = java.time.Year.now().getValue();
        for (int i = anoAtual; i <= anoAtual + 2; i++) comboAno.addItem(i);

        topo.add(comboMes);
        topo.add(comboAno);
        
        comboMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        
        panelCalendario.add(topo, BorderLayout.NORTH);

        // Painel com os dias
        JPanel dias = gerarCalendario(lugar, LocalDate.now());
        panelCalendario.add(dias, BorderLayout.CENTER);

        comboMes.addActionListener(e -> {
			try {
				atualizarCalendario();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
        comboAno.addActionListener(e -> {
			try {
				atualizarCalendario();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
		});

        panelCalendario.revalidate();
        panelCalendario.repaint();
    }

    private JPanel gerarCalendario(Lugar lugar, LocalDate dataBase) throws Exception {
        // Cria um painel principal com BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel header = new JPanel(new GridLayout(1, 7, 5, 5));
        String[] diasSemana = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String dia : diasSemana) {
            JLabel lbl = new JLabel(dia, JLabel.CENTER);
            lbl.setBorder(new EmptyBorder(5, 5, 5, 5));
            header.add(lbl);
        }
        panel.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 7, 5, 5));
        LocalDate primeiroDia = dataBase.withDayOfMonth(1);
        int diaSemanaInicio = primeiroDia.getDayOfWeek().getValue(); // 1 = seg, 7 = dom
        int diasNoMes = dataBase.lengthOfMonth();
        LocalDateTime dateTime = dataBase.atStartOfDay();

        ControllerAluguel controller = new ControllerAluguel();
        List<Aluguel> alugueis = controller.buscarAluguelPorMes(lugar, dateTime);

        // Ajuste: preencher espaços vazios antes do primeiro dia
        int inicio = (diaSemanaInicio % 7);
        for (int i = 0; i < inicio; i++) grid.add(new JLabel(""));

        // Preenche os dias
        for (int dia = 1; dia <= diasNoMes; dia++) {
            JPanel cell = new JPanel();
            cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
            cell.setBorder(BorderFactory.createTitledBorder(String.valueOf(dia)));
            cell.setBackground(Color.WHITE);

            LocalDate diaAtual = dataBase.withDayOfMonth(dia);

            alugueis.stream()
                .filter(a -> a.getHoraComeco().toLocalDate().equals(diaAtual))
                .sorted(Comparator.comparing(Aluguel::getHoraComeco))
                .forEach(a -> {
                    JTextArea lbl = new JTextArea("Lote: " +
                        a.getResponsavel().getIdLote() + " (" +
                    	a.getResponsavel().getNome() + ") - (" +
                        a.getHoraComeco().toLocalTime().getHour() + "h" + String.format("%02d", a.getHoraComeco().toLocalTime().getMinute()) + " - " +
                        a.getHoraFimPrevisto().toLocalTime().getHour() + "h" + String.format("%02d", a.getHoraComeco().toLocalTime().getMinute()) + ")\n"
                    );
                    arrumarTextAreas(lbl);
                    cell.add(lbl);
                });
            
            
            JScrollPane scrollPane = new JScrollPane(cell);
	        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  // Mostra a barra de rolagem quando necessário
	        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	        scrollPane.setPreferredSize(new Dimension(cell.getWidth(), cell.getHeight())); // Definir uma altura preferencial para o scroll
            grid.add(scrollPane);
        }

        // Adiciona a grade completa ao painel principal
        panel.add(grid, BorderLayout.CENTER);

        return panel;
    }


    private void atualizarCalendario() throws Exception {
        int mesSelecionado = comboMes.getSelectedIndex() + 1;
        int anoSelecionado = (int) comboAno.getSelectedItem();
        LocalDate novaData = LocalDate.of(anoSelecionado, mesSelecionado, 1);

        JPanel novoGrid = gerarCalendario(lugarSelecionado, novaData);
        panelCalendario.remove(1); // remove o antigo grid
        panelCalendario.add(novoGrid, BorderLayout.CENTER);
        panelCalendario.revalidate();
        panelCalendario.repaint();
    }
    
    private void abrirAgendamento(Lugar lugar) {
    	
    	ViewCadastrarAluguel viewCA = new ViewCadastrarAluguel(ViewAluguel.this.idUser, ViewAluguel.this.tipoUser, lugar);
		viewCA.setVisible(true);
    	
    }
    
    public void arrumarTextAreas (JTextArea t) {
    	
    	t.setForeground(new Color(97, 72, 44));
    	t.setFont(new Font("Candara Light", Font.BOLD, 16));
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
		

