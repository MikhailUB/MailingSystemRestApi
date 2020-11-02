package org.mikhail.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class PostOffice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "zip_code", unique = true, length = 10, nullable = false)
    private String zipCode;
    @Column(name = "name", length = 50, nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;

    public PostOffice() {
    }

    public PostOffice(String zipCode, String name, String address) {
        this.zipCode = zipCode;
        this.name = name;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostOffice)) return false;

        PostOffice that = (PostOffice) o;

        return zipCode.equals(that.zipCode);
    }

    @Override
    public int hashCode() {
        return zipCode.hashCode();
    }
}
