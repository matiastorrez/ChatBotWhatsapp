package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.webhook;

public record RequestMessage(String messaging_product, String to, RequestMessageText text){

}