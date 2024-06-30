package com.ceramica.diadisponible.dominio.puertos.salida;

import com.ceramica.diadisponible.dominio.modelos.DiaDisponible;

import java.util.List;

public interface IDiaDisponibleRepositorioPuerto {

    List<DiaDisponible> obtenerDiasDisponibles();
}
