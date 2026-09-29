package com.todocodeacademy.travelmate.model;

import com.todocodeacademy.travelmate.model.enums.TipoDestino;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "destinos")
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String pais;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoDestino tipo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal temperaturaPromedio;

    @Column(nullable = false)
    private Boolean aptoFamilias;

    @Column(nullable = false)
    private Boolean aptoParejas;

    @Column(nullable = false)
    private Boolean tranquilo;

    @Column(nullable = false, length = 80)
    private String mejorEpoca;

    public Destino() {
    }

    public Destino(
            String nombre,
            String pais,
            String ciudad,
            TipoDestino tipo,
            String descripcion,
            BigDecimal temperaturaPromedio,
            Boolean aptoFamilias,
            Boolean aptoParejas,
            Boolean tranquilo,
            String mejorEpoca
    ) {
        this.nombre = nombre;
        this.pais = pais;
        this.ciudad = ciudad;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.temperaturaPromedio = temperaturaPromedio;
        this.aptoFamilias = aptoFamilias;
        this.aptoParejas = aptoParejas;
        this.tranquilo = tranquilo;
        this.mejorEpoca = mejorEpoca;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public TipoDestino getTipo() {
        return tipo;
    }

    public void setTipo(TipoDestino tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getTemperaturaPromedio() {
        return temperaturaPromedio;
    }

    public void setTemperaturaPromedio(BigDecimal temperaturaPromedio) {
        this.temperaturaPromedio = temperaturaPromedio;
    }

    public Boolean getAptoFamilias() {
        return aptoFamilias;
    }

    public void setAptoFamilias(Boolean aptoFamilias) {
        this.aptoFamilias = aptoFamilias;
    }

    public Boolean getAptoParejas() {
        return aptoParejas;
    }

    public void setAptoParejas(Boolean aptoParejas) {
        this.aptoParejas = aptoParejas;
    }

    public Boolean getTranquilo() {
        return tranquilo;
    }

    public void setTranquilo(Boolean tranquilo) {
        this.tranquilo = tranquilo;
    }

    public String getMejorEpoca() {
        return mejorEpoca;
    }

    public void setMejorEpoca(String mejorEpoca) {
        this.mejorEpoca = mejorEpoca;
    }
}
