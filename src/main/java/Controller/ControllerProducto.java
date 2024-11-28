package Controller;

import Models.ModelConexion;
import Models.ModelProducto;
import Data.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerProducto {
    private ProductoDAO productoDAO;

    public ControllerProducto(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public void agregarProducto(ModelProducto producto) {
        try {
            productoDAO.agregarProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelProducto> obtenerTodosProductos() {
        try {
            return productoDAO.obtenerTodosProductos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarProducto(ModelProducto producto) {
        try {
            productoDAO.actualizarProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarProducto(int id) {
        try {
            productoDAO.eliminarProducto(id);
            JOptionPane.showMessageDialog(null, "Producto eliminado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarInventarioEnTabla(JTable tabla) {
        ProductoDAO productoDAO = new ProductoDAO(ModelConexion.getConnection());
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Categoría");

        try {
            List<ModelProducto> productos = productoDAO.obtenerTodosProductos();
            for (ModelProducto producto : productos) {
                modelo.addRow(new Object[]{
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getStock(),
                        producto.getCategoria()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el inventario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        tabla.setModel(modelo);
    }

    public void actualizarProductoStock(int id, int cantidadVendida) {
        try {
            ProductoDAO productoDAO = new ProductoDAO(ModelConexion.getConnection());
            productoDAO.actualizarStock(id, cantidadVendida);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el stock: " + e.getMessage());
        }
    }


}