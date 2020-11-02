package org.mikhail.modelApi;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovementApi {
    private Long mailingId;
    private String currentOfficeZip;
    private String nextOfficeZip;

    public MovementApi(Long mailingId, String currentOfficeZip, String nextOfficeZip) {
        this.mailingId = mailingId;
        this.currentOfficeZip = currentOfficeZip;
        this.nextOfficeZip = nextOfficeZip;
    }
}
