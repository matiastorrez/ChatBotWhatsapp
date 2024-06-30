package com.ceramica.mensajeria.aplicacion.casosdeuso.whatsapp;

import com.ceramica.mensajeria.dominio.puertos.entrada.VerificarWebhookCasoDeUsoPuerto;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.webhook.RequestWebhookWhatsapp;

public class WhatsappVerificarWebhookCasoDeUsoAdaptador implements VerificarWebhookCasoDeUsoPuerto {

    @org.springframework.beans.factory.annotation.Value("${whatsapp.webhook.token}")
    private String whatsappWebhookToken;

    @Override
    public String verificarWebhook(Object mensaje) {

        RequestWebhookWhatsapp mensajeRecibido = (RequestWebhookWhatsapp) mensaje;

        String mode = mensajeRecibido.hub_mode();
        String token = mensajeRecibido.hub_verify_token();
        String challenge = mensajeRecibido.hub_challenge();
        try {
            if (mode == null || token == null) {
                throw new Exception("modo o token son nulos");
            }

            if (mode.equals("suscribe") && token.equals("maticodewhatsapp456")) {
                System.out.println(mode);
                System.out.println(token);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return challenge;
    }
}
