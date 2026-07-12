package com.brandom.mipaginaweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String categoria;
    private double costo;
    private double precio;
    private String imagen;
    private int stock;
    private String tallas;
    private String descripcion;
    private String genero;

    public Producto() {}

    public Producto(String nombre, String categoria, double costo, double precio, String imagen, int stock, String tallas, String descripcion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.costo = costo;
        this.precio = precio;
        this.imagen = imagen;
        this.stock = stock;
        this.tallas = tallas;
        this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getTallas() { return tallas; }
    public void setTallas(String tallas) { this.tallas = tallas; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}