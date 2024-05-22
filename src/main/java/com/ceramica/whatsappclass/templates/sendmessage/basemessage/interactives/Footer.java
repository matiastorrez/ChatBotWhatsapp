package com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Footer {

    @JsonProperty("text")
    private String text;
}
