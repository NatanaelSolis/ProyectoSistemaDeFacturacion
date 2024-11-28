package Controller;

import Models.ModelUsuario;
import Data.UsuarioDAO;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerUsuario {
    private UsuarioDAO usuarioDAO;

    public ControllerUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void agregarUsuario(ModelUsuario usuario) {
        try {
            usuarioDAO.agregarUsuario(usuario);
            JOptionPane.showMessageDialog(null, "Usuario agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelUsuario> obtenerTodosUsuarios() {
        try {
            return usuarioDAO.obtenerTodosUsuarios();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarUsuario(ModelUsuario usuario) {
        try {
            usuarioDAO.actualizarUsuario(usuario);
            JOptionPane.showMessageDialog(null, "Usuario actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarUsuario(int id) {
        try {
            usuarioDAO.eliminarUsuario(id);
            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}