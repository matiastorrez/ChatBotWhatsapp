package com.ceramica.repository.custom.impl;


import com.ceramica.entity.OrdenDeVenta;
import com.ceramica.repository.IOrdenDeVentaRepository;
import com.ceramica.repository.custom.IOrdenDeVentaRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrdenDeVentaRepositoryCustomImpl implements IOrdenDeVentaRepositoryCustom {

    @Autowired
    private IOrdenDeVentaRepository ordenDeVentaRepository;

    @Override
    public void guardar(OrdenDeVenta ordenDeVenta) {
        this.ordenDeVentaRepository.save(ordenDeVenta);
    }

}
