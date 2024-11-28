package Data;

import Models.ModelCliente;
import Models.ModelFactura;
import Models.ModelProducto;
import Models.ModelVenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    private Connection connection;

    public FacturaDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarFactura(ModelFactura factura) throws SQLException {
        String query = "INSERT INTO 119420009_Factura (venta_id, fecha_emision, total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, factura.getVenta().getId());
            stmt.setDate(2, new java.sql.Date(factura.getFechaEmision().getTime()));
            stmt.setDouble(3, factura.getTotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al agregar la factura: " + e.getMessage(), e);
        }
    }

    public List<ModelFactura> obtenerTodasFacturas() throws SQLException {
        List<ModelFactura> facturas = new ArrayList<>();
        String query = """
        SELECT f.id AS factura_id, f.fecha_emision, f.total, 
               v.id AS venta_id, v.fecha AS venta_fecha, v.total AS venta_total, 
               c.id AS cliente_id, c.nombre AS cliente_nombre, c.correo AS cliente_correo, c.telefono AS cliente_telefono
        FROM 119420009_Factura f
        JOIN 119420009_Venta v ON f.venta_id = v.id
        JOIN 119420009_Cliente c ON v.cliente_id = c.id
    """;

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ModelCliente cliente = new ModelCliente(
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nombre"),
                        rs.getString("cliente_correo"),
                        rs.getString("cliente_telefono")
                );

                ModelVenta venta = new ModelVenta(
                        rs.getInt("venta_id"),
                        rs.getDate("venta_fecha"),
                        rs.getDouble("venta_total"),
                        cliente
                );

                ModelFactura factura = new ModelFactura(
                        rs.getInt("factura_id"),
                        venta,
                        rs.getDate("fecha_emision"),
                        rs.getDouble("total")
                );

                facturas.add(factura);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener las facturas: " + e.getMessage(), e);
        }

        return facturas;
    }



    public ModelFactura obtenerFacturaPorId(int id) throws SQLException {
        String query = "SELECT * FROM 119420009_Factura WHERE id = ?";
        ModelFactura factura = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                factura = new ModelFactura(
                        rs.getInt("id"),
                        null,
                        rs.getDate("fecha_emision"),
                        rs.getDouble("total")
                );
            }
        }
        return factura;
    }

    public void actualizarFactura(ModelFactura factura) throws SQLException {
        String query = "UPDATE 119420009_Factura SET venta_id = ?, fecha_emision = ?, total = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, factura.getVenta().getId());
            stmt.setDate(2, new java.sql.Date(factura.getFechaEmision().getTime()));
            stmt.setDouble(3, factura.getTotal());
            stmt.setInt(4, factura.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la factura: " + e.getMessage(), e);
        }
    }

    public void eliminarFactura(int id) throws SQLException {
        String query = "DELETE FROM 119420009_Factura WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<ModelProducto> obtenerProductosPorFactura(int idFactura) throws SQLException {
        List<ModelProducto> productos = new ArrayList<>();
        String sql = """
        SELECT p.id, p.nombre, dv.cantidad, p.precio 
        FROM 119420009_DetalleVenta dv 
        INNER JOIN 119420009_Producto p ON dv.producto_id = p.id 
        INNER JOIN 119420009_Factura f ON f.venta_id = dv.venta_id
        WHERE f.id = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ModelProducto producto = new ModelProducto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setStock(rs.getInt("cantidad"));
                producto.setPrecio(rs.getDouble("precio"));
                productos.add(producto);
            }
        }
        return productos;
    }





}