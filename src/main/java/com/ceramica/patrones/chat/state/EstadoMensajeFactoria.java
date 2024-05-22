package com.ceramica.patrones.chat.state;

import com.ceramica.patrones.chat.state.enums.EstadoChat;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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
