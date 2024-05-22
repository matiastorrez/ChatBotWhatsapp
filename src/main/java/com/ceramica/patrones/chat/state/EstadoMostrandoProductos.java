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
public class EstadoMostrandoProductos extends EstadoMensaje {

    //el estado anterior a este es EstadoInicioConversacion

    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.MOSTRANDO_PRODUCTOS;
    }


    @Override
    public void procesarInformacion(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage) {
        Value value = receiveMessage.getEntry().get(0).getChanges().get(0).getValue();
        if (!value.getMessages().get(0).getType().equals("interactive")) {
            throw new RuntimeException("No es la respuesta esperada");
        }

        if(!value.getMessages().get(0).getInteractive().getType().equals("list_reply")){
            throw new RuntimeException("Debes seleccionar una opcion de la lista");
        }

        if (!value.getMessages().get(0).getContext().getId().equals(whatsappMessage.getContextWaForNextResponse())) {
            throw new RuntimeException("Debes responder seleccionando una de las opciones de la lista");
        }
    }

    @Override
    public void enviarRespuestas(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage) {
        try {
            this.procesarInformacion(receiveMessage, whatsappMessage);
            Value value = receiveMessage.getEntry().get(0).getChanges().get(0).getValue();
            String respuesta = value.getMessages().get(0).getInteractive().getListReply().getTitle();
            String telefono = whatsappMessage.getTelefonoWa();

            SendButtonMessage respuestaAEnviar;
            if (respuesta.equals(Opciones.VOLVER_ATRAS.getValor())) {
                respuestaAEnviar = this.enviarMensajeMenuAnterior(telefono);
                whatsappMessage.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            } else {
                respuestaAEnviar = this.enviarMensajeSobreElProducto(telefono);
                whatsappMessage.setEstado(EstadoChat.VER_PRODUCTO);
            }
            ResponseSendingMessage respuestaRecibidaDeWhatsapp = restClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(respuestaAEnviar)
                    .retrieve()
                    .body(ResponseSendingMessage.class);

            whatsappMessage.setContextWaForNextResponse(respuestaRecibidaDeWhatsapp.getMessages().get(0).getId());
            whatsappMessage.setMensajeRecibido(respuesta);
            super.whatsappRepositoryCustom.guardar(whatsappMessage);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }
    }

    private SendButtonMessage enviarMensajeSobreElProducto(String telefono) {
        // Crear la instancia de Reply para los botones
        Reply respuestaComprarProducto = new Reply();
        respuestaComprarProducto.setId("boton-comprar-producto");
        respuestaComprarProducto.setTitle(Opciones.COMPRAR_PRODUCTO.getValor());

        Reply respuestaVerStock = new Reply();
        respuestaVerStock.setId("boton-informacion-stock");
        respuestaVerStock.setTitle(Opciones.VER_STOCK_PRODUCTO.getValor());

        // Crear la instancia de Button
        Button botonComprarProducto = new Button();
        botonComprarProducto.setReply(respuestaComprarProducto);
        botonComprarProducto.setType("reply");

        Button botonVerStock = new Button();
        botonVerStock.setReply(respuestaVerStock);
        botonVerStock.setType("reply");

        // Crear la instancia de Action
        Action action = new Action();
        action.setButtons(Arrays.asList(botonComprarProducto, botonVerStock));

        // Crear la instancia de Body
        Body body = new Body();
        body.setText("Dinos que quieres hacer con el producto");

        // Crear la instancia de Interactive
        Interactive interactive = new Interactive();
        interactive.setAction(action);
        interactive.setBody(body);
        interactive.setType("button");

        // Crear la instancia de WhatsAppMessage
        SendButtonMessage message = new SendButtonMessage();
        message.setInteractive(interactive);
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setTo("541133587926");
        message.setType("interactive");

        return message;
    }

    private SendButtonMessage enviarMensajeMenuAnterior(String telefono) {

        // Crear la instancia de Reply para los botones
        Reply respuestaVerProductosDelNegocio = new Reply();
        respuestaVerProductosDelNegocio.setId("boton-ver-productos-del-negocio");
        respuestaVerProductosDelNegocio.setTitle("Ver Productos");

        Reply respuestaVerInformacionDelNegocio = new Reply();
        respuestaVerInformacionDelNegocio.setId("boton-informacion-del-negocio");
        respuestaVerInformacionDelNegocio.setTitle("Sobre nosotros");

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
        body.setText("Somos Ceramica S.A, ¿que necesitas saber de nosotros?");

        // Crear la instancia de Interactive
        Interactive interactive = new Interactive();
        interactive.setAction(action);
        interactive.setBody(body);
        interactive.setType("button");

        // Crear la instancia de WhatsAppMessage
        SendButtonMessage message = new SendButtonMessage();
        message.setInteractive(interactive);
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setTo("541133587926");
        message.setType("interactive");

        return message;


    }
}
