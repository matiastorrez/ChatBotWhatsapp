package com.ceramica.whatsappclass.templates.responsesendingmessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Message {

    @JsonProperty("id")
    private String id;

}
