package com.lahmamsi.librarymanagementsystem.book;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Aiman 
 * The Genre repository helps to make CRUD directly to the Genre Table in database
 * */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer>{
	Optional<Genre> findById(int  id);
}
