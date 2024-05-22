package com.ceramica.repository.custom.impl;

import com.ceramica.entity.Producto;
import com.ceramica.repository.IProductoRepository;
import com.ceramica.repository.custom.IProductoRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepositoryCustomImpl implements IProductoRepositoryCustom {

    @Autowired
    private IProductoRepository productoRepository;

    @Override
    public List<Producto> obtenerTodosLosProductosHabilitados() {
        return this.productoRepository.findByHabilitadoTrue();
    }

    @Override
    public Optional<Producto> obtenerProductoPorNombre(String nombre) {
        return this.productoRepository.findByNombre(nombre);
    }
}
