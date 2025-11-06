package org.example.console;

import org.example.util.AnsiColors;

import java.util.Scanner;

public class ConsoleIO {
    private static final Scanner scanner = new Scanner(System.in);
    public static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim(); // remove leading/trailing whitespace
    }
    // gets an integer. Prevents exceptions by using try/catch to catch bad input
    public static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return readInt(prompt);
        }
    }

    // overloaded method to read an integer within a range from min to max
    public static int readInt(String prompt, int min, int max) {
        int value = readInt(prompt);
        while (value < min || value > max) {
            System.out.println("Input not within range of " + min + " and " + max + ". Please try again.");
            value = readInt(prompt);
        }
        return value;
    }
    public static void println(String message, String color) {
        System.out.println(color + message); System.out.print(AnsiColors.RESET);
    }
    public static void print(String message, String color) {
        System.out.print(color + message); System.out.print(AnsiColors.RESET);
    }

    public static void printf(String color, String format, Object... args) {
        String msg = String.format(format, args);
        System.out.print(color);
        System.out.print(msg);
        System.out.print(AnsiColors.RESET); // so later prints aren’t stuck colored
    }
    public static void printfn(String color, String format, Object... args) {
        printf(color, format + "%n", args);
    }
    public static double readDouble(String prompt) {
        Scanner scanner = new Scanner(System.in);
        double value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                value = Double.parseDouble(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
}
