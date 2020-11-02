package org.mikhail.repository;

import org.mikhail.model.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostOfficeRepository extends JpaRepository<PostOffice, String> {

    PostOffice findByZipCode(String zipCode);
}
