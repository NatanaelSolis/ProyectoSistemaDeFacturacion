package View;

import javax.swing.*;

import Controller.ControllerCliente;
import Controller.ControllerDetalleVenta;
import Controller.ControllerFactura;
import Controller.ControllerProducto;
import Data.ClienteDAO;
import Data.DetalleVentaDAO;
import Data.FacturaDAO;
import Data.ProductoDAO;
import Models.*;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;




public class FacturaView {
    private InicioView inicioView;

    private JPanel MainPanel;
    private JTable tablaProductosFactura;
    private JTextField textFieldBuscarProducto;
    private JButton agregarButton;
    private JButton generarFacturaButton;
    private JButton limpiarFacturaButton;
    private JLabel totalLabel;
    private JTable tablaProductosDisponibles;
    private JButton buscarButton;
    private JComboBox BoxCategoriaProducto;
    private JButton restablecerBusquedaButton;
    private JComboBox comboBoxClientesFactura;
    private JButton actualizarTablaButton;
    private JTextField descuentoField;
    private List<ModelProducto> productosFactura;
    private double total;

    private ModelFactura facturaActual;
    private List<ModelDetalleVenta> detallesActuales;


    public FacturaView(InicioView inicioView) {

        this.inicioView = inicioView;

        inicioView.cargarFacturasEnTabla();


        productosFactura = new ArrayList<>();
        total = 0;

        configurarTablaFactura();
        cargarProductosDisponibles();
        configurarBoxCategoria();
        cargarClientesFactura();


        descuentoField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarTotalConDescuento();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarTotalConDescuento();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarTotalConDescuento();
            }
        });


        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProductoAFactura();
            }
        });

        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFactura();
            }
        });

        limpiarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFactura();
            }
        });

        buscarButton.addActionListener(e -> buscarProducto());
        restablecerBusquedaButton.addActionListener(e -> restablecerBusqueda());

        actualizarTablaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarProductosDisponibles();
            }
        });

    }

    private void mostrarFacturaEnVentana(ModelFactura factura, List<ModelDetalleVenta> detalles) {
        JFrame frame = new JFrame("Factura");
        JTextArea facturaArea = new JTextArea();

        StringBuilder facturaTexto = new StringBuilder();
        facturaTexto.append("Factura ID: ").append(factura.getId()).append("\n");
        facturaTexto.append("Fecha de Emisión: ").append(factura.getFechaEmision()).append("\n");
        facturaTexto.append("Cliente: ").append(factura.getVenta().getCliente().getNombre()).append("\n\n");
        facturaTexto.append("Productos:\n");

        for (ModelDetalleVenta detalle : detalles) {
            facturaTexto.append("Producto: ").append(detalle.getProducto().getNombre()).append("\n");
            facturaTexto.append("Cantidad: ").append(detalle.getCantidad()).append("\n");
            facturaTexto.append("Precio Unitario: ").append(detalle.getPrecioUnitario()).append("\n");
            facturaTexto.append("Subtotal: ").append(detalle.getCantidad() * detalle.getPrecioUnitario()).append("\n");
            facturaTexto.append("-----------------------------------------\n");
        }

        facturaTexto.append("\nTotal: ").append(factura.getTotal()).append("\n");

        facturaArea.setText(facturaTexto.toString());
        facturaArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(facturaArea);
        frame.add(scrollPane);

        JButton exportarPDFButton = new JButton("Exportar a PDF");
        exportarPDFButton.addActionListener(e -> exportarFacturaAPDF(facturaTexto.toString()));
        frame.add(exportarPDFButton, BorderLayout.SOUTH);

        frame.setSize(400, 600);
        frame.setVisible(true);
    }



    private void exportarFacturaAPDF(String facturaTexto) {
        try {
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream("Factura.pdf"));

            document.open();

            document.add(new Paragraph(facturaTexto));

            document.close();

            JOptionPane.showMessageDialog(MainPanel, "Factura exportada a PDF con éxito.");
        } catch (FileNotFoundException | DocumentException e) {
            JOptionPane.showMessageDialog(MainPanel, "Error al exportar la factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void actualizarTotalConDescuento() {
        double descuento = 0.0;
        if (!descuentoField.getText().isEmpty()) {
            try {
                descuento = Double.parseDouble(descuentoField.getText()) / 100;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(MainPanel, "Por favor ingrese un valor numérico para el descuento.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        double totalConDescuento = total - (total * descuento);
        totalLabel.setText("Total con Descuento: $" + totalConDescuento);
    }




    private void configurarTablaFactura() {
        DefaultTableModel modeloFactura = new DefaultTableModel();
        modeloFactura.addColumn("ID");
        modeloFactura.addColumn("Nombre");
        modeloFactura.addColumn("Precio Unitario");
        modeloFactura.addColumn("Cantidad");
        modeloFactura.addColumn("Subtotal");

        tablaProductosFactura.setModel(modeloFactura);
        System.out.println("Tabla de factura configurada correctamente con columnas: " + modeloFactura.getColumnCount());
    }


    private void cargarClientesFactura() {
        comboBoxClientesFactura.removeAllItems();
        ControllerCliente controller = new ControllerCliente(new ClienteDAO(ModelConexion.getConnection()));

        try {
            List<ModelCliente> clientes = controller.obtenerTodosClientes();
            for (ModelCliente cliente : clientes) {
                comboBoxClientesFactura.addItem(cliente.getId() + " - " + cliente.getNombre());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainPanel, "Error al cargar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ModelCliente obtenerClienteSeleccionado() {
        String seleccion = (String) comboBoxClientesFactura.getSelectedItem();
        if (seleccion == null || seleccion.isEmpty()) {
            JOptionPane.showMessageDialog(MainPanel, "Debe seleccionar un cliente para la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int idCliente = Integer.parseInt(seleccion.split(" - ")[0]);
        ControllerCliente controller = new ControllerCliente(new ClienteDAO(ModelConexion.getConnection()));

        try {
            return controller.obtenerTodosClientes().stream()
                    .filter(cliente -> cliente.getId() == idCliente)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainPanel, "Error al obtener cliente seleccionado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void configurarBoxCategoria() {
        BoxCategoriaProducto.removeAllItems();
        BoxCategoriaProducto.addItem("ID");
        BoxCategoriaProducto.addItem("Nombre");
        BoxCategoriaProducto.addItem("Precio");
        BoxCategoriaProducto.addItem("Stock");
        BoxCategoriaProducto.addItem("Categoría");
        BoxCategoriaProducto.addItem("Todos");
    }

    private void buscarProducto() {

        String criterio = textFieldBuscarProducto.getText().trim();
        String categoria = BoxCategoriaProducto.getSelectedItem().toString();

        DefaultTableModel modeloTabla = (DefaultTableModel) tablaProductosDisponibles.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modeloTabla);
        tablaProductosDisponibles.setRowSorter(sorter);

        if (criterio.isEmpty() || categoria.equals("Todos")) {
            sorter.setRowFilter(null);
        } else {
            RowFilter<DefaultTableModel, Object> filtro = null;
            int columna = 0;

            switch (categoria) {
                case "ID":
                    columna = 0;
                    break;
                case "Nombre":
                    columna = 1;
                    break;
                case "Precio":
                    columna = 2;
                    break;
                case "Stock":
                    columna = 3;
                    break;
                case "Categoría":
                    columna = 4;
                    break;
            }

            filtro = RowFilter.regexFilter("(?i)" + criterio, columna);
            sorter.setRowFilter(filtro);
        }
    }

    private void restablecerBusqueda() {
        textFieldBuscarProducto.setText("");
        BoxCategoriaProducto.setSelectedIndex(0);
        buscarProducto();
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void cargarProductosDisponibles() {
        ControllerProducto controller = new ControllerProducto(new ProductoDAO(ModelConexion.getConnection()));
        List<ModelProducto> productos = controller.obtenerTodosProductos();

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Categoría");

        for (ModelProducto producto : productos) {
            modelo.addRow(new Object[]{
                    producto.getId(),
                    producto.getNombre(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getCategoria()
            });
        }

        tablaProductosDisponibles.setModel(modelo);
    }

    private void generarFactura() {
        if (productosFactura.isEmpty()) {
            JOptionPane.showMessageDialog(MainPanel, "No hay productos en la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ModelCliente cliente = obtenerClienteSeleccionado();
        if (cliente == null) {
            return;
        }

        try {
            double descuento = 0.0;
            if (!descuentoField.getText().isEmpty()) {
                descuento = Double.parseDouble(descuentoField.getText()) / 100;
            }
            double totalConDescuento = total - (total * descuento);

            ModelVenta venta = new ModelVenta();
            venta.setFecha(new java.sql.Date(System.currentTimeMillis()));
            venta.setTotal(totalConDescuento);
            venta.setCliente(cliente);

            facturaActual = new ModelFactura();
            facturaActual.setVenta(venta);
            facturaActual.setFechaEmision(new java.sql.Date(System.currentTimeMillis()));
            facturaActual.setTotal(totalConDescuento);

            ControllerFactura controllerFactura = new ControllerFactura(new FacturaDAO(ModelConexion.getConnection()));
            controllerFactura.agregarFactura(facturaActual);

            int ventaId = venta.getId();
            if (ventaId > 0) {
                ControllerDetalleVenta controllerDetalleVenta = new ControllerDetalleVenta(new DetalleVentaDAO(ModelConexion.getConnection()));
                detallesActuales = new ArrayList<>();

                for (ModelProducto producto : productosFactura) {
                    ModelDetalleVenta detalle = new ModelDetalleVenta(
                            0,
                            ventaId,
                            producto.getId(),
                            producto.getCantidad(),
                            producto.getPrecio()
                    );

                    controllerDetalleVenta.agregarDetalleVenta(detalle);
                    detallesActuales.add(detalle);
                }

                mostrarFacturaEnVentana(facturaActual, detallesActuales);
            }

            limpiarFactura();
            cargarProductosDisponibles();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainPanel, "Error al generar factura: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void agregarProductoAFactura() {
        int selectedRow = tablaProductosDisponibles.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MainPanel, "Seleccione un producto para agregar a la factura.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int modelRow = tablaProductosDisponibles.convertRowIndexToModel(selectedRow);

        String cantidadStr = JOptionPane.showInputDialog(MainPanel, "Ingrese la cantidad:", "Cantidad", JOptionPane.PLAIN_MESSAGE);

        if (cantidadStr == null || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(MainPanel, "Debe ingresar una cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(MainPanel, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultTableModel modeloDisponibles = (DefaultTableModel) tablaProductosDisponibles.getModel();
            DefaultTableModel modeloFactura = (DefaultTableModel) tablaProductosFactura.getModel();

            double precioOriginal = (Double) modeloDisponibles.getValueAt(modelRow, 2);

            String nuevoPrecioStr = JOptionPane.showInputDialog(MainPanel, "Precio actual: " + precioOriginal + "\nIngrese un nuevo precio (o deje en blanco para mantener el precio actual):", "Modificar Precio", JOptionPane.PLAIN_MESSAGE);
            double precio = precioOriginal;

            if (nuevoPrecioStr != null && !nuevoPrecioStr.isEmpty()) {
                try {
                    precio = Double.parseDouble(nuevoPrecioStr);
                    if (precio <= 0) {
                        JOptionPane.showMessageDialog(MainPanel, "El precio debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(MainPanel, "Debe ingresar un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            int stockDisponible = (Integer) modeloDisponibles.getValueAt(modelRow, 3);

            if (cantidad > stockDisponible) {
                JOptionPane.showMessageDialog(MainPanel, "La cantidad excede el stock disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double subtotal = precio * cantidad;

            Object[] productoSeleccionado = {
                    modeloDisponibles.getValueAt(modelRow, 0),
                    modeloDisponibles.getValueAt(modelRow, 1),
                    precio,
                    cantidad,
                    subtotal
            };

            modeloFactura.addRow(productoSeleccionado);
            productosFactura.add(new ModelProducto(
                    (Integer) productoSeleccionado[0],
                    (String) productoSeleccionado[1],
                    precio,
                    null,
                    null,
                    cantidad
            ));

            total += subtotal;
            actualizarTotal();

            modeloDisponibles.setValueAt(stockDisponible - cantidad, modelRow, 3);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(MainPanel, "Debe ingresar un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotal() {
        totalLabel.setText("Total: $" + total);
    }

    private void limpiarFactura() {
        productosFactura.clear();
        total = 0;

        DefaultTableModel modeloFactura = (DefaultTableModel) tablaProductosFactura.getModel();
        modeloFactura.setRowCount(0);

        actualizarTotal();
    }

}



