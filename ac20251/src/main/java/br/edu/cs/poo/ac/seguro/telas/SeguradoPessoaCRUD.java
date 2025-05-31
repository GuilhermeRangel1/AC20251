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

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator; 


public class SeguradoPessoaCRUD extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField txtNome;
	private JFormattedTextField txtCpf; 
	private JFormattedTextField txtDataNascimento; 
	private JFormattedTextField txtRenda; 
	private JFormattedTextField txtBonus; 

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

	private SeguradoPessoaMediator seguradoPessoaMediator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private NumberFormat decimalFormat = NumberFormat.getNumberInstance();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SeguradoPessoaCRUD frame = new SeguradoPessoaCRUD();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SeguradoPessoaCRUD() {
		seguradoPessoaMediator = SeguradoPessoaMediator.getInstancia();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null); 

		int labelX = 20;
		int fieldX = 170; 
		int fieldWidth = 200;
		int fieldHeight = 25; 
		int verticalSpacing = 35; 

		JLabel lblSeguradoTitulo = new JLabel("DADOS DO SEGURADO PESSOA");
		lblSeguradoTitulo.setBounds(labelX, 10, 250, 20);
		contentPane.add(lblSeguradoTitulo);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(labelX, 10 + verticalSpacing, 80, fieldHeight);
		contentPane.add(lblNome);
		txtNome = new JTextField();
		txtNome.setBounds(fieldX, 10 + verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtNome);
		txtNome.setColumns(10);

		JLabel lblCpf = new JLabel("CPF:");
		lblCpf.setBounds(labelX, 10 + 2 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblCpf);
		try {
		    MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
		    txtCpf = new JFormattedTextField(cpfMask);
		} catch (ParseException e) {
		    txtCpf = new JFormattedTextField(); 
		    System.err.println("Erro ao criar máscara de CPF: " + e.getMessage());
		}
		txtCpf.setBounds(fieldX, 10 + 2 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtCpf);
		txtCpf.setColumns(10);

		JLabel lblDataNascimento = new JLabel("Data Nasc. (dd/MM/yyyy):"); 
		lblDataNascimento.setBounds(labelX, 10 + 3 * verticalSpacing, 150, fieldHeight); 
		contentPane.add(lblDataNascimento);
		try {
		    MaskFormatter dateMask = new MaskFormatter("##/##/####");
		    txtDataNascimento = new JFormattedTextField(dateMask);
		} catch (ParseException e) {
		    txtDataNascimento = new JFormattedTextField(); // Fallback
		    System.err.println("Erro ao criar máscara de Data Nasc.: " + e.getMessage());
		}
		txtDataNascimento.setBounds(fieldX, 10 + 3 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtDataNascimento);
		txtDataNascimento.setColumns(10);

		JLabel lblRenda = new JLabel("Renda (R$):");
		lblRenda.setBounds(labelX, 10 + 4 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblRenda);
		NumberFormatter rendaFormatter = new NumberFormatter(decimalFormat);
		rendaFormatter.setValueClass(Double.class); 
		rendaFormatter.setAllowsInvalid(true); 
		rendaFormatter.setOverwriteMode(true);
		txtRenda = new JFormattedTextField(rendaFormatter);
		txtRenda.setBounds(fieldX, 10 + 4 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtRenda);
		txtRenda.setColumns(10);

		JLabel lblBonus = new JLabel("Bônus (R$):");
		lblBonus.setBounds(labelX, 10 + 5 * verticalSpacing, 80, fieldHeight);
		contentPane.add(lblBonus);
		NumberFormatter bonusFormatter = new NumberFormatter(decimalFormat);
		bonusFormatter.setValueClass(BigDecimal.class);
		bonusFormatter.setAllowsInvalid(true);
		bonusFormatter.setOverwriteMode(true);
		txtBonus = new JFormattedTextField(bonusFormatter);
		txtBonus.setBounds(fieldX, 10 + 5 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtBonus);
		txtBonus.setColumns(10);


		int enderecoStartY = 10 + 6 * verticalSpacing + 20; 
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
				String cpfBusca = txtCpf.getText().replace(".", "").replace("-", "").trim(); 
				if (cpfBusca.isEmpty()) {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, "CPF para busca não pode ser vazio.", "Atenção", JOptionPane.WARNING_MESSAGE);
					return;
				}

				SeguradoPessoa seguradoEncontrado = seguradoPessoaMediator.buscarSeguradoPessoa(cpfBusca);

				if (seguradoEncontrado != null) {
					preencherCampos(seguradoEncontrado);
					habilitarCamposEdicao(true);
					txtCpf.setEnabled(false); 
					btnIncluir.setEnabled(false);
					btnAlterar.setEnabled(true);
					btnExcluir.setEnabled(true);
					btnBuscar.setEnabled(true); 
				} else {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, "Segurado com CPF " + cpfBusca + " não encontrado. Você pode incluir um novo.", "Atenção", JOptionPane.INFORMATION_MESSAGE);
					limparCampos(); 
					try {
                        MaskFormatter mf = (MaskFormatter) txtCpf.getFormatter();
                        mf.setValueContainsLiteralCharacters(false); 
                        txtCpf.setText(cpfBusca); 
                        mf.setValueContainsLiteralCharacters(true);
                    } catch (Exception ex) {
                        txtCpf.setText(cpfBusca); 
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
				SeguradoPessoa novoSegurado = obterDadosDaTela();
				if (novoSegurado == null) {
					return;
				}

				String msg = seguradoPessoaMediator.incluirSeguradoPessoa(novoSegurado); 

				if (msg == null) {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, "Segurado incluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					limparCampos();
					estadoInicialTela();
				} else {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, msg, "Erro de Inclusão", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnAlterar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SeguradoPessoa seguradoParaAlterar = obterDadosDaTela();
				if (seguradoParaAlterar == null) {
					return;
				}

				String msg = seguradoPessoaMediator.alterarSeguradoPessoa(seguradoParaAlterar); 

				if (msg == null) {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, "Segurado alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
					limparCampos();
					estadoInicialTela();
				} else {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, msg, "Erro de Alteração", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cpfExcluir = txtCpf.getText().replace(".", "").replace("-", "").trim();
				if (cpfExcluir.isEmpty()) {
					JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, "CPF para exclusão não pode ser vazio.", "Atenção", JOptionPane.WARNING_MESSAGE);
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(SeguradoPessoaCRUD.this, "Tem certeza que deseja excluir o segurado com CPF: " + cpfExcluir + "?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					String msg = seguradoPessoaMediator.excluirSeguradoPessoa(cpfExcluir);

					if (msg == null) {
						JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, "Segurado excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
						limparCampos();
						estadoInicialTela();
					} else {
						JOptionPane.showMessageDialog(SeguradoPessoaCRUD.this, msg, "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if (aComponent == txtCpf) return txtNome; 
                if (aComponent == txtNome) return txtDataNascimento;
                if (aComponent == txtDataNascimento) return txtRenda;
                if (aComponent == txtRenda) return txtBonus;
                if (aComponent == txtBonus) return txtLogradouro;
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
                if (aComponent == btnLimpar) return txtCpf; 
                return null;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                if (aComponent == txtNome) return txtCpf;
                if (aComponent == txtCpf) return btnLimpar; 
                if (aComponent == txtDataNascimento) return txtNome;
                if (aComponent == txtRenda) return txtDataNascimento;
                if (aComponent == txtBonus) return txtRenda;
                if (aComponent == txtLogradouro) return txtBonus;
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
                return txtCpf; 
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return btnLimpar;
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return txtCpf; 
            }
        });

	}


	private void limparCampos() {
		txtNome.setText("");
		txtCpf.setText(""); 
		txtDataNascimento.setText("");
		txtRenda.setValue(0.0); 
		txtBonus.setValue(BigDecimal.ZERO); 
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
		txtDataNascimento.setEnabled(habilitar);
		txtRenda.setEnabled(habilitar);
		txtBonus.setEnabled(habilitar);
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
		txtCpf.setEnabled(true);      
		btnIncluir.setEnabled(false); 
		btnAlterar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnBuscar.setEnabled(true);   
		btnLimpar.setEnabled(true); 
		txtCpf.requestFocusInWindow(); 
	}


	private void preencherCampos(SeguradoPessoa segurado) {
	    txtNome.setText(segurado.getNome());
	    txtCpf.setText(segurado.getCpf());
	    if (segurado.getDataNascimento() != null) {
	        txtDataNascimento.setText(segurado.getDataNascimento().format(DATE_FORMATTER));
	    } else {
	        txtDataNascimento.setText("");
	    }
	    txtRenda.setValue(segurado.getRenda());
	    txtBonus.setValue(segurado.getBonus());

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

	private SeguradoPessoa obterDadosDaTela() {
	    String nomeStr = txtNome.getText().trim();
	    String cpfStr = txtCpf.getText().replace(".", "").replace("-", "").trim();
	    String dataNascimentoStr = txtDataNascimento.getText().replace("/", "").trim();
	    
	    Object rendaValue = txtRenda.getValue();
	    Object bonusValue = txtBonus.getValue();

	    if (nomeStr.isEmpty() || cpfStr.isEmpty() || dataNascimentoStr.isEmpty() || rendaValue == null || bonusValue == null) {
	        JOptionPane.showMessageDialog(this, "Todos os campos de Segurado Pessoa (Nome, CPF, Data Nasc., Renda, Bônus) são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
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

	    LocalDate dataNascimento = null;
	    double renda = 0.0;
	    BigDecimal bonus = BigDecimal.ZERO;

	    try {
	        dataNascimento = LocalDate.parse(dataNascimentoStr, DATE_FORMATTER);
	    } catch (DateTimeParseException e) {
	        JOptionPane.showMessageDialog(this, "Formato de Data de Nascimento inválido. Use dd/MM/yyyy.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }

	    try {
	        if (rendaValue instanceof Number) {
	            renda = ((Number) rendaValue).doubleValue();
	        } else {
	            renda = Double.parseDouble(txtRenda.getText().trim().replace(",", ".")); 
	        }
	        if (renda < 0) {
	            JOptionPane.showMessageDialog(this, "Renda não pode ser negativa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	            return null;
	        }
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Formato de Renda inválido. Digite um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
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
	    return new SeguradoPessoa(nomeStr, endereco, dataNascimento, bonus, cpfStr, renda);
	}
}