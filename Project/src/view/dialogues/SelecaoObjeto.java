package view.dialogues;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import controller.ControllerObjeto;
import model.Objeto;

public class SelecaoObjeto extends JDialog {

    private static final long serialVersionUID = 1L;
    private JList<String> listObjetos;
    private DefaultListModel<String> modelList;
    private JTextField txtFiltro;
    private Objeto ObjetoSelecionado = null;
    List<Objeto> objetos = null;

    public SelecaoObjeto(JFrame parent) {
          	
    	// O boolean "todos" indisca:
    	// Se for true: busca todos os Objetos cadastrados
    	// Se for false: busca apenas os Objetos com cadastro ativo
    	
    	super(parent, "Selecionar Objeto", true);
    	
    	
    	ControllerObjeto controller = new ControllerObjeto();
    	
    	try {
    		objetos = controller.buscarTodosObjetos();
    	}
    	catch (Exception e) {
    		JOptionPane.showMessageDialog(this, "Erro ao carregar banco de dados! Verifique a conexão.");
    		dispose();
    	}
    	
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
        listObjetos = new JList<>(modelList);
        JScrollPane scrollPane = new JScrollPane(listObjetos);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnSelecionar = new JButton("Selecionar");
        getContentPane().add(btnSelecionar, BorderLayout.SOUTH);

        carregarObjetos();

        txtFiltro.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filtrarLista(txtFiltro.getText());
            }
        });

        listObjetos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) selecionarObjeto();
            }
        });

        btnSelecionar.addActionListener(e -> selecionarObjeto());
    }

    private void carregarObjetos() {
        try {
            //ControllerObjeto controller = new ControllerObjeto();
            //List<Objeto> Objetos = controller.buscarTodosObjetos();
            modelList.clear();
            for (Objeto o : objetos) {
                modelList.addElement(o.getId() + " - " + o.getNome() + " (" + o.getNomeLugar() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filtrarLista(String filtro) {
            //ControllerObjeto controller = new ControllerObjeto();
            //List<Objeto> Objetos = controller.buscarTodosObjetos();

            modelList.clear();
            for (Objeto o : objetos) {
                if (o.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    modelList.addElement(o.getId() + " - " + o.getNome() + " (" + o.getNomeLugar() + ")");
                }
            }

    }

    private void selecionarObjeto() {
        String selecionado = listObjetos.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um Objeto!");
            return;
        }
        int id = Integer.parseInt(selecionado.split(" - ")[0]);
        try {
            ControllerObjeto controller = new ControllerObjeto();
            ObjetoSelecionado = controller.consultarObjetoPorId(id);
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Objeto getObjetoSelecionado() {
        return ObjetoSelecionado;
    }
}
