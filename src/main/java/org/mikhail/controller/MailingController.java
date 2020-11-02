package org.mikhail.controller;

import org.mikhail.model.*;
import org.mikhail.modelApi.*;
import org.mikhail.service.MailingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Почтовая система
 * REST API для обработки и отслеживания почтовых отправлений
 * /mailing - базовый path контроллера, GET - список всех отправлений
 * Методы контроллера:
 * /mailing/create  POST
 * /mailing/send    PUT
 * /mailing/accept  PUT
 * /mailing/deliver PUT
 * /mailing/history GET
 * /mailing/initialize GET - в пустой базе создает и выводит список почтовых отделений
 */
@RestController
@RequestMapping("/mailing")
public class MailingController {
    private final MailingService mailingService;

    @Autowired
    public MailingController(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    /** Регистрирует новое отправление в системе */
    @PostMapping("/create")
    public ResponseEntity<Mailing> create(@RequestBody Mailing mailing){
        Mailing newMailing = mailingService.create(mailing);
        return new ResponseEntity<>(newMailing, HttpStatus.CREATED);
    }

    /** Посылает отправление в другое отделение (убытие) */
    @PutMapping("/send")
    public MailingMovement send(@RequestBody MovementApi movementApi) {
        MailingMovement movement = mailingService.send(movementApi);
        return movement;
    }

    /** Принимает отправление в отделение (прибытие) */
    @PutMapping("/accept")
    public MailingMovement accept(@RequestBody MovementApi movementApi) {
        MailingMovement movement = mailingService.accept(movementApi);
        return movement;
    }

    /** Выдает отправление адресату */
    @PutMapping("/deliver")
    public MailingMovement deliver(@RequestBody MovementApi movementApi) {
        MailingMovement movement = mailingService.deliver(movementApi);
        return movement;
    }

    /** Выдает отправление адресату */
    @GetMapping("/history")
    public MailingHistoryApi getHistory(@RequestParam Long id) {

        MailingHistoryApi result = mailingService.getHistory(id);
        return result;
    }

    @GetMapping
    public List<Mailing> getMailing() {
        return mailingService.findAll();
    }

    @GetMapping("/initialize")
    public List<PostOffice> initialize() {
        return mailingService.initialize();
    }
}
