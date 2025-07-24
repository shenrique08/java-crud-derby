package org.api.ui;

import org.api.dao.ProdutoDAO;
import org.api.model.Produto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class EstoqueFrame extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstoqueFrame.class);

    private final ProdutoDAO produtoDAO;

    // --- Componentes da UI como campos da classe ---
    private final JTextField nomeField;
    private final JTextField descricaoField;
    private final JTextField precoField;
    private final JTextField estoqueField;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;

    public EstoqueFrame() {
        this.produtoDAO = new ProdutoDAO();

        setTitle("Controle de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DO FORMULÁRIO (Norte) ---
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nomeField = new JTextField();
        descricaoField = new JTextField();
        precoField = new JTextField();
        estoqueField = new JTextField();
        JButton addButton = new JButton("Adicionar Produto");

        formPanel.add(new JLabel("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(descricaoField);
        formPanel.add(new JLabel("Preço (ex: 99.99):"));
        formPanel.add(precoField);
        formPanel.add(new JLabel("Estoque Inicial:"));
        formPanel.add(estoqueField);
        formPanel.add(new JLabel());
        formPanel.add(addButton);

        // --- TABELA DE PRODUTOS (Centro) ---
        configurarTabela(); // Chamada para o novo método de configuração
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- AÇÕES ---
        addButton.addActionListener(e -> adicionarNovoProduto());

        // **PASSO FINAL**: Carrega os dados na tabela assim que a janela é criada
        atualizarTabela();
    }

    /**
     * Configura o JTable e seu modelo (TableModel).
     */
    private void configurarTabela() {
        String[] colunas = {"ID", "Nome", "Descrição", "Preço", "Estoque"};

        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede que o usuário edite a tabela diretamente
            }
        };
        tabelaProdutos = new JTable(tableModel);
    }

    /**
     * Busca os dados do DAO e atualiza a JTable.
     */
    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela antes de preencher

        LOGGER.info("Buscando produtos para atualizar a tabela...");
        List<Produto> produtos = produtoDAO.findAll();

        for (Produto produto : produtos) {
            Object[] rowData = {
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getEstoque()
            };
            tableModel.addRow(rowData); // Adiciona cada produto como uma nova linha
        }
        LOGGER.info("Tabela atualizada com {} produtos.", produtos.size());
    }

    private void adicionarNovoProduto() {
        try {
            String nome = nomeField.getText();
            String descricao = descricaoField.getText();
            BigDecimal preco = new BigDecimal(precoField.getText());
            int estoque = Integer.parseInt(estoqueField.getText());

            if (nome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo 'Nome' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto novoProduto = Produto.builder()
                    .nome(nome)
                    .estoque(estoque)
                    .descricao(descricao)
                    .preco(preco)
                    .build();

            LOGGER.info("Inserindo novo produto: {}", nome);
            produtoDAO.insert(novoProduto); // Usando o nome do método do seu DAO

            JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();

            // **PASSO FINAL**: Atualiza a tabela para exibir o novo produto
            atualizarTabela();

        } catch (NumberFormatException ex) {
            LOGGER.warn("Erro de formato nos dados de entrada.", ex);
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido para Preço e Estoque.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        estoqueField.setText("");
    }
}