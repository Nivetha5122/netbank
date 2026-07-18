package com.netbank.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AccountUtils {

    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {
        // Format: NB + 14 digits
        StringBuilder sb = new StringBuilder("NB");
        for (int i = 0; i < 14; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateReferenceNumber() {
        // Format: TXN + yyyyMMddHHmmss + 4 random digits
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int rand = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "TXN" + timestamp + rand;
    }

    public static String generateBillReference() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int rand = ThreadLocalRandom.current().nextInt(100, 999);
        return "BILL" + timestamp + rand;
    }

    private AccountUtils() {}
}
