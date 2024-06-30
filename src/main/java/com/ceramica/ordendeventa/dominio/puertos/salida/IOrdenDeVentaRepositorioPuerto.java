package com.ceramica.ordendeventa.dominio.puertos.salida;

import com.ceramica.ordendeventa.infraestructura.salida.basededatos.entidad.OrdenDeVentaEntidad;

public interface IOrdenDeVentaRepositorioPuerto {

    void guardar(OrdenDeVentaEntidad ordenDeVentaEntidad);
}
