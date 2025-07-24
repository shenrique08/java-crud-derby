package org.api;

import org.api.dao.ProdutoDAO;
import org.api.model.Produto;
import org.api.ui.EstoqueFrame;
import org.api.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;
import java.math.BigDecimal;
import java.util.List;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Inicializando banco de dados...");
        DatabaseUtil.initializeDatabase();
        LOGGER.info("Banco de dados pronto.");

        // 2. Povoa o banco de dados com dados iniciais, se necessário
        seedDatabase();

        // 3. Inicia a interface gráfica
        SwingUtilities.invokeLater(() -> {
            EstoqueFrame frame = new EstoqueFrame();
            frame.setVisible(true);
        });
    }

    private static void seedDatabase() {
        ProdutoDAO produtoDAO = new ProdutoDAO();

        try {
            // Verifica se a tabela já tem algum produto
            if (produtoDAO.findAll().isEmpty()) {
                LOGGER.info("Banco de dados vazio. Povoando com dados de exemplo...");

                // Lista de 5 produtos de exemplo
                List<Produto> produtosExemplo = List.of(
                        Produto.builder().nome("Notebook Gamer").descricao("Notebook com RTX 4060, 16GB RAM").preco(new BigDecimal("7500.00")).estoque(10).build(),
                        Produto.builder().nome("Mouse Sem Fio").descricao("Mouse ergonômico com 6 botões").preco(new BigDecimal("150.75")).estoque(50).build(),
                        Produto.builder().nome("Teclado Mecânico").descricao("Teclado ABNT2 com switch brown").preco(new BigDecimal("350.00")).estoque(30).build(),
                        Produto.builder().nome("Monitor 27' 4K").descricao("Monitor com painel IPS e 144Hz").preco(new BigDecimal("2200.50")).estoque(15).build(),
                        Produto.builder().nome("SSD NVMe 1TB").descricao("SSD com velocidade de leitura de 3500MB/s").preco(new BigDecimal("450.00")).estoque(40).build()
                );

                // Insere cada produto da lista no banco de dados
                for (Produto p : produtosExemplo) {
                    produtoDAO.insert(p);
                }

                LOGGER.info("Banco de dados povoado com {} produtos.", produtosExemplo.size());
            } else {
                LOGGER.info("O banco de dados já contém dados. Nenhuma ação de povoamento necessária.");
            }
        } catch (RuntimeException e) {
            LOGGER.error("Falha ao tentar povoar o banco de dados.", e);
        }
    }
}