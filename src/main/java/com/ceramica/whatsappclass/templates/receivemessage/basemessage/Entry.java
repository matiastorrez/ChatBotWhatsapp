package com.ceramica.whatsappclass.templates.receivemessage.basemessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Entry {

    private String id;
    private List<Change> changes;

}
