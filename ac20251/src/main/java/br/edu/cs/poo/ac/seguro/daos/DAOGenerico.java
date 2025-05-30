package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;

public abstract class DAOGenerico<T extends Registro> {

    private final CadastroObjetos cadastro;

    public DAOGenerico() {
        this.cadastro = new CadastroObjetos(getClasseEntidade());
    }

    public abstract Class<?> getClasseEntidade();

    public boolean incluir(T obj) {
        if (cadastro.buscar(obj.getIdUnico()) != null) {
            return false; 
        }
        cadastro.incluir(obj, obj.getIdUnico());
        return true; 
    }

    public boolean alterar(T obj) {
        if (cadastro.buscar(obj.getIdUnico()) == null) {
            return false; 
        }
        cadastro.alterar(obj, obj.getIdUnico());
        return true; 
    }

    @SuppressWarnings("unchecked")
    public T buscar(String idUnico) {
        return (T) cadastro.buscar(idUnico);
    }

    public boolean excluir(String idUnico) {
        if (cadastro.buscar(idUnico) == null) {
            return false; 
        }
        cadastro.excluir(idUnico);
        return true; 
    }

    public Registro[] buscarTodos() {
        Object[] rets = cadastro.buscarTodos();
        if (rets == null) {
            return new Registro[0];
        }
        Registro[] registros = new Registro[rets.length];
        for (int i = 0; i < rets.length; i++) {
            registros[i] = (Registro) rets[i];
        }
        return registros;
    }
}