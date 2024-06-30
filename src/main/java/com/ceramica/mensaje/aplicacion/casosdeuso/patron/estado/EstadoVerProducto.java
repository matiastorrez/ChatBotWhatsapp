package com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado;

import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import com.ceramica.ordendeventa.infraestructura.salida.basededatos.entidad.OrdenDeVentaEntidad;
import com.ceramica.producto.infraestructura.salida.basededatos.entidad.ProductoEntidad;
import com.ceramica.ordendeventa.dominio.puertos.salida.IOrdenDeVentaRepositorioPuerto;
import com.ceramica.producto.dominio.puertos.salida.IProductoRepositorioPuerto;
import com.ceramica.stock.infraestructura.salida.basededatos.entidad.StockEntidad;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.stock.aplicacion.casosdeuso.enums.EstadoStock;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.Opciones;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.Value;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.responsesendingmessage.ResponseSendingMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.Body;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.listmessage.*;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.singlemessage.SendSingleMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.singlemessage.Text;
import com.ceramica.mensajeria.dominio.puertos.salida.*;
import com.ceramica.stock.dominio.puertos.salida.IStockRepositorioPuerto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EstadoVerProducto extends EstadoMensaje {
    public EstadoVerProducto(IMensajeRepositorioPuerto mensajeRepositorioPuerto, IMensajeriaServicioPuerto mensajeriaServicioPuerto) {
        super(mensajeRepositorioPuerto, mensajeriaServicioPuerto);
    }

    //El estado anterior es EstadoMostrandoProductos

    @Override
    public EstadoChat obtenerEstado() {
        return EstadoChat.VER_PRODUCTO;
    }

    @Autowired
    private IStockRepositorioPuerto stockRepositoryCustom;

    @Autowired
    private IProductoRepositorioPuerto productoRepositoryCustom;

    @Autowired
    private IOrdenDeVentaRepositorioPuerto ordenDeVentaRepositoryCustom;


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
            String nombreProductoElegido = mensajeDeWhatsapp.getMensajeRecibido();

            ResponseSendingMessage respuestaRecibidaDeWhatsapp = null;

            if (respuesta.equals(Opciones.COMPRAR_PRODUCTO.getValor())) {
                respuestaRecibidaDeWhatsapp = this.enviarMensajeSobreLaCompraDelProducto(telefono, nombreProductoElegido);
                mensajeDeWhatsapp.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            } else if (respuesta.equals(Opciones.VER_STOCK_PRODUCTO.getValor())) {
                respuestaRecibidaDeWhatsapp = this.enviarMensajeSobreElStockDelProducto(telefono, nombreProductoElegido);
                mensajeDeWhatsapp.setEstado(EstadoChat.MOSTRANDO_PRODUCTOS);
            }

            mensajeDeWhatsapp.setContextWaForNextResponse(respuestaRecibidaDeWhatsapp.getMessages().get(0).getId());
            mensajeDeWhatsapp.setMensajeRecibido(respuesta);
            super.mensajeRepositorioPuerto.guardar(mensajeDeWhatsapp);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Problemas con el sistema");
        }

    }


    private ResponseSendingMessage enviarMensajeSobreLaCompraDelProducto(String telefono, String nombreProducto) {

        ProductoEntidad productoEntidad = this.productoRepositoryCustom.obtenerProductoPorNombre(nombreProducto).orElseThrow(() ->
                new RuntimeException("No existe el producto con este nombre")
        );

        StockEntidad stockEntidad = this.stockRepositoryCustom.obtenerUnStockDelProductoAlaVenta(productoEntidad.getId()).orElseThrow(() -> new RuntimeException("No se encontro un stock disponible para este producto"));
        stockEntidad.setEstado(EstadoStock.VENDIDO);
        stockEntidad.setTelefonoComprador(telefono);

        this.stockRepositoryCustom.guardar(stockEntidad);

        OrdenDeVentaEntidad ordenDeVentaEntidad = new OrdenDeVentaEntidad();
        ordenDeVentaEntidad.setNombre(productoEntidad.getNombre());
        ordenDeVentaEntidad.setDescripcion(productoEntidad.getDescripcion());
        ordenDeVentaEntidad.setPrecio(productoEntidad.getPrecio());
        ordenDeVentaEntidad.setTelefonoDelComprador(telefono);
        ordenDeVentaEntidad.setTotal(productoEntidad.getPrecio());

        this.ordenDeVentaRepositoryCustom.guardar(ordenDeVentaEntidad);


        SendListMessage respuestaAEnviar = this.creandoMensajeDeVerProductos(telefono);
        SendSingleMessage nuevoMensaje = this.creandoMensajeDeCompraRealizada(telefono);

        mensajeriaServicioPuerto.enviarRespuesta(nuevoMensaje);

        return (ResponseSendingMessage) mensajeriaServicioPuerto.enviarRespuesta(respuestaAEnviar);

    }


    private ResponseSendingMessage enviarMensajeSobreElStockDelProducto(String telefono, String nombreProducto) {
        long productoId = this.productoRepositoryCustom.obtenerProductoPorNombre(nombreProducto).orElseThrow(() ->
                new RuntimeException("No existe el producto con este nombre")
        ).getId();

        long stockDelProducto = this.stockRepositoryCustom.cantidadDeStockDeUnProductoALaVenta(productoId);

        Text texto = new Text();
        texto.setBody("El stock disponible para el producto " + nombreProducto + " es de: " + stockDelProducto + " unidades");
        texto.setPreviewUrl(false);
        SendSingleMessage request = new SendSingleMessage();
        request.setMessagingProduct("whatsapp");
        request.setRecipientType("individual");
        request.setTo(telefono);
        request.setType("text");
        request.setText(texto);

        SendListMessage message = this.creandoMensajeDeVerProductos(telefono);

        mensajeriaServicioPuerto.enviarRespuesta(request);

        return (ResponseSendingMessage) mensajeriaServicioPuerto.enviarRespuesta(message);
    }

    private SendListMessage creandoMensajeDeVerProductos(String telefono) {


        List<ProductoEntidad> productos = this.productoRepositoryCustom.obtenerTodosLosProductosHabilitados();
        List<Row> filas = new ArrayList<>();


        for (ProductoEntidad productoEntidad : productos) {
            Row fila = new Row();
            fila.setId(productoEntidad.getId().toString());
            fila.setTitle(productoEntidad.getNombre());
            fila.setDescription(productoEntidad.getDescripcion());
            filas.add(fila);
        }

        SendListMessage mensajeAEnviar = new SendListMessage();
        mensajeAEnviar.setMessagingProduct("whatsapp");
        mensajeAEnviar.setRecipientType("individual");
        mensajeAEnviar.setTo(telefono);
        mensajeAEnviar.setType("interactive");

        Interactive interactive = new Interactive();
        interactive.setType("list");

        Body body = new Body();
        body.setText("Te dejamos nuevamente nuestros productos para que los veas, en caso de querer volver al menu anterior seleccione la opcion: " + Opciones.VOLVER_ATRAS.getValor());
        interactive.setBody(body);

        Action action = new Action();
        action.setButton(Opciones.VER_PRODUCTOS.getValor());

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
        mensajeAEnviar.setInteractive(interactive);

        return mensajeAEnviar;
    }

    private SendSingleMessage creandoMensajeDeCompraRealizada(String telefono) {
        Text texto = new Text();
        texto.setBody("Muchas gracias por la compra");
        texto.setPreviewUrl(false);
        SendSingleMessage mensajeAEnviar = new SendSingleMessage();
        mensajeAEnviar.setMessagingProduct("whatsapp");
        mensajeAEnviar.setRecipientType("individual");
        mensajeAEnviar.setTo(telefono);
        mensajeAEnviar.setType("text");
        mensajeAEnviar.setText(texto);

        return mensajeAEnviar;
    }
}
