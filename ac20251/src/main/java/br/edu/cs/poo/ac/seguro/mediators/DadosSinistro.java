package br.edu.cs.poo.ac.seguro.mediators;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadosSinistro {
    private String placa;
    private LocalDateTime dataHoraSinistro;
    private String usuarioRegistro;
    private double valorSinistro;
    private int codigoTipoSinistro;

    public DadosSinistro(String placa, LocalDateTime dataHoraSinistro, String usuarioRegistro, double valorSinistro, int codigoTipoSinistro) {
        this.placa = placa;
        this.dataHoraSinistro = dataHoraSinistro;
        this.usuarioRegistro = usuarioRegistro;
        this.valorSinistro = valorSinistro;
        this.codigoTipoSinistro = codigoTipoSinistro;
    }

}