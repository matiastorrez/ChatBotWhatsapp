package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.singlemessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Text {

    @JsonProperty("preview_url")
    private boolean previewUrl;

    @JsonProperty("body")
    private String body;

}
