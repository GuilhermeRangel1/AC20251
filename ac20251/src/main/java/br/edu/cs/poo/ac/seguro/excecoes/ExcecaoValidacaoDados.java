package br.edu.cs.poo.ac.seguro.excecoes;

import java.util.ArrayList;
import java.util.List;

public class ExcecaoValidacaoDados extends Exception {

	private static final long serialVersionUID = 1L;
	private List<String> mensagens;

    public ExcecaoValidacaoDados(String mensagemInicial) {
        this.mensagens = new ArrayList<>();
        if (mensagemInicial != null && !mensagemInicial.trim().isEmpty()) {
            this.mensagens.add(mensagemInicial);
        }
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagem(String mensagem) {
        if (mensagem != null && !mensagem.trim().isEmpty()) {
            this.mensagens.add(mensagem);
        }
    }

    @Override
    public String getMessage() {
        return String.join("\n", mensagens); 
    }
}