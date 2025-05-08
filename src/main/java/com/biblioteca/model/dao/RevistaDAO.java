package com.biblioteca.model.dao;

import com.biblioteca.model.Revista;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RevistaDAO extends ElementoBibliotecaDAO<Revista> {

    public RevistaDAO() throws SQLException {
        super();
    }

    @Override
    public Revista obtenerPorId(int id) throws SQLException {
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "r.numero_edicion, r.categoria " +
                "FROM ElementoBiblioteca e " +
                "JOIN Revista r ON e.id = r.id " +
                "WHERE e.id = ? AND e.tipo = 'REVISTA'";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Revista(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("numero_edicion"),
                            rs.getString("categoria")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Revista> obtenerTodos() throws SQLException {
        List<Revista> revistas = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "r.numero_edicion, r.categoria " +
                "FROM ElementoBiblioteca e " +
                "JOIN Revista r ON e.id = r.id " +
                "WHERE e.tipo = 'REVISTA'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                revistas.add(new Revista(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("ano_publicacion"),
                        rs.getInt("numero_edicion"),
                        rs.getString("categoria")
                ));
            }
        }
        return revistas;
    }

    @Override
    public boolean insertar(Revista revista) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Inserta en la tabla base y obtiene el ID generado
            int id = insertarElementoBase(revista);
            revista.setId(id);

            // Inserta en la tabla Revista
            String query = "INSERT INTO Revista (id, numero_edicion, categoria) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, id);
                stmt.setInt(2, revista.getNumeroEdicion());
                stmt.setString(3, revista.getCategoria());

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
    public boolean actualizar(Revista revista) throws SQLException {
        conexion.setAutoCommit(false);
        try {
            // Actualiza la tabla base
            actualizarElementoBase(revista);

            // Actualiza la tabla Revista
            String query = "UPDATE Revista SET numero_edicion = ?, categoria = ? WHERE id = ?";
            try (PreparedStatement stmt = conexion.prepareStatement(query)) {
                stmt.setInt(1, revista.getNumeroEdicion());
                stmt.setString(2, revista.getCategoria());
                stmt.setInt(3, revista.getId());

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

    public List<Revista> buscarPorCategoria(String categoria) throws SQLException {
        List<Revista> revistas = new ArrayList<>();
        String query = "SELECT e.id, e.titulo, e.autor, e.ano_publicacion, " +
                "r.numero_edicion, r.categoria " +
                "FROM ElementoBiblioteca e " +
                "JOIN Revista r ON e.id = r.id " +
                "WHERE e.tipo = 'REVISTA' AND r.categoria LIKE ?";

        try (PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, "%" + categoria + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    revistas.add(new Revista(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("ano_publicacion"),
                            rs.getInt("numero_edicion"),
                            rs.getString("categoria")
                    ));
                }
            }
        }
        return revistas;
    }
}