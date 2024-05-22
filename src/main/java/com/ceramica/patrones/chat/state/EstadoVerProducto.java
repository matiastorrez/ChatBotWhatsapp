package com.ceramica.patrones.chat.state;

import com.ceramica.entity.OrdenDeVenta;
import com.ceramica.entity.Producto;
import com.ceramica.entity.Stock;
import com.ceramica.entity.WhatsappMessage;
import com.ceramica.enums.EstadoStock;
import com.ceramica.patrones.chat.state.enums.EstadoChat;
import com.ceramica.patrones.chat.state.enums.Opciones;
import com.ceramica.repository.custom.IOrdenDeVentaRepositoryCustom;
import com.ceramica.repository.custom.IProductoRepositoryCustom;
import com.ceramica.repository.custom.IStockRepositoryCustom;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.Value;
import com.ceramica.whatsappclass.templates.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.Body;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage.SendButtonMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.listmessage.*;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.singlemessage.SendSingleMessage;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.singlemessage.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class EstadoVerProducto extends EstadoMensaje {

    //El estado anterior es EstadoMostrandoProductos

    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.VER_PRODUCTO;
    }

    @Autowired
    private IStockRepositoryCustom stockRepositoryCustom;

    @Autowired
    private IProductoRepositoryCustom productoRepositoryCustom;

    @Autowired
    private IOrdenDeVentaRepositoryCustom ordenDeVentaRepositoryCustom;


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
            String nombreProductoElegido = whatsappMessage.getMensajeRecibido();

            ResponseSendingMessage respuestaRecibidaDeWhatsapp = null;

            if (respuesta.equals(Opciones.COMPRAR_PRODUCTO.getValor())) {
                respuestaRecibidaDeWhatsapp = this.enviarMensajeSobreLaCompraDelProducto(telefono, nombreProductoElegido);
                whatsappMessage.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            } else if (respuesta.equals(Opciones.VER_STOCK_PRODUCTO.getValor())) {
                respuestaRecibidaDeWhatsapp = this.enviarMensajeSobreElStockDelProducto(telefono, nombreProductoElegido);
                whatsappMessage.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            }

            whatsappMessage.setContextWaForNextResponse(respuestaRecibidaDeWhatsapp.getMessages().get(0).getId());
            whatsappMessage.setMensajeRecibido(respuesta);
            super.whatsappRepositoryCustom.guardar(whatsappMessage);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }

    }


    private ResponseSendingMessage enviarMensajeSobreLaCompraDelProducto(String telefono, String nombreProducto) {

        Producto producto = this.productoRepositoryCustom.obtenerProductoPorNombre(nombreProducto).orElseThrow(() ->
                new RuntimeException("No existe el producto con este nombre")
        );

        Stock stock = this.stockRepositoryCustom.obtenerUnStockDelProductoAlaVenta(producto.getId()).orElseThrow(() -> new RuntimeException("No se encontro un stock disponible para este producto"));
        stock.setEstado(EstadoStock.VENDIDO);
        stock.setTelefonoComprador(telefono);

        this.stockRepositoryCustom.guardar(stock);

        OrdenDeVenta ordenDeVenta = new OrdenDeVenta();
        ordenDeVenta.setNombre(producto.getNombre());
        ordenDeVenta.setDescripcion(producto.getDescripcion());
        ordenDeVenta.setPrecio(producto.getPrecio());
        ordenDeVenta.setTelefonoDelComprador(telefono);
        ordenDeVenta.setTotal(producto.getPrecio());

        this.ordenDeVentaRepositoryCustom.guardar(ordenDeVenta);


        SendListMessage message = this.creandoMensajeDeVerProductos();
        SendSingleMessage request = this.creandoMensajeDeCompraRealizada(telefono);

        restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ResponseSendingMessage.class);

        return restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(message)
                .retrieve()
                .body(ResponseSendingMessage.class);

    }


    private ResponseSendingMessage enviarMensajeSobreElStockDelProducto(String telefono, String nombreProducto) {
            long productoId = this.productoRepositoryCustom.obtenerProductoPorNombre(nombreProducto).orElseThrow(() ->
                    new RuntimeException("No existe el producto con este nombre")
            ).getId();

            long stockDelProducto = this.stockRepositoryCustom.cantidadDeStockDeUnProductoALaVenta(productoId);

            Text text = new Text();
            text.setBody("El stock disponible para el producto " + nombreProducto + " es de: " + stockDelProducto + " unidades");
            text.setPreviewUrl(false);
            SendSingleMessage request = new SendSingleMessage();
            request.setMessagingProduct("whatsapp");
            request.setRecipientType("individual");
            request.setTo(telefono);
            request.setType("text");
            request.setText(text);

            SendListMessage message = this.creandoMensajeDeVerProductos();

            restClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ResponseSendingMessage.class);

            return restClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(message)
                    .retrieve()
                    .body(ResponseSendingMessage.class);
    }

    private SendListMessage creandoMensajeDeVerProductos() {


        List<Producto> productos = this.productoRepositoryCustom.obtenerTodosLosProductosHabilitados();
        List<Row> filas = new ArrayList<>();


        for (Producto producto : productos) {
            Row fila = new Row();
            fila.setId(producto.getId().toString());
            fila.setTitle(producto.getNombre());
            fila.setDescription(producto.getDescripcion());
            filas.add(fila);
        }

        SendListMessage message = new SendListMessage();
        message.setMessagingProduct("whatsapp");
        message.setRecipientType("individual");
        message.setTo("541133587926");
        message.setType("interactive");

        Interactive interactive = new Interactive();
        interactive.setType("list");

        Body body = new Body();
        body.setText("Te dejamos nuevamente nuestros productos para que los veas, en caso de querer volver al menu anterior seleccione la opcion: " + Opciones.VOLVER_ATRAS.getValor());
        interactive.setBody(body);

        Action action = new Action();
        action.setButton("Ver Productos");

        Section seccionProductos = new Section();
        seccionProductos.setTitle("Productos");

        seccionProductos.setRows(filas);

        Section seccionVolver = new Section();
        seccionVolver.setTitle("Volver");

        Row filaVolver = new Row();
        filaVolver.setId("fila_volver");
        filaVolver.setTitle(Opciones.VOLVER_ATRAS.getValor());

        seccionVolver.setRows(Arrays.asList(filaVolver));

        action.setSections(Arrays.asList(seccionProductos, seccionVolver));
        interactive.setAction(action);
        message.setInteractive(interactive);

        return message;
    }

    private SendSingleMessage creandoMensajeDeCompraRealizada(String telefono) {
        Text text = new Text();
        text.setBody("Muchas gracias por la compra");
        text.setPreviewUrl(false);
        SendSingleMessage request = new SendSingleMessage();
        request.setMessagingProduct("whatsapp");
        request.setRecipientType("individual");
        request.setTo(telefono);
        request.setType("text");
        request.setText(text);

        return request;
    }
}
