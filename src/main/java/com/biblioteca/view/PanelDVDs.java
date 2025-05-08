package com.biblioteca.view;

import com.biblioteca.controller.DVDController;
import com.biblioteca.model.DVD;
import javax.swing.*;
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

    public PanelDVDs(MainFrame parent) throws SQLException {
        this.parent = parent;
        this.controller = new DVDController();

        setLayout(new BorderLayout());
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Panel de búsqueda y acciones
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarDVDs());

        panelBusqueda.add(new JLabel("Buscar por género: "));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        // Panel de acciones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");

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
                new Object[][] {},
                new String[] {"ID", "Título", "Autor", "Año", "Duración", "Género"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDVDs = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaDVDs);
        tablaDVDs.setFillsViewportHeight(true);

        // Configuración de la tabla
        tablaDVDs.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaDVDs.getColumnModel().getColumn(1).setPreferredWidth(200); // Título
        tablaDVDs.getColumnModel().getColumn(2).setPreferredWidth(150); // Autor
        tablaDVDs.getColumnModel().getColumn(3).setPreferredWidth(60);  // Año
        tablaDVDs.getColumnModel().getColumn(4).setPreferredWidth(60);  // Duración
        tablaDVDs.getColumnModel().getColumn(5).setPreferredWidth(150); // Género

        // Panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Cargar datos iniciales
        actualizarTabla();
    }

    public void actualizarTabla() {
        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Obtener DVDs y agregarlos a la tabla
        List<DVD> dvds = controller.obtenerTodos();
        for (DVD dvd : dvds) {
            modeloTabla.addRow(new Object[] {
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

        if (terminoBusqueda.isEmpty()) {
            actualizarTabla();
            return;
        }

        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Buscar DVDs y agregarlos a la tabla
        List<DVD> dvds = controller.buscarPorGenero(terminoBusqueda);
        for (DVD dvd : dvds) {
            modeloTabla.addRow(new Object[] {
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

        if (dvd != null) {
            if (controller.guardar(dvd)) {
                JOptionPane.showMessageDialog(this,
                        "DVD agregado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al agregar el DVD",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarDVD() {
        int filaSeleccionada = tablaDVDs.getSelectedRow();

        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un DVD para editar",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tablaDVDs.getValueAt(filaSeleccionada, 0);
        DVD dvd = controller.obtenerPorId(id);

        if (dvd != null) {
            DialogoAgregarElemento dialogo = new DialogoAgregarElemento(parent, "Editar DVD", "DVD", dvd);
            DVD dvdEditado = (DVD) dialogo.mostrar();

            if (dvdEditado != null) {
                if (controller.guardar(dvdEditado)) {
                    JOptionPane.showMessageDialog(this,
                            "DVD actualizado correctamente",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Error al actualizar el DVD",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void eliminarDVD() {
        int filaSeleccionada = tablaDVDs.getSelectedRow();

        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un DVD para eliminar",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tablaDVDs.getValueAt(filaSeleccionada, 0);
        String titulo = (String) tablaDVDs.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el DVD \"" + titulo + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (controller.eliminar(id)) {
                JOptionPane.showMessageDialog(this,
                        "DVD eliminado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar el DVD",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}