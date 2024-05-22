package com.ceramica.repository.custom.impl;

import com.ceramica.entity.Stock;
import com.ceramica.enums.EstadoStock;
import com.ceramica.repository.IStockRepository;
import com.ceramica.repository.custom.IStockRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StockRepositoryCustomImpl implements IStockRepositoryCustom {

    @Autowired
    private IStockRepository stockRepository;

    @Override
    public long cantidadDeStockDeUnProductoALaVenta(long producto) {
        return this.stockRepository.countByProductoAndEstado(producto, EstadoStock.A_LA_VENTA);
    }

    @Override
    public Optional<Stock> obtenerUnStockDelProductoAlaVenta(long producto) {
        return this.stockRepository.findFirstByProductoAndEstado(producto,EstadoStock.A_LA_VENTA);
    }

    @Override
    public void guardar(Stock stock) {
        this.stockRepository.save(stock);
    }
}
