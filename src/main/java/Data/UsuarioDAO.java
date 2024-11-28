package Data;

import Models.ModelUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarUsuario(ModelUsuario usuario) throws SQLException {
        String query = "INSERT INTO 119420009_Usuario (nombre, correo, telefono, rol, password) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getRol().name());
            stmt.setString(5, usuario.getPassword());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el usuario: " + e.getMessage(), e);
        }
    }

    public List<ModelUsuario> obtenerTodosUsuarios() throws SQLException {
        List<ModelUsuario> usuarios = new ArrayList<>();
        String query = "SELECT * FROM 119420009_Usuario";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ModelUsuario usuario = new ModelUsuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        ModelUsuario.Rol.valueOf(rs.getString("rol")),
                        rs.getString("password")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    public ModelUsuario obtenerUsuarioPorId(int id) throws SQLException {
        String query = "SELECT * FROM 119420009_Usuario WHERE id = ?";
        ModelUsuario usuario = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario = new ModelUsuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        ModelUsuario.Rol.valueOf(rs.getString("rol")),
                        rs.getString("password")
                );
            }
        }
        return usuario;
    }

    public void actualizarUsuario(ModelUsuario usuario) throws SQLException {
        String query = "UPDATE 119420009_Usuario SET nombre = ?, correo = ?, telefono = ?, rol = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getCorreo());
            stmt.setString(3, usuario.getTelefono());
            stmt.setString(4, usuario.getRol().name());
            stmt.setString(5, usuario.getPassword());
            stmt.setInt(6, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    public void eliminarUsuario(int id) throws SQLException {
        String query = "DELETE FROM 119420009_Usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}