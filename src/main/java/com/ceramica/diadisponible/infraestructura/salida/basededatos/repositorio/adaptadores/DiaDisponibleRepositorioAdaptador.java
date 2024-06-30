package com.ceramica.diadisponible.infraestructura.salida.basededatos.repositorio.adaptadores;

import com.ceramica.diadisponible.aplicacion.DiaDisponibleAplicacion;
import com.ceramica.diadisponible.dominio.modelos.DiaDisponible;
import com.ceramica.diadisponible.infraestructura.salida.basededatos.entidad.DiaDisponibleEntidad;
import com.ceramica.diadisponible.infraestructura.salida.basededatos.repositorio.IDiaDisponibleRepositorio;
import com.ceramica.diadisponible.dominio.puertos.salida.IDiaDisponibleRepositorioPuerto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DiaDisponibleRepositorioAdaptador implements IDiaDisponibleRepositorioPuerto {

    @Autowired
    private IDiaDisponibleRepositorio diaDisponibleRepository;

    @Override
    public List<DiaDisponible> obtenerDiasDisponibles() {
        return this.aAplicacion(this.diaDisponibleRepository.findAll());
    }


    private DiaDisponibleAplicacion aAplicacion(DiaDisponibleEntidad entidad){
        return DiaDisponibleAplicacion.builder()
                .id(entidad.getId())
                .dia(entidad.getDia())
                .horarioInicio(entidad.getHorarioInicio())
                .horarioFin(entidad.getHorarioFin())
                .fechaDeCreacion(entidad.getFechaDeCreacion())
                .fechaDeActualizacion(entidad.getFechaDeActualizacion())
                .build();
    }

    private List<DiaDisponible> aAplicacion(List<DiaDisponibleEntidad> entidades){

        List<DiaDisponible> diaDisponibleAplicaciones = new ArrayList<>();

        for (DiaDisponibleEntidad diaDisponibleEntidad : entidades){
            DiaDisponibleAplicacion diaDisponibleAplicacion = DiaDisponibleAplicacion.builder()
                    .id(diaDisponibleEntidad.getId())
                    .dia(diaDisponibleEntidad.getDia())
                    .horarioInicio(diaDisponibleEntidad.getHorarioInicio())
                    .horarioFin(diaDisponibleEntidad.getHorarioFin())
                    .fechaDeCreacion(diaDisponibleEntidad.getFechaDeCreacion())
                    .fechaDeActualizacion(diaDisponibleEntidad.getFechaDeActualizacion())
                    .build();

            diaDisponibleAplicaciones.add(diaDisponibleAplicacion);
        }

        return diaDisponibleAplicaciones;
    }
}
