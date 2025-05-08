package com.biblioteca.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConexionBD {
    // Valores por defecto que serán sobrescritos si se encuentra el archivo de propiedades
    private static String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static String USER = "root";
    private static String PASSWORD = "password";

    private static Connection instance;

    // Bloque estático que se ejecuta cuando la clase se carga por primera vez
    static {
        cargarPropiedades();
    }

    private ConexionBD() {
        // Constructor privado para evitar instanciación
    }

    // Método para cargar las propiedades desde config.properties
    private static void cargarPropiedades() {
        Properties props = new Properties();

        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);

                // Actualizar las variables estáticas con los valores del archivo de propiedades
                URL = props.getProperty("db.url", URL);
                USER = props.getProperty("db.user", USER);
                PASSWORD = props.getProperty("db.password", PASSWORD);

                System.out.println("Configuración de base de datos cargada desde config.properties");
            } else {
                System.out.println("No se encontró el archivo config.properties, usando valores por defecto");
            }
        } catch (IOException e) {
            System.err.println("Error al cargar archivo de propiedades: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                // Primero intentamos con variables de entorno
                String dbUrl = System.getenv("DB_URL");
                String dbUser = System.getenv("DB_USER");
                String dbPassword = System.getenv("DB_PASSWORD");

                // Si no hay variables de entorno, usamos los valores de config.properties
                if (dbUrl == null || dbUser == null || dbPassword == null) {
                    dbUrl = URL;
                    dbUser = USER;
                    dbPassword = PASSWORD;
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                System.out.println("Conexión establecida con la base de datos: " + dbUrl);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado", e);
            }
        }
        return instance;
    }

    public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}