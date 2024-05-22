package com.ceramica.controller;

import com.ceramica.whatsappclass.templates.receivemessage.basemessage.ReceiveBaseMessage;
import com.ceramica.whatsappclass.templates.receivemessage.webhook.RequestWebhookWhatsapp;
import com.ceramica.service.ApiWhatsappService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/whatsapp")
@CrossOrigin(origins = "*")
public class WhatsappController {


    private final ApiWhatsappService apiWhatsappService;


    public WhatsappController(ApiWhatsappService apiWhatsappService) {
        this.apiWhatsappService = apiWhatsappService;
    }

    @GetMapping(value = "/whatsapp-webhook", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> recibirWebhook(@RequestParam("hub.mode") String hubMode, @RequestParam("hub.challenge") String hubChallenge, @RequestParam("hub.verify_token") String verifyToken) {

        String challenge = apiWhatsappService.verifyebhook(new RequestWebhookWhatsapp(hubMode, hubChallenge, verifyToken));

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));

        return new ResponseEntity<>(challenge, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/whatsapp-webhook", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> recibirWebhook(@RequestBody ReceiveBaseMessage receiveMessage) {

        apiWhatsappService.processebhook(receiveMessage);
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));

        return new ResponseEntity<>("holi", httpHeaders, HttpStatus.OK);
    }

}
