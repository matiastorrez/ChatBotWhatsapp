package com.ceramica.mensajeria.aplicacion.casosdeuso.whatsapp;

import com.ceramica.mensajeria.dominio.puertos.entrada.RecibirMensajeCasoDeUsoPuerto;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.Value;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.EstadoMensaje;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.EstadoMensajeFactoria;
import com.ceramica.mensaje.aplicacion.casosdeuso.patron.estado.enums.EstadoChat;

import java.util.Optional;

public class WhatsappRecibirMensajeCasoDeUsoAdaptador implements RecibirMensajeCasoDeUsoPuerto {

    private EstadoMensajeFactoria estadoMensajeFactoria;
    private IMensajeRepositorioPuerto whatsappRepositoryCustom;

    public WhatsappRecibirMensajeCasoDeUsoAdaptador(EstadoMensajeFactoria estadoMensajeFactoria, IMensajeRepositorioPuerto whatsappRepositoryCustom) {
        this.estadoMensajeFactoria = estadoMensajeFactoria;
        this.whatsappRepositoryCustom = whatsappRepositoryCustom;
    }

    @Override
    public void recibirMensaje(Object mensaje) {

        ReceiveBaseMessage mensajeRecibido = (ReceiveBaseMessage) mensaje;

        try {

            Value value = mensajeRecibido.getEntry().get(0).getChanges().get(0).getValue();

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
            Optional<MensajeWhatsappEntidad> optionalWhatsappMessage = this.whatsappRepositoryCustom.obtenerUltimoMensajeDelChatPorNumeroDeTelefono(numeroTelefono);

            if(optionalWhatsappMessage.isEmpty()){
                //si no existe un mensaje previo significa que es la primera ves que habla con nosotros
                MensajeWhatsappEntidad iniciandoPrimerMensaje = new MensajeWhatsappEntidad();
                iniciandoPrimerMensaje.setIdWhatsapp(value.getMessages().get(0).getId());
                iniciandoPrimerMensaje.setEstado(EstadoChat.PRIMER_MENSAJE);
                iniciandoPrimerMensaje.setTelefonoWa(value.getContacts().get(0).getWaId());
                iniciandoPrimerMensaje.setMensajeRecibido(value.getMessages().get(0).getText().getBody());
                iniciandoPrimerMensaje.setTimestampWa(value.getMessages().get(0).getTimestamp());
                iniciandoPrimerMensaje.setContextWaForNextResponse(null);

                MensajeWhatsappEntidad mensajeWhatsappEntidad = this.whatsappRepositoryCustom.guardar(iniciandoPrimerMensaje);

                //obtener su estado
                EstadoChat estadoActualDelChat = mensajeWhatsappEntidad.getEstado();

                EstadoMensaje estadoMensaje = estadoMensajeFactoria.obtenerEstado(estadoActualDelChat);

                //responder apartir de ese estado
                estadoMensaje.enviarRespuestas(mensajeRecibido, mensajeWhatsappEntidad);

            }else{
                //sino vemos cual fue el ultimo mensaje que nos mando el usuario
                MensajeWhatsappEntidad mensajeWhatsappEntidad = optionalWhatsappMessage.get();
                //obtener su estado
                EstadoChat estadoActualDelChat = mensajeWhatsappEntidad.getEstado();

                EstadoMensaje estadoMensaje = estadoMensajeFactoria.obtenerEstado(estadoActualDelChat);

                //responder apartir de ese estado
                estadoMensaje.enviarRespuestas(mensajeRecibido, mensajeWhatsappEntidad);

            }

        }catch (Exception ex){
            System.out.println(ex.getMessage());
            //debemos enviar mensaje al usuario diciendo que hubo un problema con el sistema
        }
    }
}
