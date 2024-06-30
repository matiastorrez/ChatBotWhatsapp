package com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage;

import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.interactives.Context;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.singlemessage.Text;
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
