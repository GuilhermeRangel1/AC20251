package br.edu.cs.poo.ac.seguro.mediators;

import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;
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
	    String msg = validarSeguradoPessoa(seg);
	    if (msg != null) {
	        return msg;              
	    }
	    if (seguradoPessoaDAO.buscar(seg.getCpf()) == null) {
	        return "CPF do segurado pessoa não existente";
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

	    msg = validarEndereco(seg.getEndereco());
	    
	    if(msg != null) return msg;
	    
	    msg = validarCpf(seg.getCpf());
	    
	    if(msg != null) return msg;
	    
	    msg = seguradoMediator.validarDataCriacao(seg.getDataNascimento());
	    
	    if(msg != null) return "Data do nascimento deve ser informada";
	    
	    msg = validarRenda(seg.getRenda());
	    
	    if(msg != null) return msg;
	    
	    return null;  
	}
	public String validarEndereco(Endereco endereco) {
	    if (endereco == null) {
	        return "Endereço deve ser informado";
	    }
	    if (StringUtils.ehNuloOuBranco(endereco.getLogradouro())) {
	        return "Logradouro deve ser informado";
	    }
	    if (StringUtils.ehNuloOuBranco(endereco.getNumero())) {
	        return "Número do endereço deve ser informado";
	    }
	    if (StringUtils.ehNuloOuBranco(endereco.getCep())) {
	        return "CEP deve ser informado";
	    }
	    if (StringUtils.ehNuloOuBranco(endereco.getPais())) {
	        return "País deve ser informado";
	    }
	    if (StringUtils.ehNuloOuBranco(endereco.getEstado())) {
	        return "Estado deve ser informado";
	    }
	    if (StringUtils.ehNuloOuBranco(endereco.getCidade())) {
	        return "Cidade deve ser informada";
	    }
	    return null;
	}


}