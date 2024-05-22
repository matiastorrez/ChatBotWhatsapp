package com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.listmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Action {

    @JsonProperty("button")
    private String button;

    @JsonProperty("sections")
    private List<Section> sections;
}
