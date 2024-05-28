package com.ceramica.patrones.chat.state;

import com.ceramica.entity.WhatsappMessage;
import com.ceramica.patrones.chat.state.enums.EstadoChat;
import com.ceramica.patrones.chat.state.enums.Opciones;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.Value;
import com.ceramica.whatsappclass.templates.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.Body;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EstadoPrimerMensaje extends EstadoMensaje {
    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.PRIMER_MENSAJE;
    }

    @Override
    public void procesarInformacion(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage) {
        Value value = receiveMessage.getEntry().get(0).getChanges().get(0).getValue();
        if (!value.getMessages().get(0).getType().equals("text")) {
            throw new RuntimeException("Buenas, para comuncarte con nosotros debes enviarnos un texto, y no otro tipo de mensaje");
        }

    }

    @Override
    public void enviarRespuestas(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage) {
        this.procesarInformacion(receiveMessage, whatsappMessage);
        SendButtonMessage respuesta = this.creandoMensajeDeRespuesta(whatsappMessage);
        try {
            ResponseSendingMessage response = restClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(respuesta)
                    .retrieve()
                    .body(ResponseSendingMessage.class);

            whatsappMessage.setEstado(EstadoChat.INICIO_CONVERSACION);
            whatsappMessage.setContextWaForNextResponse(response.getMessages().get(0).getId());

            super.whatsappRepositoryCustom.guardar(whatsappMessage);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }
    }


    private SendButtonMessage creandoMensajeDeRespuesta(WhatsappMessage whatsappMessage) {

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

        String telefono = whatsappMessage.getTelefonoWa();

        // Crear la instancia de WhatsAppMessage
        SendButtonMessage message = new SendButtonMessage();
        message.setInteractive(interactive);
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setTo(telefono);
        message.setType("interactive");

        return message;
    }
}
