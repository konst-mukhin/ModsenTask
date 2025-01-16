package org.example.booktrackerservice.repository;

import org.example.booktrackerservice.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Integer> {
    Optional<BookStatus> findByBookId(Integer bookId);
}
