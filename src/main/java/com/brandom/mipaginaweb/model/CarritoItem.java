package com.brandom.mipaginaweb.model;

public class CarritoItem {
    private Long productoId;
    private String nombre;
    private double precio;
    private String imagen;
    private String talla;
    private int cantidad;

    public CarritoItem() {}

    public CarritoItem(Long productoId, String nombre, double precio, String imagen, String talla, int cantidad) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.talla = talla;
        this.cantidad = cantidad;
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() {
        return precio * cantidad;
    }
}
