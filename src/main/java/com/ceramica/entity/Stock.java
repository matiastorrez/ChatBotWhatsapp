package com.ceramica.entity;


import com.ceramica.enums.EstadoStock;
import com.ceramica.patrones.chat.state.enums.EstadoChat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "Stock")
@Getter
@Setter
@NoArgsConstructor
public class Stock {



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fk_producto", nullable = false)
    private Long producto;

    @Column(name = "telefono_comprador")
    private String telefonoComprador;

    @Enumerated(value = EnumType.STRING)
    @Column(name="estado",columnDefinition = "TEXT", nullable = false)
    private EstadoStock estado;

    @Column(name = "fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaDeCreacion;

    @Column(name = "fecha_actualizacion")
    @UpdateTimestamp
    private LocalDateTime fechaDeActualizacion;

}
