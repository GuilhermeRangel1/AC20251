package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.PrecoAno;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class ApoliceMediator {
    private SeguradoPessoaDAO daoSegPes = new SeguradoPessoaDAO();
    private SeguradoEmpresaDAO daoSegEmp = new SeguradoEmpresaDAO();
    private VeiculoDAO daoVel = new VeiculoDAO();
    private ApoliceDAO daoApo = new ApoliceDAO();
    private SinistroDAO daoSin = new SinistroDAO();

    private static ApoliceMediator instancia;

    private ApoliceMediator() {
    }

    public static ApoliceMediator getInstancia() {
        if (instancia == null) {
            instancia = new ApoliceMediator();
        }
        return instancia;
    }

    public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
        if (dados == null) {
            return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
        }

        String cpfOuCnpj = dados.getCpfOuCnpj();
        String placa = dados.getPlaca();
        Integer ano = dados.getAno();
        BigDecimal valorMaximoSegurado = dados.getValorMaximoSegurado();
        Integer codigoCategoria = dados.getCodigoCategoria();

        String erroValidacao = validarTodosDadosVeiculo(dados);
        if (erroValidacao != null) {
            return new RetornoInclusaoApolice(null, erroValidacao);
        }

        erroValidacao = validarCpfCnpjValorMaximo(dados);
        if (erroValidacao != null) {
            return new RetornoInclusaoApolice(null, erroValidacao);
        }

        Veiculo veiculo = daoVel.buscar(placa);
        SeguradoPessoa seguradoPessoa = null;
        SeguradoEmpresa seguradoEmpresa = null;

        if (veiculo == null) {
            if (cpfOuCnpj.length() == 11) {
                seguradoPessoa = daoSegPes.buscar(cpfOuCnpj);
                if (seguradoPessoa == null) {
                    return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
                }
            } else {
                seguradoEmpresa = daoSegEmp.buscar(cpfOuCnpj);
                if (seguradoEmpresa == null) {
                    return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
                }
            }

            CategoriaVeiculo categoria = CategoriaVeiculo.values()[codigoCategoria - 1];
            PrecoAno precoAno = obterPrecoAno(categoria.getPrecosAnos(), ano);
            if (precoAno == null || valorMaximoSegurado.compareTo(BigDecimal.valueOf(precoAno.getPreco()).multiply(new BigDecimal("0.75"))) < 0 || valorMaximoSegurado.compareTo(BigDecimal.valueOf(precoAno.getPreco())) > 0) {
                return new RetornoInclusaoApolice(null, "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria");
            }

            veiculo = new Veiculo(placa, ano, seguradoEmpresa, seguradoPessoa, categoria);
            daoVel.incluir(veiculo);
        } else {
            if (cpfOuCnpj.length() == 11) {
                seguradoPessoa = daoSegPes.buscar(cpfOuCnpj);
                if (seguradoPessoa == null) {
                    return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
                }
                veiculo.setProprietarioPessoa(seguradoPessoa);
                veiculo.setProprietarioEmpresa(null);
            } else {
                seguradoEmpresa = daoSegEmp.buscar(cpfOuCnpj);
                if (seguradoEmpresa == null) {
                    return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
                }
                veiculo.setProprietarioEmpresa(seguradoEmpresa);
                veiculo.setProprietarioPessoa(null);
            }
            daoVel.alterar(veiculo);

            String numeroApoliceExistente = gerarNumeroApolice(cpfOuCnpj, placa, Year.now().getValue());
            if (daoApo.buscar(numeroApoliceExistente) != null) {
                return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
            }
        }

        BigDecimal vpa = valorMaximoSegurado.multiply(new BigDecimal("0.03"));
        BigDecimal vpb;
        if (seguradoEmpresa != null && seguradoEmpresa.isEhLocadoraDeVeiculos()) {
            vpb = vpa.multiply(new BigDecimal("1.2"));
        } else {
            vpb = vpa;
        }

        BigDecimal bonus = BigDecimal.ZERO;
        if (seguradoPessoa != null) {
            bonus = seguradoPessoa.getBonus();
        } else if (seguradoEmpresa != null) {
            bonus = seguradoEmpresa.getBonus();
        }
        BigDecimal vpc = vpb.subtract(bonus.divide(new BigDecimal("10")));
        BigDecimal valorPremio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;

        BigDecimal valorFranquia = vpb.multiply(new BigDecimal("1.30"));

        int anoAtual = Year.now().getValue();
        String numeroApolice = gerarNumeroApolice(cpfOuCnpj, placa, anoAtual);

        Apolice apolice = new Apolice(numeroApolice, veiculo, valorFranquia, valorPremio, valorMaximoSegurado, LocalDate.now());
        daoApo.incluir(apolice);

        LocalDate anoAnterior = LocalDate.now().minusYears(1);
        Sinistro[] sinistrosAnoAnterior = daoSin.buscarTodos();
        boolean temSinistroAnoAnterior = false;
        for (Sinistro sinistro : sinistrosAnoAnterior) {
            if (sinistro.getVeiculo().equals(veiculo) && sinistro.getDataHoraSinistro().getYear() == anoAnterior.getYear()) {
                temSinistroAnoAnterior = true;
                break;
            }
        }

        if (!temSinistroAnoAnterior) {
            BigDecimal acrescimoBonus = valorPremio.multiply(new BigDecimal("0.30"));
            if (seguradoPessoa != null) {
                seguradoPessoa.setBonus(seguradoPessoa.getBonus().add(acrescimoBonus));
                daoSegPes.alterar(seguradoPessoa);
            } else if (seguradoEmpresa != null) {
                seguradoEmpresa.setBonus(seguradoEmpresa.getBonus().add(acrescimoBonus));
                daoSegEmp.alterar(seguradoEmpresa);
            }
        }

        return new RetornoInclusaoApolice(numeroApolice, null);
    }

    public Apolice buscarApolice(String numero) {
        if (StringUtils.ehNuloOuBranco(numero)) {
            return null;
        }
        return daoApo.buscar(numero);
    }

    public String excluirApolice(String numero) {
        if (StringUtils.ehNuloOuBranco(numero)) {
            return "Número deve ser informado";
        }

        Apolice apolice = daoApo.buscar(numero);
        if (apolice == null) {
            return "Apólice inexistente";
        }

        Sinistro[] sinistros = daoSin.buscarTodos();
        for (Sinistro sinistro : sinistros) {
            if (sinistro.getVeiculo().equals(apolice.getVeiculo()) && sinistro.getDataHoraSinistro().getYear() == apolice.getDataCriacao().getYear()) {
                return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
            }
        }

        daoApo.excluir(numero);
        return null;
    }

    private String validarTodosDadosVeiculo(DadosVeiculo dados) {
        if (dados == null) {
            return "Dados do veículo devem ser informados";
        }
        if (StringUtils.ehNuloOuBranco(dados.getCpfOuCnpj())) {
            return "CPF ou CNPJ deve ser informado";
        }
        if (StringUtils.ehNuloOuBranco(dados.getPlaca())) {
            return "Placa do veículo deve ser informada";
        }
        if (dados.getAno() < 2020 || dados.getAno() > 2025) { 
            return "Ano tem que estar entre 2020 e 2025, incluindo estes";
        }
        if (dados.getValorMaximoSegurado() == null) {
            return "Valor máximo segurado deve ser informado";
        }
        if (dados.getCodigoCategoria() < 1 || dados.getCodigoCategoria() > 5) {
            return "Categoria inválida";
        }
        return null;
    }

    private String validarCpfCnpjValorMaximo(DadosVeiculo dados) {
        String cpfOuCnpj = dados.getCpfOuCnpj();
        BigDecimal valorMaximoSegurado = dados.getValorMaximoSegurado();
        Integer ano = dados.getAno();
        Integer codigoCategoria = dados.getCodigoCategoria();

        if (cpfOuCnpj.length() == 11 && !ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
            return "CPF inválido";
        }
        if (cpfOuCnpj.length() == 14 && !ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) {
            return "CNPJ inválido";
        }

        CategoriaVeiculo categoria = CategoriaVeiculo.values()[codigoCategoria - 1];
        PrecoAno precoAno = obterPrecoAno(categoria.getPrecosAnos(), ano);

        if (precoAno != null && (valorMaximoSegurado.compareTo(BigDecimal.valueOf(precoAno.getPreco()).multiply(new BigDecimal("0.75"))) < 0 || valorMaximoSegurado.compareTo(BigDecimal.valueOf(precoAno.getPreco())) > 0)) {
            return "Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria";
        } else if (precoAno == null) {
            return "Preço do veículo não encontrado para a categoria e ano informados";
        }
        return null;
    }

    private PrecoAno obterPrecoAno(PrecoAno[] precosAnos, int ano) {
        for (PrecoAno precoAno : precosAnos) {
            if (precoAno.getAno() == ano) {
                return precoAno;
            }
        }
        return null;
    }

    private String gerarNumeroApolice(String cpfOuCnpj, String placa, int ano) {
        if (cpfOuCnpj.length() == 11) {
            return String.format("%d000%s%s", ano, cpfOuCnpj, placa);
        } else {
            return String.format("%d%s%s", ano, cpfOuCnpj, placa);
        }
    }
}