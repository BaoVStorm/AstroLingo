package com.example.astrolingo.function;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    public static int extractNumberFromSecondPart(String input) {
        String[] parts = input.split("_");
        try {
            // Lấy phần tử thứ 2 (index 1), ví dụ: "part1"
            String secondPart = parts[1];

            // Lấy các chữ số cuối từ "part1" bằng regex
            String number = secondPart.replaceAll("\\D+", ""); // Bỏ chữ, giữ số

            return Integer.parseInt(number); // Chuyển thành số nguyên
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Trả về -1 nếu lỗi
        }
    }

    public static String changeStringtoURLEncoder(String input) {
        String encoded = "";

        try {
            encoded = URLEncoder.encode(input, "UTF-8");
            // URLEncoder sẽ mã hóa khoảng trắng thành dấu '+', thay thế bằng '%20' nếu cần
            encoded = encoded.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encoded;
    }
}
