package com.ceramica.mensaje.dominio.puertos.salida;

import com.ceramica.mensaje.dominio.modelos.Mensaje;

import java.util.Optional;

public interface IMensajeRepositorioPuerto {

    boolean existeUnMensajeConElIdWhatsapp(String idWhatsapp);

    Optional<Mensaje> obtenerUltimoMensajeDelChatPorNumeroDeTelefono(String numeroDeTelefono);

    Mensaje guardar(Mensaje mensaje);

    void eliminarMensajesDelChatPorNumeroTelefono(String telefono);
}
