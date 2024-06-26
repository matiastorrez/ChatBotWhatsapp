package com.ceramica.whatsappclass.templates.sendmessage.basemessage.interactives.buttonmessage;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Reply {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;
}
