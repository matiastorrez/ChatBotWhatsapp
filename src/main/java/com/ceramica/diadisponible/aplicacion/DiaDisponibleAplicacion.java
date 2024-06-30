package com.ceramica.diadisponible.aplicacion;

import com.ceramica.diadisponible.dominio.modelos.DiaDisponible;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DiaDisponibleAplicacion extends DiaDisponible {

    private Long id;

}
