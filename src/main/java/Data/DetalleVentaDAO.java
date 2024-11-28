package Data;

import Models.ModelDetalleVenta;
import Models.ModelProducto;
import Models.ModelVenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {
    private Connection connection;

    public DetalleVentaDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarDetalleVenta(ModelDetalleVenta detalleVenta) throws SQLException {
        String query = "INSERT INTO 119420009_DetalleVenta (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, detalleVenta.getVentaId());
            stmt.setInt(2, detalleVenta.getProductoId());
            stmt.setInt(3, detalleVenta.getCantidad());
            stmt.setDouble(4, detalleVenta.getPrecioUnitario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar el detalle de venta: " + e.getMessage(), e);
        }
    }




    public void agregarDetallesVenta(List<ModelDetalleVenta> detallesVenta) throws SQLException {
        String query = "INSERT INTO 119420009_DetalleVenta (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (ModelDetalleVenta detalle : detallesVenta) {
                pstmt.setInt(1, detalle.getVenta().getId());
                pstmt.setInt(2, detalle.getProducto().getId());
                pstmt.setInt(3, detalle.getCantidad());
                pstmt.setDouble(4, detalle.getPrecioUnitario());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar los detalles de venta: " + e.getMessage(), e);
        }
    }


    public List<ModelDetalleVenta> obtenerTodosDetallesVenta() throws SQLException {
        List<ModelDetalleVenta> detallesVenta = new ArrayList<>();
        String query = "SELECT * FROM 119420009_DetalleVenta";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int ventaId = rs.getInt("venta_id");
                int productoId = rs.getInt("producto_id");

                VentaDAO ventaDAO = new VentaDAO(connection);
                ProductoDAO productoDAO = new ProductoDAO(connection);

                ModelVenta venta = ventaDAO.obtenerVentaPorId(ventaId);
                ModelProducto producto = productoDAO.obtenerProductoPorId(productoId);

                ModelDetalleVenta detalleVenta = new ModelDetalleVenta(
                        rs.getInt("id"),
                        ventaId,
                        productoId,
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                );


                detallesVenta.add(detalleVenta);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los detalles de venta: " + e.getMessage(), e);
        }
        return detallesVenta;
    }




    public void actualizarDetalleVenta(ModelDetalleVenta detalleVenta) throws SQLException {
        String query = "UPDATE 119420009_DetalleVenta SET venta_id = ?, producto_id = ?, cantidad = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, detalleVenta.getVenta().getId());
            stmt.setInt(2, detalleVenta.getProducto().getId());
            stmt.setDouble(3, detalleVenta.getCantidad());
            stmt.setInt(4, detalleVenta.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el detalle de venta: " + e.getMessage(), e);
        }
    }

    public void eliminarDetalleVenta(int id) throws SQLException {
        String query = "DELETE FROM 119420009_DetalleVenta WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}