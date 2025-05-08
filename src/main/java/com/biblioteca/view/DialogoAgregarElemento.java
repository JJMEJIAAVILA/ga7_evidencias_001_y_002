package com.biblioteca.view;

import com.biblioteca.model.DVD;
import com.biblioteca.model.ElementoBiblioteca;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Revista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class DialogoAgregarElemento extends JDialog {
    private MainFrame parent;
    private String tipoElemento;
    private ElementoBiblioteca elementoExistente;
    private ElementoBiblioteca resultado;

    // Campos comunes
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JSpinner spnAno;

    // Campos específicos de Libro
    private JTextField txtISBN;
    private JSpinner spnPaginas;
    private JTextField txtGeneroLibro;
    private JTextField txtEditorial;

    // Campos específicos de Revista
    private JSpinner spnEdicion;
    private JTextField txtCategoria;

    // Campos específicos de DVD
    private JSpinner spnDuracion;
    private JTextField txtGeneroDVD;

    // Botones
    private JButton btnAceptar;
    private JButton btnCancelar;

    public DialogoAgregarElemento(MainFrame parent, String titulo, String tipoElemento) {
        this(parent, titulo, tipoElemento, null);
    }

    public DialogoAgregarElemento(MainFrame parent, String titulo, String tipoElemento, ElementoBiblioteca elementoExistente) {
        super(parent, titulo, true);
        this.parent = parent;
        this.tipoElemento = tipoElemento;
        this.elementoExistente = elementoExistente;

        inicializarComponentes();
        configurarVentana();
    }

    private void inicializarComponentes() {
        // Panel principal con GridBagLayout para mayor flexibilidad
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inicializar campos comunes
        JLabel lblTitulo = new JLabel("Título:");
        txtTitulo = new JTextField(30);

        JLabel lblAutor = new JLabel("Autor:");
        txtAutor = new JTextField(30);

        JLabel lblAno = new JLabel("Año:");
        int anoActual = Calendar.getInstance().get(Calendar.YEAR);
        spnAno = new JSpinner(new SpinnerNumberModel(anoActual, 1900, anoActual, 1));

        // Agregar campos comunes
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(lblAutor, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtAutor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(lblAno, gbc);

        gbc.gridx = 1;
        panel.add(spnAno, gbc);

        // Agregar campos específicos según el tipo de elemento
        int row = 3;

        if (tipoElemento.equals("LIBRO")) {
            agregarCamposLibro(panel, gbc, row);
        } else if (tipoElemento.equals("REVISTA")) {
            agregarCamposRevista(panel, gbc, row);
        } else if (tipoElemento.equals("DVD")) {
            agregarCamposDVD(panel, gbc, row);
        }

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnAceptar);
        panelBotones.add(btnCancelar);

        // Configurar acciones de botones
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarCampos()) {
                    guardarDatos();
                    dispose();
                }
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultado = null;
                dispose();
            }
        });

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panel, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cargar datos si es edición
        if (elementoExistente != null) {
            cargarDatosElemento();
        }

        setContentPane(panelPrincipal);
    }

    private void agregarCamposLibro(JPanel panel, GridBagConstraints gbc, int row) {
        JLabel lblISBN = new JLabel("ISBN:");
        txtISBN = new JTextField(20);

        JLabel lblPaginas = new JLabel("Páginas:");
        spnPaginas = new JSpinner(new SpinnerNumberModel(100, 1, 9999, 1));

        JLabel lblGenero = new JLabel("Género:");
        txtGeneroLibro = new JTextField(20);

        JLabel lblEditorial = new JLabel("Editorial:");
        txtEditorial = new JTextField(20);

        // Agregar campos
        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(lblISBN, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtISBN, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 1;
        panel.add(lblPaginas, gbc);

        gbc.gridx = 1;
        panel.add(spnPaginas, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(lblGenero, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtGeneroLibro, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(lblEditorial, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtEditorial, gbc);
    }

    private void agregarCamposRevista(JPanel panel, GridBagConstraints gbc, int row) {
        JLabel lblEdicion = new JLabel("Edición:");
        spnEdicion = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

        JLabel lblCategoria = new JLabel("Categoría:");
        txtCategoria = new JTextField(20);

        // Agregar campos
        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(lblEdicion, gbc);

        gbc.gridx = 1;
        panel.add(spnEdicion, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(lblCategoria, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCategoria, gbc);
    }

    private void agregarCamposDVD(JPanel panel, GridBagConstraints gbc, int row) {
        JLabel lblDuracion = new JLabel("Duración (min):");
        spnDuracion = new JSpinner(new SpinnerNumberModel(90, 1, 999, 1));

        JLabel lblGenero = new JLabel("Género:");
        txtGeneroDVD = new JTextField(20);

        // Agregar campos
        gbc.gridx = 0;
        gbc.gridy = row++;
        panel.add(lblDuracion, gbc);

        gbc.gridx = 1;
        panel.add(spnDuracion, gbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(lblGenero, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtGeneroDVD, gbc);
    }

    private void cargarDatosElemento() {
        // Cargar datos comunes
        txtTitulo.setText(elementoExistente.getTitulo());
        txtAutor.setText(elementoExistente.getAutor());
        spnAno.setValue(elementoExistente.getAnoPublicacion());

        // Cargar datos específicos según el tipo
        if (elementoExistente instanceof Libro) {
            Libro libro = (Libro) elementoExistente;
            txtISBN.setText(libro.getIsbn());
            spnPaginas.setValue(libro.getNumeroPaginas());
            txtGeneroLibro.setText(libro.getGenero());
            txtEditorial.setText(libro.getEditorial());
        } else if (elementoExistente instanceof Revista) {
            Revista revista = (Revista) elementoExistente;
            spnEdicion.setValue(revista.getNumeroEdicion());
            txtCategoria.setText(revista.getCategoria());
        } else if (elementoExistente instanceof DVD) {
            DVD dvd = (DVD) elementoExistente;
            spnDuracion.setValue(dvd.getDuracion());
            txtGeneroDVD.setText(dvd.getGenero());
        }
    }

    private boolean validarCampos() {
        // Validar campos comunes
        if (txtTitulo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El título no puede estar vacío",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtAutor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El autor no puede estar vacío",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar campos específicos según el tipo
        if (tipoElemento.equals("LIBRO")) {
            if (txtISBN.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El ISBN no puede estar vacío",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (txtGeneroLibro.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El género no puede estar vacío",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (txtEditorial.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La editorial no puede estar vacía",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if (tipoElemento.equals("REVISTA")) {
            if (txtCategoria.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La categoría no puede estar vacía",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else if (tipoElemento.equals("DVD")) {
            if (txtGeneroDVD.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El género no puede estar vacío",
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private void guardarDatos() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        int ano = (int) spnAno.getValue();

        // Crear objeto según tipo y si es edición o creación
        if (tipoElemento.equals("LIBRO")) {
            String isbn = txtISBN.getText().trim();
            int paginas = (int) spnPaginas.getValue();
            String genero = txtGeneroLibro.getText().trim();
            String editorial = txtEditorial.getText().trim();

            if (elementoExistente != null) {
                Libro libro = (Libro) elementoExistente;
                libro.setTitulo(titulo);
                libro.setAutor(autor);
                libro.setAnoPublicacion(ano);
                libro.setIsbn(isbn);
                libro.setNumeroPaginas(paginas);
                libro.setGenero(genero);
                libro.setEditorial(editorial);
                resultado = libro;
            } else {
                resultado = new Libro(titulo, autor, ano, isbn, paginas, genero, editorial);
            }
        } else if (tipoElemento.equals("REVISTA")) {
            int edicion = (int) spnEdicion.getValue();
            String categoria = txtCategoria.getText().trim();

            if (elementoExistente != null) {
                Revista revista = (Revista) elementoExistente;
                revista.setTitulo(titulo);
                revista.setAutor(autor);
                revista.setAnoPublicacion(ano);
                revista.setNumeroEdicion(edicion);
                revista.setCategoria(categoria);
                resultado = revista;
            } else {
                resultado = new Revista(titulo, autor, ano, edicion, categoria);
            }
        } else if (tipoElemento.equals("DVD")) {
            int duracion = (int) spnDuracion.getValue();
            String genero = txtGeneroDVD.getText().trim();

            if (elementoExistente != null) {
                DVD dvd = (DVD) elementoExistente;
                dvd.setTitulo(titulo);
                dvd.setAutor(autor);
                dvd.setAnoPublicacion(ano);
                dvd.setDuracion(duracion);
                dvd.setGenero(genero);
                resultado = dvd;
            } else {
                resultado = new DVD(titulo, autor, ano, duracion, genero);
            }
        }
    }

    private void configurarVentana() {
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public ElementoBiblioteca mostrar() {
        setVisible(true);
        return resultado;
    }
}