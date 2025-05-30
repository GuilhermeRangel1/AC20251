package br.edu.cs.poo.ac.seguro.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public abstract class Segurado implements Serializable, Registro { 
    private static final long serialVersionUID = 1L;
    private String nome;
    private Endereco endereco;
    private LocalDate dataCriacao;
    private BigDecimal bonus;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    protected void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    protected LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public Segurado(String nome, Endereco endereco, LocalDate dataCriacao, BigDecimal bonus) {
        this.nome = nome;
        this.endereco = endereco;
        this.dataCriacao = dataCriacao;
        this.bonus = bonus;
    }

    public int getIdade() {
        LocalDate hoje = LocalDate.now();
        if (dataCriacao != null) {
            Period periodo = Period.between(dataCriacao, hoje);
            return periodo.getYears();
        }
        return 0;
    }

    public void creditarBonus(BigDecimal valor) {
        bonus = bonus.add(valor);
    }

    public void debitarBonus(BigDecimal valor) {
        bonus = bonus.subtract(valor);
    }

    public abstract boolean isEmpresa(); 
    
    @Override
    public abstract String getIdUnico();
}