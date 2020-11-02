package org.mikhail.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Mailing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "mailing_type", length = 32)
    @Enumerated(EnumType.STRING)
    private MailingType mailingType;

    @ManyToOne
    @JoinColumn(name = "start_office_id")
    private PostOffice startOffice;
    @Transient
    private String startOfficeZip;

    @ManyToOne
    @JoinColumn(name = "recipient_office_id")
    private PostOffice recipientOffice;
    @Transient
    private String recipientOfficeZip;

    @Column(name = "recipient_address", nullable = false)
    private String recipientAddress;
    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    public Mailing() {
    }

    public Mailing(MailingType mailingType, String startOfficeZip, String recipientOfficeZip,
                   String recipientAddress, String recipientName) {
        this.mailingType = mailingType;
        this.startOfficeZip = startOfficeZip;
        this.recipientOfficeZip = recipientOfficeZip;
        this.recipientAddress = recipientAddress;
        this.recipientName = recipientName;
    }
}
