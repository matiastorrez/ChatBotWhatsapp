package com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage;

import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.Body;
import com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.Footer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Interactive {

    @JsonProperty("type")
    private String type;

    @JsonProperty("header")
    private Header header;

    @JsonProperty("body")
    private Body body;

    @JsonProperty("footer")
    private Footer footer;

    @JsonProperty("action")
    private Action action;
}
