package Models;

import java.sql.Date;

public class ModelFactura {
    private Integer id;
    private ModelVenta venta;
    private Date fechaEmision;
    private Double total;

    public ModelFactura() {
    }

    public ModelFactura(Integer id, ModelVenta venta, Date fechaEmision, Double total) {
        this.id = id;
        this.venta = venta;
        this.fechaEmision = fechaEmision;
        this.total = total;
    }

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

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ModelFactura{" +
                "id=" + id +
                ", venta=" + venta +
                ", fechaEmision=" + fechaEmision +
                ", total=" + total +
                '}';
    }
}
