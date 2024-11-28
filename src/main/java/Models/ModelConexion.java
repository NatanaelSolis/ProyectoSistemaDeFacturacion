package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ModelConexion {

    private static final String URL = "jdbc:mysql://45.88.196.5:3306/u484426513_poo324";
    private static final String USER = "u484426513_poo324";
    private static final String PASSWORD = "iH6D?ZF_7AykMRw";

    private static Connection connection = null;

    private ModelConexion() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión establecida.");
            } catch (SQLException e) {
                System.err.println("Error al conectar: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}