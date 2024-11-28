package Data;

import Models.ModelCliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarCliente(ModelCliente cliente) throws SQLException {
        String query = "INSERT INTO 119420009_Cliente (nombre, correo, telefono) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getCorreo());
            stmt.setString(3, cliente.getTelefono());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el cliente: " + e.getMessage(), e);
        }
    }

    public List<ModelCliente> obtenerTodosClientes() throws SQLException {
        List<ModelCliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM 119420009_Cliente";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ModelCliente cliente = new ModelCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    public ModelCliente obtenerClientePorId(int id) throws SQLException {
        String query = "SELECT * FROM 119420009_Cliente WHERE id = ?";
        ModelCliente cliente = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente = new ModelCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
            }
        }
        return cliente;
    }

    public void actualizarCliente(ModelCliente cliente) throws SQLException {
        String query = "UPDATE 119420009_Cliente SET nombre = ?, correo = ?, telefono = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getCorreo());
            stmt.setString(3, cliente.getTelefono());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el cliente: " + e.getMessage(), e);
        }
    }

    public void eliminarCliente(int id) throws SQLException {
        String query = "DELETE FROM 119420009_Cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<ModelCliente> buscarClientes(String termino) throws SQLException {
        List<ModelCliente> clientes = new ArrayList<>();
        String query = "SELECT * FROM 119420009_Cliente WHERE nombre LIKE ? OR correo LIKE ? OR telefono LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + termino + "%");
            stmt.setString(2, "%" + termino + "%");
            stmt.setString(3, "%" + termino + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ModelCliente cliente = new ModelCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar los clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    public boolean existeCorreo(String correo) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM 119420009_Cliente WHERE correo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0;
            }
        }
        return false;
    }

}

