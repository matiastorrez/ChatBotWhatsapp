package com.ceramica.stock.dominio.puertos.salida;

import com.ceramica.stock.infraestructura.salida.basededatos.entidad.StockEntidad;

import java.util.Optional;

public interface IStockRepositorioPuerto {

    long cantidadDeStockDeUnProductoALaVenta(long producto);

    Optional<StockEntidad> obtenerUnStockDelProductoAlaVenta(long producto);

    void guardar(StockEntidad stockEntidad);

}
