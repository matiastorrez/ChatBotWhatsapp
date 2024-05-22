package com.ceramica.repository.custom.impl;

import com.ceramica.entity.DiaDisponible;
import com.ceramica.repository.IDiaDisponibleRepository;
import com.ceramica.repository.custom.IDiaDisponibleRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DiaDisponibleRepositoryCustomImpl implements IDiaDisponibleRepositoryCustom {

    @Autowired
    private IDiaDisponibleRepository diaDisponibleRepository;

    @Override
    public List<DiaDisponible> obtenerDiasDisponibles() {
        return this.diaDisponibleRepository.findAll();
    }
}
