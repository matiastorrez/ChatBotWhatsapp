package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.webhook;

public record RequestWebhookWhatsapp(String hub_mode, String hub_challenge, String hub_verify_token) {
}
