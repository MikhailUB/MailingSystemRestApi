package org.mikhail.service;

import lombok.extern.slf4j.Slf4j;
import org.mikhail.exception.*;
import org.mikhail.model.*;
import org.mikhail.modelApi.MailingHistoryApi;
import org.mikhail.modelApi.MailingStatus;
import org.mikhail.modelApi.MovementApi;
import org.mikhail.repository.*;
import org.mikhail.util.MovementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * Сервис реализующий всю логику по работе с отправлениями
 * создание, передвижения, выдача, получение истории
 * обрабатывает вызовы от контроллера, оперирует данными через репозитории,
 * обрабатывает их и возвращает результат контроллеру
 */
@Slf4j
@Service
public class MailingServiceImpl implements MailingService {
    private final MailingRepository mailingRepository;
    private final MailingMovementRepository movementRepository;
    private final PostOfficeRepository officeRepository;
    private final MovementValidator movementValidator;

    @Autowired
    public MailingServiceImpl(MailingRepository mailingRepository, MailingMovementRepository movementRepository,
                              PostOfficeRepository officeRepository, MovementValidator movementValidator) {
        this.mailingRepository = mailingRepository;
        this.movementRepository = movementRepository;
        this.officeRepository = officeRepository;
        this.movementValidator = movementValidator;
    }

    @Override
    @Transactional
    public Mailing create(Mailing mailing){
        log.info("IN MailingServiceImpl create {}", mailing);

        List<String> notZips = new ArrayList<>();
        PostOffice startOffice = officeRepository.findByZipCode(mailing.getStartOfficeZip());
        if (startOffice == null)
            notZips.add(mailing.getStartOfficeZip());

        PostOffice recipientOffice = startOffice;
        if (!mailing.getRecipientOfficeZip().equals(mailing.getStartOfficeZip()))
        {
            recipientOffice = officeRepository.findByZipCode(mailing.getRecipientOfficeZip());
            if (recipientOffice == null)
                notZips.add(mailing.getRecipientOfficeZip());
        }
        if (!notZips.isEmpty())
            throw new BadRequestException("Не найдены отделения с индексами " + String.join(",", notZips));

        mailing.setStartOffice(startOffice);
        mailing.setRecipientOffice(recipientOffice);
        mailing = mailingRepository.save(mailing);

        final Operation register = Operation.REGISTRATION;
        MailingMovement movement = new MailingMovement(mailing, startOffice, null, register);
        movementRepository.save(movement);

        return mailing;
    }

    @Override
    public List<Mailing> findAll() {
        log.info("IN MailingServiceImpl findAll");
        return mailingRepository.findAll();
    }

    @Override
    public MailingMovement send(MovementApi movement) {
        log.info("IN MailingServiceImpl send {}", movement);

        movementValidator.validate(movement, true);
        final Operation departure = Operation.DEPARTURE;

        MailingMovement result = new MailingMovement(movementValidator.getMailing(), movementValidator.getCurrentOffice(),
                movementValidator.getNextOffice(), departure);
        result = movementRepository.save(result);

        return result;
    }

    @Override
    public MailingMovement accept(MovementApi movement) {
        log.info("IN MailingServiceImpl accept {}", movement);

        movementValidator.validate(movement, false);
        final Operation arrival = Operation.ARRIVAL;

        MailingMovement result = new MailingMovement(movementValidator.getMailing(), movementValidator.getCurrentOffice(),
                movementValidator.getNextOffice(), arrival);
        result = movementRepository.save(result);

        return result;
    }

    @Override
    public MailingMovement deliver(MovementApi movement) {
        log.info("IN MailingServiceImpl deliver {}", movement);

        movementValidator.validate(movement, false);
        final Operation delivery = Operation.DELIVERY;

        MailingMovement result = new MailingMovement(movementValidator.getMailing(), movementValidator.getCurrentOffice(),
                movementValidator.getNextOffice(), delivery);
        result = movementRepository.save(result);

        return result;
    }

    @Override
    public MailingHistoryApi getHistory(Long mailingId) {
        log.info("IN MailingServiceImpl getHistory {}", mailingId);

        List<MailingMovement> list = movementRepository.findAllByMailingId(mailingId);
        Mailing mailing = null;
        MailingStatus status = MailingStatus.NOT_FOUND;

        if (!list.isEmpty()) {
            MailingMovement lastMovement = list.get(0);
            mailing = lastMovement.getMailing();

            // вычисляем статус по офису назначения, последней операции и ее типу
            if (lastMovement.getPostOffice().equals(mailing.getRecipientOffice())) {
                if (lastMovement.getOperation() == Operation.DELIVERY) {
                    status = MailingStatus.DELIVERED;
                }
                else {
                    status = MailingStatus.READY_TO_ISSUE;
                }
            }
            else if (lastMovement.getOperation() == Operation.REGISTRATION) {
                status = MailingStatus.CREATED;
            }
            else {
                status = MailingStatus.IN_TRANSIT;
            }
        }
        return new MailingHistoryApi(mailing, status, list);
    }

    @Override
    public List<PostOffice> initialize() {
        List<PostOffice> offices = officeRepository.findAll();
        if (offices.isEmpty()) {
            PostOffice office = new PostOffice("344000", "Центральное отделение", "Ростов-на-Дону");
            officeRepository.save(office);

            office = new PostOffice("344010", "10-е отделение", "Ворошиловский пр-кт, Ростов-на-Дону");
            officeRepository.save(office);

            office = new PostOffice("344020", "20-е отделение", "Буденовский пр-кт, Ростов-на-Дону");
            officeRepository.save(office);

            office = new PostOffice("344030", "30-е отделение", "Космонавтов пр-кт, Ростов-на-Дону");
            officeRepository.save(office);

            office = new PostOffice("344040", "40-е отделение", "Стачки пр-кт, Ростов-на-Дону");
            officeRepository.save(office);

            offices = officeRepository.findAll();
        }
        return offices;
    }
}
