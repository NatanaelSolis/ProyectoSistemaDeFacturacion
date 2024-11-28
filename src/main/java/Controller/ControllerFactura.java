package Controller;

import Data.DetalleVentaDAO;
import Data.VentaDAO;
import Models.*;
import Data.FacturaDAO;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class ControllerFactura {
    private FacturaDAO facturaDAO;

    public ControllerFactura(FacturaDAO facturaDAO) {
        this.facturaDAO = facturaDAO;
    }

    public void agregarFactura(ModelFactura factura) {
        try {
            FacturaDAO facturaDAO = new FacturaDAO(ModelConexion.getConnection());

            if (factura.getVenta() != null) {
                VentaDAO ventaDAO = new VentaDAO(ModelConexion.getConnection());
                ModelVenta venta = factura.getVenta();
                ventaDAO.agregarVenta(venta);

                factura.getVenta().setId(venta.getId());
            }

            facturaDAO.agregarFactura(factura);

            List<ModelDetalleVenta> detallesVenta = factura.getVenta().getDetallesVenta();
            if (detallesVenta != null && !detallesVenta.isEmpty()) {
                DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAO(ModelConexion.getConnection());
                detalleVentaDAO.agregarDetallesVenta(detallesVenta);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al agregar factura: " + e.getMessage(), e);
        }
    }






    public List<ModelFactura> obtenerTodasFacturas() {
        try {
            return facturaDAO.obtenerTodasFacturas();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener facturas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarFactura(ModelFactura factura) {
        try {
            facturaDAO.actualizarFactura(factura);
            JOptionPane.showMessageDialog(null, "Factura actualizada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarFactura(int id) {
        try {
            facturaDAO.eliminarFactura(id);
            JOptionPane.showMessageDialog(null, "Factura eliminada correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelProducto> obtenerProductosPorFactura(int idFactura) throws SQLException {
        FacturaDAO facturaDAO = new FacturaDAO(ModelConexion.getConnection());
        return facturaDAO.obtenerProductosPorFactura(idFactura);
    }

}