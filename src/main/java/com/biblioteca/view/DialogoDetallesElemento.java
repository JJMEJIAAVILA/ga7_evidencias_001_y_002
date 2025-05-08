package com.biblioteca.view;

import com.biblioteca.model.DVD;
import com.biblioteca.model.ElementoBiblioteca;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Revista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogoDetallesElemento extends JDialog {
    private ElementoBiblioteca elemento;

    public DialogoDetallesElemento(MainFrame parent, ElementoBiblioteca elemento) {
        super(parent, "Detalles de " + obtenerTipoElemento(elemento), true);
        this.elemento = elemento;

        inicializarComponentes();
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
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

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

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnCerrar);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.add(panel, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setContentPane(panelPrincipal);
    }

    private void addAtributo(JPanel panel, GridBagConstraints gbc, int row, String etiqueta, String valor) {
        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(lblEtiqueta.getFont().deriveFont(Font.BOLD));

        JLabel lblValor = new JLabel(valor);

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