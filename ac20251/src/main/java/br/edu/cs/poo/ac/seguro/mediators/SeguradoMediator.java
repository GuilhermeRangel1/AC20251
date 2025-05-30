package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDate;
import br.edu.cs.poo.ac.seguro.entidades.Endereco;

public class SeguradoMediator {

	private static SeguradoMediator med = new SeguradoMediator();

    private SeguradoMediator() {}

    public static SeguradoMediator getInstancia() {
        return med;
    }
    public String validarNome(String nome) {
        if (StringUtils.ehNuloOuBranco(nome)) {
            return "Nome deve ser informado";
        }
        if (nome.length() > 100) {
            return "Tamanho do nome deve ser no m�ximo 100 caracteres";
        }
        return null;
    }

    public String validarEndereco(Endereco endereco) {
        if (endereco == null) {
            return "Endere�o deve ser informado";
        }

        String validacaoLogradouro = validarCampoObrigatorio(endereco.getLogradouro(), "Logradouro");
        if (validacaoLogradouro != null) return validacaoLogradouro;

        String validacaoCep = validarCep(endereco.getCep());
        if (validacaoCep != null) return validacaoCep;

        String validacaoCidade = validarCampoObrigatorio(endereco.getCidade(), "Cidade");
        if (validacaoCidade != null) return "Cidade deve ser informada";
        if(endereco.getCidade().length()>100 == true) return "Tamanho da cidade deve ser no m�ximo 100 caracteres";

        String validacaoEstado = validarSiglaEstado(endereco.getEstado());
        if (validacaoEstado != null) return validacaoEstado;

        String validacaoPais = validarCampoObrigatorio(endereco.getPais(), "País");
        if (validacaoPais != null) return "Pa�s deve ser informado";
        if(endereco.getPais().length()>40 == true) return "Tamanho do pa�s deve ser no m�ximo 40 caracteres";

        if (endereco.getNumero() != null && endereco.getNumero().length() > 20) {
            return "Tamanho do n�mero deve ser no m�ximo 20 caracteres";
        }
        if (endereco.getComplemento() != null && endereco.getComplemento().length() > 30) {
            return "Tamanho do complemento deve ser no m�ximo 30 caracteres";
        }
        return null;
    }


    private String validarCampoObrigatorio(String campo, String nomeCampo) {
        if (StringUtils.ehNuloOuBranco(campo)) {
            return nomeCampo + " deve ser informado";
        }
        return null;
    }

    private String validarCep(String cep) {
        if (StringUtils.ehNuloOuBranco(cep)) {
            return "CEP deve ser informado";
        }
        if (cep.length() != 8) {
            return "Tamanho do CEP deve ser 8 caracteres";
        }
        if (!StringUtils.temSomenteNumeros(cep)) {
            return "CEP deve ter formato NNNNNNNN";
        }
        return null;
    }

    private String validarSiglaEstado(String estado) {
        if (StringUtils.ehNuloOuBranco(estado)) {
            return "Sigla do estado deve ser informada";
        }
        if (estado.length() != 2) {
            return "Tamanho da sigla do estado deve ser 2 caracteres";
        }
        return null;
    }

    public String validarDataCriacao(LocalDate dataCriacao) {
        if (dataCriacao == null) {
            return "Data da cria��o deve ser informada";
        }
        if (dataCriacao.isAfter(LocalDate.now())) {
            return "Data da cria��o deve ser menor ou igual � data atual";
        }
        return null;
    }

    public BigDecimal ajustarDebitoBonus(BigDecimal bonus, BigDecimal valorDebito) {
        if (bonus == null || valorDebito == null) {
            return BigDecimal.ZERO;
        }
        if (bonus.compareTo(valorDebito) <= 0) {
            return bonus;
        } else {
            return valorDebito;
        }
    }

}
