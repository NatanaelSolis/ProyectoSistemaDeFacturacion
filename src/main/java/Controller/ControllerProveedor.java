package Controller;

import Models.ModelProveedor;
import Data.ProveedorDAO;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerProveedor {
    private ProveedorDAO proveedorDAO;

    public ControllerProveedor(ProveedorDAO proveedorDAO) {
        this.proveedorDAO = proveedorDAO;
    }

    public void agregarProveedor(ModelProveedor proveedor) {
        try {
            proveedorDAO.agregarProveedor(proveedor);
            JOptionPane.showMessageDialog(null, "Proveedor agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelProveedor> obtenerTodosProveedores() {
        try {
            return proveedorDAO.obtenerTodosProveedores();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener proveedores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarProveedor(ModelProveedor proveedor) {
        try {
            proveedorDAO.actualizarProveedor(proveedor);
            JOptionPane.showMessageDialog(null, "Proveedor actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarProveedor(int id) {
        try {
            proveedorDAO.eliminarProveedor(id);
            JOptionPane.showMessageDialog(null, "Proveedor eliminado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
