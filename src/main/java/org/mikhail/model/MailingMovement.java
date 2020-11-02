package org.mikhail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class MailingMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mailing_id")
    @JsonIgnore
    private Mailing mailing;

    @ManyToOne
    @JoinColumn(name = "post_office_id")
    private PostOffice postOffice;

    @ManyToOne
    @JoinColumn(name = "next_office_id")
    private PostOffice nextOffice;

    @Column(name = "operation_date")
    private LocalDateTime operationDate;

    @Column(name = "operation", length = 32)
    @Enumerated(EnumType.STRING)
    private Operation operation;

    public MailingMovement() {
    }

    public MailingMovement(Mailing mailing, PostOffice postOffice, PostOffice nextOffice, Operation operation) {
        this.mailing = mailing;
        this.postOffice = postOffice;
        this.nextOffice = nextOffice;
        this.operationDate = LocalDateTime.now();
        this.operation = operation;
    }
}
