package com.ceramica.mensajeria.infraestructura.entrada.controlador;

import com.ceramica.mensajeria.aplicacion.servicios.MensajeriaServicio;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.mensajeria.infraestructura.salida.externo.whatsapp.clases.receivemessage.webhook.RequestWebhookWhatsapp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/whatsapp")
@CrossOrigin(origins = "*")
public class MensajeriaControlador {


    private final MensajeriaServicio mensajeriaServicio;


    public MensajeriaControlador(MensajeriaServicio mensajeriaServicio) {
        this.mensajeriaServicio = mensajeriaServicio;
    }

    @GetMapping(value = "/whatsapp-webhook", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> recibirWebhook(@RequestParam("hub.mode") String hubMode, @RequestParam("hub.challenge") String hubChallenge, @RequestParam("hub.verify_token") String verifyToken) {

        String challenge = mensajeriaServicio.verificarWebhook(new RequestWebhookWhatsapp(hubMode, hubChallenge, verifyToken));

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));

        return new ResponseEntity<>(challenge, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/whatsapp-webhook", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> recibirWebhook(@RequestBody ReceiveBaseMessage receiveMessage) {
        mensajeriaServicio.recibirMensaje(receiveMessage);
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));

        return new ResponseEntity<>("holi", httpHeaders, HttpStatus.OK);
    }

}
