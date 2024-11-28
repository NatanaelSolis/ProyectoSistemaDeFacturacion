package Controller;

import Models.ModelDetalleVenta;
import Data.DetalleVentaDAO;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerDetalleVenta {
    private DetalleVentaDAO detalleVentaDAO;

    public ControllerDetalleVenta(DetalleVentaDAO detalleVentaDAO) {
        this.detalleVentaDAO = detalleVentaDAO;
    }

    public void agregarDetalleVenta(ModelDetalleVenta detalleVenta) {
        try {
            detalleVentaDAO.agregarDetalleVenta(detalleVenta);
            JOptionPane.showMessageDialog(null, "Detalle de venta agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar detalle de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelDetalleVenta> obtenerTodosDetallesVenta() {
        try {
            return detalleVentaDAO.obtenerTodosDetallesVenta();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener detalles de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarDetalleVenta(ModelDetalleVenta detalleVenta) {
        try {
            detalleVentaDAO.actualizarDetalleVenta(detalleVenta);
            JOptionPane.showMessageDialog(null, "Detalle de venta actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar detalle de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarDetalleVenta(int id) {
        try {
            detalleVentaDAO.eliminarDetalleVenta(id);
            JOptionPane.showMessageDialog(null, "Detalle de venta eliminado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar detalle de venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}