CREATE TABLE PRODUTO (
                         id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
                         nome VARCHAR(100) NOT NULL,
                         descricao VARCHAR(255),
                         preco DECIMAL(10, 2) NOT NULL,
                         estoque INT NOT NULL
)