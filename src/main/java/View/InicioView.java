package View;

import Controller.ControllerCliente;
import Controller.ControllerFactura;
import Controller.ControllerProducto;
import Controller.ControllerProveedor;
import Data.ClienteDAO;
import Data.FacturaDAO;
import Data.ProductoDAO;
import Data.ProveedorDAO;
import Models.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.itextpdf.text.Document;

public class InicioView {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTable TablaInventario;
    private JButton buscarProductoButton;
    private JTextField textFieldBuscarInventario;
    private JButton agregarProductoButton;
    private JButton eliminarButtonInventario;
    private JButton restablecerButton;
    private JComboBox filtroComboBox;
    private JTable tableClientes;
    private JTextField textField1;
    private JButton buscarButton;
    private JButton agregarClienteButton;
    private JButton eliminarClienteButton;
    private JTable tableDetallesDeFacturas;
    private JTextField textFieldBuscarFactura;
    private JButton buscarFacturaButton;
    private JButton eliminarFacturaButton;
    private JButton actualizaTablaButton;
    private JTable tableDetallesProductos;
    private JTable tableProvedores;
    private JButton BotonBuscar;
    private JTextField textFieldBuscarProvedores;
    private JButton agregarProvedorButton;
    private JButton eliminarProvedorButton;

