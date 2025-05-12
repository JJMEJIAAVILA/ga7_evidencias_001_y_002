package com.biblioteca.view;

import com.biblioteca.model.DVD;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Revista;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DialogoAgregarElemento extends JDialog {
    private JPanel panelCampos;
    private JPanel panelBotones;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private Map<String, JTextField> campos;
    private Object resultado;
    private String tipoElemento;
    private Object elementoAEditar;

    public DialogoAgregarElemento(JFrame parent, String titulo, String tipo) {
        super(parent, titulo, true);
        this.tipoElemento = tipo;
        this.elementoAEditar = null;
        inicializar();
    }

    public DialogoAgregarElemento(JFrame parent, String titulo, String tipo, Object elemento) {
        super(parent, titulo, true);
        this.tipoElemento = tipo;
        this.elementoAEditar = elemento;
        inicializar();
        llenarCamposParaEditar();
    }

    private void inicializar() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        campos = new HashMap<>();

        crearCampos();

        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarDatos());
        btnCancelar.addActionListener(e -> cancelar());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void crearCampos() {
        switch (tipoElemento) {
            case "LIBRO":
                crearCampo("Título:");
                crearCampo("Autor:");
                crearCampo("Año:");
                crearCampo("ISBN:");
                crearCampo("Páginas:");
                crearCampo("Género:");
                crearCampo("Editorial:");
                break;
            case "DVD":
                crearCampo("Título:");
                crearCampo("Autor:");
                crearCampo("Año:");
                crearCampo("Duración:");
                crearCampo("Género:");
                break;
            case "REVISTA":
                crearCampo("Título:");
                crearCampo("Autor:");
                crearCampo("Año:");
                crearCampo("Edición:");
                crearCampo("Categoría:");
                break;
        }
    }

    private void crearCampo(String etiqueta) {
        JLabel label = new JLabel(etiqueta);
        JTextField textField = new JTextField(20);
        panelCampos.add(label);
        panelCampos.add(textField);
        campos.put(etiqueta.replace(":", ""), textField);
    }

    private void llenarCamposParaEditar() {
        if (elementoAEditar != null) {
            switch (tipoElemento) {
                case "LIBRO":
                    Libro libro = (Libro) elementoAEditar;
                    campos.get("Título").setText(libro.getTitulo());
                    campos.get("Autor").setText(libro.getAutor());
                    campos.get("Año").setText(String.valueOf(libro.getAnoPublicacion()));
                    campos.get("ISBN").setText(libro.getIsbn());
                    campos.get("Páginas").setText(String.valueOf(libro.getNumeroPaginas()));
                    campos.get("Género").setText(libro.getGenero());
                    campos.get("Editorial").setText(libro.getEditorial());
                    break;
                case "DVD":
                    DVD dvd = (DVD) elementoAEditar;
                    campos.get("Título").setText(dvd.getTitulo());
                    campos.get("Autor").setText(dvd.getAutor());
                    campos.get("Año").setText(String.valueOf(dvd.getAnoPublicacion()));
                    campos.get("Duración").setText(String.valueOf(dvd.getDuracion()));
                    campos.get("Género").setText(dvd.getGenero());
                    break;
                case "REVISTA":
                    Revista revista = (Revista) elementoAEditar;
                    campos.get("Título").setText(revista.getTitulo());
                    campos.get("Autor").setText(revista.getAutor());
                    campos.get("Año").setText(String.valueOf(revista.getAnoPublicacion()));
                    campos.get("Edición").setText(String.valueOf(revista.getNumeroEdicion()));
                    campos.get("Categoría").setText(revista.getCategoria());
                    break;
            }
        }
    }

    private void guardarDatos() {
        try {
            switch (tipoElemento) {
                case "LIBRO":
                    String tituloLibro = campos.get("Título").getText();
                    String autorLibro = campos.get("Autor").getText();
                    int anoLibro = Integer.parseInt(campos.get("Año").getText());
                    String isbnLibro = campos.get("ISBN").getText();
                    int paginasLibro = Integer.parseInt(campos.get("Páginas").getText());
                    String generoLibro = campos.get("Género").getText();
                    String editorialLibro = campos.get("Editorial").getText();
                    if (elementoAEditar instanceof Libro) {
                        Libro libroExistente = (Libro) elementoAEditar;
                        resultado = new Libro(libroExistente.getId(), tituloLibro, autorLibro, anoLibro, isbnLibro, paginasLibro, generoLibro, editorialLibro);
                    } else {
                        resultado = new Libro(tituloLibro, autorLibro, anoLibro, isbnLibro, paginasLibro, generoLibro, editorialLibro);
                    }
                    break;
                case "DVD":
                    String tituloDVD = campos.get("Título").getText();
                    String autorDVD = campos.get("Autor").getText();
                    int anoDVD = Integer.parseInt(campos.get("Año").getText());
                    int duracionDVD = Integer.parseInt(campos.get("Duración").getText());
                    String generoDVD = campos.get("Género").getText();
                    if (elementoAEditar instanceof DVD) {
                        DVD dvdExistente = (DVD) elementoAEditar;
                        resultado = new DVD(dvdExistente.getId(), tituloDVD, autorDVD, anoDVD, duracionDVD, generoDVD);
                    } else {
                        resultado = new DVD(tituloDVD, autorDVD, anoDVD, duracionDVD, generoDVD);
                    }
                    break;
                case "REVISTA":
                    String tituloRevista = campos.get("Título").getText();
                    String autorRevista = campos.get("Autor").getText();
                    int anoRevista = Integer.parseInt(campos.get("Año").getText());
                    int edicionRevista = Integer.parseInt(campos.get("Edición").getText());
                    String categoriaRevista = campos.get("Categoría").getText();
                    if (elementoAEditar instanceof Revista) {
                        Revista revistaExistente = (Revista) elementoAEditar;
                        resultado = new Revista(revistaExistente.getId(), tituloRevista, autorRevista, anoRevista, edicionRevista, categoriaRevista);
                    } else {
                        resultado = new Revista(tituloRevista, autorRevista, anoRevista, edicionRevista, categoriaRevista);
                    }
                    break;
            }
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese números válidos para el año y la duración/páginas/edición.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelar() {
        resultado = null;
        dispose();
    }

    public Object mostrar() {
        setVisible(true);
        return resultado;
    }
}