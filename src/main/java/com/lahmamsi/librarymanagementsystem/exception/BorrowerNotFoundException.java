package com.lahmamsi.librarymanagementsystem.exception;

public class BorrowerNotFoundException extends RuntimeException{

	
	private static final long serialVersionUID = 1L;

	public BorrowerNotFoundException(String message) {
		super(message);
	}
}
