package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado;

import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;
import com.ceramica.mensajeria.dominio.puertos.salida.IMensajeriaServicioPuerto;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.Opciones;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.Value;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.Body;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.*;

import java.util.Arrays;

public class EstadoPrimerMensaje extends EstadoMensaje {


    public EstadoPrimerMensaje(IMensajeRepositorioPuerto mensajeRepositorioPuerto, IMensajeriaServicioPuerto mensajeriaServicioPuerto) {
        super(mensajeRepositorioPuerto, mensajeriaServicioPuerto);
    }

    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.PRIMER_MENSAJE;
    }

    @Override
    public void procesarInformacion(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp) {
        Value value = mensajeRecibido.getEntry().get(0).getChanges().get(0).getValue();
        if (!value.getMessages().get(0).getType().equals("text")) {
            throw new RuntimeException("Buenas, para comuncarte con nosotros debes enviarnos un texto, y no otro tipo de mensaje");
        }

    }

    @Override
    public void enviarRespuestas(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp) {
        this.procesarInformacion(mensajeRecibido, mensajeDeWhatsapp);
        SendButtonMessage respuesta = this.creandoMensajeDeRespuesta(mensajeDeWhatsapp);
        try {
            ResponseSendingMessage response = (ResponseSendingMessage) mensajeriaServicioPuerto.enviarRespuesta(respuesta);

            mensajeDeWhatsapp.setEstado(EstadoChat.INICIO_CONVERSACION);
            mensajeDeWhatsapp.setContextWaForNextResponse(response.getMessages().get(0).getId());

            super.mensajeRepositorioPuerto.guardar(mensajeDeWhatsapp);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }
    }


    private SendButtonMessage creandoMensajeDeRespuesta(MensajeWhatsappEntidad mensajeWhatsappEntidad) {

        // Crear la instancia de Reply para los botones
        Reply respuestaVerProductosDelNegocio = new Reply();
        respuestaVerProductosDelNegocio.setId("boton-ver-productos-del-negocio");
        respuestaVerProductosDelNegocio.setTitle(Opciones.VER_PRODUCTOS.getValor());

        Reply respuestaVerInformacionDelNegocio = new Reply();
        respuestaVerInformacionDelNegocio.setId("boton-informacion-del-negocio");
        respuestaVerInformacionDelNegocio.setTitle(Opciones.SOBRE_NOSOTROS.getValor());

        // Crear la instancia de Button
        Button botonVerProductosDelNegocio = new Button();
        botonVerProductosDelNegocio.setReply(respuestaVerProductosDelNegocio);
        botonVerProductosDelNegocio.setType("reply");

        Button botonVerInformacionDelNegocio = new Button();
        botonVerInformacionDelNegocio.setReply(respuestaVerInformacionDelNegocio);
        botonVerInformacionDelNegocio.setType("reply");

        // Crear la instancia de Action
        Action action = new Action();
        action.setButtons(Arrays.asList(botonVerProductosDelNegocio, botonVerInformacionDelNegocio));

        // Crear la instancia de Body
        Body body = new Body();
        body.setText("Hola, somos Negocio S.A, ¿que necesitas saber de nosotros?");

        // Crear la instancia de Interactive
        Interactive interactive = new Interactive();
        interactive.setAction(action);
        interactive.setBody(body);
        interactive.setType("button");

        String telefono = mensajeWhatsappEntidad.getTelefonoWa();

        // Crear la instancia de WhatsAppMessage
        SendButtonMessage mensajeAEnviar = new SendButtonMessage();
        mensajeAEnviar.setInteractive(interactive);
        mensajeAEnviar.setMessagingProduct("whatsapp");
        mensajeAEnviar.setRecipientType("individual");
        mensajeAEnviar.setTo(telefono);
        mensajeAEnviar.setType("interactive");

        return mensajeAEnviar;
    }
}
