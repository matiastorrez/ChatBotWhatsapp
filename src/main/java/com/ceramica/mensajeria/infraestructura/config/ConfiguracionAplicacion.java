package com.ceramica.mensajeria.infraestructura.config;

import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.*;
import com.ceramica.mensajeria.aplicacion.casosdeuso.whatsapp.WhatsappRecibirMensajeCasoDeUsoAdaptador;
import com.ceramica.mensajeria.aplicacion.casosdeuso.whatsapp.WhatsappVerificarWebhookCasoDeUsoAdaptador;
import com.ceramica.mensajeria.aplicacion.servicios.MensajeriaServicio;
import com.ceramica.mensaje.infraestructura.salida.basededatos.repositorio.IMensajeRepositorio;
import com.ceramica.mensaje.infraestructura.salida.basededatos.repositorio.adaptadores.MensajeRepositorioAdaptador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class ConfiguracionAplicacion {
    @Autowired
    private IMensajeRepositorio mensajeRepositorio;
    @Autowired
    private MensajeRepositorioAdaptador whatsappRepositorioAdaptador;

    @Bean
    public MensajeriaServicio mensajeriaServicio(EstadoMensajeFactoria estadoMensajeFactoria){
        return new MensajeriaServicio(new WhatsappRecibirMensajeCasoDeUsoAdaptador(estadoMensajeFactoria, whatsappRepositorioAdaptador), new WhatsappVerificarWebhookCasoDeUsoAdaptador());
    }
}
