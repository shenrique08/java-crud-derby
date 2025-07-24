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

    // Componentes da UI
    private JTextField nomeField;
    private JTextField descricaoField;
    private JTextField precoField;
    private JTextField estoqueField;
    private JTable tabelaProdutos;
    private DefaultTableModel tableModel;
    private List<Produto> produtosList;

    public EstoqueFrame() {
        this.produtoDAO = new ProdutoDAO();

        setTitle("Controle de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Painel do Formulário (Norte)
        JPanel formPanel = criarFormularioPanel();
        add(formPanel, BorderLayout.NORTH);

        // Painel da Tabela (Centro)
        JScrollPane scrollPane = criarTabelaPanel();
        add(scrollPane, BorderLayout.CENTER);

        // Painel de Ações (Sul)
        JPanel acoesPanel = criarAcoesPanel();
        add(acoesPanel, BorderLayout.SOUTH);

        // **ALTERAÇÃO 1**: A linha abaixo foi REMOVIDA para não carregar os dados inicialmente.
        // atualizarTabela();
    }

    private JPanel criarFormularioPanel() {
        // ... (este método continua igual)
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicializa os campos aqui, já que são campos da classe
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

        addButton.addActionListener(e -> adicionarNovoProduto());
        return formPanel;
    }

    private JScrollPane criarTabelaPanel() {
        // ... (este método continua igual)
        String[] colunas = {"ID", "Nome", "Descrição", "Preço", "Estoque"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaProdutos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        return scrollPane;
    }

    private JPanel criarAcoesPanel() {
        JPanel acoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // **ALTERAÇÃO 2**: Novo botão adicionado
        JButton mostrarButton = new JButton("Mostrar/Atualizar Produtos");

        JButton comprarButton = new JButton("Comprar");
        JButton venderButton = new JButton("Vender");
        JButton deletarButton = new JButton("Deletar Produto");

        acoesPanel.add(mostrarButton); // Botão adicionado à tela
        acoesPanel.add(comprarButton);
        acoesPanel.add(venderButton);
        acoesPanel.add(deletarButton);

        // Ações dos botões
        mostrarButton.addActionListener(e -> atualizarTabela()); // Ação conectada
        comprarButton.addActionListener(e -> executarCompraVenda(true));
        venderButton.addActionListener(e -> executarCompraVenda(false));
        deletarButton.addActionListener(e -> deletarProdutoSelecionado());

        return acoesPanel;
    }

    // ... (O restante dos métodos, como deletarProdutoSelecionado, executarCompraVenda,
    //      atualizarTabela, adicionarNovoProduto e limparCampos, continuam exatamente os mesmos)

    private void deletarProdutoSelecionado() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para deletar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Produto produtoSelecionado = produtosList.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja deletar o produto '" + produtoSelecionado.getNome() + "'?",
                "Confirmar Deleção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produtoDAO.delete(produtoSelecionado.getId());
                LOGGER.info("Produto ID {} deletado pelo usuário.", produtoSelecionado.getId());
                atualizarTabela();
            } catch (RuntimeException e) {
                LOGGER.error("Falha ao deletar produto.", e);
                JOptionPane.showMessageDialog(this, "Erro ao deletar produto: " + e.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void executarCompraVenda(boolean isCompra) {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String quantidadeStr = JOptionPane.showInputDialog(this, "Digite a quantidade:", (isCompra ? "Comprar" : "Vender"), JOptionPane.QUESTION_MESSAGE);
        if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) return;

        try {
            int quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser positiva.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Produto produtoSelecionado = produtosList.get(selectedRow);
            int estoqueAtual = produtoSelecionado.getEstoque();

            if (!isCompra && quantidade > estoqueAtual) {
                JOptionPane.showMessageDialog(this, "Estoque insuficiente.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int novoEstoque = isCompra ? estoqueAtual + quantidade : estoqueAtual - quantidade;

            Produto produtoAtualizado = Produto.builder()
                    .id(produtoSelecionado.getId())
                    .nome(produtoSelecionado.getNome())
                    .descricao(produtoSelecionado.getDescricao())
                    .preco(produtoSelecionado.getPreco())
                    .estoque(novoEstoque)
                    .build();

            produtoDAO.update(produtoAtualizado);
            LOGGER.info("Produto ID {} atualizado. Novo estoque: {}", produtoSelecionado.getId(), novoEstoque);

            atualizarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            LOGGER.error("Falha ao atualizar o estoque.", ex);
            JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        try {
            this.produtosList = produtoDAO.findAll();
            for (Produto produto : produtosList) {
                tableModel.addRow(new Object[]{produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getEstoque()});
            }
        } catch (RuntimeException e) {
            LOGGER.error("Falha ao carregar produtos.", e);
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarNovoProduto() {
        try {
            String nome = nomeField.getText();
            BigDecimal preco = new BigDecimal(precoField.getText());
            int estoque = Integer.parseInt(estoqueField.getText());

            Produto novoProduto = Produto.builder()
                    .nome(nome)
                    .descricao(descricaoField.getText())
                    .preco(preco)
                    .estoque(estoque)
                    .build();

            produtoDAO.insert(novoProduto);
            JOptionPane.showMessageDialog(this, "Produto adicionado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            limparCampos();
            atualizarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço e Estoque devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            LOGGER.error("Falha ao inserir produto.", ex);
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        descricaoField.setText("");
        precoField.setText("");
        estoqueField.setText("");
    }
}