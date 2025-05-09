package com.exam.util;

import java.util.Scanner;

public class InputUtil {
    public static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("❌ Invalid input! Please enter a valid number: ");
            }
        }
    }
}
