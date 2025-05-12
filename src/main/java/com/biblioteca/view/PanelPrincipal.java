package com.biblioteca.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    // Colores personalizados
    private Color colorPrimario = new Color(59, 89, 152); // Azul oscuro
    private Color colorTextoPrincipal = Color.BLACK;
    private Color colorTextoSecundario = Color.DARK_GRAY;
    private Font fuentePrincipal = new Font("Arial", Font.PLAIN, 14);
    private Font fuenteTitulos = new Font("Arial", Font.BOLD, 16);
    private Color colorFondoPanel = Color.WHITE;

    public PanelPrincipal(MainFrame parent) throws SQLException {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(colorFondoPanel);

        inicializarToolBar();
        inicializarPaneles();
    }

    private void inicializarToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(colorPrimario);
        toolBar.setForeground(Color.WHITE);
        toolBar.setBorder(new EmptyBorder(5, 10, 5, 10));

        JButton btnLibros = crearBotonToolBar("Libros");
        JButton btnRevistas = crearBotonToolBar("Revistas");
        JButton btnDVDs = crearBotonToolBar("DVDs");

        btnLibros.addActionListener(e -> mostrarPanelLibros());
        btnRevistas.addActionListener(e -> mostrarPanelRevistas());
        btnDVDs.addActionListener(e -> mostrarPanelDVDs());

        toolBar.add(btnLibros);
        toolBar.add(new JToolBar.Separator(new Dimension(10, 0))); // Separador con espacio
        toolBar.add(btnRevistas);
        toolBar.add(new JToolBar.Separator(new Dimension(10, 0)));
        toolBar.add(btnDVDs);

        add(toolBar, BorderLayout.NORTH);
    }

    private JButton crearBotonToolBar(String texto) {
        JButton boton = new JButton(texto);
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorPrimario);
        boton.setFont(fuentePrincipal);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return boton;
    }

    private void inicializarPaneles() {
        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        contenedor.setBackground(colorFondoPanel);

        try {
            panelLibros = new PanelLibros(parent);
            panelRevistas = new PanelRevistas(parent);
            panelDVDs = new PanelDVDs(parent);

            contenedor.add(panelLibros, "LIBROS");
            contenedor.add(panelRevistas, "REVISTAS");
            contenedor.add(panelDVDs, "DVDS");

            // Panel de bienvenida con estilos
            JPanel panelBienvenida = new JPanel(new BorderLayout());
            panelBienvenida.setBackground(colorFondoPanel);
            JLabel lblBienvenida = new JLabel("Bienvenido al Sistema de Biblioteca", JLabel.CENTER);
            lblBienvenida.setFont(new Font("Arial", Font.BOLD, 28));
            lblBienvenida.setForeground(colorPrimario);

            JLabel lblInstrucciones = new JLabel("<html><center>Seleccione una opción en la barra de herramientas<br>para gestionar los elementos de la biblioteca.</center></html>", JLabel.CENTER);
            lblInstrucciones.setFont(fuentePrincipal);
            lblInstrucciones.setForeground(colorTextoSecundario);

            panelBienvenida.add(lblBienvenida, BorderLayout.CENTER);
            panelBienvenida.add(lblInstrucciones, BorderLayout.SOUTH);
            panelBienvenida.setBorder(new EmptyBorder(50, 50, 50, 50)); // Margen para el panel de bienvenida

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