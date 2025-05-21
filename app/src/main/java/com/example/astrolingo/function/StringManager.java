package com.example.astrolingo.function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManager {
    public static boolean containsSpecialCharacter(String input) {
        // Regex: bất kỳ ký tự nào không phải chữ cái hoặc số
        String regex = "[^\\p{L}0-9 ]";
        return input.matches(".*" + regex + ".*");
    }

    public static boolean containsSpace(String input) {
        return input.contains(" ");
    }

    public static boolean isPhoneNumber(String input) {
        // Regex cho số điện thoại VN: bắt đầu bằng 0, theo sau là 9-10 chữ số
        return input.matches("^0\\d{9,10}$");
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int extractLastNumber(String input) {
        String[] parts = input.split("_");
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return -1; // hoặc giá trị mặc định nếu không hợp lệ
        }
    }

    public static String extractSecondPart(String input) {
        String[] parts = input.split("_");
        try {
            return parts[1]; // phần tử thứ 2 (index 1)
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null; // hoặc giá trị mặc định nếu không hợp lệ
        }
    }
}
