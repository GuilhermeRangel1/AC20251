package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.Registro;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.ValidadorCpfCnpj;

public class ApoliceMediator {
    private static ApoliceMediator instancia;

    private SeguradoPessoaDAO daoSegPes;
    private SeguradoEmpresaDAO daoSegEmp;
    private VeiculoDAO daoVel;
    private ApoliceDAO daoApo;
    private SinistroDAO daoSin;


    private ApoliceMediator(
            SeguradoPessoaDAO daoSegPes,
            SeguradoEmpresaDAO daoSegEmp,
            VeiculoDAO daoVel,
            ApoliceDAO daoApo,
            SinistroDAO daoSin
    ) {
        this.daoSegPes = daoSegPes;
        this.daoSegEmp = daoSegEmp;
        this.daoVel = daoVel;
        this.daoApo = daoApo;
        this.daoSin = daoSin;
    }

    public static synchronized ApoliceMediator getInstancia() {
        if (instancia == null) {
            instancia = new ApoliceMediator(
                    new SeguradoPessoaDAO(),
                    new SeguradoEmpresaDAO(),
                    new VeiculoDAO(),
                    new ApoliceDAO(),
                    new SinistroDAO()
            );
        }
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        if (dados == null) {
            return new RetornoInclusaoApolice(null, "Dados do ve�culo devem ser informados");
        }

        if (dados.getPlaca() == null || dados.getPlaca().isBlank()) {
            return new RetornoInclusaoApolice(null, "Placa do ve�culo deve ser informada");
        }

        if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().trim().isEmpty()) {
            return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");
        }

        String cpfOuCnpj = dados.getCpfOuCnpj().replaceAll("[^0-9]", "");

        if (cpfOuCnpj.length() == 11) {
            if (!ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
                return new RetornoInclusaoApolice(null, "CPF inv�lido");
            }
        } else if (cpfOuCnpj.length() == 14) {
            if (!ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) {
                return new RetornoInclusaoApolice(null, "CNPJ inv�lido");
            }
        } else {
            return new RetornoInclusaoApolice(null, "CPF ou CNPJ inválido (tamanho incorreto)");
        }

        int anoAtual = LocalDate.now().getYear();
        if (dados.getAno() < 2020 || dados.getAno() > anoAtual) {
            return new RetornoInclusaoApolice(null,
            		"Ano tem que estar entre 2020 e 2025, incluindo estes");
        }

