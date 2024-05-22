package com.ceramica.service;


import com.ceramica.entity.WhatsappMessage;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.whatsappclass.templates.receivemessage.basemessage.Value;
import com.ceramica.whatsappclass.templates.receivemessage.webhook.RequestWebhookWhatsapp;
import com.ceramica.patrones.chat.state.EstadoMensaje;
import com.ceramica.patrones.chat.state.EstadoMensajeFactoria;
import com.ceramica.repository.custom.IWhatsappRepositoryCustom;
import com.ceramica.patrones.chat.state.enums.EstadoChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ApiWhatsappService {


    @Autowired
    private EstadoMensajeFactoria estadoMensajeFactoria;

    @Autowired
    private IWhatsappRepositoryCustom whatsappRepositoryCustom;


    @org.springframework.beans.factory.annotation.Value("${whatsapp.webhook.token}")
    private String whatsappWebhookToken;


    public String verifyebhook(RequestWebhookWhatsapp request) {
        String mode = request.hub_mode();
        String token = request.hub_verify_token();
        String challenge = request.hub_challenge();
        try {
            if (mode == null || token == null) {
                throw new Exception("modo o token son nulos");
            }

            if (mode.equals("suscribe") && token.equals(whatsappWebhookToken)) {
                System.out.println(mode);
                System.out.println(token);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return challenge;

    }

    //este  metodo recibe el mensaje que me envian
    @Transactional
    public void processebhook(ReceiveBaseMessage receiveMessage) {

        try {

            Value value = receiveMessage.getEntry().get(0).getChanges().get(0).getValue();

            if(value.getMessages() == null){
                throw new Exception("No es un mensaje enviado");
            }

            //verificar si el id de whatsapp no existe
            String numeroTelefono = value.getContacts().get(0).getWaId();
            boolean existeMensajeConEsteId = whatsappRepositoryCustom.existeUnMensajeConElIdWhatsapp(numeroTelefono);

            if(existeMensajeConEsteId){
                throw new Exception("Este mensaje ya fue recibido, no puede enviar el mismo mensaje con este id");
            }

            //obtener el ultimo mensaje del usuario por numero de telefono
            Optional<WhatsappMessage> optionalWhatsappMessage = this.whatsappRepositoryCustom.obtenerUltimoMensajeDelChatPorNumeroDeTelefono(numeroTelefono);

            if(optionalWhatsappMessage.isEmpty()){
                //si no existe un mensaje previo significa que es la primera ves que habla con nosotros
                WhatsappMessage iniciandoPrimerMensaje = new WhatsappMessage();
                iniciandoPrimerMensaje.setIdWhatsapp(value.getMessages().get(0).getId());
                iniciandoPrimerMensaje.setEstado(EstadoChat.PRIMER_MENSAJE);
                iniciandoPrimerMensaje.setTelefonoWa(value.getContacts().get(0).getWaId());
                iniciandoPrimerMensaje.setMensajeRecibido(value.getMessages().get(0).getText().getBody());
                iniciandoPrimerMensaje.setTimestampWa(value.getMessages().get(0).getTimestamp());
                iniciandoPrimerMensaje.setContextWaForNextResponse(null);

                WhatsappMessage whatsappMessage = this.whatsappRepositoryCustom.guardar(iniciandoPrimerMensaje);

                //obtener su estado
                EstadoChat estadoActualDelChat = whatsappMessage.getEstado();

                EstadoMensaje estadoMensaje = estadoMensajeFactoria.obtenerEstado(estadoActualDelChat);

                //responder apartir de ese estado
                estadoMensaje.enviarRespuestas(receiveMessage,whatsappMessage);

            }else{
                //sino vemos cual fue el ultimo mensaje que nos mando el usuario
                WhatsappMessage whatsappMessage = optionalWhatsappMessage.get();
                //obtener su estado
                EstadoChat estadoActualDelChat = whatsappMessage.getEstado();

                EstadoMensaje estadoMensaje = estadoMensajeFactoria.obtenerEstado(estadoActualDelChat);

                //responder apartir de ese estado
                estadoMensaje.enviarRespuestas(receiveMessage,whatsappMessage);

            }

        }catch (Exception ex){
            System.out.println(ex.getMessage());
            //debemos enviar mensaje al usuario diciendo que hubo un problema con el sistema
        }

    }


}
