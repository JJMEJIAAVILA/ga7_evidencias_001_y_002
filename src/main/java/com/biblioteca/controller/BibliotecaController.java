package com.biblioteca.controller;

import com.biblioteca.model.ElementoBiblioteca;
import com.biblioteca.model.dao.ElementoBibliotecaDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador base abstracto para manejar elementos de biblioteca
 * Implementa funciones comunes como obtener, guardar y eliminar elementos
 * @param <T> Tipo de elemento que extiende de ElementoBiblioteca
 */
public abstract class BibliotecaController<T extends ElementoBiblioteca> {
    protected ElementoBibliotecaDAO<T> dao;

    /**
     * Constructor que recibe el DAO correspondiente
     * @param dao DAO para operaciones de base de datos
     */
    public BibliotecaController(ElementoBibliotecaDAO<T> dao) {
        this.dao = dao;
    }

    /**
     * Obtiene un elemento por su ID
     * @param id ID del elemento a obtener
     * @return El elemento encontrado o null si no existe
     */
    public T obtenerPorId(int id) {
        try {
            return dao.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener el elemento: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todos los elementos
     * @return Lista de elementos o lista vacía si ocurre un error
     */
    public List<T> obtenerTodos() {
        try {
            return dao.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener los elementos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Guarda un elemento (inserta nuevo o actualiza existente)
     * @param elemento Elemento a guardar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean guardar(T elemento) {
        try {
            if (elemento.getId() < 0) {
                return dao.insertar(elemento);
            } else {
                return dao.actualizar(elemento);
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar el elemento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un elemento por su ID
     * @param id ID del elemento a eliminar
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean eliminar(int id) {
        try {
            return dao.eliminar(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar el elemento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}