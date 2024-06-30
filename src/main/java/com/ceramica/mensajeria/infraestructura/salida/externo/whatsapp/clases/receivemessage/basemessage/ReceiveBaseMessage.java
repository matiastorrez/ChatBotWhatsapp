package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ReceiveBaseMessage {

    private String object;
    private List<Entry> entry;
/*
    @Override
    public void validate() {
        try {
            Value value = this.getEntry().get(0).getChanges().get(0).getValue();
            if (value.getMessages() == null) {
                throw new Exception("No existe mensaje");
            }

            this.response();
        }catch ( Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void response() {

        com.ceramica.model.sendmessage.basemessage.singlemessage.Text text = new Text();
        text.setBody(this.getEntry().get(0).getChanges().get(0).getValue().getMessages().get(0).getInteractive().getListReply().getDescription());
        text.setPreviewUrl(false);
        SendSingleMessage request = new SendSingleMessage();
        request.setMessagingProduct("whatsapp");
        request.setRecipientType("individual");
        request.setTo("541133587926");
        request.setType("text");
        request.setText(text);

        RestClient restClient =  RestClient.builder()
                .baseUrl("https://graph.facebook.com/v19.0/315807674948060/messages")
                .defaultHeader("Authorization", "Bearer EAAF7Lxfjs18BOxx8M29EFfDZCLh43Egfp1QpiXraHKO3CszUZCtOeiMtLbA2p1EILqNZCYJ6XE1WQkKZAAibZAfcKaMXYJAsUSD4P5lhMH0BddLvwhOGqKwvIwCOmrOdfZChIsfmSy6tpxebg6FL2yHZBPkmirQnZCNvy5CWGR1pRZAidNHcYzxtZB7kDvcAvbxJkbQ3o5PSk0FHN4")
                .build();

        try {
            restClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException ex) {
            System.out.println(ex.getMessage());
            throw new RestClientResponseException("Error al enviar mensaje", 400, "Error al enviar mensaje", null, null, null);
        }
    }*/
}
