package br.edu.cs.poo.ac.seguro.daos;

import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Registro; 
import java.util.Optional;
import java.util.List; 
import java.util.ArrayList; 
import java.util.Arrays; 
import java.util.stream.Collectors; 

public class ApoliceDAO extends DAOGenerico<Apolice> {

    @Override
    public Class<?> getClasseEntidade() {
        return Apolice.class;
    }

    public Optional<Apolice> findByNumero(String numero) {
        Apolice apolice = super.buscar(numero);
        return Optional.ofNullable(apolice);
    }

    public boolean incluirApolice(Apolice apolice) {
        if (findByNumero(apolice.getNumero()).isPresent()) {
            return false;
        } else {
            super.incluir(apolice);
            return true;
        }
    }

    public boolean alterarApolice(Apolice apolice) {
        if (findByNumero(apolice.getNumero()).isEmpty()) {
            return false;
        } else {
            super.alterar(apolice);
            return true;
        }
    }

    public boolean excluirApolice(String numero) {
        if (findByNumero(numero).isEmpty()) {
            return false;
        } else {
            super.excluir(numero);
            return true;
        }
    }

    public Optional<Apolice> findByPlaca(String placa) {
        return Optional.empty();
    }

    public List<Apolice> buscarTodasAsApolices() {
        Registro[] registros = super.buscarTodos();

        if (registros == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(registros)
                     .filter(r -> r instanceof Apolice)
                     .map(r -> (Apolice) r)
                     .collect(Collectors.toList());
    }
}