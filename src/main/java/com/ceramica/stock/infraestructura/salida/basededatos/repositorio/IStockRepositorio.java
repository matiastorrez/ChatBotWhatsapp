package com.ceramica.stock.infraestructura.salida.basededatos.repositorio;

import com.ceramica.stock.infraestructura.salida.basededatos.entidad.StockEntidad;
import com.ceramica.stock.aplicacion.casosdeuso.enums.EstadoStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStockRepositorio extends JpaRepository<StockEntidad,Long> {

    Optional<StockEntidad> findFirstByProductoAndEstado(Long producto, EstadoStock estado);

    long countByProductoAndEstado(Long producto, EstadoStock estado);

}
