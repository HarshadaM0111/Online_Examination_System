package com.exam.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("[AUDIT] [" + timestamp + "] " + message);
    }
    
    public static void logAction(int adminId, String name, String action) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[AUDIT LOG] Time: " + timestamp);
        System.out.println("Admin ID   : " + adminId);
        System.out.println("Action     : " + action.toUpperCase());
        System.out.println("Performed by: " + name);
        System.out.println();
    }
}
