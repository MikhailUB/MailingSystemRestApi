package org.mikhail.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mikhail.model.Mailing;
import org.mikhail.model.MailingMovement;
import org.mikhail.model.MailingType;
import org.mikhail.model.Operation;
import org.mikhail.modelApi.MovementApi;
import org.mikhail.repository.MailingMovementRepository;
import org.mikhail.repository.MailingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailingServiceImplTest {

    private final String startOffice = "344000";
    private final String endOffice = "344020";

    @Autowired
    private MailingRepository mailingRepository;
    @Autowired
    private MailingMovementRepository movementRepository;
    @Autowired
    private MailingServiceImpl mailingService;

    @Test
    public void createAndFindAll() {
        Mailing mailing = new Mailing(MailingType.LETTER, startOffice, endOffice, "Ростов-на-Дону", "create Михаил");

        mailing = mailingService.create(mailing);
        final Long id = mailing.getId();
        List<Mailing> all = mailingService.findAll();

        assertEquals(1, all.stream().filter(m -> m.getId().equals(id)).count());
    }

    @Test
    public void send() {
        Mailing mailing = new Mailing(MailingType.LETTER, startOffice, endOffice, "Ростов-на-Дону", "send Андрей");
        mailing = mailingService.create(mailing);

        MovementApi movement = new MovementApi(mailing.getId(), startOffice, endOffice);
        MailingMovement sendOperation = mailingService.send(movement);
        Optional<MailingMovement> byId = movementRepository.findById(sendOperation.getId());

        assertTrue(byId.get().getPostOffice().getZipCode().equals(startOffice) && byId.get().getOperation() == Operation.DEPARTURE);
    }

    @Test
    public void accept() {
    }

    @Test
    public void deliver() {
    }

    @Test
    public void getHistory() {
    }
}