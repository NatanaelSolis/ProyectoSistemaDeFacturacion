package Controller;

import Models.ModelCliente;
import Data.ClienteDAO;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerCliente {
    private ClienteDAO clienteDAO;

    public ControllerCliente(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void agregarCliente(ModelCliente cliente) {
        try {
            clienteDAO.agregarCliente(cliente);
            JOptionPane.showMessageDialog(null, "Cliente agregado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al agregar Cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<ModelCliente> obtenerTodosClientes() {
        try {
            return clienteDAO.obtenerTodosClientes();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    public void actualizarCliente(ModelCliente cliente) {
        try {
            clienteDAO.actualizarCliente(cliente);
            JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar Cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarCliente(int id) {
        try {
            clienteDAO.eliminarCliente(id);
            JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar Cliente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




}
