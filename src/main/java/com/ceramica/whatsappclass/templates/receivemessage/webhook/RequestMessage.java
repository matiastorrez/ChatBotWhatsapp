package com.ceramica.whatsappclass.templates.receivemessage.webhook;

public record RequestMessage(String messaging_product, String to, RequestMessageText text){

}