package com.ceramica.diadisponible.infraestructura.salida.basededatos.repositorio;

import com.ceramica.diadisponible.infraestructura.salida.basededatos.entidad.DiaDisponibleEntidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDiaDisponibleRepositorio extends JpaRepository<DiaDisponibleEntidad, Long> {

}
