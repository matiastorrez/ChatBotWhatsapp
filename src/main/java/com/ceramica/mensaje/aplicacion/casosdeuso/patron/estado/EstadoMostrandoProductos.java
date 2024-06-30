package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado;

import com.ceramica.mensajeria.dominio.puertos.salida.IMensajeriaServicioPuerto;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.Opciones;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.Value;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.Body;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.*;

import java.util.Arrays;

public class EstadoMostrandoProductos extends EstadoMensaje {
    public EstadoMostrandoProductos(IMensajeRepositorioPuerto mensajeRepositorioPuerto, IMensajeriaServicioPuerto mensajeriaServicioPuerto) {
        super(mensajeRepositorioPuerto, mensajeriaServicioPuerto);
    }

    //el estado anterior a este es EstadoInicioConversacion

    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.MOSTRANDO_PRODUCTOS;
    }


    @Override
    public void procesarInformacion(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp) {
        Value value = mensajeRecibido.getEntry().get(0).getChanges().get(0).getValue();
        if (!value.getMessages().get(0).getType().equals("interactive")) {
            throw new RuntimeException("No es la respuesta esperada");
        }

        if(!value.getMessages().get(0).getInteractive().getType().equals("list_reply")){
            throw new RuntimeException("Debes seleccionar una opcion de la lista");
        }

        if (!value.getMessages().get(0).getContext().getId().equals(mensajeDeWhatsapp.getContextWaForNextResponse())) {
            throw new RuntimeException("Debes responder seleccionando una de las opciones de la lista");
        }
    }

    @Override
    public void enviarRespuestas(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp) {
        try {
            this.procesarInformacion(mensajeRecibido, mensajeDeWhatsapp);
            Value value = mensajeRecibido.getEntry().get(0).getChanges().get(0).getValue();
            String respuesta = value.getMessages().get(0).getInteractive().getListReply().getTitle();
            String telefono = mensajeDeWhatsapp.getTelefonoWa();

            SendButtonMessage respuestaAEnviar;
            if (respuesta.equals(Opciones.VOLVER_ATRAS.getValor())) {
                respuestaAEnviar = this.enviarMensajeMenuAnterior(telefono);
                mensajeDeWhatsapp.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            } else {
                respuestaAEnviar = this.enviarMensajeSobreElProducto(telefono);
                mensajeDeWhatsapp.setEstado(EstadoChat.VER_PRODUCTO);
            }
            ResponseSendingMessage respuestaRecibidaDeWhatsapp = (ResponseSendingMessage) mensajeriaServicioPuerto.enviarRespuesta(respuestaAEnviar);


            mensajeDeWhatsapp.setContextWaForNextResponse(respuestaRecibidaDeWhatsapp.getMessages().get(0).getId());
            mensajeDeWhatsapp.setMensajeRecibido(respuesta);
            super.mensajeRepositorioPuerto.guardar(mensajeDeWhatsapp);
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
        SendButtonMessage mensajeAEnviar = new SendButtonMessage();
        mensajeAEnviar.setInteractive(interactive);
        mensajeAEnviar.setMessagingProduct("whatsapp");
        mensajeAEnviar.setRecipientType("individual");
        mensajeAEnviar.setTo(telefono);
        mensajeAEnviar.setType("interactive");

        return mensajeAEnviar;
    }

    private SendButtonMessage enviarMensajeMenuAnterior(String telefono) {

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
        body.setText("Somos Ceramica S.A, ¿que necesitas saber de nosotros?");

        // Crear la instancia de Interactive
        Interactive interactive = new Interactive();
        interactive.setAction(action);
        interactive.setBody(body);
        interactive.setType("button");

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
