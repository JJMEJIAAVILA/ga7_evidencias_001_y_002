package com.biblioteca.controller;

import com.biblioteca.model.DVD;
import com.biblioteca.model.ElementoBiblioteca;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Revista;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para realizar búsquedas complejas entre
 * diferentes tipos de elementos de la biblioteca
 */
public class BusquedaController {
    private LibroController libroController;
    private RevistaController revistaController;
    private DVDController dvdController;

    /**
     * Constructor que inicializa los controladores específicos
     * @throws SQLException si hay error de conexión
     */
    public BusquedaController() throws SQLException {
        this.libroController = new LibroController();
        this.revistaController = new RevistaController();
        this.dvdController = new DVDController();
    }

    /**
     * Busca elementos por título en toda la biblioteca
     * @param titulo Título o parte del título a buscar
     * @return Lista de elementos encontrados (libros, revistas, DVDs)
     */
    public List<ElementoBiblioteca> buscarPorTitulo(String titulo) {
        List<ElementoBiblioteca> resultado = new ArrayList<>();

        // Agregar libros
        resultado.addAll(libroController.buscarPorTitulo(titulo));

        // Agregar revistas
        resultado.addAll(revistaController.buscarPorTitulo(titulo));

        // Agregar DVDs
        resultado.addAll(dvdController.buscarPorTitulo(titulo));

        return resultado;
    }

    /**
     * Busca elementos por autor en toda la biblioteca
     * @param autor Autor o parte del nombre del autor a buscar
     * @return Lista de elementos encontrados (libros, revistas, DVDs)
     */
    public List<ElementoBiblioteca> buscarPorAutor(String autor) {
        List<ElementoBiblioteca> resultado = new ArrayList<>();

        // Buscar en libros
        for (Libro libro : libroController.obtenerTodos()) {
            if (libro.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                resultado.add(libro);
            }
        }

        // Buscar en revistas
        for (Revista revista : revistaController.obtenerTodos()) {
            if (revista.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                resultado.add(revista);
            }
        }

        // Buscar en DVDs
        for (DVD dvd : dvdController.obtenerTodos()) {
            if (dvd.getAutor().toLowerCase().contains(autor.toLowerCase())) {
                resultado.add(dvd);
            }
        }

        return resultado;
    }

    /**
     * Busca elementos por año de publicación
     * @param anoDesde Año inicial del rango
     * @param anoHasta Año final del rango
     * @return Lista de elementos publicados en ese rango de años
     */
    public List<ElementoBiblioteca> buscarPorAno(int anoDesde, int anoHasta) {
        List<ElementoBiblioteca> resultado = new ArrayList<>();

        // Función para verificar si un elemento está en el rango de años
        List<ElementoBiblioteca> todosElementos = new ArrayList<>();
        todosElementos.addAll(libroController.obtenerTodos());
        todosElementos.addAll(revistaController.obtenerTodos());
        todosElementos.addAll(dvdController.obtenerTodos());

        for (ElementoBiblioteca elemento : todosElementos) {
            int ano = elemento.getAnoPublicacion();
            if (ano >= anoDesde && ano <= anoHasta) {
                resultado.add(elemento);
            }
        }

        return resultado;
    }
}