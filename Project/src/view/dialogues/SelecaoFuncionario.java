package view.dialogues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import controller.ControllerFuncionario;
import model.Funcionario;

public class SelecaoFuncionario extends JDialog {

    private static final long serialVersionUID = 1L;
    private JList<String> listFuncionarios;
    private DefaultListModel<String> modelList;
    private JTextField txtFiltro;
    private Funcionario funcionarioSelecionado = null;

    public SelecaoFuncionario(JFrame parent) {
        
    	super(parent, "Selecionar Funcionário", true);
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
        listFuncionarios = new JList<>(modelList);
        JScrollPane scrollPane = new JScrollPane(listFuncionarios);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        getContentPane().add(btnSelecionar, BorderLayout.SOUTH);

        carregarFuncionarios();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        txtFiltro.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtrarLista(txtFiltro.getText());
            }
        });

        listFuncionarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) selecionarFuncionario();
            }
        });

        btnSelecionar.addActionListener(e -> selecionarFuncionario());
    }

    private void carregarFuncionarios() {
        try {
            ControllerFuncionario controller = new ControllerFuncionario();
            List<Funcionario> funcionarios = controller.buscarTodosFuncionarios();
            modelList.clear();
            for (Funcionario f : funcionarios) {
                modelList.addElement(f.getId() + " - " + f.getNome() + " (" + f.getCpf() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarLista(String filtro) {
        try {
            ControllerFuncionario controller = new ControllerFuncionario();
            List<Funcionario> funcionarios = controller.buscarTodosFuncionarios();

            modelList.clear();
            for (Funcionario f : funcionarios) {
                if (f.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    modelList.addElement(f.getId() + " - " + f.getNome() + " (" + f.getCpf() + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selecionarFuncionario() {
        String selecionado = listFuncionarios.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um funcionário!");
            return;
        }
        int id = Integer.parseInt(selecionado.split(" - ")[0]);
        try {
            ControllerFuncionario controller = new ControllerFuncionario();
            funcionarioSelecionado = controller.buscarPorId(id);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Funcionario getFuncionarioSelecionado() {
    	if (funcionarioSelecionado == null) {
    		dispose();
    	}
        return funcionarioSelecionado;
    }
}
