package com.todocodeacademy.travelmate.model;

import com.todocodeacademy.travelmate.model.enums.TipoTransporte;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paquetes_turisticos")
public class PaqueteTuristico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Integer cantidadDias;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioPorPersona;

    @Column(nullable = false)
    private Boolean incluyeTraslado;

    @Column(nullable = false)
    private Boolean incluyeDesayuno;

    @Column(nullable = false)
    private Boolean incluyeExcursiones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoTransporte tipoTransporte;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "destino_id")
    private Destino destino;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "paquete_actividad",
            joinColumns = @JoinColumn(name = "paquete_id"),
            inverseJoinColumns = @JoinColumn(name = "actividad_id")
    )
    private List<Actividad> actividades = new ArrayList<>();

    public PaqueteTuristico() {
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

    public Integer getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(Integer cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public BigDecimal getPrecioPorPersona() {
        return precioPorPersona;
    }

    public void setPrecioPorPersona(BigDecimal precioPorPersona) {
        this.precioPorPersona = precioPorPersona;
    }

    public Boolean getIncluyeTraslado() {
        return incluyeTraslado;
    }

    public void setIncluyeTraslado(Boolean incluyeTraslado) {
        this.incluyeTraslado = incluyeTraslado;
    }

    public Boolean getIncluyeDesayuno() {
        return incluyeDesayuno;
    }

    public void setIncluyeDesayuno(Boolean incluyeDesayuno) {
        this.incluyeDesayuno = incluyeDesayuno;
    }

    public Boolean getIncluyeExcursiones() {
        return incluyeExcursiones;
    }

    public void setIncluyeExcursiones(Boolean incluyeExcursiones) {
        this.incluyeExcursiones = incluyeExcursiones;
    }

    public TipoTransporte getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporte tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
}
