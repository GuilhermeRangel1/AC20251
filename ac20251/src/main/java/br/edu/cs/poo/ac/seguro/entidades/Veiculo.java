package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Veiculo implements Serializable, Registro {
    private static final long serialVersionUID = 1L;
    private String placa;
    private int ano;
    private Segurado proprietario; 
    private CategoriaVeiculo categoria;

    public Veiculo(String placa, int ano, SeguradoEmpresa proprietarioEmpresa, SeguradoPessoa proprietarioPessoa, CategoriaVeiculo categoria) {
        this.placa = placa;
        this.ano = ano;
        this.categoria = categoria;
        if (proprietarioPessoa != null) {
            this.proprietario = proprietarioPessoa;
        } else if (proprietarioEmpresa != null) {
            this.proprietario = proprietarioEmpresa;
        } else {
            throw new IllegalArgumentException("Um veículo deve ter um proprietário (pessoa ou empresa).");
        }
    }

    public Veiculo(String placa, int ano, Segurado proprietario, CategoriaVeiculo categoria) {
        this.placa = placa;
        this.ano = ano;
        this.proprietario = proprietario;
        this.categoria = categoria;
    }

    @Override
    public String getIdUnico() {
        return this.placa;
    }
}