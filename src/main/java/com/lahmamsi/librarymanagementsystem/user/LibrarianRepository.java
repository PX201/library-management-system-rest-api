package com.lahmamsi.librarymanagementsystem.user;

/**
 * @author Aiman
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {

	Optional<Librarian> findByEmail(String email);
}
