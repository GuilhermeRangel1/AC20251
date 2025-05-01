package br.edu.cs.poo.ac.seguro.testes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.*;

public class TesteSinistroDAO extends SinistroDAO{
	private SinistroDAO dao = new SinistroDAO();
	protected Class getClasse() {
		return SinistroDAO.class;
	}
	@Test
	public void teste01() {	
        Endereco endereco = new Endereco("Rua das Flores", "12345-678", "100", "Apto 101", "Brasil", "São Paulo", "São Paulo");
            SeguradoEmpresa seguradoEmpresa = new SeguradoEmpresa("Empresa ABC Ltda", endereco, LocalDate.of(2010, 10, 10), new BigDecimal("100000.00"), "12.345.678/0001-99", 1000000.00, false);
            Veiculo veiculo = new Veiculo("ABC1234", 2020, seguradoEmpresa, null, CategoriaVeiculo.BASICO);
		    LocalDateTime dataHoraSinistro = LocalDate.of(2020, 1, 1).atStartOfDay();
		    LocalDateTime dataHoraRegistro = LocalDateTime.now();
		String numero = "123456";
		cadastro.incluir(new Sinistro(numero,veiculo,dataHoraSinistro,dataHoraRegistro, "Carlos", BigDecimal.ZERO,TipoSinistro.COLISAO), numero);
		Sinistro seg = dao.buscar(numero);
		Assertions.assertNull(seg);
	}
	@Test
	public void teste02() {
		Endereco endereco = new Endereco("Rua das Bananeiras", "8765-432", "001", "Apto 21", "Brasil", "São Paulo", "São Paulo");
		SeguradoPessoa seguradoPessoa = new SeguradoPessoa("Carlos Silva", endereco, LocalDate.of(1985, 5, 20), new BigDecimal("5000.00"), "123.456.789-00", 3000.0);
		Veiculo veiculo = new Veiculo("ABC1234", 2020, null, seguradoPessoa, CategoriaVeiculo.BASICO);
	    LocalDateTime dataHoraSinistro = LocalDate.of(2020, 1, 1).atStartOfDay();
	    LocalDateTime dataHoraRegistro = LocalDateTime.now();
	    String numero = "567890";
	    cadastro.incluir(new Sinistro(numero,veiculo,dataHoraSinistro,dataHoraRegistro, "Carlos", BigDecimal.ZERO,TipoSinistro.COLISAO), numero);
	    boolean ret = dao.excluir(numero);
	    Assertions.assertTrue(ret);
	}
}

