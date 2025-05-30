package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;

public class SeguradoPessoaDAO extends SeguradoDAO {

    public SeguradoPessoa buscar(String cpf) {
        Segurado seguradoGenerico = super.buscar(cpf); 
        if (seguradoGenerico instanceof SeguradoPessoa) {
            return (SeguradoPessoa) seguradoGenerico;
        }
        return null;
    }
}