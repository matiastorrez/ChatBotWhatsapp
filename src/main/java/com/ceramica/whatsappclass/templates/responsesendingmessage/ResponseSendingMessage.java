package com.ceramica.whatsappclass.templates.responsesendingmessage;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseSendingMessage {

    @JsonProperty("messaging_product")
    private String messagingProduct;
    @JsonProperty("contacts")
    private List<Contact> contacts;
    @JsonProperty("messages")
    private List<Message> messages;
}
