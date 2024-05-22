package com.ceramica.repository.custom.impl;

import com.ceramica.entity.WhatsappMessage;
import com.ceramica.repository.IWhatsappRepository;
import com.ceramica.repository.custom.IWhatsappRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class WhatsappRepositoryCustomImpl implements IWhatsappRepositoryCustom {

    @Autowired
    private IWhatsappRepository whatsappRepository;

    @Override
    public boolean existeUnMensajeConElIdWhatsapp(String idWhatsapp) {
        return this.whatsappRepository.existsByIdWhatsapp(idWhatsapp);
    }

    @Override
    public Optional<WhatsappMessage> obtenerUltimoMensajeDelChatPorNumeroDeTelefono(String numeroDeTelefono) {
        return this.whatsappRepository.findTopByTelefonoWaOrderByIdDesc(numeroDeTelefono);
    }

    @Override
    public WhatsappMessage guardar(WhatsappMessage whatsappMessage) {
        return this.whatsappRepository.save(whatsappMessage);
    }

    @Override
    public void eliminarMensajesDelChatPorNumeroTelefono(String telefono) {
        this.whatsappRepository.deleteMessagesByTelephoneNumber(telefono);
    }
}
