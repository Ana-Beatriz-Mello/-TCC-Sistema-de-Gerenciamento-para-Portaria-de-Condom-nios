package view.dialogues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import controller.ControllerLugar;
import dao.DaoLugar;
import model.Lugar;

public class SelecaoLugar extends JDialog {

    private static final long serialVersionUID = 1L;
    private JList<String> listLugares;
    private DefaultListModel<String> modelList;
    private JTextField txtFiltro;
    private Integer idLugarSelecionado = null;
    private Lugar lugarSelecionado = null;

    public SelecaoLugar(JFrame parent) {
        super(parent, "Selecione o Lugar", true);
        setBounds(100, 100, 400, 300);
        getContentPane().setLayout(new BorderLayout());
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

        txtFiltro = new JTextField();
        panel.add(txtFiltro, BorderLayout.NORTH);

        modelList = new DefaultListModel<>();
        listLugares = new JList<>(modelList);
        JScrollPane scrollPane = new JScrollPane(listLugares);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        getContentPane().add(btnSelecionar, BorderLayout.SOUTH);

        carregarLugares();

        txtFiltro.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtrarLista(txtFiltro.getText());
            }
        });

        // Selecionar com duplo clique
        listLugares.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) selecionarLugar();
            }
        });

        // Selecionar com botão
        btnSelecionar.addActionListener(e -> selecionarLugar());
    }

    private void carregarLugares() {
        try {
            ControllerLugar controller = new ControllerLugar();
            List<Lugar> lugares = controller.buscarTodosLugares();

            modelList.clear();
            for (Lugar l : lugares) {
                modelList.addElement(l.getId() + " - " + l.getNome());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar lugares!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarLista(String filtro) {
        try {
            ControllerLugar controller = new ControllerLugar();
            List<Lugar> lugares = controller.buscarTodosLugares();

            modelList.clear();
            for (Lugar l : lugares) {
                if (l.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    modelList.addElement(l.getId() + " - " + l.getNome());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selecionarLugar() {
        String selecionado = listLugares.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um lugar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        idLugarSelecionado = Integer.parseInt(selecionado.split(" - ")[0]);
        try {
        	DaoLugar dao = new DaoLugar();
        	lugarSelecionado = dao.consultarPorId(idLugarSelecionado);
        }
        catch (Exception e) {
        	
        	JOptionPane.showMessageDialog(this, "Houve um problema na obtenção do lugar! \nTente recarregar a página.", "Aviso", JOptionPane.WARNING_MESSAGE);
        	dispose();
        	
        }
        dispose();
    }
    
    public Lugar getLugarSelecionado() {
        return lugarSelecionado;
    }

    public Integer getIdLugarSelecionado() {
        return idLugarSelecionado;
    }
}
