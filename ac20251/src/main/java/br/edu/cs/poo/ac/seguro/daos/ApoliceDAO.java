package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApoliceDAO {
    private final List<Apolice> apolices = new ArrayList<>();
    protected final CadastroObjetos cadastro;

    public ApoliceDAO() {
        this.cadastro = new CadastroObjetos(Apolice.class);
    }

    public Optional<Apolice> findByNumero(String numero) {
        Optional<Apolice> apoliceLocal = apolices.stream()
                .filter(a -> a.getNumero().equalsIgnoreCase(numero))
                .findFirst();

        if (apoliceLocal.isPresent()) {
            return apoliceLocal;
        }

        Apolice apoliceCadastro = (Apolice) cadastro.buscar(numero);
        if (apoliceCadastro != null) {
            apolices.add(apoliceCadastro); 
            return Optional.of(apoliceCadastro);
        }

        return Optional.empty();
    }

    public Apolice buscar(String numero) {
        return findByNumero(numero).orElse(null);
    }

    public void insert(Apolice apolice) {
        apolices.add(apolice);
        cadastro.incluir(apolice, apolice.getNumero());
    }

    public boolean incluir(Apolice segurado) {
        if (findByNumero(segurado.getNumero()).isPresent()) {
            return false;
        } else {
            insert(segurado);
            return true;
        }
    }

    public Optional<Apolice> findByPlaca(String placa) {
        return Optional.empty();
    }

    public void update(String placa, Apolice novaApolice) {
    }

    public boolean alterar(Apolice segurado) {
        if (findByNumero(segurado.getNumero()).isEmpty()) {
            return false;
        } else {
            cadastro.alterar(segurado, segurado.getNumero());
            apolices.removeIf(a -> a.getNumero().equalsIgnoreCase(segurado.getNumero()));
            apolices.add(segurado);
            return true;
        }
    }

    public void remove(String placa) {

    }

    public boolean excluir(String numero) {
        if (findByNumero(numero).isEmpty()) {
            return false;
        } else {
            cadastro.excluir(numero);
            apolices.removeIf(a -> a.getNumero().equalsIgnoreCase(numero)); 
            return true;
        }
    }
}