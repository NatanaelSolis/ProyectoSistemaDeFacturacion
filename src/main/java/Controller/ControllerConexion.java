package Controller;

import Models.ModelConexion;
import View.LoginView2;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ControllerConexion {
    private LoginView2 vista;

    public ControllerConexion(LoginView2 vista) {
        this.vista = vista;
    }

    public void abrirConexion() {
        Connection connection = ModelConexion.getConnection();

        if (connection != null) {
            try {
                connection.close();
                JOptionPane.showMessageDialog(null, "Conexión Establecida.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Conexión presenta errores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo establecer la conexión.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
