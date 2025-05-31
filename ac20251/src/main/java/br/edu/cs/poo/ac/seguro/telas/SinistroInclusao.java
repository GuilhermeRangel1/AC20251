package br.edu.cs.poo.ac.seguro.telas;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Color;


public class SinistroInclusao extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField txtPlaca;
	private JFormattedTextField txtDataHoraSinistro;
	private JTextField txtUsuarioRegistro;
	private JFormattedTextField txtValorSinistro;
	private JComboBox<String> cmbTipoSinistro;

	private JButton btnIncluir;
	private JButton btnLimpar;

	private SinistroMediator sinistroMediator;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private NumberFormat decimalFormat = NumberFormat.getNumberInstance();


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SinistroInclusao frame = new SinistroInclusao();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SinistroInclusao() {
		sinistroMediator = SinistroMediator.getInstancia();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		int labelX = 20;
		int fieldX = 170;
		int fieldWidth = 250;
		int fieldHeight = 25;
		int verticalSpacing = 35;

		JLabel lblTitulo = new JLabel("INCLUSÃO DE SINISTRO");
		lblTitulo.setBounds(labelX, 10, 250, 20);
		contentPane.add(lblTitulo);

		JLabel lblPlaca = new JLabel("Placa:");
		lblPlaca.setBounds(labelX, 10 + verticalSpacing, 120, fieldHeight);
		contentPane.add(lblPlaca);
		txtPlaca = new JTextField();
		txtPlaca.setBounds(fieldX, 10 + verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtPlaca);
		txtPlaca.setColumns(10);

		JLabel lblDataHoraSinistro = new JLabel("Data/Hora Sinistro:");
		lblDataHoraSinistro.setBounds(labelX, 10 + 2 * verticalSpacing, 150, fieldHeight);
		contentPane.add(lblDataHoraSinistro);
		try {
		    MaskFormatter dateTimeMask = new MaskFormatter("##/##/#### ##:##:##");
            dateTimeMask.setPlaceholderCharacter('_'); 
            dateTimeMask.setAllowsInvalid(false); 
            dateTimeMask.setOverwriteMode(true);
            txtDataHoraSinistro = new JFormattedTextField(dateTimeMask);
		} catch (ParseException e) {
		    txtDataHoraSinistro = new JFormattedTextField();
		    System.err.println("Erro ao criar máscara de Data/Hora: " + e.getMessage());
		}
		txtDataHoraSinistro.setBounds(fieldX, 10 + 2 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtDataHoraSinistro);
		txtDataHoraSinistro.setColumns(10);

		JLabel lblUsuarioRegistro = new JLabel("Usuário Registro:");
		lblUsuarioRegistro.setBounds(labelX, 10 + 3 * verticalSpacing, 120, fieldHeight);
		contentPane.add(lblUsuarioRegistro);
		txtUsuarioRegistro = new JTextField();
		txtUsuarioRegistro.setBounds(fieldX, 10 + 3 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtUsuarioRegistro);
		txtUsuarioRegistro.setColumns(10);

		JLabel lblValorSinistro = new JLabel("Valor Sinistro (R$):");
		lblValorSinistro.setBounds(labelX, 10 + 4 * verticalSpacing, 120, fieldHeight);
		contentPane.add(lblValorSinistro);
		NumberFormatter valorSinistroFormatter = new NumberFormatter(decimalFormat);
		valorSinistroFormatter.setValueClass(Double.class);
		valorSinistroFormatter.setAllowsInvalid(true); 
		valorSinistroFormatter.setOverwriteMode(true);
		txtValorSinistro = new JFormattedTextField(valorSinistroFormatter);
		txtValorSinistro.setBounds(fieldX, 10 + 4 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(txtValorSinistro);
		txtValorSinistro.setColumns(10);
        txtValorSinistro.setValue(0.0); 

		JLabel lblTipoSinistro = new JLabel("Tipo de Sinistro:");
		lblTipoSinistro.setBounds(labelX, 10 + 5 * verticalSpacing, 120, fieldHeight);
		contentPane.add(lblTipoSinistro);
		
		List<String> nomesTiposSinistro = new ArrayList<>();
		for (TipoSinistro tipo : TipoSinistro.values()) {
		    nomesTiposSinistro.add(tipo.getNome());
		}
		Collections.sort(nomesTiposSinistro); 
		
		cmbTipoSinistro = new JComboBox<>(new DefaultComboBoxModel<>(nomesTiposSinistro.toArray(new String[0])));
		cmbTipoSinistro.setBounds(fieldX, 10 + 5 * verticalSpacing, fieldWidth, fieldHeight);
		contentPane.add(cmbTipoSinistro);

        JLabel lblObsDataHora = new JLabel("Obs: Data/Hora no formato dd/MM/yyyy HH:mm:ss");
        lblObsDataHora.setBounds(labelX, 10 + 6 * verticalSpacing, 400, fieldHeight); 
        lblObsDataHora.setFont(new Font("SansSerif", Font.PLAIN, 11)); 
        lblObsDataHora.setForeground(Color.DARK_GRAY); 
        contentPane.add(lblObsDataHora);


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
				incluirSinistro();
			}
		});

		setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if (aComponent == txtPlaca) return txtDataHoraSinistro;
                if (aComponent == txtDataHoraSinistro) return txtUsuarioRegistro;
                if (aComponent == txtUsuarioRegistro) return txtValorSinistro;
                if (aComponent == txtValorSinistro) return cmbTipoSinistro;
                if (aComponent == cmbTipoSinistro) return btnIncluir;
                if (aComponent == btnIncluir) return btnLimpar;
                if (aComponent == btnLimpar) return txtPlaca;
                return null;
            }

            @Override
            public Component getComponentBefore(Container aContainer, Component aComponent) {
                if (aComponent == txtDataHoraSinistro) return txtPlaca;
                if (aComponent == txtUsuarioRegistro) return txtDataHoraSinistro;
                if (aComponent == txtValorSinistro) return txtUsuarioRegistro;
                if (aComponent == cmbTipoSinistro) return txtValorSinistro;
                if (aComponent == btnIncluir) return cmbTipoSinistro;
                if (aComponent == btnLimpar) return btnIncluir;
                if (aComponent == txtPlaca) return btnLimpar;
                return null;
            }

            @Override
            public Component getFirstComponent(Container aContainer) {
                return txtPlaca;
            }

            @Override
            public Component getLastComponent(Container aContainer) {
                return btnLimpar;
            }

            @Override
            public Component getDefaultComponent(Container aContainer) {
                return txtPlaca;
            }
        });
	}

	private void limparCampos() {
		txtPlaca.setText("");
		txtDataHoraSinistro.setText("");
		txtUsuarioRegistro.setText("");
		txtValorSinistro.setValue(0.0);
		if (cmbTipoSinistro.getItemCount() > 0) {
		    cmbTipoSinistro.setSelectedIndex(0); 
		}
		txtPlaca.requestFocusInWindow();
	}

	private void incluirSinistro() {
        String placaStr = txtPlaca.getText().trim();
        String dataHoraSinistroStr = txtDataHoraSinistro.getText().trim(); 

        String usuarioRegistroStr = txtUsuarioRegistro.getText().trim();
        Object valorSinistroValue = txtValorSinistro.getValue();
        
        String nomeTipoSinistroSelecionado = (String) cmbTipoSinistro.getSelectedItem();
        int codigoTipoSinistro = -1; 

        for (TipoSinistro tipo : TipoSinistro.values()) {
            if (tipo.getNome().equals(nomeTipoSinistroSelecionado)) {
                codigoTipoSinistro = tipo.getCodigo();
                break;
            }
        }

        LocalDateTime dataHoraSinistro = null;
        Double valorSinistro = null; 

        if (placaStr.isEmpty() || dataHoraSinistroStr.isEmpty() || usuarioRegistroStr.isEmpty() || valorSinistroValue == null) {
            JOptionPane.showMessageDialog(this, "Todos os campos (Placa, Data/Hora Sinistro, Usuário Registro, Valor Sinistro) são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (codigoTipoSinistro == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo de sinistro válido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            dataHoraSinistro = LocalDateTime.parse(dataHoraSinistroStr, DATETIME_FORMATTER);
            if (dataHoraSinistro.isAfter(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this, "Data/Hora do Sinistro não pode ser futura.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de Data/Hora do Sinistro inválido. Use dd/MM/yyyy HH:mm:ss.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (valorSinistroValue instanceof Number) {
                valorSinistro = ((Number) valorSinistroValue).doubleValue();
            } else {
                valorSinistro = decimalFormat.parse(txtValorSinistro.getText().trim()).doubleValue();
            }
            if (valorSinistro < 0) {
                JOptionPane.showMessageDialog(this, "Valor do sinistro não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (ParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de Valor do Sinistro inválido. Digite um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DadosSinistro dados = new DadosSinistro(placaStr, dataHoraSinistro, usuarioRegistroStr, valorSinistro, codigoTipoSinistro);

        try {
            String numeroSinistroGerado = sinistroMediator.incluirSinistro(dados, LocalDateTime.now());
            JOptionPane.showMessageDialog(this, "Sinistro incluído com sucesso! Anote o número do sinistro: " + numeroSinistroGerado, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
        } catch (ExcecaoValidacaoDados e) {
            String mensagens = e.getMensagens().stream().collect(Collectors.joining("\n"));
            JOptionPane.showMessageDialog(this, mensagens, "Erro de Inclusão", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
	}
}