package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado;

import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadoMensajeFactoria {

    private final Map<EstadoChat, EstadoMensaje> estados;

    public EstadoMensajeFactoria(List<EstadoMensaje> listaEstados) {
        this.estados = new HashMap<>();
        for (EstadoMensaje estado : listaEstados) {
            estados.put(estado.obtenerEstado(), estado);
        }
    }

    public EstadoMensaje obtenerEstado(EstadoChat estado) {
        return estados.get(estado);
    }

}
