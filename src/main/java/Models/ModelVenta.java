package Models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ModelVenta {
    private Integer id;
    private Date fecha;
    private Double total;
    private ModelCliente cliente;
    private List<ModelDetalleVenta> detallesVenta;
    public ModelVenta() {
        detallesVenta = new ArrayList<>();
    }

    public ModelVenta(Integer id, Date fecha, Double total, ModelCliente cliente) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
        this.detallesVenta = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ModelCliente getCliente() {
        return cliente;
    }

    public void setCliente(ModelCliente cliente) {
        this.cliente = cliente;
    }

    public List<ModelDetalleVenta> getDetallesVenta() {
        return detallesVenta;
    }

    public void setDetallesVenta(List<ModelDetalleVenta> detallesVenta) {
        this.detallesVenta = detallesVenta;
    }

    @Override
    public String toString() {
        return "ModelVenta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", total=" + total +
                ", cliente=" + cliente +
                '}';
    }
}
