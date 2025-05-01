package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;


public class SeguradoPessoaMediator {
	private SeguradoPessoaDAO seguradoPessoaDAO= new SeguradoPessoaDAO();
	private static SeguradoPessoaMediator med = new SeguradoPessoaMediator();
	private SeguradoMediator seguradoMediator = SeguradoMediator.getInstancia();

	private SeguradoPessoaMediator() {}
	
	public static SeguradoPessoaMediator getInstancia() {
		return med;
	}
	
	public String validarCpf(String cpf) {
	    if (StringUtils.ehNuloOuBranco(cpf)) {
	        return "CPF deve ser informado";
	    }
	    if(cpf.length()!= 11) {
	    	return "CPF deve ter 11 caracteres";
	    }
	    if(!ValidadorCpfCnpj.ehCpfValido(cpf)) {
	    	return "CPF com dígito inválido";
	    }
	    return null;
	}
	public String validarRenda(double renda) {
		if(renda<0.0) {
			return "Renda deve ser maior ou igual à zero";
		}
		return null;
	}
	public String incluirSeguradoPessoa(SeguradoPessoa seg) {
		String msg = validarSeguradoPessoa(seg);
		if(msg!=null) {
			return msg;
		}
		if(seguradoPessoaDAO.buscar(seg.getCpf())!= null) {
			return "CPF do segurado pessoa já existente";
		}
		seguradoPessoaDAO.incluir(seg);
		return null;
	}
	public String alterarSeguradoPessoa(SeguradoPessoa seg) {
		if(seguradoPessoaDAO.buscar(seg.getCpf()) == null) {
			return "CPF do segurado pessoa não encontrado";
		}
		String msg = validarSeguradoPessoa(seg);
	    if(msg != null) {
	        return msg;
	    }
	    seguradoPessoaDAO.alterar(seg);
	    return null;
	}
	public String excluirSeguradoPessoa(String cpf) {
		if(seguradoPessoaDAO.buscar(cpf) == null)  return "CPF do segurado pessoa não existente";
		seguradoPessoaDAO.excluir(cpf);
		return null;
	}
	public SeguradoPessoa buscarSeguradoPessoa(String cpf) {
		return seguradoPessoaDAO.buscar(cpf);
	}
	public String validarSeguradoPessoa(SeguradoPessoa seg) {
	    if(seg == null) return "Segurado pessoa não pode ser nulo";

	    String msg = seguradoMediator.validarNome(seg.getNome());
	    
	    if(msg != null) return msg;

	    msg = seguradoMediator.validarEndereco(seg.getEndereco());
	    
	    if(msg != null) return msg;
	    
	    msg = validarCpf(seg.getCpf());
	    
	    if(msg != null) return msg;
	    
	    msg = seguradoMediator.validarDataCriacao(seg.getDataNascimento());
	    
	    if(msg != null) return msg;
	    
	    msg = validarRenda(seg.getRenda());
	    
	    if(msg != null) return msg;
	    
	    return null;  
	}
}