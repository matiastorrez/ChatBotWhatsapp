package com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Button {

    @JsonProperty("reply")
    private Reply reply;

    @JsonProperty("type")
    private String type;


}
