package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
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
import model.Lugar;
import model.Objeto;
import util.RoundedBorder;

public class ViewEmprestarObjetos extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel panelAlugueis;
    private JPanel panelObjetos;
    private JButton btnConfirmar = new RoundedBorder("Confirmar Empréstimo",15);

    private Aluguel aluguelSelecionado;
    private List<JCheckBox> checkBoxesObjetos = new ArrayList<>();

    private String idUser = null;
    private int tipoUser = 999;

    public ViewEmprestarObjetos(String idUser, int tipoUser) {

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
			JOptionPane.showMessageDialog(ViewEmprestarObjetos.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//

        setTitle("Empréstimo de Objetos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Painel de aluguéis
        panelAlugueis = new JPanel(new BorderLayout());
        
        TitledBorder border1 = BorderFactory.createTitledBorder("Aluguéis de hoje");
        border1.setTitleFont(new Font("Candara Light", Font.BOLD, 20));
        panelAlugueis.setBorder(border1);

        JScrollPane scrollAlugueis = new JScrollPane();
        JList<String> listAlugueis = new JList<>();
        scrollAlugueis.setViewportView(listAlugueis);
        panelAlugueis.add(scrollAlugueis, BorderLayout.CENTER);
        add(panelAlugueis, BorderLayout.WEST);
        panelAlugueis.setPreferredSize(new Dimension(350, 0));

        // Painel de objetos
        panelObjetos = new JPanel(new GridLayout(0, 2, 5, 5));
        panelObjetos.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollObjetos = new JScrollPane(panelObjetos);
        
        TitledBorder border2 = BorderFactory.createTitledBorder("Objetos disponíveis");
        border2.setTitleFont(new Font("Candara Light", Font.BOLD, 20));
        scrollObjetos.setBorder(border2);
        
        add(scrollObjetos, BorderLayout.CENTER);     
        add(btnConfirmar, BorderLayout.SOUTH);

        // Carrega aluguéis do dia
        try {
            ControllerAluguel controllerAluguel = new ControllerAluguel();
            List<Aluguel> alugueis = controllerAluguel.buscarAlugueisDoDia();

            DefaultListModel<String> model = new DefaultListModel<>();
            for (Aluguel a : alugueis) {
                model.addElement("<html><b>" + a.getLugar().getNome() + "</b><br/>"
                		+ a.getHoraComeco().toLocalDate().getDayOfMonth() + "/" + a.getHoraComeco().toLocalDate().getMonthValue() + " - "
                        + a.getHoraComeco().toLocalTime().getHour() + "h" + String.format("%02d", a.getHoraComeco().toLocalTime().getMinute())
                        + " às "
                        + a.getHoraFimPrevisto().toLocalTime().getHour() + "h" + String.format("%02d", a.getHoraFimPrevisto().toLocalTime().getMinute())
                        + "<br/>Responsável: " + a.getResponsavel().getNome()
                        + " (Lote " + a.getResponsavel().getIdLote() + ")</html>");
            }
            listAlugueis.setModel(model);

            // Disposição arrumada para os aluguéis
            listAlugueis.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(5, 5, 5, 5), // margem externa
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1) // borda da “caixa”
                ));
                label.setBackground(isSelected ? new Color(200, 230, 255) : Color.WHITE);
                label.setForeground(Color.DARK_GRAY);
                label.setFont(new Font("Candara Light", Font.BOLD, 15));
                return label;
            });

            // Ao clicar em um aluguel, carregar objetos
            listAlugueis.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int index = listAlugueis.getSelectedIndex();
                    if (index >= 0) {
                        aluguelSelecionado = alugueis.get(index);
                        try {
                            carregarObjetos(aluguelSelecionado.getLugar());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar aluguéis: " + e.getMessage());
        }

        btnConfirmar.addActionListener(e -> {
            try {
                confirmarEmprestimo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar empréstimo: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void carregarObjetos(Lugar lugar) throws Exception {
        panelObjetos.removeAll();
        checkBoxesObjetos.clear();

        ControllerObjeto controller = new ControllerObjeto();
        List<Objeto> objetos = controller.buscarObjetosDoLugar(lugar);

        
        List<Objeto> objetosSelecionados = new ArrayList<>();

        for (Objeto obj : objetos) {
       
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setOpaque(true);
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1, true),
                    new EmptyBorder(10, 10, 10, 10)
            ));

        
            JLabel lblNome = new JLabel(obj.getNome(), SwingConstants.CENTER);
            lblNome.setFont(new Font("Candara Light", Font.BOLD, 15));
            card.add(lblNome, BorderLayout.CENTER);

           
            card.putClientProperty("objeto", obj);
            card.putClientProperty("selecionado", false);

      
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!(Boolean) card.getClientProperty("selecionado")) {
                        card.setBackground(new Color(247, 230, 252));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!(Boolean) card.getClientProperty("selecionado")) {
                        card.setBackground(Color.WHITE);
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    boolean selecionado = (Boolean) card.getClientProperty("selecionado");
                    if (selecionado) {
                        card.setBackground(Color.WHITE);
                        card.setBorder(null);
                        card.putClientProperty("selecionado", false);
                        objetosSelecionados.remove(obj);
                    } else {
                        card.setBackground(new Color(232, 204, 237)); // Cor quando seleciona
                        card.setBorder(new LineBorder(Color.BLACK, 3));
                        card.putClientProperty("selecionado", true);
                        objetosSelecionados.add(obj);
                    }
                }
            });

            panelObjetos.add(card);
        }

        panelObjetos.revalidate();
        panelObjetos.repaint();
    }



    private void confirmarEmprestimo() throws Exception {
        if (aluguelSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluguel primeiro!");
            return;
        }

        List<Objeto> selecionados = new ArrayList<>();
        for (Component comp : panelObjetos.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel card = (JPanel) comp;
                boolean selecionado = (Boolean) card.getClientProperty("selecionado");
                if (selecionado) {
                    Objeto obj = (Objeto) card.getClientProperty("objeto");
                    selecionados.add(obj);
                }
            }
        }

        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um objeto!");
            return;
        }

        ControllerAluguel controller = new ControllerAluguel();
        controller.incluirObjetos(aluguelSelecionado, selecionados);

        JOptionPane.showMessageDialog(this, "Objetos emprestados com sucesso!");
        dispose();
    }
}
