package org.mikhail.service;

import org.mikhail.model.Mailing;
import org.mikhail.model.MailingMovement;
import org.mikhail.model.PostOffice;
import org.mikhail.modelApi.MailingHistoryApi;
import org.mikhail.modelApi.MovementApi;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MailingService {
    @Transactional
    Mailing create(Mailing mailing);

    MailingMovement send(MovementApi movement);

    MailingMovement accept(MovementApi movement);

    MailingMovement deliver(MovementApi movement);

    MailingHistoryApi getHistory(Long mailingId);

    List<Mailing> findAll();

    List<PostOffice> initialize();
}
