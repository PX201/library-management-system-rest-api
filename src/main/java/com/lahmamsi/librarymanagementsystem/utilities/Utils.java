package com.lahmamsi.librarymanagementsystem.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class Utils {

	public static Pageable page(int pageSize, int pageNumber, String field) {
		return PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, field));
	}
	
	
}
