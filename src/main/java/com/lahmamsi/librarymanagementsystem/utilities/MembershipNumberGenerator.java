package com.lahmamsi.librarymanagementsystem.utilities;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;

public class MembershipNumberGenerator {

    // Define the format for the timestamp part of the membership number
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmss");


    // Generate a unique membership number
    public static String generateMembershipNumber() {
        // Generate a timestamp-based part
        String timestampPart = DATE_FORMAT.format(new Date());

        Random random = new Random();
		// Generate a random part
        String randomPart = random .nextInt(10) + "";
        // Combine the timestamp and random parts to form the membership number
        return randomPart + timestampPart ;
    }
}
