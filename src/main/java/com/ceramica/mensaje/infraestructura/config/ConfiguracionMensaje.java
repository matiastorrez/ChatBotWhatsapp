package com.ceramica.mensaje.infraestructura.config;

import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.*;
import com.ceramica.mensaje.infraestructura.salida.basededatos.repositorio.adaptadores.MensajeRepositorioAdaptador;
import com.ceramica.mensajeria.dominio.puertos.salida.IMensajeriaServicioPuerto;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.adaptador.WhatsappServicioAdaptador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConfiguracionMensaje {
    @Autowired
    private MensajeRepositorioAdaptador whatsappRepositorioAdaptador;
    @Bean
    public EstadoMensajeFactoria estadoMensajeFactoria(List<EstadoMensaje> estadoMensajes){
        return new EstadoMensajeFactoria(estadoMensajes);
    }

    @Bean
    public IMensajeriaServicioPuerto mensajeriaServicioPuerto(){
        return new WhatsappServicioAdaptador();
    }

    @Bean
    public EstadoMensaje estadoInicioConversacion(IMensajeriaServicioPuerto mensajeriaServicioPuerto){
        return new EstadoInicioConversacion(whatsappRepositorioAdaptador,mensajeriaServicioPuerto);
    }

    @Bean
    public EstadoMensaje estadoMostrandoProductos(IMensajeriaServicioPuerto mensajeriaServicioPuerto){
        return new EstadoMostrandoProductos(whatsappRepositorioAdaptador,mensajeriaServicioPuerto);
    }

    @Bean
    public EstadoMensaje estadoPrimerMensaje(IMensajeriaServicioPuerto mensajeriaServicioPuerto){
        return new EstadoPrimerMensaje(whatsappRepositorioAdaptador,mensajeriaServicioPuerto);
    }

    @Bean
    public EstadoMensaje estadoVerProducto(IMensajeriaServicioPuerto mensajeriaServicioPuerto){
        return new EstadoVerProducto(whatsappRepositorioAdaptador,mensajeriaServicioPuerto);
    }



}
