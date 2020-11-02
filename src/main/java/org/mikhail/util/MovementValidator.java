package org.mikhail.util;

import lombok.Getter;
import org.mikhail.exception.BadRequestException;
import org.mikhail.model.*;
import org.mikhail.modelApi.MovementApi;
import org.mikhail.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Scope("prototype")
public class MovementValidator {
    @Getter
    private Mailing mailing;
    @Getter
    private PostOffice currentOffice;
    @Getter
    private PostOffice nextOffice;

    private final MailingRepository mailingRepository;
    private final PostOfficeRepository officeRepository;

    @Autowired
    public MovementValidator(MailingRepository mailingRepository, PostOfficeRepository officeRepository) {
        this.mailingRepository = mailingRepository;
        this.officeRepository = officeRepository;
    }

    public void validate(MovementApi movement, boolean checkToOffice) {
        Optional<Mailing> mailing = mailingRepository.findById(movement.getMailingId());
        PostOffice currentOffice = officeRepository.findByZipCode(movement.getCurrentOfficeZip());
        PostOffice nextOffice = checkToOffice ? officeRepository.findByZipCode(movement.getNextOfficeZip()) : null;

        StringBuilder errors = new StringBuilder();
        if (!mailing.isPresent()) {
            errors.append("Неверный идентификатор отправления ").append(movement.getMailingId()).append(System.lineSeparator());
        }
        if (currentOffice == null) {
            errors.append("Неверный индекс отделения ").append(movement.getCurrentOfficeZip()).append(System.lineSeparator());
        }
        if (checkToOffice && nextOffice == null) {
            errors.append("Неверный индекс отделения ").append(movement.getNextOfficeZip()).append(System.lineSeparator());
        }
        if (errors.length() > 0)
            throw new BadRequestException(errors.toString());

        this.mailing = mailing.get();
        this.currentOffice = currentOffice;
        this.nextOffice = nextOffice;
    }
}
