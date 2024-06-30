package com.ceramica.stock.infraestructura.salida.basededatos.repositorio.adaptadores;

import com.ceramica.stock.infraestructura.salida.basededatos.entidad.StockEntidad;
import com.ceramica.stock.aplicacion.casosdeuso.enums.EstadoStock;
import com.ceramica.stock.infraestructura.salida.basededatos.repositorio.IStockRepositorio;
import com.ceramica.stock.dominio.puertos.salida.IStockRepositorioPuerto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StockRepositorioAdaptador implements IStockRepositorioPuerto {

    @Autowired
    private IStockRepositorio stockRepository;

    @Override
    public long cantidadDeStockDeUnProductoALaVenta(long producto) {
        return this.stockRepository.countByProductoAndEstado(producto, EstadoStock.A_LA_VENTA);
    }

    @Override
    public Optional<StockEntidad> obtenerUnStockDelProductoAlaVenta(long producto) {
        return this.stockRepository.findFirstByProductoAndEstado(producto,EstadoStock.A_LA_VENTA);
    }

    @Override
    public void guardar(StockEntidad stockEntidad) {
        this.stockRepository.save(stockEntidad);
    }
}
