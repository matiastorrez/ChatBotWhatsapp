package com.ceramica.repository;

import com.ceramica.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProductoRepository extends JpaRepository<Producto,Long> {

    List<Producto> findByHabilitadoTrue();

    Optional<Producto> findByNombre(String nombre);
}
