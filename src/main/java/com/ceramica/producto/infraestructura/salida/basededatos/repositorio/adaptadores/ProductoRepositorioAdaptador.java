package com.ceramica.producto.infraestructura.salida.basededatos.repositorio.adaptadores;

import com.ceramica.producto.infraestructura.salida.basededatos.entidad.ProductoEntidad;
import com.ceramica.producto.infraestructura.salida.basededatos.repositorio.IProductoRepositorio;
import com.ceramica.producto.dominio.puertos.salida.IProductoRepositorioPuerto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepositorioAdaptador implements IProductoRepositorioPuerto {

    @Autowired
    private IProductoRepositorio productoRepository;

    @Override
    public List<ProductoEntidad> obtenerTodosLosProductosHabilitados() {
        return this.productoRepository.findByHabilitadoTrue();
    }

    @Override
    public Optional<ProductoEntidad> obtenerProductoPorNombre(String nombre) {
        return this.productoRepository.findByNombre(nombre);
    }
}
