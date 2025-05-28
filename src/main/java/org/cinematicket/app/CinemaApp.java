package org.cinematicket.app;

import com.formdev.flatlaf.FlatLightLaf;
import org.cinematicket.dao.VendaDAO;
import org.cinematicket.model.Venda;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class CinemaApp extends JFrame {

    private VendaDAO vendaDAO = new VendaDAO();
    private DefaultTableModel tabelaModel;
    private JTable tabela;

    private JTextField txtNomePessoa, txtNomeFilme, txtLocalFilme, txtNumeroIngresso, txtValorIngresso;
    private JDatePickerImpl datePicker;
    private JSpinner spinnerHora;

    private JButton btnConcluir, btnEditar, btnCancelar, btnLimpar;

    private static final DateTimeFormatter formatterBR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public CinemaApp() {
        super("Venda de Ingressos - Cinema");
        initUI();
        carregarVendasNaTabela();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(245, 245, 245));

        tabelaModel = new DefaultTableModel(new String[]{"ID", "Pessoa", "Filme", "DataHora", "Local", "Ingresso", "Valor", "Cancelado"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(tabelaModel);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        tabela.getTableHeader().setBackground(new Color(220, 220, 220));
        tabela.getTableHeader().setForeground(new Color(80, 80, 80));

        tabela.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                setText(value != null && value.equals("Sim") ? "Sim" : "Não");
                setForeground(value != null && value.equals("Sim") ? new Color(180, 50, 50) : new Color(50, 130, 50));
                setHorizontalAlignment(CENTER);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        formPanel.setPreferredSize(new Dimension(410, getHeight()));
        formPanel.setBackground(Color.white);

        formPanel.add(createLabelField("Nome da Pessoa:", txtNomePessoa = new JTextField(20)));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createLabelField("Nome do Filme:", txtNomeFilme = new JTextField(20)));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createLabelField("Data do Filme:", datePicker = createDatePicker()));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createLabelField("Hora do Filme:", spinnerHora = createTimeSpinner()));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createLabelField("Local do Filme:", txtLocalFilme = new JTextField(20)));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createLabelField("Número do Ingresso:", txtNumeroIngresso = new JTextField(20)));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createLabelField("Valor do Ingresso:", txtValorIngresso = new JTextField(20)));
        formPanel.add(Box.createVerticalStrut(25));

        ImageIcon iconConcluir = loadIcon("concluir.png");
        ImageIcon iconEditar = loadIcon("editar.png");
        ImageIcon iconCancelar = loadIcon("cancelar.png");
        ImageIcon iconLimpar = loadIcon("limpar.png");

        btnConcluir = createButton("Concluir Venda", iconConcluir);
        btnEditar = createButton("Editar Venda", iconEditar);
        btnCancelar = createButton("Cancelar Venda", iconCancelar);
        btnLimpar = createButton("Limpar", iconLimpar);

        btnConcluir.addActionListener(e -> concluirVenda());
        btnEditar.addActionListener(e -> editarVenda());
        btnCancelar.addActionListener(e -> cancelarVenda());
        btnLimpar.addActionListener(e -> limparFormulario());

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 18, 18));
        buttonPanel.setBackground(Color.white);

        btnConcluir.setPreferredSize(new Dimension(180, 40));
        btnEditar.setPreferredSize(new Dimension(180, 40));
        btnCancelar.setPreferredSize(new Dimension(180, 40));
        btnLimpar.setPreferredSize(new Dimension(180, 40));

        buttonPanel.setPreferredSize(new Dimension(400, 100));

        buttonPanel.add(btnConcluir);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnLimpar);

        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.EAST);
    }

    private ImageIcon loadIcon(String iconName) {
        URL url = getClass().getResource("/icons/" + iconName);
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            System.err.println("Ícone não encontrado: " + iconName);
            return null;
        }
    }

    private JPanel createLabelField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 3));
        panel.setBackground(Color.white);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(90, 90, 90));
        panel.add(label, BorderLayout.NORTH);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, Icon icon) {
        JButton btn = new JButton(text, icon);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(200, 200, 200));
        btn.setForeground(new Color(60, 60, 60));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 160, 160)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);

        btn.setIconTextGap(8);
        btn.setOpaque(true);

        btn.setMinimumSize(new Dimension(180, 40));
        btn.setPreferredSize(new Dimension(180, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(170, 170, 170));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(200, 200, 200));
            }
        });

        return btn;
    }

    private JDatePickerImpl createDatePicker() {
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());
    }

    private JSpinner createTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
        spinner.setEditor(editor);
        spinner.setValue(new java.util.Date());
        return spinner;
    }

    private void carregarVendasNaTabela() {
        tabelaModel.setRowCount(0);
        List<Venda> vendas = vendaDAO.listar();

        for (Venda v : vendas) {
            tabelaModel.addRow(new Object[]{
                    v.getId(),
                    v.getNomePessoa(),
                    v.getNomeFilme(),
                    v.getDataHoraFilme().format(formatterBR),
                    v.getLocalFilme(),
                    v.getNumeroIngresso(),
                    v.getValorIngresso(),
                    v.isCancelado() ? "Sim" : "Não"
            });
        }
    }

    private void concluirVenda() {
        // Validação dos campos obrigatórios
        StringBuilder camposFaltando = new StringBuilder();

        if (txtNomePessoa.getText().trim().isEmpty())
            camposFaltando.append("- Nome da Pessoa\n");

        if (txtNomeFilme.getText().trim().isEmpty())
            camposFaltando.append("- Nome do Filme\n");

        if (datePicker.getModel().getValue() == null)
            camposFaltando.append("- Data do Filme\n");

        if (spinnerHora.getValue() == null)
            camposFaltando.append("- Hora do Filme\n");

        if (txtLocalFilme.getText().trim().isEmpty())
            camposFaltando.append("- Local do Filme\n");

        if (txtNumeroIngresso.getText().trim().isEmpty())
            camposFaltando.append("- Número do Ingresso\n");

        if (txtValorIngresso.getText().trim().isEmpty())
            camposFaltando.append("- Valor do Ingresso\n");

        if (camposFaltando.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Ainda falta preencher alguns campos:\n" + camposFaltando.toString(),
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Venda venda = new Venda();
            venda.setNomePessoa(txtNomePessoa.getText().trim());
            venda.setNomeFilme(txtNomeFilme.getText().trim());

            java.util.Date dataSelecionada = (java.util.Date) datePicker.getModel().getValue();
            java.util.Date horaSelecionada = (java.util.Date) spinnerHora.getValue();

            LocalDateTime dataHora = LocalDateTime.of(
                    new Date(dataSelecionada.getTime()).toLocalDate(),
                    LocalTime.of(horaSelecionada.getHours(), horaSelecionada.getMinutes())
            );

            venda.setDataHoraFilme(dataHora);
            venda.setLocalFilme(txtLocalFilme.getText().trim());
            venda.setNumeroIngresso(txtNumeroIngresso.getText().trim());

            try {
                venda.setValorIngresso(new BigDecimal(txtValorIngresso.getText().trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor do ingresso inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            venda.setCancelado(false);

            vendaDAO.salvar(venda);
            carregarVendasNaTabela();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Venda concluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao concluir venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarVenda() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validação dos campos obrigatórios
        StringBuilder camposFaltando = new StringBuilder();

        if (txtNomePessoa.getText().trim().isEmpty())
            camposFaltando.append("- Nome da Pessoa\n");

        if (txtNomeFilme.getText().trim().isEmpty())
            camposFaltando.append("- Nome do Filme\n");

        if (datePicker.getModel().getValue() == null)
            camposFaltando.append("- Data do Filme\n");

        // Não precisa validar spinnerHora.getValue() == null, pois nunca é null

        if (txtLocalFilme.getText().trim().isEmpty())
            camposFaltando.append("- Local do Filme\n");

        if (txtNumeroIngresso.getText().trim().isEmpty())
            camposFaltando.append("- Número do Ingresso\n");

        if (txtValorIngresso.getText().trim().isEmpty())
            camposFaltando.append("- Valor do Ingresso\n");

        if (camposFaltando.length() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Ainda falta preencher alguns campos:\n" + camposFaltando.toString(),
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = (Long) tabelaModel.getValueAt(selectedRow, 0);
            Venda venda = vendaDAO.buscarPorId(id);

            venda.setNomePessoa(txtNomePessoa.getText().trim());
            venda.setNomeFilme(txtNomeFilme.getText().trim());

            java.util.Date dataSelecionada = (java.util.Date) datePicker.getModel().getValue();
            java.util.Date horaSelecionada = (java.util.Date) spinnerHora.getValue();

            LocalDateTime dataHora = LocalDateTime.of(
                    new Date(dataSelecionada.getTime()).toLocalDate(),
                    LocalTime.of(horaSelecionada.getHours(), horaSelecionada.getMinutes())
            );

            venda.setDataHoraFilme(dataHora);
            venda.setLocalFilme(txtLocalFilme.getText().trim());
            venda.setNumeroIngresso(txtNumeroIngresso.getText().trim());

            try {
                venda.setValorIngresso(new BigDecimal(txtValorIngresso.getText().trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor do ingresso inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            vendaDAO.editar(venda);
            carregarVendasNaTabela();
            limparFormulario();
            JOptionPane.showMessageDialog(this, "Venda editada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar venda: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarVenda() {
        int selectedRow = tabela.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tabelaModel.getValueAt(selectedRow, 0);
        Venda venda = vendaDAO.buscarPorId(id);

        if (venda == null) {
            JOptionPane.showMessageDialog(this, "Venda não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (venda.isCancelado()) {
            JOptionPane.showMessageDialog(this, "Venda já se encontra cancelada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        vendaDAO.cancelarVenda(id);
        carregarVendasNaTabela();
        limparFormulario();
        JOptionPane.showMessageDialog(this, "Venda cancelada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limparFormulario() {
        txtNomePessoa.setText("");
        txtNomeFilme.setText("");
        datePicker.getModel().setValue(null);
        spinnerHora.setValue(new java.util.Date());
        txtLocalFilme.setText("");
        txtNumeroIngresso.setText("");
        txtValorIngresso.setText("");
        tabela.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar tema FlatLaf");
        }
        SwingUtilities.invokeLater(() -> new CinemaApp().setVisible(true));
    }
}
