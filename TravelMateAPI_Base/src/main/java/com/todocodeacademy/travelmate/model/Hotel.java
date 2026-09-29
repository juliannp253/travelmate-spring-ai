package com.todocodeacademy.travelmate.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hoteles")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 140)
    private String nombre;

    @Column(nullable = false)
    private Integer estrellas;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioNoche;

    @Column(nullable = false)
    private Boolean desayunoIncluido;

    @Column(nullable = false)
    private Boolean pileta;

    @Column(nullable = false)
    private Boolean spa;

    @Column(nullable = false)
    private Boolean aptoNinos;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "destino_id")
    private Destino destino;

    public Hotel() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Integer estrellas) {
        this.estrellas = estrellas;
    }

    public BigDecimal getPrecioNoche() {
        return precioNoche;
    }

    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche;
    }

    public Boolean getDesayunoIncluido() {
        return desayunoIncluido;
    }

    public void setDesayunoIncluido(Boolean desayunoIncluido) {
        this.desayunoIncluido = desayunoIncluido;
    }

    public Boolean getPileta() {
        return pileta;
    }

    public void setPileta(Boolean pileta) {
        this.pileta = pileta;
    }

    public Boolean getSpa() {
        return spa;
    }

    public void setSpa(Boolean spa) {
        this.spa = spa;
    }

    public Boolean getAptoNinos() {
        return aptoNinos;
    }

    public void setAptoNinos(Boolean aptoNinos) {
        this.aptoNinos = aptoNinos;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }
}
