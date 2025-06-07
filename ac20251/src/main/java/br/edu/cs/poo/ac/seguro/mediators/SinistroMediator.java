package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.time.temporal.ChronoUnit;


import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;

public class SinistroMediator {

    private VeiculoDAO daoVeiculo = new VeiculoDAO();
    private ApoliceDAO daoApolice = new ApoliceDAO();
    private SinistroDAO daoSinistro = new SinistroDAO();
    private static SinistroMediator instancia;

    public static SinistroMediator getInstancia() {
        if (instancia == null) {
            instancia = new SinistroMediator();
        }
        return instancia;
    }

    private SinistroMediator() {
    }

    public String incluirSinistro(DadosSinistro dados, LocalDateTime dataHoraAtual) throws ExcecaoValidacaoDados {
        ExcecaoValidacaoDados excecoes = new ExcecaoValidacaoDados(null);

        LocalDateTime dataHoraAtualTruncada = dataHoraAtual.truncatedTo(ChronoUnit.SECONDS);

        if (dados == null) {
            excecoes.adicionarMensagem("Dados do sinistro devem ser informados");
        } else {
            LocalDateTime dataHoraSinistroTruncada = (dados.getDataHoraSinistro() != null) ?
                                                      dados.getDataHoraSinistro().truncatedTo(ChronoUnit.SECONDS) : null;

            if (dataHoraSinistroTruncada == null) { 
                excecoes.adicionarMensagem("Data/hora do sinistro deve ser informada");
            } else {
                if (!dataHoraSinistroTruncada.isBefore(dataHoraAtualTruncada)) { 
                    excecoes.adicionarMensagem("Data/hora do sinistro deve ser menor que a data/hora atual");
                }
            }

            if (dados.getPlaca() == null || dados.getPlaca().trim().isEmpty()) {
                excecoes.adicionarMensagem("Placa do Veiculo deve ser informada");
            }

            if (dados.getUsuarioRegistro() == null || dados.getUsuarioRegistro().trim().isEmpty()) {
                excecoes.adicionarMensagem("Usuario do registro de sinistro deve ser informado");
            }

            if (dados.getValorSinistro() <= 0) {
                excecoes.adicionarMensagem("Valor do sinistro deve ser maior que zero");
            }

            if (TipoSinistro.getTipoSinistro(dados.getCodigoTipoSinistro()) == null) {
                excecoes.adicionarMensagem("Codigo do tipo de sinistro invalido");
            }
        }

        if (!excecoes.getMensagens().isEmpty()) {
            throw excecoes;
        }


        Veiculo veiculo = null;
        String placaFormatada = dados.getPlaca().trim();
        if (!placaFormatada.isEmpty()) {
            veiculo = daoVeiculo.buscar(placaFormatada);
            if (veiculo == null) {
                excecoes.adicionarMensagem("Veículo não cadastrado");
            }
        }

        if (!excecoes.getMensagens().isEmpty()) {
            throw excecoes;
        }

        List<Apolice> todasApolices = new ArrayList<>();
        Registro[] apolicesArray = daoApolice.buscarTodos();
        if (apolicesArray != null) {
            for (Registro reg : apolicesArray) {
                if (reg instanceof Apolice) {
                    todasApolices.add((Apolice) reg);
                }
            }
        }

        Apolice apoliceVigente = null;

        if (Objects.nonNull(todasApolices)) {
            for (Apolice apolice : todasApolices) {
                if (apolice != null && apolice.getVeiculo() != null && apolice.getDataCriacao() != null) {
                    if (apolice.getVeiculo().getIdUnico().equals(placaFormatada)) {
                        LocalDateTime dataInicioVigencia = apolice.getDataCriacao().atStartOfDay().truncatedTo(ChronoUnit.SECONDS);
                        LocalDateTime dataFimVigencia = dataInicioVigencia.plusYears(1);
                        LocalDateTime dataSinistroTruncada = dados.getDataHoraSinistro().truncatedTo(ChronoUnit.SECONDS);


                        if (!dataSinistroTruncada.isBefore(dataInicioVigencia) &&
                            dataSinistroTruncada.isBefore(dataFimVigencia)) {
                            apoliceVigente = apolice;
                            break;
                        }
                    }
                }
            }
        }

        if (apoliceVigente == null) {
            excecoes.adicionarMensagem("Nao existe apolice vigente para o veiculo");
        } else {
            BigDecimal valorSinistroBD = BigDecimal.valueOf(dados.getValorSinistro());
            if (valorSinistroBD.compareTo(apoliceVigente.getValorMaximoSegurado()) > 0) {
                excecoes.adicionarMensagem("Valor do sinistro nao pode ultrapassar o valor maximo segurado constante na apolice");
            }
        }

        if (!excecoes.getMensagens().isEmpty()) {
            throw excecoes;
        }


        String numeroApoliceCobertura = apoliceVigente.getNumero();
        List<Sinistro> sinistrosExistentes = new ArrayList<>();
        Registro[] sinistrosArray = daoSinistro.buscarTodos();
        if (sinistrosArray != null) {
            for (Registro reg : sinistrosArray) {
                if (reg instanceof Sinistro) {
                    sinistrosExistentes.add((Sinistro) reg);
                }
            }
        }

        int sequencial = 1;

        if (Objects.nonNull(sinistrosExistentes)) {
            List<Sinistro> sinistrosDaApolice = new ArrayList<>();
            for (Sinistro s : sinistrosExistentes) {
                if (s.getNumeroApolice() != null && s.getNumeroApolice().equals(numeroApoliceCobertura)) {
                    sinistrosDaApolice.add(s);
                }
            }

            if (!sinistrosDaApolice.isEmpty()) {
                Collections.sort(sinistrosDaApolice, new ComparadorSinistroSequencial());
                int ultimoSequencial = sinistrosDaApolice.get(sinistrosDaApolice.size() - 1).getSequencial();
                sequencial = ultimoSequencial + 1;
            }
        }

        String numeroSinistro = "S" + numeroApoliceCobertura + String.format("%03d", sequencial);

        Sinistro novoSinistro = new Sinistro(
                numeroSinistro,
                veiculo, 
                dados.getDataHoraSinistro().truncatedTo(ChronoUnit.SECONDS),
                dataHoraAtualTruncada,
                dados.getUsuarioRegistro(),
                BigDecimal.valueOf(dados.getValorSinistro()),
                TipoSinistro.getTipoSinistro(dados.getCodigoTipoSinistro())
        );
        novoSinistro.setSequencial(sequencial);
        novoSinistro.setNumeroApolice(numeroApoliceCobertura);

        boolean incluido = daoSinistro.incluir(novoSinistro);
        if (!incluido) {
            excecoes.adicionarMensagem("Falha ao incluir o sinistro no sistema de persistência.");
            throw excecoes;
        }

        return novoSinistro.getIdUnico();
    }
}