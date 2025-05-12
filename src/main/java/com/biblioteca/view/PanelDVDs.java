package com.biblioteca.view;

import com.biblioteca.controller.DVDController;
import com.biblioteca.model.DVD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelDVDs extends JPanel {
    private MainFrame parent;
    private DVDController controller;

    private JTable tablaDVDs;
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

    public PanelDVDs(MainFrame parent) throws SQLException {
        this.parent = parent;
        this.controller = new DVDController();

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
        JLabel lblBuscar = new JLabel("Buscar por género: ");
        lblBuscar.setFont(fuentePrincipal);
        lblBuscar.setForeground(colorTextoPrincipal);
        txtBuscar = new JTextField(20);
        txtBuscar.setFont(fuentePrincipal);
        btnBuscar = new JButton("Buscar");
        estilizarBoton(btnBuscar);
        btnBuscar.addActionListener(e -> buscarDVDs());

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

        btnAgregar.addActionListener(e -> agregarDVD());
        btnEditar.addActionListener(e -> editarDVD());
        btnEliminar.addActionListener(e -> eliminarDVD());
        btnActualizar.addActionListener(e -> actualizarTabla());

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnActualizar);

        panelSuperior.add(panelBusqueda, BorderLayout.WEST);
        panelSuperior.add(panelAcciones, BorderLayout.EAST);

        // Tabla de DVDs
        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Título", "Autor", "Año", "Duración", "Género"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDVDs = new JTable(modeloTabla);
        tablaDVDs.setFont(fuentePrincipal);
        tablaDVDs.setForeground(colorTextoPrincipal);
        tablaDVDs.setSelectionBackground(colorSecundario);
        tablaDVDs.setSelectionForeground(colorTextoPrincipal);
        tablaDVDs.setShowGrid(true);
        tablaDVDs.setGridColor(Color.LIGHT_GRAY);
        tablaDVDs.getTableHeader().setFont(fuenteTitulos);
        tablaDVDs.getTableHeader().setBackground(new Color(240, 240, 240)); // Gris claro para el encabezado
        tablaDVDs.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245)); // Filas alternas
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaDVDs);
        scrollPane.setBorder(new LineBorder(Color.LIGHT_GRAY));
        tablaDVDs.setFillsViewportHeight(true);

        // Configuración de las columnas
        tablaDVDs.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaDVDs.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaDVDs.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaDVDs.getColumnModel().getColumn(3).setPreferredWidth(60);
        tablaDVDs.getColumnModel().getColumn(4).setPreferredWidth(60);
        tablaDVDs.getColumnModel().getColumn(5).setPreferredWidth(150);

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
        List<DVD> dvds = controller.obtenerTodos();
        for (DVD dvd : dvds) {
            modeloTabla.addRow(new Object[]{
                    dvd.getId(),
                    dvd.getTitulo(),
                    dvd.getAutor(),
                    dvd.getAnoPublicacion(),
                    dvd.getDuracion(),
                    dvd.getGenero()
            });
        }
    }

    private void buscarDVDs() {
        String terminoBusqueda = txtBuscar.getText().trim();
        modeloTabla.setRowCount(0);
        List<DVD> dvds = controller.buscarPorGenero(terminoBusqueda);
        for (DVD dvd : dvds) {
            modeloTabla.addRow(new Object[]{
                    dvd.getId(),
                    dvd.getTitulo(),
                    dvd.getAutor(),
                    dvd.getAnoPublicacion(),
                    dvd.getDuracion(),
                    dvd.getGenero()
            });
        }
    }

    private void agregarDVD() {
        DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Agregar DVD", "DVD");
        DVD dvd = (DVD) dialogo.mostrar();
        if (dvd != null && controller.guardar(dvd)) {
            JOptionPane.showMessageDialog(this, "DVD agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
        } else if (dvd != null) {
            JOptionPane.showMessageDialog(this, "Error al agregar el DVD", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarDVD() {
        int filaSeleccionada = tablaDVDs.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tablaDVDs.getValueAt(filaSeleccionada, 0);
            DVD dvd = controller.obtenerPorId(id);
            if (dvd != null) {
                DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Editar DVD", "DVD", dvd);
                DVD dvdEditado = (DVD) dialogo.mostrar();
                if (dvdEditado != null && controller.guardar(dvdEditado)) {
                    JOptionPane.showMessageDialog(this, "DVD actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else if (dvdEditado != null) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar el DVD", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un DVD para editar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarDVD() {
        int filaSeleccionada = tablaDVDs.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (int) tablaDVDs.getValueAt(filaSeleccionada, 0);
            String titulo = (String) tablaDVDs.getValueAt(filaSeleccionada, 1);
            int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el DVD \"" + titulo + "\"?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION && controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "DVD eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else if (confirmacion == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el DVD", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un DVD para eliminar", "Atención", JOptionPane.WARNING_MESSAGE);
        }
    }
}