package com.lahmamsi.librarymanagementsystem.exception;

public class InvalidPageNumberException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPageNumberException(String message){
		super(message);
	}
}
