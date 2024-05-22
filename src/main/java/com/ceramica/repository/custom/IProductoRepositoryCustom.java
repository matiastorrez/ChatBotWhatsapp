package com.ceramica.repository.custom;

import com.ceramica.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoRepositoryCustom {

    List<Producto> obtenerTodosLosProductosHabilitados();

    Optional<Producto> obtenerProductoPorNombre(String nombre);

}
