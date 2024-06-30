package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.adaptador;

import com.ceramica.mensajeria.dominio.puertos.salida.IMensajeriaServicioPuerto;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.responsesendingmessage.ResponseSendingMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.Collections;

public class WhatsappServicioAdaptador implements IMensajeriaServicioPuerto {

    @Value("${whatsapp.baseurl}")
    private String whatsappBaseUrl;

    @Value("${whatsapp.phone-number-identifier}")
    private String whatsappPhoneNumberIdentifier;

    @Value("${whatsapp.token}")
    private String whatsappToken;

    private final RestClient restClient;

    public WhatsappServicioAdaptador() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        String url = "https://graph.facebook.com/v19.0/315807674948060/messages";
        String tokenAuthorization = "Bearer EAAF7Lxfjs18BO7JxdzLHHzjclO8irBzhGMrVa0s1sle5eZBVlBB5eAFv3m9KhtUHQIZCVMZC7BIUyCZAddM59qHa4QJ7r4hqdhJXiRhgeQvilEFuNveSdkv2A4UZCOnZBR8mDOkssycZCNlsZAwD5gRxEVBnnccP6vp2I7hWpt4XHvDtjRsntdzM7SpaegGixlLYYn1pVmflDjSZB";
        System.out.println(url);
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .messageConverters(converters -> converters.add(converter))
                .defaultHeader("Authorization", tokenAuthorization)
                .build();

    }

    @Override
    public ResponseSendingMessage enviarRespuesta(Object mensajeRespuesta) {
        return restClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .body(mensajeRespuesta)
                .retrieve()
                .body(ResponseSendingMessage.class);
    }
}
