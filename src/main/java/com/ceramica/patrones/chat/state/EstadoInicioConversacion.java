package com.ceramica.patrones.chat.state;

import com.ceramica.entity.DiaDisponible;
import com.ceramica.entity.Producto;
import com.ceramica.entity.WhatsappMessage;
import com.ceramica.patrones.chat.state.enums.Opciones;
import com.ceramica.repository.custom.IDiaDisponibleRepositoryCustom;
import com.ceramica.repository.custom.IProductoRepositoryCustom;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.Value;
import com.ceramica.patrones.chat.state.enums.EstadoChat;
import com.ceramica.whatsappclass.templates.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.Body;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.Button;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.Reply;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.SendButtonMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.listmessage.*;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.singlemessage.SendSingleMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.singlemessage.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EstadoInicioConversacion extends EstadoMensaje {

    //El estado anterior es EstadoPrimerMensaje
    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.INICIO_CONVERSACION;
    }

    @Autowired
    private IProductoRepositoryCustom productoRepositoryCustom;

    @Autowired
    private IDiaDisponibleRepositoryCustom diaDisponibleRepositoryCustom;

    @Override
    public void procesarInformacion(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage) {
        Value value = receiveMessage.getEntry().get(0).getChanges().get(0).getValue();
        //enviar la request al estado (los estados tendra el metodo procesar informacion, en este caso verificaran si la respuesta es un texto, o un interactive)
        if (!value.getMessages().get(0).getType().equals("interactive")) {
            throw new RuntimeException("No es la respuesta esperada");
        }

        if (!value.getMessages().get(0).getInteractive().getType().equals("button_reply")) {
            throw new RuntimeException("Debes seleccionar un boton");
        }

        if (!value.getMessages().get(0).getContext().getId().equals(whatsappMessage.getContextWaForNextResponse())) {
            throw new RuntimeException("Debes responder seleccionando uno de las botones enviados recientemente");
        }

    }

    @Override
    public void enviarRespuestas(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage) {

        try {
            this.procesarInformacion(receiveMessage, whatsappMessage);

            Value value = receiveMessage.getEntry().get(0).getChanges().get(0).getValue();
            String respuesta = value.getMessages().get(0).getInteractive().getButtonReply().getTitle();
            String telefono = whatsappMessage.getTelefonoWa();

            ResponseSendingMessage respuestaRecibidaDeWhatsapp = null;
            System.out.println(respuesta);
            if (respuesta.equals(Opciones.VER_PRODUCTOS.getValor())) {
                respuestaRecibidaDeWhatsapp = this.creandoMensajeMostrandoProductos(telefono);
                whatsappMessage.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
                System.out.println("entre en ver productos");
            } else if (respuesta.equals(Opciones.SOBRE_NOSOTROS.getValor())) {
                respuestaRecibidaDeWhatsapp = this.creandoMensajeSobreNosotros(telefono);
                whatsappMessage.setEstado(EstadoChat.INICIO_CONVERSACION);
                System.out.println("entre en sobre nostros");

            }

            whatsappMessage.setMensajeRecibido(respuesta);
            whatsappMessage.setContextWaForNextResponse(respuestaRecibidaDeWhatsapp.getMessages().get(0).getId());
            super.whatsappRepositoryCustom.guardar(whatsappMessage);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }
    }

    private ResponseSendingMessage creandoMensajeMostrandoProductos(String telefono) {

        List<Producto> productos = this.productoRepositoryCustom.obtenerTodosLosProductosHabilitados();
        List<Row> filas = new ArrayList<>();


        for (Producto producto : productos) {
            Row fila = new Row();
            fila.setId(producto.getId().toString());
            fila.setTitle(producto.getNombre());
            fila.setDescription(producto.getDescripcion() + "\nEl precio es de: $" + producto.getPrecio());
            filas.add(fila);
        }

        SendListMessage message = new SendListMessage();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setTo(telefono);
        message.setType("interactive");

        Interactive interactive = new Interactive();
        interactive.setType("list");

       /* Header header = new Header();
        header.setType("text");
        header.setText("Aca te mostramos todos nuestros productos");
        interactive.setHeader(header);*/

        Body body = new Body();
        body.setText("Aca te mostramos todos nuestros productos, en caso de querer volver al menu anterior seleccione la opcion: " + Opciones.VOLVER_ATRAS.getValor());
        interactive.setBody(body);

      /*  Footer footer = new Footer();
        footer.setText("FOOTER_TEXT");
        interactive.setFooter(footer);
*/
        Action action = new Action();
        action.setButton(Opciones.VER_PRODUCTOS.getValor());

        Section seccionProductos = new Section();
        seccionProductos.setTitle("Productos");

        seccionProductos.setRows(filas);

      /*  Section section2 = new Section();
        section2.setTitle("Otros");

        Row row2_1 = new Row();
        row2_1.setId("SECTION_2_ROW_1_ID");
        row2_1.setTitle("Coca Cola");
        //row2_1.setDescription("SECTION_2_ROW_1_DESCRIPTION");

        Row row2_2 = new Row();
        row2_2.setId("SECTION_2_ROW_2_ID");
        row2_2.setTitle("Hamburguesa");
        //row2_2.setDescription("SECTION_2_ROW_2_DESCRIPTION");

        section2.setRows(Arrays.asList(row2_1, row2_2));*/

        Section seccionVolver = new Section();
        seccionVolver.setTitle("Volver");
        Row filaVolver = new Row();
        filaVolver.setId("fila_volver");
        filaVolver.setTitle(Opciones.VOLVER_ATRAS.getValor());
        //row3_1.setDescription("SECTION_2_ROW_1_DESCRIPTION");

        seccionVolver.setRows(Arrays.asList(filaVolver));

        action.setSections(Arrays.asList(seccionProductos, seccionVolver));
        interactive.setAction(action);
        message.setInteractive(interactive);

        return restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(message)
                .retrieve()
                .body(ResponseSendingMessage.class);

    }


    private ResponseSendingMessage creandoMensajeSobreNosotros(String telefono) {

        SendSingleMessage mensajeDiasDisponibles = this.creandoMensajeDiasDisponbiles(telefono);
        SendButtonMessage mensajeDeInicio = this.creandoMensajeDeInicio(telefono);

        restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(mensajeDiasDisponibles)
                .retrieve()
                .body(ResponseSendingMessage.class);


        return restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(mensajeDeInicio)
                .retrieve()
                .body(ResponseSendingMessage.class);
    }

    private SendSingleMessage creandoMensajeDiasDisponbiles(String telefono){
        List<DiaDisponible> diasDisponibles = this.diaDisponibleRepositoryCustom.obtenerDiasDisponibles();
        StringBuilder sb = new StringBuilder();

        for (DiaDisponible diaDisponible : diasDisponibles){
            sb.append(diaDisponible.getDia()).append(" de: ").append(diaDisponible.getHorarioInicio()).append(" a ").append(diaDisponible.getHorarioFin()).append("\n");
        }

        String mensaje = sb.toString();


        Text text = new Text();
        text.setBody(mensaje);
        text.setPreviewUrl(false);
        SendSingleMessage request = new SendSingleMessage();
        request.setMessagingProduct("whatsapp");
        request.setRecipientType("individual");
        request.setTo(telefono);
        request.setType("text");
        request.setText(text);

        return request;


    }

    private SendButtonMessage creandoMensajeDeInicio(String telefono){
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
        com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.Action action = new com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.Action();
        action.setButtons(Arrays.asList(botonVerProductosDelNegocio, botonVerInformacionDelNegocio));

        // Crear la instancia de Body
        Body body = new Body();
        body.setText("Hola, somos Negocio S.A, ¿que necesitas saber de nosotros?");

        // Crear la instancia de Interactive
        com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.Interactive interactive = new com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.Interactive();
        interactive.setAction(action);
        interactive.setBody(body);
        interactive.setType("button");

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
