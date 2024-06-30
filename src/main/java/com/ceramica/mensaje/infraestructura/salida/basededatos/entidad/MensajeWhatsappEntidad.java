package com.ceramica.mensaje.infraestructura.salida.basededatos.entidad;


import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "WhatsappMessage")
@Getter
@Setter
@NoArgsConstructor
public class MensajeWhatsappEntidad {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "fecha_creacion")
    @CreationTimestamp
    private LocalDateTime fechaDeCreacion;

    @Column(name="mensaje_recibido",columnDefinition = "TEXT", nullable = false)
    private String mensajeRecibido;

    @Column(name="id_wa",columnDefinition = "TEXT", nullable = false)
    private String idWhatsapp;

    @Column(name="context_wa_for_next_response", columnDefinition = "TEXT")
    private String contextWaForNextResponse;

    @Column(name="timestamp_wa", length = 75, nullable = false)
    private String timestampWa;

    @Column(name="telefono_wa", length = 75, nullable = false)
    private String telefonoWa;

    @Enumerated(value = EnumType.STRING)
    @Column(name="estado_chat",columnDefinition = "TEXT", nullable = false)
    private EstadoChat estado;

}
