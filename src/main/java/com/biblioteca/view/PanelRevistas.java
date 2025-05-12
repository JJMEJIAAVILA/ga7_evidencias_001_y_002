package com.biblioteca.view;

import com.biblioteca.controller.RevistaController;
import com.biblioteca.model.Revista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelRevistas extends JPanel {
    private MainFrame parent;
    private RevistaController controller;

    private JTable tablaRevistas;
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

    public PanelRevistas(MainFrame parent) throws SQLException {
        this.parent = parent;
        this.controller = new RevistaController();

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
        JLabel lblBuscar = new JLabel("Buscar por categoría: ");
        lblBuscar.setFont(fuentePrincipal);
        lblBuscar.setForeground(colorTextoPrincipal);
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(fuentePrincipal);
        btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar);
        btnBuscar.addActionListener(e -> buscarRevistas());

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

        btnAgregar.addActionListener(e -> agregarRevista());
        btnEditar.addActionListener(e -> editarRevista());
        btnEliminar.addActionListener(e -> eliminarRevista());
        btnActualizar.addActionListener(e -> actualizarTabla());

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnActualizar);

        panelSuperior.add(panelBusqueda, BorderLayout.WEST);
        panelSuperior.add(panelAcciones, BorderLayout.EAST);

        // Tabla de revistas
        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Título", "Autor", "Año", "Edición", "Categoría"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRevistas = new JTable(modeloTabla);
        tablaRevistas.setFont(fuentePrincipal);
        tablaRevistas.setForeground(colorTextoPrincipal);
        tablaRevistas.setSelectionBackground(colorSecundario);
        tablaRevistas.setSelectionForeground(colorTextoPrincipal);
        tablaRevistas.setShowGrid(true);
        tablaRevistas.setGridColor(Color.LIGHT_GRAY);
        tablaRevistas.getTableHeader().setFont(fuenteTitulos);
        tablaRevistas.getTableHeader().setBackground(new Color(240, 240, 240)); // Gris claro para el encabezado
        tablaRevistas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)); // Filas alternas
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaRevistas);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        tablaRevistas.setFillsViewportHeight(true);

        // Configuración de la tabla
        tablaRevistas.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaRevistas.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tablaRevistas.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
        tablaRevistas.getColumnModel().getColumn(3).setPreferredWidth(60);  // Año
        tablaRevistas.getColumnModel().getColumn(4).setPreferredWidth(60);  // Edición
        tablaRevistas.getColumnModel().getColumn(5).setPreferredWidth(150); // Categoría

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
        List<Revista> revistas = controller.obtenerTodos();
        for (Revista revista : revistas) {
            modeloTabla.addRow(new Object[]{
                    revista.getId(),
                    revista.getTitulo(),
                    revista.getAutor(),
                    revista.getAnoPublicacion(),
                    revista.getNumeroEdicion(),
                    revista.getCategoria()
            });
        }
    }

    private void buscarRevistas() {
        String terminoBusqueda = txtBuscar.getText().trim();
        modeloTabla.setRowCount(0);
        List<Revista> revistas = controller.buscarPorCategoria(terminoBusqueda);
        for (Revista revista : revistas) {
            modeloTabla.addRow(new Object[]{
                    revista.getId(),
                    revista.getTitulo(),
                    revista.getAutor(),
                    revista.getAnoPublicacion(),
                    revista.getNumeroEdicion(),
                    revista.getCategoria()
            });
        }
    }

    private void agregarRevista() {
        DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Agregar Revista", "REVISTA");
        Revista revista = (Revista) dialogo.mostrar();
        if (revista != null && controller.guardar(revista)) {
            JOptionPane.showMessageDialog(this, "Revista agregada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
        } else if (revista != null) {
            JOptionPane.showMessageDialog(this, "Error al agregar la revista", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarRevista() {
        int filaSeleccionada = tablaRevistas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tablaRevistas.getValueAt(filaSeleccionada, 0);
            Revista revista = controller.obtenerPorId(id);
            if (revista != null) {
                DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Editar Revista", "REVISTA", revista);
                Revista revistaEditada = (Revista) dialogo.mostrar();
                if (revistaEditada != null && controller.guardar(revistaEditada)) {
                    JOptionPane.showMessageDialog(this, "Revista actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else if (revistaEditada != null) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la revista", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una revista para editar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarRevista() {
        int filaSeleccionada = tablaRevistas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tablaRevistas.getValueAt(filaSeleccionada, 0);
            String titulo = (String) tablaRevistas.getValueAt(filaSeleccionada, 1);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la revista \"" + titulo + "\"?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION && controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Revista eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else if (confirmacion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la revista", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una revista para eliminar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }
}