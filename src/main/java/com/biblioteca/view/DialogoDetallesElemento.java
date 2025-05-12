package com.biblioteca.view;

import com.biblioteca.model.DVD;
import com.biblioteca.model.ElementoBiblioteca;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Revista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogoDetallesElemento extends JDialog {
    private ElementoBiblioteca elemento;

    // Colores personalizados
    private Color colorPrimario = new Color(59, 89, 152); // Azul oscuro
    private Color colorTextoPrincipal = Color.BLACK;
    private Color colorTextoSecundario = Color.DARK_GRAY;
    private Font fuentePrincipal = new Font("Arial", Font.PLAIN, 14);
    private Font fuenteTitulos = new Font("Arial", Font.BOLD, 16);

    public DialogoDetallesElemento(MainFrame parent, ElementoBiblioteca elemento) {
        super(parent, "Detalles de " + obtenerTipoElemento(elemento), true);
        this.elemento = elemento;

        inicializarComponentes();
        establecerEstilos(); // Aplicar estilos
        configurarVentana();
    }

    private static String obtenerTipoElemento(ElementoBiblioteca elemento) {
        if (elemento instanceof Libro) {
            return "Libro";
        } else if (elemento instanceof Revista) {
            return "Revista";
        } else if (elemento instanceof DVD) {
            return "DVD";
        } else {
            return "Elemento";
        }
    }

    private void inicializarComponentes() {
        // Panel principal con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Aumentar el espacio
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; // Para que los valores se expandan

        // Mostrar detalles comunes
        addAtributo(panel, gbc, 0, "ID:", String.valueOf(elemento.getId()));
        addAtributo(panel, gbc, 1, "Título:", elemento.getTitulo());
        addAtributo(panel, gbc, 2, "Autor:", elemento.getAutor());
        addAtributo(panel, gbc, 3, "Año:", String.valueOf(elemento.getAnoPublicacion()));
        addAtributo(panel, gbc, 4, "Tipo:", elemento.getTipo());

        // Mostrar detalles específicos según el tipo
        int row = 5;

        if (elemento instanceof Libro) {
            Libro libro = (Libro) elemento;
            addAtributo(panel, gbc, row++, "ISBN:", libro.getIsbn());
            addAtributo(panel, gbc, row++, "Páginas:", String.valueOf(libro.getNumeroPaginas()));
            addAtributo(panel, gbc, row++, "Género:", libro.getGenero());
            addAtributo(panel, gbc, row++, "Editorial:", libro.getEditorial());
        } else if (elemento instanceof Revista) {
            Revista revista = (Revista) elemento;
            addAtributo(panel, gbc, row++, "Edición:", String.valueOf(revista.getNumeroEdicion()));
            addAtributo(panel, gbc, row++, "Categoría:", revista.getCategoria());
        } else if (elemento instanceof DVD) {
            DVD dvd = (DVD) elemento;
            addAtributo(panel, gbc, row++, "Duración:", dvd.getDuracion() + " min");
            addAtributo(panel, gbc, row++, "Género:", dvd.getGenero());
        }

        // Botón cerrar con estilo
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(220, 220, 220)); // Gris claro
        btnCerrar.setForeground(colorTextoPrincipal);
        btnCerrar.setFont(fuentePrincipal);
        btnCerrar.setFocusPainted(false); // Opcional: quitar el foco visual al hacer clic

        btnCerrar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnCerrar);
        panelBotones.setBackground(Color.WHITE); // Fondo blanco para el panel de botones

        // Panel principal con borde y espaciado
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15)); // Más espacio entre componentes
        panelPrincipal.add(panel, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15)); // Mayor margen

        setContentPane(panelPrincipal);
    }

    private void establecerEstilos() {
        // Estilos para etiquetas
        UIManager.put("Label.font", fuentePrincipal);
        UIManager.put("Label.foreground", colorTextoPrincipal);

        // Estilos para los valores
        JLabel.setDefaultLocale(new java.util.Locale("es", "CO")); // Establecer el idioma
        UIManager.put("JLabel.font", fuentePrincipal);
        UIManager.put("JLabel.foreground", colorTextoSecundario);

        // Estilo para el panel principal
        JPanel panelPrincipal = (JPanel) getContentPane();
        panelPrincipal.setBackground(Color.WHITE);
    }

    private void addAtributo(JPanel panel, GridBagConstraints gbc, int row, String etiqueta, String valor) {
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(fuenteTitulos); // Fuente más grande y en negrita para la etiqueta

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(fuentePrincipal);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(lblEtiqueta, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(lblValor, gbc);
    }

    private void configurarVentana() {
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());
    }
}