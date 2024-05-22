package com.ceramica.patrones.chat.state;

import com.ceramica.entity.WhatsappMessage;
import com.ceramica.repository.custom.IWhatsappRepositoryCustom;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.patrones.chat.state.enums.EstadoChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

public abstract class EstadoMensaje {

    @Autowired
    protected IWhatsappRepositoryCustom whatsappRepositoryCustom;

    @Autowired
    protected RestClient restClient;


    public abstract EstadoChat obtenerEstado();

    public abstract void procesarInformacion(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage);

    public abstract void enviarRespuestas(ReceiveBaseMessage receiveMessage, WhatsappMessage whatsappMessage);
}
