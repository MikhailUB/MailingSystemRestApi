package org.mikhail.modelApi;

import lombok.*;
import org.mikhail.model.MailingType;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MailingApi {
    private MailingType mailingType;
    private String startOfficeZip;
    private String recipientOfficeZip;
    private String recipientAddress;
    private String recipientName;

    public MailingApi(MailingType mailingType, String startOfficeZip, String recipientOfficeZip, String recipientAddress, String recipientName) {
        this.mailingType = mailingType;
        this.startOfficeZip = startOfficeZip;
        this.recipientOfficeZip = recipientOfficeZip;
        this.recipientAddress = recipientAddress;
        this.recipientName = recipientName;
    }
}
