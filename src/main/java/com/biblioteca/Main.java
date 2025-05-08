package com.biblioteca;

import com.biblioteca.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.biblioteca.model.dao.ConexionBD;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        // Intentar establecer el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al establecer el Look and Feel: " + e.getMessage());
        }

        // Verificar la conexión a la base de datos antes de iniciar la aplicación
        try {
            ConexionBD.getConnection();
            System.out.println("Conexión a la base de datos establecida correctamente.");

            // Iniciar la aplicación en el Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            });

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "No se pudo conectar con la base de datos.\nError: " + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);

            System.exit(1);
        }
    }
}