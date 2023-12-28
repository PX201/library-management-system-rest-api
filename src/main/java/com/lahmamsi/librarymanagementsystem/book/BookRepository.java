package com.lahmamsi.librarymanagementsystem.book;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Aiman 
 * The book repository helps to make CRUD directly to the Book Table in database
 * */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>{
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author, Pageable pageable);
    
//    @Query(value = "SELECT COUNT(*) FROM book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',:keywords,'%')) OR b.author LIKE LOWER(CONCAT('%',:keywords,'%')) OR b.description LIKE LOWER(CONCAT('%',:keywords,'%'))", nativeQuery = true)
//    long countByTitleOrAuthorORDescription(@Param(value = "keywords") String keywords);
    
    long countByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(String keyword1, String keyword2);

    Optional<Book> findByIsbn(String isbn);
    
	List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

	List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbn(String title, String author,String idbn);

}
