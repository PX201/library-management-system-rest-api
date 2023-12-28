package com.lahmamsi.librarymanagementsystem.utilities;

import org.apache.commons.lang3.EnumUtils;

import com.lahmamsi.librarymanagementsystem.exception.InvalidPageNumberException;
import com.lahmamsi.librarymanagementsystem.exception.NotAFieldException;

public class ParamValidationUtils {
	public static <E extends Enum<E>> void checkFieldValidity(Class<E> enumClass, String field) {
		if (!EnumUtils.isValidEnum(enumClass, field))
			throw new NotAFieldException(field + " is not a valid field");
	}

	public static void checkPageNumberValidity(int pageSize, int pageNumber, long numberOfEntities) {
		var totalPages = (int) Math.ceil((double) numberOfEntities / pageSize);
		if (pageNumber < 0 || pageNumber > totalPages)
			throw new InvalidPageNumberException(pageNumber + " is an invalid page number / out of range");
	}

}
