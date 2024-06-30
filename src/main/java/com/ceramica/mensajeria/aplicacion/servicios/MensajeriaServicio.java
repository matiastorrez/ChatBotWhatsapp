package com.ceramica.mensajeria.aplicacion.servicios;

import com.ceramica.mensajeria.dominio.puertos.entrada.RecibirMensajeCasoDeUsoPuerto;
import com.ceramica.mensajeria.dominio.puertos.entrada.VerificarWebhookCasoDeUsoPuerto;

public class MensajeriaServicio implements RecibirMensajeCasoDeUsoPuerto, VerificarWebhookCasoDeUsoPuerto {

    private RecibirMensajeCasoDeUsoPuerto recibirMensajeCasoDeUsoPuerto;
    private VerificarWebhookCasoDeUsoPuerto verificarWebhookCasoDeUsoPuerto;

    public MensajeriaServicio(RecibirMensajeCasoDeUsoPuerto recibirMensajeCasoDeUsoPuerto, VerificarWebhookCasoDeUsoPuerto verificarWebhookCasoDeUsoPuerto) {
        this.recibirMensajeCasoDeUsoPuerto = recibirMensajeCasoDeUsoPuerto;
        this.verificarWebhookCasoDeUsoPuerto = verificarWebhookCasoDeUsoPuerto;
    }

    @Override
    public void recibirMensaje(Object mensaje) {
        this.recibirMensajeCasoDeUsoPuerto.recibirMensaje(mensaje);
    }

    @Override
    public String verificarWebhook(Object mensaje) {
        return this.verificarWebhookCasoDeUsoPuerto.verificarWebhook(mensaje);
    }
}
