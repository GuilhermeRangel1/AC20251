package br.edu.cs.poo.ac.seguro.telas;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;

public class ApoliceInclusao extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField txtCpfCnpj;
	private JTextField txtPlaca;
	private JFormattedTextField txtAno;
	private JFormattedTextField txtValorMaximoSegurado;
	private JComboBox<String> cmbCategoriaVeiculo;
	private JButton btnIncluir;
	private JButton btnLimpar;
	private ApoliceMediator apoliceMediator;
    private NumberFormat decimalFormat = NumberFormat.getNumberInstance();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApoliceInclusao frame = new ApoliceInclusao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ApoliceInclusao() {
		apoliceMediator = ApoliceMediator.getInstancia();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 480); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		int labelX = 20;
		int fieldX = 190; 
		int fieldWidth = 250;
		int fieldHeight = 25;
		int verticalSpacing = 35;

		JLabel lblTitulo = new JLabel("INCLUSÃO DE APÓLICE");
		lblTitulo.setBounds(labelX, 10, 250, 20);
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(lblTitulo);

		JLabel lblCpfCnpj = new JLabel("CPF ou CNPJ:");
		lblCpfCnpj.setBounds(labelX, 10 + verticalSpacing, 150, fieldHeight);
		contentPane.add(lblCpfCnpj);
		txtCpfCnpj = new JTextField();
		txtCpfCnpj.setBounds(fieldX, 10 + verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtCpfCnpj);
		txtCpfCnpj.setColumns(10);

		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setBounds(labelX, 10 + 2 * verticalSpacing, 150, fieldHeight);
		contentPane.add(lblPlaca);
		txtPlaca = new JTextField();
		txtPlaca.setBounds(fieldX, 10 + 2 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtPlaca);
		txtPlaca.setColumns(10);

		JLabel lblAno = new JLabel("Ano do Veículo (####):");
		lblAno.setBounds(labelX, 10 + 3 * verticalSpacing, 160, fieldHeight);
		contentPane.add(lblAno);
		try {
		    MaskFormatter anoMask = new MaskFormatter("####");
		    txtAno = new JFormattedTextField(anoMask);
		} catch (ParseException e) {
		    txtAno = new JFormattedTextField();
		    System.err.println("Erro ao criar máscara de Ano: " + e.getMessage());
		}
		txtAno.setBounds(fieldX, 10 + 3 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtAno);
		txtAno.setColumns(4);


		JLabel lblValorMaximoSegurado = new JLabel("Valor Máximo Segurado (R$):");
		lblValorMaximoSegurado.setBounds(labelX, 10 + 4 * verticalSpacing, 180, fieldHeight);
		contentPane.add(lblValorMaximoSegurado);
		NumberFormatter valorMaxSeguradoFormatter = new NumberFormatter(decimalFormat);
		valorMaxSeguradoFormatter.setValueClass(BigDecimal.class);
		valorMaxSeguradoFormatter.setAllowsInvalid(true);
		valorMaxSeguradoFormatter.setOverwriteMode(true);
		txtValorMaximoSegurado = new JFormattedTextField(valorMaxSeguradoFormatter);
		txtValorMaximoSegurado.setBounds(fieldX, 10 + 4 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtValorMaximoSegurado);
		txtValorMaximoSegurado.setColumns(10);
        txtValorMaximoSegurado.setValue(BigDecimal.ZERO);


		JLabel lblCategoriaVeiculo = new JLabel("Categoria do Veículo:");
		lblCategoriaVeiculo.setBounds(labelX, 10 + 5 * verticalSpacing, 150, fieldHeight);
		contentPane.add(lblCategoriaVeiculo);
		
		List<String> nomesCategorias = new ArrayList<>();
		for (CategoriaVeiculo categoria : CategoriaVeiculo.values()) {
		    nomesCategorias.add(categoria.getNome());
		}
		Collections.sort(nomesCategorias); 
		
		cmbCategoriaVeiculo = new JComboBox<>(new DefaultComboBoxModel<>(nomesCategorias.toArray(new String[0])));
		cmbCategoriaVeiculo.setBounds(fieldX, 10 + 5 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(cmbCategoriaVeiculo);


		int buttonX = fieldX + fieldWidth + 20;
		int buttonWidth = 120;
		int buttonHeight = 35;
		int buttonSpacing = 40;

		btnIncluir = new JButton("Incluir");
		btnIncluir.setBounds(buttonX, 20, buttonWidth, buttonHeight);
		contentPane.add(btnIncluir);

		btnLimpar = new JButton("Limpar");
		btnLimpar.setBounds(buttonX, 20 + buttonSpacing, buttonWidth, buttonHeight);
		contentPane.add(btnLimpar);

		limparCampos(); 

		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCampos();
			}
		});

		btnIncluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incluirApolice();
			}
		});

		setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if (aComponent == txtCpfCnpj) return txtPlaca;
                if (aComponent == txtPlaca) return txtAno;
                if (aComponent == txtAno) return txtValorMaximoSegurado;
                if (aComponent == txtValorMaximoSegurado) return cmbCategoriaVeiculo;
                if (aComponent == cmbCategoriaVeiculo) return btnIncluir;
                if (aComponent == btnIncluir) return btnLimpar;
                if (aComponent == btnLimpar) return txtCpfCnpj;
                return null;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                if (aComponent == txtPlaca) return txtCpfCnpj;
                if (aComponent == txtAno) return txtPlaca;
                if (aComponent == txtValorMaximoSegurado) return txtAno;
                if (aComponent == cmbCategoriaVeiculo) return txtValorMaximoSegurado;
                if (aComponent == btnIncluir) return cmbCategoriaVeiculo;
                if (aComponent == btnLimpar) return btnIncluir;
                if (aComponent == txtCpfCnpj) return btnLimpar;
                return null;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return txtCpfCnpj;
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return btnLimpar;
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return txtCpfCnpj;
            }
        });
	}

	private void limparCampos() {
		txtCpfCnpj.setText("");
		txtPlaca.setText("");
		txtAno.setText(""); 
		txtValorMaximoSegurado.setValue(BigDecimal.ZERO); 
		if (cmbCategoriaVeiculo.getItemCount() > 0) {
		    cmbCategoriaVeiculo.setSelectedIndex(0); 
		}
		txtCpfCnpj.requestFocusInWindow();
	}

	private void incluirApolice() {
	    String cpfOuCnpjStr = txtCpfCnpj.getText().trim();
	    String placaStr = txtPlaca.getText().trim();
	    String anoStr = txtAno.getText().trim(); 
	    Object valorMaximoSeguradoValue = txtValorMaximoSegurado.getValue();
	    
	    String nomeCategoriaSelecionada = (String) cmbCategoriaVeiculo.getSelectedItem();
	    int codigoCategoria = -1; 

	    for (CategoriaVeiculo categoria : CategoriaVeiculo.values()) {
	        if (categoria.getNome().equals(nomeCategoriaSelecionada)) {
	            codigoCategoria = categoria.getCodigo();
	            break;
	        }
	    }

	    int ano = 0;
	    BigDecimal valorMaximoSegurado = BigDecimal.ZERO;

	    if (cpfOuCnpjStr.isEmpty() || placaStr.isEmpty() || anoStr.isEmpty() || valorMaximoSeguradoValue == null) {
	        JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    if (codigoCategoria == -1) {
	        JOptionPane.showMessageDialog(this, "Selecione uma categoria de veículo válida.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try {
	        ano = Integer.parseInt(anoStr);
	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Formato do Ano inválido. Use 4 dígitos numéricos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try {
	        if (valorMaximoSeguradoValue instanceof BigDecimal) {
	            valorMaximoSegurado = (BigDecimal) valorMaximoSeguradoValue;
	        } else if (valorMaximoSeguradoValue instanceof Number) {
	            valorMaximoSegurado = BigDecimal.valueOf(((Number) valorMaximoSeguradoValue).doubleValue());
	        } else {
	            String valorText = txtValorMaximoSegurado.getText().trim();
	            if (valorText.isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Valor máximo segurado não pode ser vazio.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            valorMaximoSegurado = new BigDecimal(valorText.replace(",", "."));
	        }
	        
	        if (valorMaximoSegurado.compareTo(BigDecimal.ZERO) <= 0) {
	            JOptionPane.showMessageDialog(this, "Valor máximo segurado deve ser maior que zero.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	    } catch (NumberFormatException e) {
	        JOptionPane.showMessageDialog(this, "Formato de Valor Máximo Segurado inválido. Digite um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
	        return;
	    }


	    DadosVeiculo dados = new DadosVeiculo(cpfOuCnpjStr, placaStr, ano, valorMaximoSegurado, codigoCategoria);

	    RetornoInclusaoApolice retorno = apoliceMediator.incluirApolice(dados);

	    if (retorno.isSucesso()) {
	        JOptionPane.showMessageDialog(this, "Apólice incluída com sucesso! Anote o número da apólice: " + retorno.getNumeroApolice(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	        limparCampos();
	    } else {
	        JOptionPane.showMessageDialog(this, retorno.getMensagemErro(), "Erro de Inclusão", JOptionPane.ERROR_MESSAGE);
	    }
	}
}