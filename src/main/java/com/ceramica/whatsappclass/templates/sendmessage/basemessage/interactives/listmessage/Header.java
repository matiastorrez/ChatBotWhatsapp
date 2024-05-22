package com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.listmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Header {
    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;
}
