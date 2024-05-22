package com.ceramica.whatsappclass.templates.sendmessage.basemessage.singlemessage;

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
