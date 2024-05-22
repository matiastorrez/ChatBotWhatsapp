package com.ceramica.repository;

import com.ceramica.entity.Stock;
import com.ceramica.enums.EstadoStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStockRepository extends JpaRepository<Stock,Long> {

    Optional<Stock> findFirstByProductoAndEstado(Long producto, EstadoStock estado);

    long countByProductoAndEstado(Long producto, EstadoStock estado);

}
