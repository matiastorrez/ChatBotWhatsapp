package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.sendmessage.basemessage.interactives.buttonmessage;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Action {

    @JsonProperty("buttons")
    private List<Button> buttons;



}
