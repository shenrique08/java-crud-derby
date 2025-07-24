package org.api;

import org.api.ui.EstoqueFrame;
import org.api.util.DatabaseUtil;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Primeiro, garante que o banco de dados e a tabela existam
        System.out.println("Inicializando banco de dados...");
        DatabaseUtil.initializeDatabase();
        System.out.println("Banco de dados pronto.");

        // Em seguida, inicia a interface gráfica na thread de eventos do Swing
        // Isso é uma boa prática para evitar problemas de concorrência na UI
        SwingUtilities.invokeLater(() -> {
            EstoqueFrame frame = new EstoqueFrame();
            frame.setVisible(true);
        });
    }
}