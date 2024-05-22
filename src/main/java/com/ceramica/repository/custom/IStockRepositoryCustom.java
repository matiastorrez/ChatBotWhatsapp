package com.ceramica.repository.custom;

import com.ceramica.entity.Stock;

import java.util.Optional;

public interface IStockRepositoryCustom {

    long cantidadDeStockDeUnProductoALaVenta(long producto);

    Optional<Stock> obtenerUnStockDelProductoAlaVenta(long producto);

    void guardar(Stock stock);

}
