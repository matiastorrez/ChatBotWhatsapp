package com.ceramica.stock.infraestructura.salida.basededatos.entidad;


import com.ceramica.stock.aplicacion.casosdeuso.enums.EstadoStock;
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
public class StockEntidad {



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
