package com.ceramica.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "OrdenDeVenta")
@Getter
@Setter
@NoArgsConstructor
public class OrdenDeVenta {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="telefono_del_comprador", length = 75, nullable = false)
    private String telefonoDelComprador;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name ="descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaDeCreacion;

    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaDeActualizacion;


}
