package model;

import java.time.LocalDateTime;

public class Aluguel {

    private int id;
    private Morador responsavel;
    private Lugar lugar;
    private LocalDateTime horaComeco;
    private LocalDateTime horaFimPrevisto;
    private LocalDateTime horaTermino;

    public Aluguel(Morador responsavel, Lugar lugar, LocalDateTime horaComeco,
                   LocalDateTime horaFimPrevisto) throws Exception {
        this.setResponsavel(responsavel);
        this.setLugar(lugar);
        this.setHoraComeco(horaComeco);
        this.setHoraFimPrevisto(horaFimPrevisto);
        this.horaTermino = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Morador getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Morador responsavel) throws Exception {
        if (responsavel == null) {
            throw new Exception("Responsável pelo aluguel não pode ser nulo!");
        }
        this.responsavel = responsavel;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) throws Exception {
        if (lugar == null) {
            throw new Exception("Lugar não pode ser nulo!");
        }
        this.lugar = lugar;
    }

    public LocalDateTime getHoraComeco() {
        return horaComeco;
    }

    public void setHoraComeco(LocalDateTime horaComeco) throws Exception {
        if (horaComeco == null) {
            throw new Exception("Hora de início não pode ser nula!");
        }
        this.horaComeco = horaComeco;
    }

    public LocalDateTime getHoraFimPrevisto() {
        return horaFimPrevisto;
    }

    public void setHoraFimPrevisto(LocalDateTime horaFimPrevisto) throws Exception {
        if (horaFimPrevisto != null && horaFimPrevisto.isBefore(horaComeco)) {
            throw new Exception("Hora de término prevista não pode ser antes da hora de início!");
        }
        this.horaFimPrevisto = horaFimPrevisto;
    }

    public LocalDateTime getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(LocalDateTime horaTermino) throws Exception {
        if (horaTermino != null && horaTermino.isBefore(horaComeco)) {
            throw new Exception("Hora de término não pode ser antes da hora de início!");
        }
        this.horaTermino = horaTermino;
    }

    // Método para finalizar aluguel
    public void finalizar() throws Exception {
        this.setHoraTermino(LocalDateTime.now()); // captura a hora atual automaticamente
    }
}
