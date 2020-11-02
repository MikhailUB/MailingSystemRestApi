package org.mikhail.repository;

import org.mikhail.model.Mailing;
import org.mikhail.model.MailingMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MailingMovementRepository extends JpaRepository<MailingMovement, Long> {
    @Query("select mm from MailingMovement mm where mm.mailing.id = :id order by mm.operationDate desc")
    List<MailingMovement> findAllByMailingId(@Param("id") Long id);
}
