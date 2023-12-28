package com.lahmamsi.librarymanagementsystem.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author Aiman
 *
 */
@RestController
@RequestMapping("/api/v1/test")
public class TestController {
	
	// Endpoint to verify if the authentication is functioning properly.
	// This endpoint is used to test if the authentication setup is working.
	@GetMapping("")
	public ResponseEntity<Void> test(){
		return ResponseEntity.ok().build();
	}

}
