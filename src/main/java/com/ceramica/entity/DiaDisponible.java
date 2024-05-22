package com.ceramica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "DiaDisponible")
@Getter
@Setter
@NoArgsConstructor
public class DiaDisponible {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "dia", nullable = false)
    private String dia;

    @Column(name ="horario_inicio", columnDefinition = "TEXT", nullable = false)
    private String horarioInicio;

    @Column(name ="horario_fin", columnDefinition = "TEXT", nullable = false)
    private String horarioFin;

    @Column(name = "fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaDeCreacion;

    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaDeActualizacion;

}
