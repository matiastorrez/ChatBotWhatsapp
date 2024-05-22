package com.ceramica.repository;

import com.ceramica.entity.WhatsappMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IWhatsappRepository extends JpaRepository<WhatsappMessage,Long> {

    boolean existsByIdWhatsapp(String idWhatsapp);

    @Query("SELECT wm FROM WhatsappMessage wm WHERE wm.telefonoWa = :telefonoWa ORDER BY wm.id DESC")
    Optional<WhatsappMessage> findTopByTelefonoWaOrderByIdDesc(String telefonoWa);


    @Modifying
    @Query("DELETE FROM WhatsappMessage wm WHERE wm.telefonoWa = :telefonoWa")
    void deleteMessagesByTelephoneNumber(@Param("telefonoWa") String telefonoWa);

}
