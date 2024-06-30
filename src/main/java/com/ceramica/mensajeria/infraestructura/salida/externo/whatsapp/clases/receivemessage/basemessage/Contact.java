package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Contact {

    private Profile profile;
    @JsonProperty("wa_id")
    private String waId;

}
