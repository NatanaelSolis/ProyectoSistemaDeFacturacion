package Controller;

import Models.ModelVenta;
import Data.VentaDAO;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerVenta {
    private VentaDAO ventaDAO;

    public ControllerVenta(VentaDAO ventaDAO) {
        this.ventaDAO = ventaDAO;
    }

    public void agregarVenta(ModelVenta venta) {
        try {
            ventaDAO.agregarVenta(venta);
            JOptionPane.showMessageDialog(null, "Venta agregada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelVenta> obtenerTodasVentas() {
        try {
            return ventaDAO.obtenerTodasVentas();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener ventas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarVenta(ModelVenta venta) {
        try {
            ventaDAO.actualizarVenta(venta);
            JOptionPane.showMessageDialog(null, "Venta actualizada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarVenta(int id) {
        try {
            ventaDAO.eliminarVenta(id);
            JOptionPane.showMessageDialog(null, "Venta eliminada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar venta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
