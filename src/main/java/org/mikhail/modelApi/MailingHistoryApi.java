package org.mikhail.modelApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mikhail.model.Mailing;
import org.mikhail.model.MailingMovement;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MailingHistoryApi {
    private Mailing mailing;
    private MailingStatus status;
    private List<MailingMovement> items;
}
