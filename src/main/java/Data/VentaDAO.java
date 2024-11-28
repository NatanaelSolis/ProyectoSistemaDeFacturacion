package Data;

import Models.ModelVenta;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {
    private Connection connection;

    public VentaDAO(Connection connection) {
        this.connection = connection;
    }

    public void agregarVenta(ModelVenta venta) throws SQLException {
        String sql = "INSERT INTO 119420009_Venta (fecha, total, cliente_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, venta.getFecha());
            stmt.setBigDecimal(2, BigDecimal.valueOf(venta.getTotal()));
            stmt.setInt(3, venta.getCliente().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    venta.setId(rs.getInt(1));
                }
            }
        }
    }


    public List<ModelVenta> obtenerTodasVentas() throws SQLException {
        List<ModelVenta> ventas = new ArrayList<>();
        String query = "SELECT * FROM 119420009_Venta";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                ModelVenta venta = new ModelVenta(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getDouble("total"),
                        null
                );
                ventas.add(venta);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener las ventas: " + e.getMessage(), e);
        }
        return ventas;
    }

    public ModelVenta obtenerVentaPorId(int id) throws SQLException {
        String query = "SELECT * FROM 119420009_Venta WHERE id = ?";
        ModelVenta venta = null;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                venta = new ModelVenta(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getDouble("total"),
                        null
                );
            }
        }
        return venta;
    }

    public void actualizarVenta(ModelVenta venta) throws SQLException {
        String query = "UPDATE 119420009_Venta SET fecha = ?, total = ?, cliente_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, new java.sql.Date(venta.getFecha().getTime()));
            stmt.setDouble(2, venta.getTotal());
            stmt.setInt(3, venta.getCliente().getId());
            stmt.setInt(4, venta.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la venta: " + e.getMessage(), e);
        }
    }

    public void eliminarVenta(int id) throws SQLException {
        String query = "DELETE FROM 119420009_Venta WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}