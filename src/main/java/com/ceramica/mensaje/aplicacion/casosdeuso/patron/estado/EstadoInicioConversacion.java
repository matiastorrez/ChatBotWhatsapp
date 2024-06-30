package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado;

import com.ceramica.diadisponible.aplicacion.DiaDisponibleAplicacion;
import com.ceramica.diadisponible.dominio.modelos.DiaDisponible;
import com.ceramica.mensajeria.dominio.puertos.salida.IMensajeriaServicioPuerto;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import com.ceramica.diadisponible.infraestructura.salida.basededatos.entidad.DiaDisponibleEntidad;
import com.ceramica.producto.infraestructura.salida.basededatos.entidad.ProductoEntidad;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.Button;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.Reply;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.SendButtonMessage;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.Opciones;
import com.ceramica.diadisponible.dominio.puertos.salida.IDiaDisponibleRepositorioPuerto;
import com.ceramica.producto.dominio.puertos.salida.IProductoRepositorioPuerto;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.Value;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.Body;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.listmessage.*;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.singlemessage.SendSingleMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.singlemessage.Text;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EstadoInicioConversacion extends EstadoMensaje {


    public EstadoInicioConversacion(IMensajeRepositorioPuerto mensajeRepositorioPuerto, IMensajeriaServicioPuerto mensajeriaServicioPuerto) {
        super(mensajeRepositorioPuerto, mensajeriaServicioPuerto);
    }

    //El estado anterior es EstadoPrimerMensaje
    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.INICIO_CONVERSACION;
    }

    @Autowired
    private IProductoRepositorioPuerto productoRepositoryCustom;

    @Autowired
    private IDiaDisponibleRepositorioPuerto diaDisponibleRepositoryCustom;

    @Override
    public void procesarInformacion(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp) {
        Value value = mensajeRecibido.getEntry().get(0).getChanges().get(0).getValue();
        //enviar la request al estado (los estados tendra el metodo procesar informacion, en este caso verificaran si la respuesta es un texto, o un interactive)
        if (!value.getMessages().get(0).getType().equals("interactive")) {
            throw new RuntimeException("No es la respuesta esperada");
        }

        if (!value.getMessages().get(0).getInteractive().getType().equals("button_reply")) {
            throw new RuntimeException("Debes seleccionar un boton");
        }

        if (!value.getMessages().get(0).getContext().getId().equals(mensajeDeWhatsapp.getContextWaForNextResponse())) {
            throw new RuntimeException("Debes responder seleccionando uno de las botones enviados recientemente");
        }

    }

    @Override
    public void enviarRespuestas(ReceiveBaseMessage mensajeRecibido, MensajeWhatsappEntidad mensajeDeWhatsapp) {

        try {
            this.procesarInformacion(mensajeRecibido, mensajeDeWhatsapp);

            Value value = mensajeRecibido.getEntry().get(0).getChanges().get(0).getValue();
            String respuesta = value.getMessages().get(0).getInteractive().getButtonReply().getTitle();
            String telefono = mensajeDeWhatsapp.getTelefonoWa();

            ResponseSendingMessage respuestaRecibidaDeWhatsapp = null;
            if (respuesta.equals(Opciones.VER_PRODUCTOS.getValor())) {
                respuestaRecibidaDeWhatsapp = this.creandoMensajeMostrandoProductos(telefono);
                mensajeDeWhatsapp.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            } else if (respuesta.equals(Opciones.SOBRE_NOSOTROS.getValor())) {
                respuestaRecibidaDeWhatsapp = this.creandoMensajeSobreNosotros(telefono);
                mensajeDeWhatsapp.setEstado(EstadoChat.INICIO_CONVERSACION);
            }

            mensajeDeWhatsapp.setMensajeRecibido(respuesta);
            mensajeDeWhatsapp.setContextWaForNextResponse(respuestaRecibidaDeWhatsapp.getMessages().get(0).getId());
            super.mensajeRepositorioPuerto.guardar(mensajeDeWhatsapp);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }
    }

    private ResponseSendingMessage creandoMensajeMostrandoProductos(String telefono) {

        List<ProductoEntidad> productos = this.productoRepositoryCustom.obtenerTodosLosProductosHabilitados();
        List<Row> filas = new ArrayList<>();


        for (ProductoEntidad productoEntidad : productos) {
            Row fila = new Row();
            fila.setId(productoEntidad.getId().toString());
            fila.setTitle(productoEntidad.getNombre());
            fila.setDescription(productoEntidad.getDescripcion() + "\nEl precio es de: $" + productoEntidad.getPrecio());
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

        return (ResponseSendingMessage) mensajeriaServicioPuerto.enviarRespuesta(message);

    }


    private ResponseSendingMessage creandoMensajeSobreNosotros(String telefono) {

        SendSingleMessage mensajeDiasDisponibles = this.creandoMensajeDiasDisponbiles(telefono);
        SendButtonMessage mensajeDeInicio = this.creandoMensajeDeInicio(telefono);
        mensajeriaServicioPuerto.enviarRespuesta(mensajeDiasDisponibles);
        return (ResponseSendingMessage) mensajeriaServicioPuerto.enviarRespuesta(mensajeDeInicio);

    }

    private SendSingleMessage creandoMensajeDiasDisponbiles(String telefono) {
        List<DiaDisponible> diasDisponibles = this.diaDisponibleRepositoryCustom.obtenerDiasDisponibles();
        StringBuilder mensajeConstruido = new StringBuilder();

        for (DiaDisponible diaDisponible : diasDisponibles) {
            DiaDisponibleAplicacion diaDisponibleAplicacion = (DiaDisponibleAplicacion) diaDisponible;
            mensajeConstruido.append(diaDisponibleAplicacion.getDia()).append(" de: ").append(diaDisponibleAplicacion.getHorarioInicio()).append(" a ").append(diaDisponibleAplicacion.getHorarioFin()).append("\n");
        }

        Text texto = new Text();
        texto.setBody(mensajeConstruido.toString());
        texto.setPreviewUrl(false);

        SendSingleMessage mensajeAEnviar = new SendSingleMessage();
        mensajeAEnviar.setMessagingProduct("whatsapp");
        mensajeAEnviar.setRecipientType("individual");
        mensajeAEnviar.setTo(telefono);
        mensajeAEnviar.setType("text");
        mensajeAEnviar.setText(texto);

        return mensajeAEnviar;

    }

    private SendButtonMessage creandoMensajeDeInicio(String telefono) {
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
        com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.Action action = new com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.Action();
        action.setButtons(Arrays.asList(botonVerProductosDelNegocio, botonVerInformacionDelNegocio));

        // Crear la instancia de Body
        Body body = new Body();
        body.setText("Hola, somos Negocio S.A, ¿que necesitas saber de nosotros?");

        // Crear la instancia de Interactive
        com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.Interactive interactive = new com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage.Interactive();
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
