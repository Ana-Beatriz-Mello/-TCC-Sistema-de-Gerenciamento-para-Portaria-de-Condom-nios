package view.dialogues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import controller.ControllerMorador;
import model.Morador;

public class SelecaoMorador extends JDialog {

    private static final long serialVersionUID = 1L;
    private JList<String> listMoradores;
    private DefaultListModel<String> modelList;
    private JTextField txtFiltro;
    private Morador moradorSelecionado = null;

    public SelecaoMorador(JFrame parent, boolean todos) {
        
    	// O boolean "todos" indisca:
    	// Se for true: busca todos os moradores cadastrados
    	// Se for false: busca apenas os moradores com cadastro ativo
    	
    	super(parent, "Selecionar Morador", true);
        setBounds(100, 100, 400, 300);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);

        txtFiltro = new JTextField();
        panel.add(txtFiltro, BorderLayout.NORTH);

        modelList = new DefaultListModel<>();
        listMoradores = new JList<>(modelList);
        JScrollPane scrollPane = new JScrollPane(listMoradores);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        getContentPane().add(btnSelecionar, BorderLayout.SOUTH);

        carregarMoradores(todos);

        txtFiltro.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtrarLista(txtFiltro.getText(), todos);
            }
        });

        listMoradores.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) selecionarMorador();
            }
        });

        btnSelecionar.addActionListener(e -> selecionarMorador());
    }

    private void carregarMoradores(boolean todos) {
        try {
            ControllerMorador controller = new ControllerMorador();
            List<Morador> moradores = controller.buscarTodosMoradores(todos);
            modelList.clear();
            for (Morador m : moradores) {
                modelList.addElement(m.getId() + " - " + m.getNome() + " (" + m.getCpf() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarLista(String filtro, boolean todos) {
        try {
            ControllerMorador controller = new ControllerMorador();
            List<Morador> moradores = controller.buscarTodosMoradores(todos);

            modelList.clear();
            for (Morador m : moradores) {
                if (m.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    modelList.addElement(m.getId() + " - " + m.getNome() + " (" + m.getCpf() + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selecionarMorador() {
        String selecionado = listMoradores.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um morador!");
            return;
        }
        int id = Integer.parseInt(selecionado.split(" - ")[0]);
        try {
            ControllerMorador controller = new ControllerMorador();
            moradorSelecionado = controller.buscarPorId(id);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Morador getMoradorSelecionado() {
        return moradorSelecionado;
    }
}
