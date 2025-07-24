package org.api.dao;

import org.api.model.Produto;
import org.api.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoDAO.class);

    public void insert(Produto produto) {
        String sql = "INSERT INTO PRODUTO (nome, descricao, preco, estoque) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            statement.setBigDecimal(3, produto.getPreco());
            statement.setInt(4, produto.getEstoque());
            int rowsAffected = statement.executeUpdate();
            LOGGER.info("Rows affected: {}", rowsAffected);

        } catch (SQLException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public List<Produto> findAll() {
        String sql = "SELECT * FROM PRODUTO";
        List<Produto> produtos = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Produto produto = Produto.builder()
                        .id(resultSet.getLong("id"))
                        .nome(resultSet.getString("nome"))
                        .descricao(resultSet.getString("descricao"))
                        .preco(resultSet.getBigDecimal("preco"))
                        .estoque(resultSet.getInt("estoque"))
                        .build();

                produtos.add(produto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return produtos;
    }

    public void update(Produto produto) {
        String sql = "UPDATE PRODUTO SET nome = ?, descricao = ?, preco = ?, estoque = ? WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            statement.setBigDecimal(3, produto.getPreco());
            statement.setInt(4, produto.getEstoque());
            statement.setLong(5, produto.getId());

            int rowsAffected = statement.executeUpdate();
            LOGGER.info("Tuplas afetadas:  {}", rowsAffected);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM PRODUTO WHERE id = ?";

        try(Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0)
                LOGGER.info("Deletado com sucesso");
            else
                LOGGER.warn("Produto com id informado não foi encontrado");

        } catch (SQLException e) {
            LOGGER.error("Failed to delete product with id: {}", id, e);
            throw new RuntimeException(e);
        }
    }
}
