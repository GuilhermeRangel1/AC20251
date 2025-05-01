package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.*;

public class TesteApoliceDAO extends ApoliceDAO {
    private ApoliceDAO dao = new ApoliceDAO();

    protected Class getClasse() {
        return ApoliceDAO.class;
    }

    @Test
    public void teste01() {
        String numeroApolice = "A123456";
        Endereco endereco = new Endereco("Rua das Flores", "12345-678", "100", "Apto 101", "Brasil", "São Paulo", "São Paulo");
        SeguradoEmpresa seguradoEmpresa = new SeguradoEmpresa("Empresa ABC Ltda", endereco, LocalDate.of(2010, 10, 10), new BigDecimal("100000.00"), "12.345.678/0001-99", 1000000.00, false);
        Veiculo veiculo = new Veiculo("ABC1234", 2020, seguradoEmpresa, null, CategoriaVeiculo.BASICO);
        cadastro.incluir(new Apolice(numeroApolice, veiculo, new BigDecimal("500.00"), new BigDecimal("1200.00"), new BigDecimal("100000.00"), LocalDate.now()), numeroApolice);
        Apolice apoliceEncontrada = dao.buscar(numeroApolice);
        Assertions.assertNotNull(apoliceEncontrada);
        Assertions.assertEquals(numeroApolice, apoliceEncontrada.getNumero());
        Assertions.assertEquals(veiculo, apoliceEncontrada.getVeiculo());
        Assertions.assertEquals(new BigDecimal("500.00"), apoliceEncontrada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1200.00"), apoliceEncontrada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("100000.00"), apoliceEncontrada.getValorMaximoSegurado());
        Assertions.assertNotNull(apoliceEncontrada.getDataCriacao()); 
    }

    @Test
    public void teste02() {
        String numero = "A31321321";
        Endereco endereco = new Endereco("Rua das Bananeiras", "12345-678", "001", "Apto 21", "Brasil", "São Paulo", "São Paulo");
        SeguradoEmpresa seguradoEmpresa = new SeguradoEmpresa("Empresa XYZ Ltda", endereco, LocalDate.of(2010, 10, 10), new BigDecimal("5000.00"), "12.345.678/0001-99", 1000000.00, false);
        Veiculo veiculo = new Veiculo("XYZ5678", 2022, seguradoEmpresa, null, CategoriaVeiculo.LUXO);
        cadastro.incluir(new Apolice(numero, veiculo, new BigDecimal("500.00"), new BigDecimal("1200.00"), new BigDecimal("100000.00"), LocalDate.now()), numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
        Assertions.assertNull(dao.buscar(numero)); 
    }
}