package com.biblioteca.controller;

import com.biblioteca.model.Revista;
import com.biblioteca.model.dao.RevistaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador específico para manejar revistas
 * Hereda funcionalidades básicas de BibliotecaController
 * y agrega operaciones específicas para revistas
 */
public class RevistaController extends BibliotecaController<Revista> {
    private RevistaDAO revistaDAO;

    /**
     * Constructor que inicializa el DAO de revistas
     * @throws SQLException si hay error de conexión
     */
    public RevistaController() throws SQLException {
        super(new RevistaDAO());
        this.revistaDAO = (RevistaDAO) this.dao;
    }

    /**
     * Busca revistas por categoría
     * @param categoria Categoría o parte de la categoría a buscar
     * @return Lista de revistas encontradas
     */
    public List<Revista> buscarPorCategoria(String categoria) {
        try {
            return revistaDAO.buscarPorCategoria(categoria);
        } catch (SQLException e) {
            System.err.println("Error al buscar revistas por categoría: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Método adicional para buscar revistas por título
     * @param titulo Título o parte del título a buscar
     * @return Lista de revistas encontradas
     */
    public List<Revista> buscarPorTitulo(String titulo) {
        List<Revista> revistas = obtenerTodos();
        List<Revista> resultado = new ArrayList<>();

        for (Revista revista : revistas) {
            if (revista.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultado.add(revista);
            }
        }

        return resultado;
    }

    /**
     * Método adicional para buscar revistas por número de edición
     * @param edicion Número de edición a buscar
     * @return Lista de revistas con la edición especificada
     */
    public List<Revista> buscarPorEdicion(int edicion) {
        List<Revista> revistas = obtenerTodos();
        List<Revista> resultado = new ArrayList<>();

        for (Revista revista : revistas) {
            if (revista.getNumeroEdicion() == edicion) {
                resultado.add(revista);
            }
        }

        return resultado;
    }
}