package model;

import java.time.LocalDateTime;

public class Encomenda {

    private int id;
    private String codRastreio;
    private String codIdentificador;
    private LocalDateTime recebimento; // Hora que funcionário cadastrou encomenda
    private int destinatario; // Número do lote (fixo)
    private Funcionario recebedor;
    private Morador recebidoPor; // Morador que buscou a encomenda
    private LocalDateTime recolhimento; // Hora que condômino buscou a encomenda

    public static final int TAM_MAX_COD = 50;
    public static final int TAM_MAX_DESTINATARIO = 10;

    public Encomenda(String codRastreio, String codIdentificador,
                     int destinatario, Funcionario recebedor) throws Exception {
        this.setCodRastreio(codRastreio);
        this.setCodIdentificador(codIdentificador);
        this.recebimento = LocalDateTime.now();
        this.setDestinatario(destinatario);
        this.setRecebedor(recebedor);
        this.recebidoPor = null; // Inicialmente ninguém retirou
        this.recolhimento = null; // Inicialmente sem recolhimento
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodRastreio() {
        return codRastreio;
    }

    public void setCodRastreio(String codRastreio) throws Exception {
        if (codRastreio != null && codRastreio.length() > TAM_MAX_COD) {
            throw new Exception("Código de rastreio muito longo!");
        }
        this.codRastreio = codRastreio;
    }

    public String getCodIdentificador() {
        return codIdentificador;
    }

    public void setCodIdentificador(String codIdentificador) throws Exception {
        if (codIdentificador != null && codIdentificador.length() > TAM_MAX_COD) {
            throw new Exception("Código identificador muito longo!");
        }
        this.codIdentificador = codIdentificador;
    }

    public LocalDateTime getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(LocalDateTime recebimento) throws Exception {
        if (recebimento == null) {
            throw new Exception("Data de recebimento não pode ser nula!");
        }
        this.recebimento = recebimento;
    }

    public int getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(int destinatario) throws Exception {
        this.destinatario = destinatario;
    }

    public Funcionario getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(Funcionario recebedor) throws Exception {
        if (recebedor == null) {
            throw new Exception("Recebedor não pode ser nulo!");
        }
        this.recebedor = recebedor;
    }

    public Morador getRecebidoPor() {
        return recebidoPor;
    }

    public void setRecebidoPor(Morador recebidoPor) {
        this.recebidoPor = recebidoPor; // pode ser null inicialmente
    }

    public LocalDateTime getRecolhimento() {
        return recolhimento;
    }

    public void setRecolhimento(LocalDateTime recolhimento) {
        this.recolhimento = recolhimento;
    }
    
 // Método para receber encomenda
    public void receber(Morador morador) throws Exception {
        this.setRecolhimento(LocalDateTime.now());
        this.setRecebidoPor(morador);
    }
}
