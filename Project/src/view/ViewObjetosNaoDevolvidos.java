package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import controller.ControllerAluguel;
import controller.ControllerObjeto;
import model.Aluguel;
import model.Lugar;
import model.Objeto;
import util.RoundedBorder;

public class ViewObjetosNaoDevolvidos extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel panelObjetos;
    private JButton btnMarcarDevolvido = new RoundedBorder("Marcar como Devolvido", 15);
    private ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private ArrayList<int[]> objetosPerdidos = new ArrayList<>();

    private String idUser = null;
    private int tipoUser = 999;

    public ViewObjetosNaoDevolvidos(String idUser, int tipoUser) {
        setTitle("Objetos Não Devolvidos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

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
			JOptionPane.showMessageDialog(ViewObjetosNaoDevolvidos.this, "Problemas ao carregar recursos visuais.", "Aviso", JOptionPane.WARNING_MESSAGE);
			dispose();
		}
		setIconImage(icon.getImage());
		//

        panelObjetos = new JPanel();
        panelObjetos.setLayout(new BoxLayout(panelObjetos, BoxLayout.Y_AXIS));
        panelObjetos.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panelObjetos);
        TitledBorder border = BorderFactory.createTitledBorder("Objetos emprestados");
        border.setTitleFont(new Font("Candara Light", Font.BOLD, 20));
        scrollPane.setBorder(border);
        add(scrollPane, BorderLayout.CENTER);

        add(btnMarcarDevolvido, BorderLayout.SOUTH);

        carregarObjetosNaoDevolvidos();

        btnMarcarDevolvido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    marcarComoDevolvido();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ViewObjetosNaoDevolvidos.this, "Erro ao atualizar: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    private void carregarObjetosNaoDevolvidos() {
        	panelObjetos.removeAll();
    	try {
            ControllerAluguel controllerAluguel = new ControllerAluguel();
            ControllerObjeto controllerObjeto = new ControllerObjeto();

            objetosPerdidos = new ArrayList<>(controllerAluguel.objetosPerdidos());

            for (int[] dados : objetosPerdidos) {
                int idObjeto = dados[0];
                int idAluguel = dados[1];

                Objeto objeto = controllerObjeto.consultarObjetoPorId(idObjeto);
                Aluguel aluguel = controllerAluguel.buscarPorId(idAluguel);
                Lugar lugar = objeto.getLugar();

                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 1),
                        new EmptyBorder(10, 15, 10, 15)
                ));
                card.setBackground(Color.WHITE);
                card.setMaximumSize(new Dimension(700, 110));

                // NÃO MOSTRA A CHECKBOX
                JCheckBox check = new JCheckBox();
                check.putClientProperty("objeto", objeto);
                check.setVisible(false);
                checkBoxes.add(check);

                JLabel lblInfo = new JLabel("<html>"
                        + "<b>Objeto:</b> " + objeto.getNome() + "<br/>"
                        + "<b>Responsável:</b> " + aluguel.getResponsavel().getNome()
                        + " - Lote: " + aluguel.getResponsavel().getIdLote() + "<br/>"
                        + "<b>Código do Aluguel:</b> " + aluguel.getId() + "<br/>"
                        + "<b>Lugar:</b> " + lugar.getNome()
                        + "<br/><b>Dia " + aluguel.getHoraComeco().toLocalDate().getDayOfMonth() + "/"
                        + aluguel.getHoraComeco().toLocalDate().getMonthValue()
                        + " às " + aluguel.getHoraComeco().toLocalTime().getHour() + ":"
                        + String.format("%02d", aluguel.getHoraComeco().toLocalTime().getMinute())
                        + "</html>");
                lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                lblInfo.setBorder(new EmptyBorder(5, 10, 5, 10));
                card.add(lblInfo, BorderLayout.CENTER);

                // PARA CLICAR NO CARD PARA SELECIONAR
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        check.setSelected(!check.isSelected());
                        atualizarSelecaoCard(card, check.isSelected());
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!check.isSelected())
                            card.setBackground(new Color(250, 250, 220));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (!check.isSelected())
                            card.setBackground(Color.WHITE);
                    }
                });

                lblInfo.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        card.dispatchEvent(e);
                    }
                });

                atualizarSelecaoCard(card, false);

                panelObjetos.add(Box.createVerticalStrut(8));
                panelObjetos.add(card);
            }

            panelObjetos.revalidate();
            panelObjetos.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar objetos: " + e.getMessage());
        }
    }

    private void atualizarSelecaoCard(JPanel card, boolean selecionado) {
        if (selecionado) {
            card.setBackground(new Color(247, 230, 252)); // COR AO SELECIONAR
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        } else {
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    private void marcarComoDevolvido() throws Exception {
        ControllerObjeto controllerObjeto = new ControllerObjeto();
        ArrayList<Objeto> selecionados = new ArrayList<>();

        for (JCheckBox check : checkBoxes) {
            if (check.isSelected()) {
                Objeto obj = (Objeto) check.getClientProperty("objeto");
                obj.setDisponivel(true);
                selecionados.add(obj);
            }
        }

        if (selecionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione ao menos um objeto para marcar como devolvido!");
            return;
        }

        for (Objeto obj : selecionados) {
            controllerObjeto.atualizarObjeto(obj);
        }

        JOptionPane.showMessageDialog(this, "Objetos atualizados como devolvidos com sucesso!");
        carregarObjetosNaoDevolvidos();
        return;
    }
}
