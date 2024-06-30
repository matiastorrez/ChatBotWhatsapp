package com.ceramica.mensaje.infraestructura.salida.basededatos.repositorio.adaptadores;

import com.ceramica.mensaje.aplicacion.MensajeWhatsappAplicacion;
import com.ceramica.mensaje.dominio.modelos.Mensaje;
import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import com.ceramica.mensaje.infraestructura.salida.basededatos.repositorio.IMensajeRepositorio;
import com.ceramica.mensaje.dominio.puertos.salida.IMensajeRepositorioPuerto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class MensajeRepositorioAdaptador implements IMensajeRepositorioPuerto {

    @Autowired
    private IMensajeRepositorio whatsappRepository;

    public MensajeRepositorioAdaptador(IMensajeRepositorio whatsappRepository) {
        this.whatsappRepository = whatsappRepository;
    }

    @Override
    public boolean existeUnMensajeConElIdWhatsapp(String idWhatsapp) {
        return this.whatsappRepository.existsByIdWhatsapp(idWhatsapp);
    }

    @Override
    public Optional<Mensaje> obtenerUltimoMensajeDelChatPorNumeroDeTelefono(String numeroDeTelefono) {
        return this.whatsappRepository.findTopByTelefonoWaOrderByIdDesc(numeroDeTelefono).map(this::aAplicacion);
    }

    @Override
    public Mensaje guardar(Mensaje mensaje) {
        return this.aAplicacion(this.whatsappRepository.save(this.aEntidad(mensaje)));
    }

    @Override
    public void eliminarMensajesDelChatPorNumeroTelefono(String telefono) {
        this.whatsappRepository.deleteMessagesByTelephoneNumber(telefono);
    }


    private Mensaje aAplicacion(MensajeWhatsappEntidad entidad){
        return MensajeWhatsappAplicacion.builder()
                .id(entidad.getId())
                .fechaDeCreacion(entidad.getFechaDeCreacion())
                .mensajeRecibido(entidad.getMensajeRecibido())
                .telefono(entidad.getTelefonoWa())
                .idWhatsapp(entidad.getIdWhatsapp())
                .contextWaForNextResponse(entidad.getContextWaForNextResponse())
                .timestampWa(entidad.getTimestampWa())
                .estado(entidad.getEstado())
                .build();
    }

    private MensajeWhatsappEntidad aEntidad(Mensaje mensaje){

        MensajeWhatsappAplicacion mensajeWhatsappAplicacion = (MensajeWhatsappAplicacion) mensaje;

        MensajeWhatsappEntidad mensajeWhatsappEntidad = new MensajeWhatsappEntidad();
        mensajeWhatsappEntidad.setFechaDeCreacion(mensajeWhatsappAplicacion.getFechaDeCreacion());
        mensajeWhatsappEntidad.setMensajeRecibido(mensajeWhatsappAplicacion.getMensajeRecibido());
        mensajeWhatsappEntidad.setTelefonoWa(mensajeWhatsappAplicacion.getTelefono());
        mensajeWhatsappEntidad.setIdWhatsapp(mensajeWhatsappAplicacion.getIdWhatsapp());
        mensajeWhatsappEntidad.setContextWaForNextResponse(mensajeWhatsappAplicacion.getContextWaForNextResponse());
        mensajeWhatsappEntidad.setTimestampWa(mensajeWhatsappAplicacion.getTimestampWa());
        mensajeWhatsappEntidad.setEstado(mensajeWhatsappAplicacion.getEstado());


        return mensajeWhatsappEntidad;
    }

}
