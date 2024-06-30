package com.ceramica.producto.dominio.puertos.salida;

import com.ceramica.producto.infraestructura.salida.basededatos.entidad.ProductoEntidad;

import java.util.List;
import java.util.Optional;

public interface IProductoRepositorioPuerto {

    List<ProductoEntidad> obtenerTodosLosProductosHabilitados();

    Optional<ProductoEntidad> obtenerProductoPorNombre(String nombre);

}
