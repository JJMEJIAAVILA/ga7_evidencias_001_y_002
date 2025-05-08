package com.biblioteca.controller;

import com.biblioteca.model.DVD;
import com.biblioteca.model.dao.DVDDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador específico para manejar DVDs
 * Hereda funcionalidades básicas de BibliotecaController
 * y agrega operaciones específicas para DVDs
 */
public class DVDController extends BibliotecaController<DVD> {
    private DVDDAO dvdDAO;

    /**
     * Constructor que inicializa el DAO de DVDs
     * @throws SQLException si hay error de conexión
     */
    public DVDController() throws SQLException {
        super(new DVDDAO());
        this.dvdDAO = (DVDDAO) this.dao;
    }

    /**
     * Busca DVDs por género
     * @param genero Género o parte del género a buscar
     * @return Lista de DVDs encontrados
     */
    public List<DVD> buscarPorGenero(String genero) {
        try {
            return dvdDAO.buscarPorGenero(genero);
        } catch (SQLException e) {
            System.err.println("Error al buscar DVDs por género: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Método adicional para buscar DVDs por título
     * @param titulo Título o parte del título a buscar
     * @return Lista de DVDs encontrados
     */
    public List<DVD> buscarPorTitulo(String titulo) {
        List<DVD> dvds = obtenerTodos();
        List<DVD> resultado = new ArrayList<>();

        for (DVD dvd : dvds) {
            if (dvd.getTitulo().toLowerCase().contains(titulo.toLowerCase())) {
                resultado.add(dvd);
            }
        }

        return resultado;
    }

    /**
     * Método adicional para buscar DVDs por duración
     * @param duracionMinima Duración mínima en minutos
     * @param duracionMaxima Duración máxima en minutos
     * @return Lista de DVDs con duración en el rango especificado
     */
    public List<DVD> buscarPorDuracion(int duracionMinima, int duracionMaxima) {
        List<DVD> dvds = obtenerTodos();
        List<DVD> resultado = new ArrayList<>();

        for (DVD dvd : dvds) {
            int duracion = dvd.getDuracion();
            if (duracion >= duracionMinima && duracion <= duracionMaxima) {
                resultado.add(dvd);
            }
        }

        return resultado;
    }
}