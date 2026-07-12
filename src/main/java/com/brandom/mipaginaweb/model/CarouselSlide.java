package com.brandom.mipaginaweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CarouselSlide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String subtitulo;
    private String textoBoton;
    private String enlaceBoton;
    private String badgeTexto;
    private String badgeColor;
    private String gradienteInicio;
    private String gradienteFin;
    private String imagen;
    private Integer orden;
    private Boolean activo = true;

    public CarouselSlide() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }

    public String getTextoBoton() { return textoBoton; }
    public void setTextoBoton(String textoBoton) { this.textoBoton = textoBoton; }

    public String getEnlaceBoton() { return enlaceBoton; }
    public void setEnlaceBoton(String enlaceBoton) { this.enlaceBoton = enlaceBoton; }

    public String getBadgeTexto() { return badgeTexto; }
    public void setBadgeTexto(String badgeTexto) { this.badgeTexto = badgeTexto; }

    public String getBadgeColor() { return badgeColor; }
    public void setBadgeColor(String badgeColor) { this.badgeColor = badgeColor; }

    public String getGradienteInicio() { return gradienteInicio; }
    public void setGradienteInicio(String gradienteInicio) { this.gradienteInicio = gradienteInicio; }

    public String getGradienteFin() { return gradienteFin; }
    public void setGradienteFin(String gradienteFin) { this.gradienteFin = gradienteFin; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}