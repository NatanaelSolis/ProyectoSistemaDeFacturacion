package View;

import Controller.ControllerProducto;
import Data.ProductoDAO;
import Models.ModelConexion;
import Models.ModelProducto;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EliminarProductoView {
    private JTextField textFieldEliminarProducto;
    private JButton eliminarButton;
    private JButton salirButton;
    private JPanel MainPanel;
    private InicioView inicioView;

    public JPanel getMainPanel() {
        return MainPanel;
    }



    public EliminarProductoView (InicioView inicioView){

        this.inicioView = inicioView;

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {eliminarProducto();}
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {cerrarPestana();}
        });


    }

    private void eliminarProducto (){
        String id = textFieldEliminarProducto.getText();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(MainPanel, "Por favor, complete el campo solicitado.", "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }

        try {
            int idINT = Integer.parseInt(id);

            ControllerProducto controller = new ControllerProducto(new ProductoDAO(ModelConexion.getConnection()));

            controller.eliminarProducto(idINT);

            controller.cargarInventarioEnTabla(inicioView.getTablaInventario());

            JOptionPane.showMessageDialog(MainPanel, "Producto Eliminado Correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            cerrarPestana();

        }catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(MainPanel, "El ID debe ser un número válido.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(MainPanel, "Error al eliminar producto " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

    }

    private void cerrarPestana(){
        JTabbedPane tabbedPane = (JTabbedPane) MainPanel.getParent();
        tabbedPane.remove(MainPanel);
    }



}
