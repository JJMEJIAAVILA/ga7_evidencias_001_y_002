package com.biblioteca.model.dao;

import com.biblioteca.model.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO extends ElementoBibliotecaDAO<Libro> {

    public LibroDAO() throws SQLException {
        super();
    }

    @Override
    public Libro obtenerPorId(int id) throws SQLException {
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "l.isbn, l.numero_paginas, l.genero, l.editorial " +
                "FROM ElementoBiblioteca e " +
                "JOIN Libro l ON e.id = l.id " +
                "WHERE e.id = ? AND e.tipo = 'LIBRO'";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getString("isbn"),
                            rs.getInt("numero_paginas"),
                            rs.getString("genero"),
                            rs.getString("editorial")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Libro> obtenerTodos() throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "l.isbn, l.numero_paginas, l.genero, l.editorial " +
                "FROM ElementoBiblioteca e " +
                "JOIN Libro l ON e.id = l.id " +
                "WHERE e.tipo = 'LIBRO'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                libros.add(new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacion"),
                        rs.getString("isbn"),
                        rs.getInt("numero_paginas"),
                        rs.getString("genero"),
                        rs.getString("editorial")
                ));
            }
        }
        return libros;
    }

    @Override
    public boolean insertar(Libro libro) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Inserta en la tabla base y obtiene el ID generado
            int id = insertarElementoBase(libro);
            libro.setId(id);

            // Inserta en la tabla Libro
            String query = "INSERT INTO Libro (id, isbn, numero_paginas, genero, editorial) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setString(2, libro.getIsbn());
                stmt.setInt(3, libro.getNumeroPaginas());
                stmt.setString(4, libro.getGenero());
                stmt.setString(5, libro.getEditorial());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    @Override
    public boolean actualizar(Libro libro) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Actualiza la tabla base
            actualizarElementoBase(libro);

            // Actualiza la tabla Libro
            String query = "UPDATE Libro SET isbn = ?, numero_paginas = ?, genero = ?, editorial = ? WHERE id = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setString(1, libro.getIsbn());
                stmt.setInt(2, libro.getNumeroPaginas());
                stmt.setString(3, libro.getGenero());
                stmt.setString(4, libro.getEditorial());
                stmt.setInt(5, libro.getId());

                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    conexion.commit();
                    return true;
                } else {
                    conexion.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    public List<Libro> buscarPorTitulo(String titulo) throws SQLException {
        List<Libro> libros = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "l.isbn, l.numero_paginas, l.genero, l.editorial " +
                "FROM ElementoBiblioteca e " +
                "JOIN Libro l ON e.id = l.id " +
                "WHERE e.tipo = 'LIBRO' AND e.titulo LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    libros.add(new Libro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getString("isbn"),
                            rs.getInt("numero_paginas"),
                            rs.getString("genero"),
                            rs.getString("editorial")
                    ));
                }
            }
        }
        return libros;
    }
}