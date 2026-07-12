package com.brandom.mipaginaweb.model;

import jakarta.persistence.*;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreProducto;
    private double precio;
    private double costo;
    private String talla;
    private int cantidad;
    private String estado;
    private String fecha;

    public Pedido() {}

    public Pedido(String nombreProducto, double precio, double costo, String talla, int cantidad, String estado, String fecha) {
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.costo = costo;
        this.talla = talla;
        this.cantidad = cantidad;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getGanancia() {
        return (precio - costo) * cantidad;
    }

    public double getTotal() {
        return precio * cantidad;
    }
}
