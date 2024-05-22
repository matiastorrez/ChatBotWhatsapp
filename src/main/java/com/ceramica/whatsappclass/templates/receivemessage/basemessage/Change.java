package com.ceramica.whatsappclass.templates.receivemessage.basemessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Change {
    private String field;
    private Value value;
}
