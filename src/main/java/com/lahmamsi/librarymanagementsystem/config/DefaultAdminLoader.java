package com.lahmamsi.librarymanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lahmamsi.librarymanagementsystem.user.LibrarianService;

import jakarta.annotation.PostConstruct;

/**
 * 
 * @author Aiman
 *This Component made to help initializing a default Admin during the application start-up, As the register endpoint is secured
 */
@Component
public class DefaultAdminLoader {

	@Autowired
    private final LibrarianService service;

    public DefaultAdminLoader(LibrarianService service) {
        this.service = service;
    }

    /**
     * Initializes a default admin user when the application starts.
     * Invokes the LibrarianService method to set a default admin in the database for initial login.
     */
    @PostConstruct
    public void createDefaultAdmin() {
        service.createDefaultAdminUser(); //Set a default Admin in database for the first login 
    }
}

