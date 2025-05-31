package br.edu.cs.poo.ac.seguro.telas;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.swing.JCheckBox; 

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;


public class SeguradoEmpresaCRUD extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField txtNome;
	private JFormattedTextField txtCnpj;
	private JFormattedTextField txtDataAbertura;
	private JFormattedTextField txtFaturamento;
	private JFormattedTextField txtBonus; 
	private JCheckBox chkLocadoraDeVeiculos; 

	private JTextField txtLogradouro;
	private JFormattedTextField txtCep;
	private JTextField txtNumero;
	private JTextField txtComplemento;
	private JTextField txtPais;
	private JTextField txtEstado;
	private JTextField txtCidade;

	private JButton btnIncluir;
	private JButton btnAlterar;
	private JButton btnBuscar;
	private JButton btnExcluir;
	private JButton btnLimpar;

	private SeguradoEmpresaMediator seguradoEmpresaMediator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private NumberFormat decimalFormat = NumberFormat.getNumberInstance();


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeguradoEmpresaCRUD frame = new SeguradoEmpresaCRUD();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SeguradoEmpresaCRUD() {
		seguradoEmpresaMediator = SeguradoEmpresaMediator.getInstancia();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 650); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		int labelX = 20;
		int fieldX = 170;
		int fieldWidth = 200;
		int fieldHeight = 25;
		int verticalSpacing = 35;

		JLabel lblSeguradoTitulo = new JLabel("DADOS DO SEGURADO EMPRESA");
		lblSeguradoTitulo.setBounds(labelX, 10, 250, 20);
		contentPane.add(lblSeguradoTitulo);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(labelX, 10 + verticalSpacing, 80, fieldHeight);
		contentPane.add(lblNome);
		txtNome = new JTextField();
		txtNome.setBounds(fieldX, 10 + verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtNome);
		txtNome.setColumns(10);

		JLabel lblCnpj = new JLabel("CNPJ:");
		lblCnpj.setBounds(labelX, 10 + 2 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblCnpj);
		try {
		    MaskFormatter cnpjMask = new MaskFormatter("##.###.###/####-##");
		    txtCnpj = new JFormattedTextField(cnpjMask);
		} catch (ParseException e) {
		    txtCnpj = new JFormattedTextField();
		    System.err.println("Erro ao criar máscara de CNPJ: " + e.getMessage());
		}
		txtCnpj.setBounds(fieldX, 10 + 2 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtCnpj);
		txtCnpj.setColumns(10);

		JLabel lblDataAbertura = new JLabel("Data Abertura (dd/MM/yyyy):");
		lblDataAbertura.setBounds(labelX, 10 + 3 * verticalSpacing, 150, fieldHeight);
		contentPane.add(lblDataAbertura);
		try {
		    MaskFormatter dateMask = new MaskFormatter("##/##/####");
		    txtDataAbertura = new JFormattedTextField(dateMask);
		} catch (ParseException e) {
		    txtDataAbertura = new JFormattedTextField();
		    System.err.println("Erro ao criar máscara de Data Abertura: " + e.getMessage());
		}
		txtDataAbertura.setBounds(fieldX, 10 + 3 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtDataAbertura);
		txtDataAbertura.setColumns(10);

		JLabel lblFaturamento = new JLabel("Faturamento (R$):");
		lblFaturamento.setBounds(labelX, 10 + 4 * verticalSpacing, 120, fieldHeight);
		contentPane.add(lblFaturamento);
		NumberFormatter faturamentoFormatter = new NumberFormatter(decimalFormat);
		faturamentoFormatter.setValueClass(Double.class);
		faturamentoFormatter.setAllowsInvalid(false);
		faturamentoFormatter.setOverwriteMode(true);
		txtFaturamento = new JFormattedTextField(faturamentoFormatter);
		txtFaturamento.setBounds(fieldX, 10 + 4 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtFaturamento);
		txtFaturamento.setColumns(10);

		JLabel lblBonus = new JLabel("Bônus (R$):");
		lblBonus.setBounds(labelX, 10 + 5 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblBonus);
		NumberFormatter bonusFormatter = new NumberFormatter(decimalFormat);
		bonusFormatter.setValueClass(BigDecimal.class);
		bonusFormatter.setAllowsInvalid(false);
		bonusFormatter.setOverwriteMode(true);
		txtBonus = new JFormattedTextField(bonusFormatter);
		txtBonus.setBounds(fieldX, 10 + 5 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtBonus);
		txtBonus.setColumns(10);

		JLabel lblLocadora = new JLabel("É Locadora de Veículos?");
		lblLocadora.setBounds(labelX, 10 + 6 * verticalSpacing, 150, fieldHeight);
		contentPane.add(lblLocadora);
		chkLocadoraDeVeiculos = new JCheckBox();
		chkLocadoraDeVeiculos.setBounds(fieldX, 10 + 6 * verticalSpacing, 20, fieldHeight);
		contentPane.add(chkLocadoraDeVeiculos);


		int enderecoStartY = 10 + 7 * verticalSpacing + 20;
		JLabel lblEnderecoTitulo = new JLabel("DADOS DE ENDEREÇO");
		lblEnderecoTitulo.setBounds(labelX, enderecoStartY, 200, 20);
		contentPane.add(lblEnderecoTitulo);

		JLabel lblLogradouro = new JLabel("Logradouro:");
		lblLogradouro.setBounds(labelX, enderecoStartY + verticalSpacing, 80, fieldHeight);
		contentPane.add(lblLogradouro);
		txtLogradouro = new JTextField();
		txtLogradouro.setBounds(fieldX, enderecoStartY + verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtLogradouro);
		txtLogradouro.setColumns(10);

		JLabel lblCep = new JLabel("CEP:");
		lblCep.setBounds(labelX, enderecoStartY + 2 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblCep);
		try {
		    MaskFormatter cepMask = new MaskFormatter("#####-###");
		    txtCep = new JFormattedTextField(cepMask);
		} catch (ParseException e) {
		    txtCep = new JFormattedTextField();
		    System.err.println("Erro ao criar máscara de CEP: " + e.getMessage());
		}
		txtCep.setBounds(fieldX, enderecoStartY + 2 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtCep);
		txtCep.setColumns(10);

		JLabel lblNumero = new JLabel("Número:");
		lblNumero.setBounds(labelX, enderecoStartY + 3 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblNumero);
		txtNumero = new JTextField();
		txtNumero.setBounds(fieldX, enderecoStartY + 3 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtNumero);
		txtNumero.setColumns(10);

		JLabel lblComplemento = new JLabel("Compl.:");
		lblComplemento.setBounds(labelX, enderecoStartY + 4 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblComplemento);
		txtComplemento = new JTextField();
		txtComplemento.setBounds(fieldX, enderecoStartY + 4 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtComplemento);
		txtComplemento.setColumns(10);

		JLabel lblPais = new JLabel("País:");
		lblPais.setBounds(labelX, enderecoStartY + 5 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblPais);
		txtPais = new JTextField();
		txtPais.setBounds(fieldX, enderecoStartY + 5 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtPais);
		txtPais.setColumns(10);

		JLabel lblEstado = new JLabel("Estado:");
		lblEstado.setBounds(labelX, enderecoStartY + 6 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblEstado);
		txtEstado = new JTextField();
		txtEstado.setBounds(fieldX, enderecoStartY + 6 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtEstado);
		txtEstado.setColumns(10);

		JLabel lblCidade = new JLabel("Cidade:");
		lblCidade.setBounds(labelX, enderecoStartY + 7 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblCidade);
		txtCidade = new JTextField();
		txtCidade.setBounds(fieldX, enderecoStartY + 7 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtCidade);
		txtCidade.setColumns(10);

		int buttonX = 500;
		int buttonWidth = 120;
		int buttonHeight = 35;
		int buttonSpacing = 40;

		btnIncluir = new JButton("Incluir");
		btnIncluir.setBounds(buttonX, 20, buttonWidth, buttonHeight);
		contentPane.add(btnIncluir);

		btnAlterar = new JButton("Alterar");
		btnAlterar.setBounds(buttonX, 20 + buttonSpacing, buttonWidth, buttonHeight);
		contentPane.add(btnAlterar);

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(buttonX, 20 + 2 * buttonSpacing, buttonWidth, buttonHeight);
		contentPane.add(btnBuscar);

		btnExcluir = new JButton("Excluir");
		btnExcluir.setBounds(buttonX, 20 + 3 * buttonSpacing, buttonWidth, buttonHeight);
		contentPane.add(btnExcluir);

		btnLimpar = new JButton("Limpar");
		btnLimpar.setBounds(buttonX, 20 + 4 * buttonSpacing, buttonWidth, buttonHeight);
		contentPane.add(btnLimpar);

		estadoInicialTela();

		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCampos();
				estadoInicialTela();
			}
		});

		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cnpjBusca = txtCnpj.getText().replace(".", "").replace("/", "").replace("-", "").trim();
				if (cnpjBusca.isEmpty()) {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, "CNPJ para busca não pode ser vazio.", "Atenção", JOptionPane.WARNING_MESSAGE);
					return;
				}

				SeguradoEmpresa seguradoEncontrado = seguradoEmpresaMediator.buscarSeguradoEmpresa(cnpjBusca);

				if (seguradoEncontrado != null) {
					preencherCampos(seguradoEncontrado);
					habilitarCamposEdicao(true);
					txtCnpj.setEnabled(false);
					btnIncluir.setEnabled(false);
					btnAlterar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnBuscar.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, "Segurado com CNPJ " + cnpjBusca + " não encontrado. Você pode incluir um novo.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					limparCampos();
					try {
                        MaskFormatter mf = (MaskFormatter) txtCnpj.getFormatter();
                        mf.setValueContainsLiteralCharacters(false);
                        txtCnpj.setText(cnpjBusca);
                        mf.setValueContainsLiteralCharacters(true);
                    } catch (Exception ex) {
                        txtCnpj.setText(cnpjBusca);
                    }
					habilitarCamposEdicao(true);
					btnIncluir.setEnabled(true);
					btnAlterar.setEnabled(false);
					btnExcluir.setEnabled(false);
					btnBuscar.setEnabled(true);
				}
			}
		});

		btnIncluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SeguradoEmpresa novoSegurado = obterDadosDaTela();
				if (novoSegurado == null) {
					return;
				}

				String msg = seguradoEmpresaMediator.incluirSeguradoEmpresa(novoSegurado);

				if (msg == null) {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, "Segurado incluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					limparCampos();
					estadoInicialTela();
				} else {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, msg, "Erro de Inclusão", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SeguradoEmpresa seguradoParaAlterar = obterDadosDaTela();
				if (seguradoParaAlterar == null) {
					return;
				}

				String msg = seguradoEmpresaMediator.alterarSeguradoEmpresa(seguradoParaAlterar);

				if (msg == null) {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, "Segurado alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					limparCampos();
					estadoInicialTela();
				} else {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, msg, "Erro de Alteração", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cnpjExcluir = txtCnpj.getText().replace(".", "").replace("/", "").replace("-", "").trim();
				if (cnpjExcluir.isEmpty()) {
					JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, "CNPJ para exclusão não pode ser vazio.", "Atenção", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(SeguradoEmpresaCRUD.this, "Tem certeza que deseja excluir o segurado com CNPJ: " + cnpjExcluir + "?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					String msg = seguradoEmpresaMediator.excluirSeguradoEmpresa(cnpjExcluir);

					if (msg == null) {
						JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, "Segurado excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
						limparCampos();
						estadoInicialTela();
					} else {
						JOptionPane.showMessageDialog(SeguradoEmpresaCRUD.this, msg, "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if (aComponent == txtCnpj) return txtNome;
                if (aComponent == txtNome) return txtDataAbertura;
                if (aComponent == txtDataAbertura) return txtFaturamento;
                if (aComponent == txtFaturamento) return txtBonus;
                if (aComponent == txtBonus) return chkLocadoraDeVeiculos;
                if (aComponent == chkLocadoraDeVeiculos) return txtLogradouro;
                if (aComponent == txtLogradouro) return txtCep;
                if (aComponent == txtCep) return txtNumero;
                if (aComponent == txtNumero) return txtComplemento;
                if (aComponent == txtComplemento) return txtPais;
                if (aComponent == txtPais) return txtEstado;
                if (aComponent == txtEstado) return txtCidade;
                if (aComponent == txtCidade) return btnIncluir;
                if (aComponent == btnIncluir) return btnAlterar;
                if (aComponent == btnAlterar) return btnBuscar;
                if (aComponent == btnBuscar) return btnExcluir;
                if (aComponent == btnExcluir) return btnLimpar;
                if (aComponent == btnLimpar) return txtCnpj;
                return null;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                if (aComponent == txtNome) return txtCnpj;
                if (aComponent == txtCnpj) return btnLimpar;
                if (aComponent == txtDataAbertura) return txtNome;
                if (aComponent == txtFaturamento) return txtDataAbertura;
                if (aComponent == txtBonus) return txtFaturamento;
                if (aComponent == chkLocadoraDeVeiculos) return txtBonus;
                if (aComponent == txtLogradouro) return chkLocadoraDeVeiculos;
                if (aComponent == txtCep) return txtLogradouro;
                if (aComponent == txtNumero) return txtCep;
                if (aComponent == txtComplemento) return txtNumero;
                if (aComponent == txtPais) return txtComplemento;
                if (aComponent == txtEstado) return txtPais;
                if (aComponent == txtCidade) return txtEstado;
                if (aComponent == btnIncluir) return txtCidade;
                if (aComponent == btnAlterar) return btnIncluir;
                if (aComponent == btnBuscar) return btnAlterar;
                if (aComponent == btnExcluir) return btnBuscar;
                if (aComponent == btnLimpar) return btnExcluir;
                return null;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return txtCnpj;
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return btnLimpar;
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return txtCnpj;
            }
        });
	}

	private void limparCampos() {
		txtNome.setText("");
		txtCnpj.setText("");
		txtDataAbertura.setText("");
		txtFaturamento.setValue(0.0);
		txtBonus.setValue(BigDecimal.ZERO);
		chkLocadoraDeVeiculos.setSelected(false);

		txtLogradouro.setText("");
		txtCep.setText("");
		txtNumero.setText("");
		txtComplemento.setText("");
		txtPais.setText("");
		txtEstado.setText("");
		txtCidade.setText("");
	}

	private void habilitarCamposEdicao(boolean habilitar) {
		txtNome.setEnabled(habilitar);
		txtDataAbertura.setEnabled(habilitar);
		txtFaturamento.setEnabled(habilitar);
		txtBonus.setEnabled(habilitar);
		chkLocadoraDeVeiculos.setEnabled(habilitar);

		txtLogradouro.setEnabled(habilitar);
		txtCep.setEnabled(habilitar);
		txtNumero.setEnabled(habilitar);
		txtComplemento.setEnabled(habilitar);
		txtPais.setEnabled(habilitar);
		txtEstado.setEnabled(habilitar);
		txtCidade.setEnabled(habilitar);
	}

	private void estadoInicialTela() {
		limparCampos();
		habilitarCamposEdicao(false);
		txtCnpj.setEnabled(true);
		btnIncluir.setEnabled(false);
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnBuscar.setEnabled(true);
		btnLimpar.setEnabled(true);
		txtCnpj.requestFocusInWindow();
	}

	private void preencherCampos(SeguradoEmpresa segurado) {
	    txtNome.setText(segurado.getNome());
	    txtCnpj.setText(segurado.getCnpj());
	    if (segurado.getDataAbertura() != null) {
	        txtDataAbertura.setText(segurado.getDataAbertura().format(DATE_FORMATTER));
	    } else {
	        txtDataAbertura.setText("");
	    }
	    txtFaturamento.setValue(segurado.getFaturamento());
	    txtBonus.setValue(segurado.getBonus());
	    chkLocadoraDeVeiculos.setSelected(segurado.isEhLocadoraDeVeiculos());

	    Endereco endereco = segurado.getEndereco();
	    if (endereco != null) {
	        txtLogradouro.setText(endereco.getLogradouro());
	        txtCep.setText(endereco.getCep());
	        txtNumero.setText(endereco.getNumero());
	        txtComplemento.setText(endereco.getComplemento());
	        txtPais.setText(endereco.getPais());
	        txtEstado.setText(endereco.getEstado());
	        txtCidade.setText(endereco.getCidade());
	    } else {
	        txtLogradouro.setText("");
	        txtCep.setText("");
	        txtNumero.setText("");
	        txtComplemento.setText("");
	        txtPais.setText("");
	        txtEstado.setText("");
	        txtCidade.setText("");
	    }
	}

	private SeguradoEmpresa obterDadosDaTela() {
	    String nomeStr = txtNome.getText().trim();
	    String cnpjStr = txtCnpj.getText().replace(".", "").replace("/", "").replace("-", "").trim();
	    String dataAberturaStr = txtDataAbertura.getText().replace("/", "").trim();
	    
	    Object faturamentoValue = txtFaturamento.getValue();
	    Object bonusValue = txtBonus.getValue();
	    boolean ehLocadora = chkLocadoraDeVeiculos.isSelected();

	    if (nomeStr.isEmpty() || cnpjStr.isEmpty() || dataAberturaStr.isEmpty() || faturamentoValue == null || bonusValue == null) {
	        JOptionPane.showMessageDialog(this, "Todos os campos de Segurado Empresa (Nome, CNPJ, Data Abertura, Faturamento, Bônus) são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }
	    
	    String logradouroStr = txtLogradouro.getText().trim();
	    String cepStr = txtCep.getText().replace("-", "").trim();
	    String numeroStr = txtNumero.getText().trim();
	    String paisStr = txtPais.getText().trim();
	    String estadoStr = txtEstado.getText().trim();
	    String cidadeStr = txtCidade.getText().trim();
	    if (logradouroStr.isEmpty() || cepStr.isEmpty() || numeroStr.isEmpty() || paisStr.isEmpty() || estadoStr.isEmpty() || cidadeStr.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Todos os campos de Endereço (Logradouro, CEP, Número, País, Estado, Cidade) são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }
	    String complementoStr = txtComplemento.getText().trim();

	    LocalDate dataAbertura = null;
	    double faturamento = 0.0;
	    BigDecimal bonus = BigDecimal.ZERO;

	    try {
	        dataAbertura = LocalDate.parse(dataAberturaStr, DATE_FORMATTER);
	    } catch (DateTimeParseException e) {
	        JOptionPane.showMessageDialog(this, "Formato de Data de Abertura inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }

	    try {
	        if (faturamentoValue instanceof Number) {
	            faturamento = ((Number) faturamentoValue).doubleValue();
	        } else {
	            faturamento = Double.parseDouble(txtFaturamento.getText().trim().replace(",", "."));
	        }
	        if (faturamento < 0) {
	            JOptionPane.showMessageDialog(this, "Faturamento não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	            return null;
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Formato de Faturamento inválido. Digite um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }

	    try {
	        if (bonusValue instanceof BigDecimal) {
	            bonus = (BigDecimal) bonusValue;
	        } else if (bonusValue instanceof Number) {
	            bonus = BigDecimal.valueOf(((Number) bonusValue).doubleValue());
	        } else {
	            bonus = new BigDecimal(txtBonus.getText().trim().replace(",", "."));
	        }

	        if (bonus.compareTo(BigDecimal.ZERO) < 0) {
	             JOptionPane.showMessageDialog(this, "Bônus não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	             return null;
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Formato de Bônus inválido. Digite um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }

	    Endereco endereco = new Endereco(logradouroStr, cepStr, numeroStr, complementoStr, paisStr, estadoStr, cidadeStr);

	    return new SeguradoEmpresa(nomeStr, endereco, dataAbertura, bonus, cnpjStr, faturamento, ehLocadora);
	}
}