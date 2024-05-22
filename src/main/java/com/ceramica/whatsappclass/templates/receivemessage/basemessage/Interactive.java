package com.ceramica.whatsappclass.templates.receivemessage.basemessage;

import com.ceramica.whatsappclass.templates.receivemessage.basemessage.interactives.listmessage.ListReply;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Interactive {

    private String type;
    @JsonProperty("list_reply")
    private ListReply listReply;
    @JsonProperty("button_reply")
    private ListReply buttonReply;

}
