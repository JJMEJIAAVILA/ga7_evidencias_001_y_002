package com.biblioteca.view;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Panel principal que contiene los paneles de libros, revistas y DVDs
 * Gestiona la navegación entre ellos usando un CardLayout
 */
public class PanelPrincipal extends JPanel {
    private MainFrame parent;
    private CardLayout cardLayout;
    private JPanel contenedor;

    private PanelLibros panelLibros;
    private PanelRevistas panelRevistas;
    private PanelDVDs panelDVDs;

    private JToolBar toolBar;

    public PanelPrincipal(MainFrame parent) throws SQLException {
        this.parent = parent;
        setLayout(new BorderLayout());

        inicializarToolBar();
        inicializarPaneles();
    }

    private void inicializarToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnLibros = new JButton("Libros");
        JButton btnRevistas = new JButton("Revistas");
        JButton btnDVDs = new JButton("DVDs");

        btnLibros.addActionListener(e -> mostrarPanelLibros());
        btnRevistas.addActionListener(e -> mostrarPanelRevistas());
        btnDVDs.addActionListener(e -> mostrarPanelDVDs());

        toolBar.add(btnLibros);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(btnRevistas);
        toolBar.add(new JToolBar.Separator());
        toolBar.add(btnDVDs);

        add(toolBar, BorderLayout.NORTH);
    }

    private void inicializarPaneles() {
        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);

        try {
            panelLibros = new PanelLibros(parent);
            panelRevistas = new PanelRevistas(parent);
            panelDVDs = new PanelDVDs(parent);

            contenedor.add(panelLibros, "LIBROS");
            contenedor.add(panelRevistas, "REVISTAS");
            contenedor.add(panelDVDs, "DVDS");

            // Panel de bienvenida
            JPanel panelBienvenida = new JPanel(new BorderLayout());
            JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Biblioteca", JLabel.CENTER);
            lblBienvenida.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel lblInstrucciones = new JLabel("<html><center>Seleccione una opción en la barra de herramientas<br>para gestionar los elementos de la biblioteca.</center></html>", JLabel.CENTER);

            panelBienvenida.add(lblBienvenida, BorderLayout.CENTER);
            panelBienvenida.add(lblInstrucciones, BorderLayout.SOUTH);

            contenedor.add(panelBienvenida, "BIENVENIDA");

            add(contenedor, BorderLayout.CENTER);
            cardLayout.show(contenedor, "BIENVENIDA");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al inicializar los paneles: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void mostrarPanelLibros() {
        panelLibros.actualizarTabla();
        cardLayout.show(contenedor, "LIBROS");
    }

    public void mostrarPanelRevistas() {
        panelRevistas.actualizarTabla();
        cardLayout.show(contenedor, "REVISTAS");
    }

    public void mostrarPanelDVDs() {
        panelDVDs.actualizarTabla();
        cardLayout.show(contenedor, "DVDS");
    }
}