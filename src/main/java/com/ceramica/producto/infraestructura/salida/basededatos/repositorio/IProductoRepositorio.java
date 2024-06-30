package com.ceramica.producto.infraestructura.salida.basededatos.repositorio;

import com.ceramica.producto.infraestructura.salida.basededatos.entidad.ProductoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductoRepositorio extends JpaRepository<ProductoEntidad,Long> {

    List<ProductoEntidad> findByHabilitadoTrue();

    Optional<ProductoEntidad> findByNombre(String nombre);
}
