package org.mikhail.repository;

import org.mikhail.model.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailingRepository extends JpaRepository<Mailing, Long> {

}
