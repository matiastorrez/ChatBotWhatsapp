package com.ceramica.mensaje.infraestructura.salida.basededatos.repositorio;

import com.ceramica.mensaje.infraestructura.salida.basededatos.entidad.MensajeWhatsappEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IMensajeRepositorio extends JpaRepository<MensajeWhatsappEntidad,Long> {

    boolean existsByIdWhatsapp(String idWhatsapp);

    @Query("SELECT wm FROM WhatsappMessage wm WHERE wm.telefonoWa = :telefonoWa ORDER BY wm.id DESC")
    Optional<MensajeWhatsappEntidad> findTopByTelefonoWaOrderByIdDesc(String telefonoWa);


    @Modifying
    @Query("DELETE FROM WhatsappMessage wm WHERE wm.telefonoWa = :telefonoWa")
    void deleteMessagesByTelephoneNumber(@Param("telefonoWa") String telefonoWa);

}
