package com.biblioteca.view;

import com.biblioteca.controller.LibroController;
import com.biblioteca.model.Libro;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel para la gestión de libros
 * Permite buscar, agregar, editar y eliminar libros
 */
public class PanelLibros extends JPanel {
    private MainFrame parent;
    private LibroController controller;

    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnAgregar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnActualizar;

    // Colores personalizados
    private Color colorPrimario = new Color(59, 89, 152); // Azul oscuro
    private Color colorSecundario = new Color(205, 229, 255); // Azul claro
    private Color colorTextoPrincipal = Color.BLACK;
    private Color colorTextoSecundario = Color.DARK_GRAY;
    private Font fuentePrincipal = new Font("Arial", Font.PLAIN, 14);
    private Font fuenteTitulos = new Font("Arial", Font.BOLD, 16);

    public PanelLibros(MainFrame parent) throws SQLException {
        this.parent = parent;
        this.controller = new LibroController();

        setLayout(new BorderLayout());
        establecerEstilos(); // Aplicar estilos al panel
        inicializarComponentes();
        actualizarTabla(); // Cargar datos iniciales
    }

    private void establecerEstilos() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void inicializarComponentes() {
        // Panel de búsqueda y acciones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(new EmptyBorder(0, 0, 10, 0)); // Espacio inferior

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar por título: ");
        lblBuscar.setFont(fuentePrincipal);
        lblBuscar.setForeground(colorTextoPrincipal);
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(fuentePrincipal);
        btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar);
        btnBuscar.addActionListener(e -> buscarLibros());

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        // Panel de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

        estilizarBoton(btnAgregar);
        estilizarBoton(btnEditar);
        estilizarBoton(btnEliminar);
        estilizarBoton(btnActualizar);

        btnAgregar.addActionListener(e -> agregarLibro());
        btnEditar.addActionListener(e -> editarLibro());
        btnEliminar.addActionListener(e -> eliminarLibro());
        btnActualizar.addActionListener(e -> actualizarTabla());

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnActualizar);

        panelSuperior.add(panelBusqueda, BorderLayout.WEST);
        panelSuperior.add(panelAcciones, BorderLayout.EAST);

        // Tabla de libros
        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Título", "Autor", "Año", "ISBN", "Páginas", "Género", "Editorial"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setFont(fuentePrincipal);
        tablaLibros.setForeground(colorTextoPrincipal);
        tablaLibros.setSelectionBackground(colorSecundario);
        tablaLibros.setSelectionForeground(colorTextoPrincipal);
        tablaLibros.setShowGrid(true);
        tablaLibros.setGridColor(Color.LIGHT_GRAY);
        tablaLibros.getTableHeader().setFont(fuenteTitulos);
        tablaLibros.getTableHeader().setBackground(new Color(240, 240, 240)); // Gris claro para el encabezado
        tablaLibros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)); // Filas alternas
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        tablaLibros.setFillsViewportHeight(true);

        // Configuración de las columnas
        tablaLibros.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaLibros.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tablaLibros.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
        tablaLibros.getColumnModel().getColumn(3).setPreferredWidth(60);  // Año
        tablaLibros.getColumnModel().getColumn(4).setPreferredWidth(100); // ISBN
        tablaLibros.getColumnModel().getColumn(5).setPreferredWidth(60);  // Páginas
        tablaLibros.getColumnModel().getColumn(6).setPreferredWidth(100); // Género
        tablaLibros.getColumnModel().getColumn(7).setPreferredWidth(150); // Editorial

        // Panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(colorSecundario);
        boton.setForeground(colorTextoPrincipal);
        boton.setFont(fuentePrincipal.deriveFont(Font.PLAIN, 13)); // Reducimos un poco el tamaño de la fuente
        boton.setFocusPainted(false);
        boton.setBorder(new LineBorder(Color.GRAY));
        boton.setMargin(new Insets(8, 15, 8, 15)); // Aumentamos el margen (padding)
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Libro> libros = controller.obtenerTodos();
        for (Libro libro : libros) {
            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getAnoPublicacion(),
                    libro.getIsbn(),
                    libro.getNumeroPaginas(),
                    libro.getGenero(),
                    libro.getEditorial()
            });
        }
    }

    private void buscarLibros() {
        String terminoBusqueda = txtBuscar.getText().trim();
        modeloTabla.setRowCount(0);
        List<Libro> libros = controller.buscarPorTitulo(terminoBusqueda);
        for (Libro libro : libros) {
            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getAnoPublicacion(),
                    libro.getIsbn(),
                    libro.getNumeroPaginas(),
                    libro.getGenero(),
                    libro.getEditorial()
            });
        }
    }

    private void agregarLibro() {
        DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Agregar Libro", "LIBRO");
        Libro libro = (Libro) dialogo.mostrar();
        if (libro != null && controller.guardar(libro)) {
            JOptionPane.showMessageDialog(this, "Libro agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
        } else if (libro != null) {
            JOptionPane.showMessageDialog(this, "Error al agregar el libro", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tablaLibros.getValueAt(filaSeleccionada, 0);
            Libro libro = controller.obtenerPorId(id);
            if (libro != null) {
                DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Editar Libro", "LIBRO", libro);
                Libro libroEditado = (Libro) dialogo.mostrar();
                if (libroEditado != null && controller.guardar(libroEditado)) {
                    JOptionPane.showMessageDialog(this, "Libro actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else if (libroEditado != null) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el libro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro para editar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tablaLibros.getValueAt(filaSeleccionada, 0);
            String titulo = (String) tablaLibros.getValueAt(filaSeleccionada, 1);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el libro \"" + titulo + "\"?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION && controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else if (confirmacion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el libro", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un libro para eliminar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }
}