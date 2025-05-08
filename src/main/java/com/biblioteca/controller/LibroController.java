package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.model.dao.LibroDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador específico para manejar libros
 * Hereda funcionalidades básicas de BibliotecaController
 * y agrega operaciones específicas para libros
 */
public class LibroController extends BibliotecaController<Libro> {
    private LibroDAO libroDAO;

    /**
     * Constructor que inicializa el DAO de libros
     * @throws SQLException si hay error de conexión
     */
    public LibroController() throws SQLException {
        super(new LibroDAO());
        this.libroDAO = (LibroDAO) this.dao;
    }

    /**
     * Busca libros por título
     * @param titulo Título o parte del título a buscar
     * @return Lista de libros encontrados
     */
    public List<Libro> buscarPorTitulo(String titulo) {
        try {
            return libroDAO.buscarPorTitulo(titulo);
        } catch (SQLException e) {
            System.err.println("Error al buscar libros por título: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Método adicional para buscar libros por género
     * @param genero Género a buscar
     * @return Lista de libros del género especificado
     */
    public List<Libro> buscarPorGenero(String genero) {
        List<Libro> libros = obtenerTodos();
        List<Libro> resultado = new ArrayList<>();

        for (Libro libro : libros) {
            if (libro.getGenero().toLowerCase().contains(genero.toLowerCase())) {
                resultado.add(libro);
            }
        }

        return resultado;
    }

    /**
     * Método adicional para buscar libros por editorial
     * @param editorial Editorial a buscar
     * @return Lista de libros de la editorial especificada
     */
    public List<Libro> buscarPorEditorial(String editorial) {
        List<Libro> libros = obtenerTodos();
        List<Libro> resultado = new ArrayList<>();

        for (Libro libro : libros) {
            if (libro.getEditorial().toLowerCase().contains(editorial.toLowerCase())) {
                resultado.add(libro);
            }
        }

        return resultado;
    }
}