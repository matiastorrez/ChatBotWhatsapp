package com.ceramica.ordendeventa.infraestructura.salida.basededatos.repositorio.adaptadores;


import com.ceramica.ordendeventa.infraestructura.salida.basededatos.entidad.OrdenDeVentaEntidad;
import com.ceramica.ordendeventa.infraestructura.salida.basededatos.repositorio.IOrdenDeVentaRepositorio;
import com.ceramica.ordendeventa.dominio.puertos.salida.IOrdenDeVentaRepositorioPuerto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrdenDeVentaRepositorioAdaptador implements IOrdenDeVentaRepositorioPuerto {

    @Autowired
    private IOrdenDeVentaRepositorio ordenDeVentaRepository;

    @Override
    public void guardar(OrdenDeVentaEntidad ordenDeVentaEntidad) {
        this.ordenDeVentaRepository.save(ordenDeVentaEntidad);
    }

}
