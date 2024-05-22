package com.ceramica.whatsappclass.templates.receivemessage.basemessage;

import com.ceramica.whatsappclass.templates.receivemessage.basemessage.interactives.Context;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.singlemessage.Text;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

    private String from;
    private String id;
    private String timestamp;
    private String type;
    private Interactive interactive;
    private Text text;
    private Context context;

}