        if (dados.getCodigoCategoria() <= 0 || dados.getCodigoCategoria() > CategoriaVeiculo.values().length) {
            return new RetornoInclusaoApolice(null, "Categoria inv�lida");
        }
        CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria() - 1];

        if (dados.getValorMaximoSegurado() == null) {
            return new RetornoInclusaoApolice(null, "Valor m�ximo segurado deve ser informado");
        }
        if (dados.getValorMaximoSegurado().compareTo(BigDecimal.ZERO) <= 0) {
            return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser maior que zero");
        }

        double valorReferencia = 0.0;
        for (PrecoAno precoAno : categoria.getPrecosAnos()) {
            if (precoAno.getAno() == dados.getAno()) {
                valorReferencia = precoAno.getPreco();
                break;
            }
        }
        if (valorReferencia == 0.0) {
             return new RetornoInclusaoApolice(null, "Não foi encontrado valor de referência para o ano e categoria do veículo.");
        }

        BigDecimal valorMinimoAceitavel = BigDecimal.valueOf(valorReferencia * 0.75).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorMaximoAceitavel = BigDecimal.valueOf(valorReferencia).setScale(2, RoundingMode.HALF_UP);

        if (dados.getValorMaximoSegurado().compareTo(valorMinimoAceitavel) < 0 ||
            dados.getValorMaximoSegurado().compareTo(valorMaximoAceitavel) > 0) {
            return new RetornoInclusaoApolice(
                    null,
                    "Valor m�ximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria"
            );
        }

        boolean isCpf = cpfOuCnpj.length() == 11;
        SeguradoPessoa segPes = null;
        SeguradoEmpresa segEmp = null;

        if (isCpf) {
            segPes = daoSegPes.buscar(cpfOuCnpj);
            if (segPes == null) {
                return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
            }
        } else {
            segEmp = daoSegEmp.buscar(cpfOuCnpj);
            if (segEmp == null) {
                return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
            }
        }

        String numeroApolice = isCpf
                ? anoAtual + "000" + cpfOuCnpj + dados.getPlaca()
                : anoAtual + cpfOuCnpj + dados.getPlaca();

        Optional<Apolice> apoliceExistente = daoApo.findByNumero(numeroApolice);
        if (apoliceExistente.isPresent()) {
            return new RetornoInclusaoApolice(null, "Ap�lice j� existente para ano atual e ve�culo");
        }

        Veiculo veiculo = daoVel.buscar(dados.getPlaca());
        if (veiculo == null) {
            veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), segEmp, segPes, categoria);
            daoVel.incluir(veiculo);
        } else {
            if (isCpf) {
                veiculo.setProprietario(segPes);
            } else {
                veiculo.setProprietario(segEmp);
            }
            daoVel.alterar(veiculo);
        }

        BigDecimal vpa = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal vpb = vpa;
        if (segEmp != null && segEmp.isEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal bonus = BigDecimal.ZERO;
        if (isCpf && segPes != null) {
            bonus = segPes.getBonus();
        } else if (!isCpf && segEmp != null) {
            bonus = segEmp.getBonus();
        }

        BigDecimal vpc = vpb.subtract(bonus.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP));

        BigDecimal premio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;
        premio = premio.setScale(2, RoundingMode.HALF_UP);

        BigDecimal franquia = vpb.multiply(new BigDecimal("1.3"))
                .setScale(2, RoundingMode.HALF_UP);

        LocalDate dataCriacaoApolice = LocalDate.now();

        Apolice novaApolice = new Apolice(
                numeroApolice,
                veiculo,
                franquia,
                premio,
                dados.getValorMaximoSegurado(),
                dataCriacaoApolice
        );

        boolean incluido = daoApo.incluirApolice(novaApolice);
        if (!incluido) {
            return new RetornoInclusaoApolice(null, "Falha ao incluir a apólice no sistema de persistência.");
        }

        final Veiculo veiculoParaSinistro = veiculo;
        final int anoAnterior = dataCriacaoApolice.minusYears(1).getYear();

        List<Sinistro> sinistrosDoAnoAnterior = Arrays.stream(daoSin.buscarTodos())
                .filter(s -> s instanceof Sinistro)
                .map(s -> (Sinistro)s)
                .filter(s -> s.getDataHoraSinistro() != null && s.getDataHoraSinistro().getYear() == anoAnterior)
                .filter(s -> s.getVeiculo() != null && s.getVeiculo().equals(veiculoParaSinistro))
                .collect(Collectors.toList());

        boolean teveSinistroNoAnoAnterior = !sinistrosDoAnoAnterior.isEmpty();

        if (!teveSinistroNoAnoAnterior) {
            BigDecimal credito = premio.multiply(new BigDecimal("0.3"))
                    .setScale(2, RoundingMode.HALF_UP);
            if (isCpf && segPes != null) {
                segPes.setBonus(segPes.getBonus().add(credito));
                daoSegPes.alterar(segPes);
            } else if (!isCpf && segEmp != null) {
                segEmp.setBonus(segEmp.getBonus().add(credito));
                daoSegEmp.alterar(segEmp);
            }
        }

        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        if (numero == null || numero.isBlank()) {
            return null;
        }
        return daoApo.findByNumero(numero).orElse(null);
    }

    public String excluirApolice(String numero) {
        if (numero == null || numero.isBlank()) {
            return "N�mero deve ser informado";
        }

        Optional<Apolice> apoliceOpt = daoApo.findByNumero(numero);
        if (apoliceOpt.isEmpty()) {
            return "Ap�lice inexistente";
        }

        Apolice apolice = apoliceOpt.get();
        int anoVigenciaApolice = apolice.getDataCriacao().getYear();

        final Veiculo veiculoApolice = apolice.getVeiculo();
        List<Sinistro> sinistrosDoAnoVigencia = Arrays.stream(daoSin.buscarTodos())
                .filter(s -> s instanceof Sinistro)
                .map(s -> (Sinistro)s)
                .filter(s -> s.getDataHoraSinistro() != null && s.getDataHoraSinistro().getYear() == anoVigenciaApolice)
                .filter(s -> s.getVeiculo() != null && s.getVeiculo().equals(veiculoApolice))
                .collect(Collectors.toList());

        boolean temSinistroNesteAnoParaApolice = !sinistrosDoAnoVigencia.isEmpty();

        if (temSinistroNesteAnoParaApolice) {
            return "Não é possível excluir a apólice: Existe sinistro cadastrado para o veículo em questão no mesmo ano de vigência da apólice.";
        }

        boolean excluido = daoApo.excluirApolice(numero);
        if (!excluido) {
            return "Falha ao excluir a apólice no sistema de persistência.";
        }

        return null;
    }

    public List<Apolice> buscarTodasApolices() {
        return daoApo.buscarTodasAsApolices();
    }
}