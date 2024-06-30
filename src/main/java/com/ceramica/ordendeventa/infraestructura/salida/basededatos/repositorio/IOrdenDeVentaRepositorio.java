package com.ceramica.ordendeventa.infraestructura.salida.basededatos.repositorio;

import com.ceramica.ordendeventa.infraestructura.salida.basededatos.entidad.OrdenDeVentaEntidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrdenDeVentaRepositorio extends JpaRepository<OrdenDeVentaEntidad,Long> {
}
