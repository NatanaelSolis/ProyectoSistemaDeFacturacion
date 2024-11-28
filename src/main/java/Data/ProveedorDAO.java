package Data;

import Models.ModelProveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    private Connection connection;

    public ProveedorDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarProveedor(ModelProveedor proveedor) throws SQLException {
        String query = "INSERT INTO 119420009_Proveedor (nombre, contacto) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getContacto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el proveedor: " + e.getMessage(), e);
        }
    }

    public List<ModelProveedor> obtenerTodosProveedores() throws SQLException {
        List<ModelProveedor> proveedores = new ArrayList<>();
        String query = "SELECT * FROM 119420009_Proveedor";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ModelProveedor proveedor = new ModelProveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contacto")
                );
                proveedores.add(proveedor);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los proveedores: " + e.getMessage(), e);
        }
        return proveedores;
    }

    public ModelProveedor obtenerProveedorPorId(int id) throws SQLException {
        String query = "SELECT * FROM 119420009_Proveedor WHERE id = ?";
        ModelProveedor proveedor = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                proveedor = new ModelProveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("contacto")
                );
            }
        }
        return proveedor;
    }

    public void actualizarProveedor(ModelProveedor proveedor) throws SQLException {
        String query = "UPDATE 119420009_Proveedor SET nombre = ?, contacto = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getContacto());
            stmt.setInt(3, proveedor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el proveedor: " + e.getMessage(), e);
        }
    }

    public void eliminarProveedor(int id) throws SQLException {
        String query = "DELETE FROM 119420009_Proveedor WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}