package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.Segurado;

public class SeguradoEmpresaDAO extends SeguradoDAO {

    public SeguradoEmpresa buscar(String cnpj) {
        Segurado seguradoGenerico = super.buscar(cnpj);
        if (seguradoGenerico instanceof SeguradoEmpresa) {
            return (SeguradoEmpresa) seguradoGenerico;
        }
        return null;
    }

}