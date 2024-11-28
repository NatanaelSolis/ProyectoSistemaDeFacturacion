package Models;

public class ModelDetalleVenta {

    private Integer id;
    private ModelVenta venta;
    private ModelProducto producto;
    private int cantidad;
    private double precioUnitario;

    public ModelDetalleVenta(int id, ModelVenta venta, ModelProducto producto, int cantidad, double precioUnitario) {
        this.id = id;
        this.venta = venta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public ModelDetalleVenta(int id, int ventaId, int productoId, int cantidad, double precioUnitario) {
        this.id = id;
        this.venta = new ModelVenta();
        this.venta.setId(ventaId);
        this.producto = new ModelProducto();
        this.producto.setId(productoId);
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ModelVenta getVenta() {
        return venta;
    }

    public void setVenta(ModelVenta venta) {
        this.venta = venta;
    }

    public int getVentaId() {
        return this.venta.getId();
    }

    public ModelProducto getProducto() {
        return producto;
    }

    public void setProducto(ModelProducto producto) {
        this.producto = producto;
    }

    public int getProductoId() {
        return this.producto.getId();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public String toString() {
        return "ModelDetalleVenta{" +
                "id=" + id +
                ", venta=" + venta +
                ", producto=" + producto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}
