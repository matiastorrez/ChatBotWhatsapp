package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Image {

    @JsonProperty("id")
    private String id;

}
