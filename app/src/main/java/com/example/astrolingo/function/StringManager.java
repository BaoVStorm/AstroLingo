package com.example.astrolingo.function;

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
}
