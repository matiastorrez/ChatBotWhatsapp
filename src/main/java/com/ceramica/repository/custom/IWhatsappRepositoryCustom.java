package com.ceramica.repository.custom;

import com.ceramica.entity.WhatsappMessage;

import java.util.Optional;

public interface IWhatsappRepositoryCustom {

    boolean existeUnMensajeConElIdWhatsapp(String idWhatsapp);

    Optional<WhatsappMessage> obtenerUltimoMensajeDelChatPorNumeroDeTelefono(String numeroDeTelefono);

    WhatsappMessage guardar(WhatsappMessage whatsappMessage);

    void eliminarMensajesDelChatPorNumeroTelefono(String telefono);
}
