package View;

import javax.swing.*;

import Controller.ControllerProducto;
import Data.ProductoDAO;
import Models.ModelConexion;
import Models.ModelProducto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AgregarProducto {
    private JPanel MainPanel;
    private JTextField textFieldNombre;
    private JFormattedTextField textFieldPrecio;
    private JTextField textFieldStock;
    private JTextField textFieldCategoria;
    private JButton guardarButton;
    private JButton salirButton;

    private InicioView inicioView;


    public AgregarProducto(InicioView inicioView) {
        this.inicioView = inicioView;

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrarPestaña();
            }
        });
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void guardarProducto() {
        String nombre = textFieldNombre.getText();
        String precio = textFieldPrecio.getText();
        String stock = textFieldStock.getText();
        String categoria = textFieldCategoria.getText();

        if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(MainPanel, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precioDouble = Double.parseDouble(precio);
            int stockInt = Integer.parseInt(stock);

            ModelProducto nuevoProducto = new ModelProducto(null, nombre, precioDouble, stockInt, categoria);
            nuevoProducto.setNombre(nombre);
            nuevoProducto.setPrecio(precioDouble);
            nuevoProducto.setStock(stockInt);
            nuevoProducto.setCategoria(categoria);

            ControllerProducto controller = new ControllerProducto(new ProductoDAO(ModelConexion.getConnection()));
            controller.agregarProducto(nuevoProducto);

            controller.cargarInventarioEnTabla(inicioView.getTablaInventario());

            cerrarPestaña();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(MainPanel, "Precio y Stock deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarPestaña() {
        JTabbedPane tabbedPane = (JTabbedPane) MainPanel.getParent();
        tabbedPane.remove(MainPanel);
    }
}

