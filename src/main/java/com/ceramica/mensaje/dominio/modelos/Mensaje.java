package com.ceramica.mensaje.dominio.modelos;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class Mensaje {

    private LocalDateTime fechaDeCreacion;

    private String mensajeRecibido;

    private String telefono;



}
