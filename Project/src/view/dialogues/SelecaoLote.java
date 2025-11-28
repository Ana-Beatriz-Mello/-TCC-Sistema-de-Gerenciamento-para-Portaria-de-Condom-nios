package view.dialogues;

import java.awt.BorderLayout;
import java.awt.Frame;

import dao.DaoMorador;
import model.Morador;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import controller.ControllerMorador;
import java.util.ArrayList;
import java.util.List;

public class SelecaoLote extends JDialog {

	private static final long serialVersionUID = 1L;
	private JList<Integer> listaLotes;
    private JButton btnSelecionar;
    private JTextField campoBusca;
    private Integer loteSelecionado;

    public SelecaoLote(Frame parent) {
        super(parent, "Seleção de Lote", true);
        setSize(300, 350);
        setLocationRelativeTo(parent); // centraliza na tela
        setLayout(new BorderLayout(5, 5));

        DefaultListModel<Integer> model = new DefaultListModel<>();

        try {
            DaoMorador dao = new DaoMorador();
            List<Integer> lotes = dao.buscaLotes();

            if (lotes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum lote cadastrado!");
            } else {
                for (Integer numero : lotes) {
                    model.addElement(numero);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lotes: " + e.getMessage());
        }

        listaLotes = new JList<>(model);
        listaLotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(listaLotes);

        // Painel para campo de busca
        JPanel painelBusca = new JPanel(new BorderLayout(5, 5));
        campoBusca = new JTextField();
        JButton btnBuscar = new JButton("Ir");

        btnBuscar.addActionListener(e -> buscarLote(model));
        campoBusca.addActionListener(e -> buscarLote(model)); // Enter também busca

        painelBusca.add(new JLabel("Digite o número do lote:"), BorderLayout.WEST);
        painelBusca.add(campoBusca, BorderLayout.CENTER);
        painelBusca.add(btnBuscar, BorderLayout.EAST);

        // Botão selecionar
        btnSelecionar = new JButton("Selecionar");
        btnSelecionar.addActionListener(e -> {
            loteSelecionado = listaLotes.getSelectedValue();
            dispose();
        });

        add(painelBusca, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnSelecionar, BorderLayout.SOUTH);
    }

    // Construtor alternativo para exibir apenas os lotes que um certo morador tem permissão
    public SelecaoLote(Frame parent, Integer id) {
        super(parent, "Seleção de Lote", true);

        setSize(300, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        DefaultListModel<Integer> model = new DefaultListModel<>();

        carregarLotesMorador(model, id);

        listaLotes = new JList<>(model);
        listaLotes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(listaLotes);

        // Painel para campo de busca
        JPanel painelBusca = new JPanel(new BorderLayout(5, 5));
        campoBusca = new JTextField();
        JButton btnBuscar = new JButton("Ir");

        btnBuscar.addActionListener(e -> buscarLote(model));
        campoBusca.addActionListener(e -> buscarLote(model)); // Enter também busca

        painelBusca.add(new JLabel("Digite o número do lote:"), BorderLayout.WEST);
        painelBusca.add(campoBusca, BorderLayout.CENTER);
        painelBusca.add(btnBuscar, BorderLayout.EAST);

        // Botão selecionar
        btnSelecionar = new JButton("Selecionar");
        btnSelecionar.addActionListener(e -> {
            loteSelecionado = listaLotes.getSelectedValue();
            dispose();
        });

        add(painelBusca, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnSelecionar, BorderLayout.SOUTH);
    }

    private void carregarLotesMorador (DefaultListModel<Integer> model, int idMorador) {
    	
    	  List<Integer> lotes = new ArrayList<Integer>();
    	
    	 try {
           
                 ControllerMorador controller = new ControllerMorador();
                 
                 Morador m = controller.buscarPorId(idMorador);
                 
                 lotes = controller.lotesComPermissao(m);
             }

         catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Erro ao carregar lotes: " + e.getMessage());
             e.printStackTrace();
             return;
         }
         
         for (Integer numero : lotes) {
             model.addElement(numero);
         }
    	
    }
    
    private void buscarLote(DefaultListModel<Integer> model) {
        try {
            int numero = Integer.parseInt(campoBusca.getText().trim());

            for (int i = 0; i < model.size(); i++) {
                if (model.getElementAt(i) == numero) {
                    listaLotes.setSelectedIndex(i);
                    listaLotes.ensureIndexIsVisible(i); // rola até o item
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Lote não encontrado!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um número válido!");
        }
    }

    public Integer getLoteSelecionado() {
        return loteSelecionado;
    }
}
