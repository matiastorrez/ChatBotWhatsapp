package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado;

import com.ceramica.mensajeria.dominio.puertos.salida.IMensajeriaServicioPuerto;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;

public abstract class EstadoMensaje {

    protected IMensajeRepositorioPuerto mensajeRepositorioPuerto;

    protected IMensajeriaServicioPuerto mensajeriaServicioPuerto;

    public EstadoMensaje(IMensajeRepositorioPuerto mensajeRepositorioPuerto, IMensajeriaServicioPuerto mensajeriaServicioPuerto) {
        this.mensajeRepositorioPuerto = mensajeRepositorioPuerto;
        this.mensajeriaServicioPuerto = mensajeriaServicioPuerto;
    }

    public abstract EstadoChat obtenerEstado();

    public abstract void procesarInformacion(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp);

    public abstract void enviarRespuestas(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp);
}
