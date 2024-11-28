package Data;

import Models.ModelProducto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private Connection connection;

    public ProductoDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarProducto(ModelProducto producto) throws SQLException {
        String query = "INSERT INTO 119420009_Producto (nombre, precio, stock, categoria) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setString(4, producto.getCategoria());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el producto: " + e.getMessage(), e);
        }
    }

    public List<ModelProducto> obtenerTodosProductos() throws SQLException {
        List<ModelProducto> productos = new ArrayList<>();
        String query = "SELECT * FROM 119420009_Producto";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ModelProducto producto = new ModelProducto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("categoria")
                );
                productos.add(producto);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los productos: " + e.getMessage(), e);
        }
        return productos;
    }

    public ModelProducto obtenerProductoPorId(int id) throws SQLException {
        String query = "SELECT * FROM 119420009_Producto WHERE id = ?";
        ModelProducto producto = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                producto = new ModelProducto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getString("categoria")
                );
            }
        }
        return producto;
    }

    public void actualizarProducto(ModelProducto producto) throws SQLException {
        String query = "UPDATE 119420009_Producto SET nombre = ?, precio = ?, stock = ?, categoria = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getStock());
            stmt.setString(4, producto.getCategoria());
            stmt.setInt(5, producto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    public void eliminarProducto(int id) throws SQLException {
        String query = "DELETE FROM 119420009_Producto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void actualizarStock(int id, int cantidadVendida) throws SQLException {
        String query = "UPDATE 119420009_Producto SET stock = stock + ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cantidadVendida);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }


}