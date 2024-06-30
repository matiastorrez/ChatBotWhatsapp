package com.ceramica.diadisponible.dominio.modelos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class DiaDisponible {

    private String dia;

    private String horarioInicio;

    private String horarioFin;

    private LocalDateTime fechaDeCreacion;

    private LocalDateTime fechaDeActualizacion;



}
