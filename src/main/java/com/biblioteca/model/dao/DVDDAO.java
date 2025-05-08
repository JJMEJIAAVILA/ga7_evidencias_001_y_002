package com.biblioteca.model.dao;

import com.biblioteca.model.DVD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DVDDAO extends ElementoBibliotecaDAO<DVD> {

    public DVDDAO() throws SQLException {
        super();
    }

    @Override
    public DVD obtenerPorId(int id) throws SQLException {
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "d.duracion, d.genero " +
                "FROM ElementoBiblioteca e " +
                "JOIN DVD d ON e.id = d.id " +
                "WHERE e.id = ? AND e.tipo = 'DVD'";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new DVD(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("duracion"),
                            rs.getString("genero")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<DVD> obtenerTodos() throws SQLException {
        List<DVD> dvds = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "d.duracion, d.genero " +
                "FROM ElementoBiblioteca e " +
                "JOIN DVD d ON e.id = d.id " +
                "WHERE e.tipo = 'DVD'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                dvds.add(new DVD(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacion"),
                        rs.getInt("duracion"),
                        rs.getString("genero")
                ));
            }
        }
        return dvds;
    }

    @Override
    public boolean insertar(DVD dvd) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Inserta en la tabla base y obtiene el ID generado
            int id = insertarElementoBase(dvd);
            dvd.setId(id);

            // Inserta en la tabla DVD
            String query = "INSERT INTO DVD (id, duracion, genero) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setInt(2, dvd.getDuracion());
                stmt.setString(3, dvd.getGenero());

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
    public boolean actualizar(DVD dvd) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Actualiza la tabla base
            actualizarElementoBase(dvd);

            // Actualiza la tabla DVD
            String query = "UPDATE DVD SET duracion = ?, genero = ? WHERE id = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, dvd.getDuracion());
                stmt.setString(2, dvd.getGenero());
                stmt.setInt(3, dvd.getId());

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

    public List<DVD> buscarPorGenero(String genero) throws SQLException {
        List<DVD> dvds = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "d.duracion, d.genero " +
                "FROM ElementoBiblioteca e " +
                "JOIN DVD d ON e.id = d.id " +
                "WHERE e.tipo = 'DVD' AND d.genero LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, "%" + genero + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dvds.add(new DVD(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("duracion"),
                            rs.getString("genero")
                    ));
                }
            }
        }
        return dvds;
    }
}