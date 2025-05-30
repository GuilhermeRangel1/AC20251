package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class SeguradoEmpresaMediator {
	private SeguradoEmpresaDAO seguradoEmpresaDAO = new SeguradoEmpresaDAO();
	private static SeguradoEmpresaMediator med = new SeguradoEmpresaMediator();
	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();

	private SeguradoEmpresaMediator() {}

	public static SeguradoEmpresaMediator getInstancia() {
		return med;
	}

	public String validarCnpj(String cnpj) {
		if (StringUtils.ehNuloOuBranco(cnpj)) {
			return "CNPJ deve ser informado";
		}
		if (cnpj.length() != 14) {
			return "CNPJ deve ter 14 caracteres";
		}
		if (!ValidadorCpfCnpj.ehCnpjValido(cnpj)) {
			return "CNPJ com d�gito inv�lido";
		}
		return null;
	}

	public String validarFaturamento(double faturamento) {
		if (faturamento <= 0.0) {
			return "Faturamento deve ser maior que zero";
		}
		return null;
	}

	public String incluirSeguradoEmpresa(SeguradoEmpresa seg) {
		String msg = validarSeguradoEmpresa(seg);
		if (msg != null) {
			return msg;
		}
		if (seguradoEmpresaDAO.buscar(seg.getCnpj()) != null) {
			return "CNPJ do segurado empresa j� existente";
		}
		seguradoEmpresaDAO.incluir(seg);
		return null;
	}


	public String alterarSeguradoEmpresa(SeguradoEmpresa seg) {
	    String msg = validarSeguradoEmpresa(seg);
	    if (msg != null) {
	        return msg; 
	    }
	    if (seguradoEmpresaDAO.buscar(seg.getCnpj()) == null) {
	        return "CNPJ do segurado empresa n�o existente";
	    }
	    seguradoEmpresaDAO.alterar(seg);
	    return null;
	}


	public String excluirSeguradoEmpresa(String cnpj) {
		if (seguradoEmpresaDAO.buscar(cnpj) == null) {
			return "CNPJ do segurado empresa n�o existente";
		}
		seguradoEmpresaDAO.excluir(cnpj);
		return null;
	}

	public SeguradoEmpresa buscarSeguradoEmpresa(String cnpj) {
		return seguradoEmpresaDAO.buscar(cnpj);
	}

	public String validarSeguradoEmpresa(SeguradoEmpresa seg) {
		if (seg == null) return "Segurado empresa não pode ser nulo";

		String msg = seguradoMediator.validarNome(seg.getNome());
		if (msg != null) return msg;

		msg = seguradoMediator.validarEndereco(seg.getEndereco());
		if (msg != null) return msg;

		msg = validarCnpj(seg.getCnpj());
		if (msg != null) return msg;

		msg = seguradoMediator.validarDataCriacao(seg.getDataAbertura());
		if (msg != null) return "Data da abertura deve ser informada";

		msg = validarFaturamento(seg.getFaturamento());
		if (msg != null) return msg;

		return null;
	}
}
