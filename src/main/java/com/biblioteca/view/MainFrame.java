package com.biblioteca.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private PanelPrincipal panelPrincipal;

    // Colores personalizados
    private Color colorPrimario = new Color(59, 89, 152); // Azul oscuro
    private Color colorTextoPrincipal = Color.BLACK;
    private Font fuentePrincipal = new Font("Arial", Font.PLAIN, 14);
    private Font fuenteMenu; // Declaramos fuenteMenu a nivel de instancia
    private Border bordeMenu = BorderFactory.createEmptyBorder(5, 10, 5, 10); // Borde para los menús

    public MainFrame() {
        establecerLookAndFeel();
        configurarVentana();
        inicializarFuentes(); // Inicializamos las fuentes aquí
        inicializarComponentes();
    }

    private void establecerLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void configurarVentana() {
        setTitle("Sistema de Biblioteca");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
    }

    private void inicializarFuentes() {
        // Intentamos usar "Segoe UI Emoji", si no está disponible, usamos fuentePrincipal
        Font fuenteEmoji = new Font("Segoe UI Emoji", Font.PLAIN, 14);
        if (!fuenteEmoji.getFamily().equals("Segoe UI Emoji")) {
            fuenteMenu = fuentePrincipal;
        } else {
            fuenteMenu = fuenteEmoji;
        }
    }

    private void inicializarComponentes() {
        try {
            panelPrincipal = new PanelPrincipal(this);
            add(panelPrincipal, BorderLayout.CENTER);
            setJMenuBar(crearMenuBar());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al inicializar los componentes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(colorPrimario);
        menuBar.setForeground(Color.WHITE);
        menuBar.setFont(fuenteMenu);
        menuBar.setBorder(new EmptyBorder(2, 2, 2, 2));

        // Menú Archivo
        JMenu menuArchivo = new JMenu("Archivo 🚪");
        menuArchivo.setForeground(Color.BLACK);
        menuArchivo.setFont(fuenteMenu);
        menuArchivo.setBorder(bordeMenu);
        JMenuItem itemSalir = new JMenuItem("Salir 🚪");
        itemSalir.setFont(fuenteMenu);
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);

        // Menú Catálogo
        JMenu menuCatalogo = new JMenu("Catálogo 📚");
        menuCatalogo.setForeground(Color.BLACK);
        menuCatalogo.setFont(fuenteMenu);
        menuCatalogo.setBorder(bordeMenu);
        JMenuItem itemLibros = new JMenuItem("Libros 📖");
        itemLibros.setFont(fuenteMenu);
        itemLibros.addActionListener(e -> panelPrincipal.mostrarPanelLibros());
        JMenuItem itemRevistas = new JMenuItem("Revistas 📰");
        itemRevistas.setFont(fuenteMenu);
        itemRevistas.addActionListener(e -> panelPrincipal.mostrarPanelRevistas());
        JMenuItem itemDVDs = new JMenuItem("DVDs 📀");
        itemDVDs.setFont(fuenteMenu);
        itemDVDs.addActionListener(e -> panelPrincipal.mostrarPanelDVDs());

        menuCatalogo.add(itemLibros);
        menuCatalogo.add(itemRevistas);
        menuCatalogo.add(itemDVDs);

        // Menú Ayuda
        JMenu menuAyuda = new JMenu("Ayuda ❓");
        menuAyuda.setForeground(Color.BLACK);
        menuAyuda.setFont(fuenteMenu);
        menuAyuda.setBorder(bordeMenu);
        JMenuItem itemAcercaDe = new JMenuItem("Acerca de ℹ️");
        itemAcercaDe.setFont(fuenteMenu);
        itemAcercaDe.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Sistema de Biblioteca v1.0\nDesarrollado con Java y Swing",
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE)
        );
        menuAyuda.add(itemAcercaDe);

        menuBar.add(menuArchivo);
        menuBar.add(menuCatalogo);
        menuBar.add(menuAyuda);

        return menuBar;
    }
}