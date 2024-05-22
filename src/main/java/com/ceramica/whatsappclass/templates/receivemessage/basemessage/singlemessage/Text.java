package com.ceramica.whatsappclass.templates.receivemessage.basemessage.singlemessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Text {
    @JsonProperty("body")
    private String body;
}