    public InicioView() {

        cargarProveedoresEnTabla();

        BotonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProveedor();
            }
        });

        agregarProvedorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProveedor();
            }
        });

        eliminarProvedorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProveedor();
            }
        });

        configurarTablaFacturas();
        cargarFacturasEnTabla();

        configurarTablaClientes();
        mostrarClientes();

        agregarProductoButton.addActionListener(e -> abrirAgregarProductoTab());
        eliminarButtonInventario.addActionListener(e -> abrirEliminarProducto());
        buscarProductoButton.addActionListener(e -> buscarProducto());
        restablecerButton.addActionListener(e -> restablecerTabla());

        filtroComboBox.addItem("Todos");
        filtroComboBox.addItem("ID");
        filtroComboBox.addItem("Nombre");
        filtroComboBox.addItem("Precio");
        filtroComboBox.addItem("Stock");
        filtroComboBox.addItem("Categoría");

        buscarProductoButton.addActionListener(e -> buscarProducto());

        tabbedPane1.addTab("Facturación", new FacturaView(this).getMainPanel());


        agregarClienteButton.addActionListener(e -> agregarCliente());
        eliminarClienteButton.addActionListener(e -> eliminarCliente());
        buscarButton.addActionListener(e -> buscarClientes());


        buscarFacturaButton.addActionListener(e -> buscarFacturas());
        actualizaTablaButton.addActionListener(e -> cargarFacturasEnTabla());
        eliminarFacturaButton.addActionListener(e -> eliminarFactura());

        tableDetallesDeFacturas.setAutoCreateRowSorter(false);

        configurarTablaDetallesProductos();

        tableDetallesDeFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableDetallesDeFacturas.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        int idFactura = Integer.parseInt(tableDetallesDeFacturas.getValueAt(selectedRow, 0).toString());
                        cargarDetallesProductos(idFactura);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Error al cargar detalles: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void cargarProveedoresEnTabla() {
        ControllerProveedor controller = new ControllerProveedor(new ProveedorDAO(ModelConexion.getConnection()));
        List<ModelProveedor> proveedores = controller.obtenerTodosProveedores();

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Contacto");

        for (ModelProveedor proveedor : proveedores) {
            modelo.addRow(new Object[]{
                    proveedor.getId(),
                    proveedor.getNombre(),
                    proveedor.getContacto()
            });
        }

        tableProvedores.setModel(modelo);
    }

    private void buscarProveedor() {
        String criterio = textFieldBuscarProvedores.getText().trim();

        if (criterio.isEmpty()) {
            cargarProveedoresEnTabla();
            return;
        }

        DefaultTableModel modeloTabla = (DefaultTableModel) tableProvedores.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tableProvedores.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filtro = RowFilter.regexFilter("(?i)" + criterio, 1);
        sorter.setRowFilter(filtro);
    }

    private void agregarProveedor() {
        String nombre = JOptionPane.showInputDialog(mainPanel, "Ingrese el nombre del proveedor:", "Agregar Proveedor", JOptionPane.PLAIN_MESSAGE);
        String contacto = JOptionPane.showInputDialog(mainPanel, "Ingrese el contacto del proveedor:", "Agregar Proveedor", JOptionPane.PLAIN_MESSAGE);

        if (nombre == null || nombre.isEmpty() || contacto == null || contacto.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Debe completar todos los campos para agregar un proveedor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ControllerProveedor controller = new ControllerProveedor(new ProveedorDAO(ModelConexion.getConnection()));
            ModelProveedor nuevoProveedor = new ModelProveedor(0, nombre, contacto);
            controller.agregarProveedor(nuevoProveedor);

            JOptionPane.showMessageDialog(mainPanel, "Proveedor agregado correctamente.");
            cargarProveedoresEnTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al agregar el proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProveedor() {
        int selectedRow = tableProvedores.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Seleccione un proveedor para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = tableProvedores.convertRowIndexToModel(selectedRow);
        DefaultTableModel modelo = (DefaultTableModel) tableProvedores.getModel();
        int proveedorId = (int) modelo.getValueAt(modelRow, 0);

        int confirmacion = JOptionPane.showConfirmDialog(mainPanel, "¿Está seguro de eliminar el proveedor seleccionado?", "Eliminar Proveedor", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                ControllerProveedor controller = new ControllerProveedor(new ProveedorDAO(ModelConexion.getConnection()));
                controller.eliminarProveedor(proveedorId);

                JOptionPane.showMessageDialog(mainPanel, "Proveedor eliminado correctamente.");
                cargarProveedoresEnTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainPanel, "Error al eliminar el proveedor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void configurarTablaDetallesProductos() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Producto");
        model.addColumn("Nombre Producto");
        model.addColumn("Cantidad");
        model.addColumn("Precio Unitario");
        model.addColumn("Subtotal");
        tableDetallesProductos.setModel(model);
    }

    private void cargarDetallesProductos(int idFactura) {
        DefaultTableModel model = (DefaultTableModel) tableDetallesProductos.getModel();
        model.setRowCount(0);

        ControllerFactura controller = new ControllerFactura(new FacturaDAO(ModelConexion.getConnection()));

        try {
            List<ModelProducto> productos = controller.obtenerProductosPorFactura(idFactura);

            if (productos.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "No hay productos asociados a esta factura.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (ModelProducto producto : productos) {
                model.addRow(new Object[]{
                        producto.getId(),
                        producto.getNombre(),
                        producto.getStock(),
                        producto.getPrecio(),
                        producto.getStock() * producto.getPrecio()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar productos de la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarTablaFacturas() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Cliente");
        model.addColumn("Fecha de Emisión");
        model.addColumn("Total");

        tableDetallesDeFacturas.setModel(model);
        tableDetallesDeFacturas.getTableHeader().setVisible(true);
        tableDetallesDeFacturas.setAutoCreateRowSorter(true);
    }

    public void cargarFacturasEnTabla() {
        DefaultTableModel model = (DefaultTableModel) tableDetallesDeFacturas.getModel();
        model.setRowCount(0);

        ControllerFactura controller = new ControllerFactura(new FacturaDAO(ModelConexion.getConnection()));
        try {
            List<ModelFactura> facturas = controller.obtenerTodasFacturas();

            System.out.println("Facturas obtenidas: " + facturas.size());

            if (facturas.isEmpty()) {
                System.out.println("No se obtuvieron facturas de la base de datos.");
                return;
            }

            for (ModelFactura factura : facturas) {
                System.out.println("Factura ID: " + factura.getId());
                System.out.println("Fecha Emisión: " + factura.getFechaEmision());
                System.out.println("Total: " + factura.getTotal());

                if (factura.getVenta() != null && factura.getVenta().getCliente() != null) {
                    System.out.println("Cliente: " + factura.getVenta().getCliente().getNombre());
                } else {
                    System.out.println("Relación con cliente no cargada.");
                }

                model.addRow(new Object[]{
                        factura.getId(),
                        factura.getVenta() != null && factura.getVenta().getCliente() != null
                                ? factura.getVenta().getCliente().getNombre()
                                : "Sin cliente",
                        factura.getFechaEmision(),
                        factura.getTotal()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al cargar las facturas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarFacturas() {
        String termino = textFieldBuscarFactura.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tableDetallesDeFacturas.getModel();
        model.setRowCount(0);

        ControllerFactura controller = new ControllerFactura(new FacturaDAO(ModelConexion.getConnection()));
        try {
            List<ModelFactura> facturas = controller.obtenerTodasFacturas().stream()
                    .filter(factura -> String.valueOf(factura.getId()).contains(termino) ||
                            factura.getVenta().getCliente().getNombre().toLowerCase().contains(termino.toLowerCase()))
                    .toList();

            for (ModelFactura factura : facturas) {
                model.addRow(new Object[]{
                        factura.getId(),
                        factura.getVenta().getCliente().getNombre(),
                        factura.getFechaEmision(),
                        factura.getTotal()
                });
            }

            if (facturas.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "No se encontraron facturas con el criterio ingresado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al buscar facturas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarFactura() {
        int filaSeleccionada = tableDetallesDeFacturas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Seleccione una factura para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idFactura = (int) tableDetallesDeFacturas.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(mainPanel, "¿Está seguro de que desea eliminar esta factura?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            ControllerFactura controller = new ControllerFactura(new FacturaDAO(ModelConexion.getConnection()));
            try {
                controller.eliminarFactura(idFactura);
                JOptionPane.showMessageDialog(mainPanel, "Factura eliminada correctamente.");
                cargarFacturasEnTabla();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(mainPanel, "Error al eliminar la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void configurarTablaClientes() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Correo");
        model.addColumn("Teléfono");
        tableClientes.setModel(model);
    }

    private void mostrarClientes() {
        DefaultTableModel model = (DefaultTableModel) tableClientes.getModel();
        model.setRowCount(0);
        ControllerCliente controller = new ControllerCliente(new ClienteDAO(ModelConexion.getConnection()));
        List<ModelCliente> clientes = controller.obtenerTodosClientes();
        for (ModelCliente cliente : clientes) {
            model.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getCorreo(),
                    cliente.getTelefono()
            });
        }
    }

    private void agregarCliente() {

        String nombre = JOptionPane.showInputDialog(mainPanel, "Ingrese el nombre del cliente:");
        String correo = JOptionPane.showInputDialog(mainPanel, "Ingrese el correo del cliente:");
        String telefono = JOptionPane.showInputDialog(mainPanel, "Ingrese el teléfono del cliente:");

        if (nombre == null || nombre.isEmpty() || correo == null || correo.isEmpty() || telefono == null || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ControllerCliente controller = new ControllerCliente(new ClienteDAO(ModelConexion.getConnection()));
        ModelCliente cliente = new ModelCliente(0, nombre, correo, telefono);
        controller.agregarCliente(cliente);
        mostrarClientes();
    }

    private void eliminarCliente() {
        int filaSeleccionada = tableClientes.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(mainPanel, "Seleccione un cliente para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idCliente = (int) tableClientes.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(mainPanel, "¿Está seguro de que desea eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            ControllerCliente controller = new ControllerCliente(new ClienteDAO(ModelConexion.getConnection()));
            controller.eliminarCliente(idCliente);
            mostrarClientes();
        }
    }

    private void buscarClientes() {
        String termino = textField1.getText().trim();
        DefaultTableModel model = (DefaultTableModel) tableClientes.getModel();
        model.setRowCount(0);

        ControllerCliente controller = new ControllerCliente(new ClienteDAO(ModelConexion.getConnection()));
        List<ModelCliente> clientes = new ArrayList<>();
        try {
            clientes = controller.obtenerTodosClientes().stream()
                    .filter(cliente -> cliente.getNombre().toLowerCase().contains(termino.toLowerCase()) ||
                            cliente.getCorreo().toLowerCase().contains(termino.toLowerCase()) ||
                            cliente.getTelefono().contains(termino))
                    .toList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainPanel, "Error al buscar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        for (ModelCliente cliente : clientes) {
            model.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getCorreo(),
                    cliente.getTelefono()
            });
        }

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "No se encontraron clientes con el término ingresado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public JPanel getPanel() {
        return mainPanel;
    }


    public void abrirEliminarProducto() {

        EliminarProductoView eliminarProductoView = new EliminarProductoView(this);

        tabbedPane1.addTab("Eliminar Producto", eliminarProductoView.getMainPanel());

        tabbedPane1.setSelectedComponent(eliminarProductoView.getMainPanel());

    }

    public void abrirAgregarProductoTab() {
        AgregarProducto agregarProductoView = new AgregarProducto(this);

        tabbedPane1.addTab("Agregar Producto", agregarProductoView.getMainPanel());

        tabbedPane1.setSelectedComponent(agregarProductoView.getMainPanel());
    }

    public JTable getTablaInventario() {
        return TablaInventario;
    }

    public void mostrarInventario() {
        if (TablaInventario == null) {
            System.out.println("Error: TablaInventario no está inicializado.");
            return;
        }
        System.out.println("Cargando datos en TablaInventario...");
        ControllerProducto controller = new ControllerProducto(new ProductoDAO(ModelConexion.getConnection()));
        controller.cargarInventarioEnTabla(TablaInventario);
        System.out.println("Datos cargados en TablaInventario.");
    }

    private void restablecerTabla() {
        ControllerProducto controller = new ControllerProducto(new ProductoDAO(ModelConexion.getConnection()));
        controller.cargarInventarioEnTabla(TablaInventario);
        textFieldBuscarInventario.setText("");
    }





    private void buscarProducto() {
        String criterio = (String) filtroComboBox.getSelectedItem();
        String textoBusqueda = textFieldBuscarInventario.getText().trim();

        ControllerProducto controller = new ControllerProducto(new ProductoDAO(ModelConexion.getConnection()));

        List<ModelProducto> productos = controller.obtenerTodosProductos();

        List<ModelProducto> productosFiltrados = switch (criterio) {
            case "ID" -> {
                try {
                    int idBusqueda = Integer.parseInt(textoBusqueda);
                    yield productos.stream()
                            .filter(producto -> producto.getId() == idBusqueda)
                            .toList();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainPanel, "El ID debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    yield List.of();
                }
            }
            case "Nombre" -> productos.stream()
                    .filter(producto -> producto.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase()))
                    .toList();
            case "Precio" -> {
                try {
                    double precioBusqueda = Double.parseDouble(textoBusqueda);
                    yield productos.stream()
                            .filter(producto -> producto.getPrecio() == precioBusqueda)
                            .toList();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainPanel, "El precio debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    yield List.of();
                }
            }
            case "Stock" -> {
                try {
                    int stockBusqueda = Integer.parseInt(textoBusqueda);
                    yield productos.stream()
                            .filter(producto -> producto.getStock() == stockBusqueda)
                            .toList();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainPanel, "El stock debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    yield List.of();
                }
            }
            case "Categoría" -> productos.stream()
                    .filter(producto -> producto.getCategoria().toLowerCase().contains(textoBusqueda.toLowerCase()))
                    .toList();
            case "Todos" -> productos;
            default -> List.of();
        };

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Categoría");

        for (ModelProducto producto : productosFiltrados) {
            modelo.addRow(new Object[]{
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getCategoria()
            });
        }

        TablaInventario.setModel(modelo);

        if (productosFiltrados.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "No se encontraron productos con el criterio seleccionado.", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
