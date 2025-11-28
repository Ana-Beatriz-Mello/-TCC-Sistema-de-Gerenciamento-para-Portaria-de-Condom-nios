package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import controller.ControllerAluguel;
import controller.ControllerObjeto;
import model.Aluguel;
import model.Objeto;
import util.RoundedBorder;

public class ViewFinalizarAluguel extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panelAlugueis = new JPanel();
    private JPanel panelObjetos = new JPanel(new GridLayout(0, 2, 5, 5));
    private JButton btnFinalizar = new RoundedBorder("Finalizar Aluguel",15);

    private Aluguel aluguelSelecionado;
    private List<JCheckBox> checkBoxesObjetos = new ArrayList<>();
    private List<JPanel> cardsAlugueis = new ArrayList<>();

    private String idUser = null;
    private int tipoUser = 999;

    public ViewFinalizarAluguel(String idUser, int tipoUser) {
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
			JOptionPane.showMessageDialog(ViewFinalizarAluguel.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//

        setTitle("Finalizar Aluguéis");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Painel esquerdo: Listagem de aluguéis
        
        panelAlugueis.setLayout(new BoxLayout(panelAlugueis, BoxLayout.Y_AXIS));
        panelAlugueis.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelAlugueis.setBackground(new Color(250, 250, 250));

        JScrollPane scrollAlugueis = new JScrollPane(panelAlugueis);
        
        TitledBorder border1 = BorderFactory.createTitledBorder("Aluguéis até o fim do dia");
        border1.setTitleFont(new Font("Candara Light", Font.BOLD, 20));
        scrollAlugueis.setBorder(border1);
        
        scrollAlugueis.setPreferredSize(new Dimension(350, 0));
        scrollAlugueis.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollAlugueis, BorderLayout.WEST);

        // Painel direito: Objetos emprestados
        
        panelObjetos.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollObjetos = new JScrollPane(panelObjetos);
        
        TitledBorder border2 = BorderFactory.createTitledBorder("Objetos emprestados");
        border2.setTitleFont(new Font("Candara Light", Font.BOLD, 20));
        scrollObjetos.setBorder(border2);
        
        scrollObjetos.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollObjetos, BorderLayout.CENTER);

        
        btnFinalizar.setEnabled(false);
        add(btnFinalizar, BorderLayout.SOUTH);

        carregarAlugueis();

        btnFinalizar.addActionListener(e -> {
            try {
                finalizarAluguel();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao finalizar aluguel: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void carregarAlugueis() {
        try {
            ControllerAluguel controllerAluguel = new ControllerAluguel();
            List<Aluguel> alugueis = controllerAluguel.buscarAluguelParaFinalizar();

            panelAlugueis.removeAll();
            cardsAlugueis.clear();

            for (Aluguel a : alugueis) {
                JPanel card = criarCardAluguel(a);
                cardsAlugueis.add(card);
                panelAlugueis.add(card);
                panelAlugueis.add(Box.createVerticalStrut(10));
            }

            panelAlugueis.revalidate();
            panelAlugueis.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar aluguéis: " + e.getMessage());
            dispose();
        }
    }

    private JPanel criarCardAluguel(Aluguel aluguel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(300, 100));

        // conteúdo do card
        JLabel lblTitulo = new JLabel("<html><b>" + aluguel.getLugar().getNome() + "</b></html>");
        lblTitulo.setFont(new Font("Candara Light", Font.BOLD, 30));

        JLabel lblHorario = new JLabel("Horário: " +
                aluguel.getHoraComeco().toLocalTime().getHour() + ":" +  String.format("%02d", aluguel.getHoraComeco().toLocalTime().getMinute()) + " - " +
                aluguel.getHoraFimPrevisto().toLocalTime().getHour() + ":" + String.format("%02d", aluguel.getHoraComeco().toLocalTime().getMinute()));
        lblHorario.setFont(new Font("Candara Light", Font.PLAIN, 18));
        lblHorario.setForeground(Color.DARK_GRAY);

        JLabel lblResponsavel = new JLabel("Responsável: " + aluguel.getResponsavel().getNome());
        lblResponsavel.setFont(new Font("Candara Light", Font.PLAIN, 18));
        lblResponsavel.setForeground(new Color(60, 60, 60));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);
        info.setBorder(new EmptyBorder(10, 10, 10, 10));

        info.add(lblTitulo);
        info.add(Box.createVerticalStrut(5));
        info.add(lblHorario);
        info.add(lblResponsavel);

        card.add(info, BorderLayout.CENTER);

        // efeito hover e seleção
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 255));
                card.setBorder(new LineBorder(new Color(100, 149, 237), 2, true));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (aluguel != aluguelSelecionado) {
                    card.setBackground(Color.WHITE);
                    card.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                selecionarAluguel(aluguel, card);
            }
        });

        return card;
    }

    private void selecionarAluguel(Aluguel aluguel, JPanel cardSelecionado) {
        this.aluguelSelecionado = aluguel;
        btnFinalizar.setEnabled(true);

        // destacar o card selecionado
        for (JPanel c : cardsAlugueis) {
            c.setBackground(Color.WHITE);
            c.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        }
        cardSelecionado.setBackground(new Color(220, 235, 255));
        cardSelecionado.setBorder(new LineBorder(new Color(70, 130, 180), 2, true));

        // carregar objetos do aluguel selecionado
        try {
            carregarObjetosDoAluguel(aluguelSelecionado);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar objetos: " + e.getMessage());
        }
    }

    private void carregarObjetosDoAluguel(Aluguel aluguel) throws Exception {
        panelObjetos.removeAll();
        checkBoxesObjetos.clear();

        // Se precisar limpar painel de objetos, só precisa chamar esse método com null
        if (aluguel == null) {
        	panelObjetos.revalidate();
        	panelObjetos.repaint();
        	return;
        }
        
        ControllerObjeto controller = new ControllerObjeto();
        List<Objeto> objetos = controller.buscarObjetosDoAluguel(aluguel);

        if (objetos.isEmpty()) {
            JLabel lbl = new JLabel("Nenhum objeto emprestado neste aluguel.");
            lbl.setFont(new Font("Arial", Font.ITALIC, 12));
            panelObjetos.add(lbl);
        } else {
            for (Objeto obj : objetos) {
                JCheckBox cb = new JCheckBox(obj.getNome());
                cb.putClientProperty("objeto", obj);
                checkBoxesObjetos.add(cb);
                panelObjetos.add(cb);
            }
        }

        panelObjetos.revalidate();
        panelObjetos.repaint();
    }

    private void finalizarAluguel() throws Exception {
        if (aluguelSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluguel primeiro!");
            return;
        }

        List<Objeto> devolvidos = new ArrayList<>();
        for (JCheckBox cb : checkBoxesObjetos) {
            if (cb.isSelected()) {
                devolvidos.add((Objeto) cb.getClientProperty("objeto"));
            }
        }

        if (devolvidos.isEmpty() && !checkBoxesObjetos.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Nenhum objeto foi marcado como devolvido. Deseja finalizar mesmo assim?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
        }

        ControllerAluguel controller = new ControllerAluguel();
        controller.finalizarAluguel(aluguelSelecionado, devolvidos);

        JOptionPane.showMessageDialog(this, "Aluguel finalizado com sucesso!");
        carregarAlugueis();
        carregarObjetosDoAluguel(null);
    }
}
