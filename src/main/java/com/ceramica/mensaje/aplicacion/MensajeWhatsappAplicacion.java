package com.ceramica.mensaje.aplicacion;

import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;
import com.ceramica.mensaje.dominio.modelos.Mensaje;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MensajeWhatsappAplicacion extends Mensaje {

    private Long id;

    private String idWhatsapp;

    private String contextWaForNextResponse;

    private String timestampWa;

    private EstadoChat estado;

}
