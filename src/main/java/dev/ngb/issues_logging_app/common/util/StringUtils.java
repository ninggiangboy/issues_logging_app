package dev.ngb.issues_logging_app.common.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @Deprecated
    private StringUtils() {
    }

    public static boolean isEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return email.matches(EMAIL_REGEX);
    }

    public static String convertToSlug(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }

        return input.toLowerCase()
                .replace("[áàảạãăắằẳẵặâấầẩẫậäåæą]", "a")
                .replace("[óòỏõọôốồổỗộơớờởỡợöœø]", "o")
                .replace("[éèẻẽẹêếềểễệěėëę]", "e")
                .replace("[úùủũụưứừửữự]", "u")
                .replace("[iíìỉĩịïîį]", "i")
                .replace("[ùúüûǘůűūų]", "u")
                .replace("[ßşśšș]", "s")
                .replace("[źžż]", "z")
                .replace("[ýỳỷỹỵÿ]", "y")
                .replace("[ǹńňñ]", "n")
                .replace("[çćč]", "c")
                .replace("[ğǵ]", "g")
                .replace("[ŕř]", "r")
                .replace("[·/_,:;]", "-")
                .replace("[ťț]", "t")
                .replace("ḧ", "h")
                .replace("ẍ", "x")
                .replace("ẃ", "w")
                .replace("ḿ", "m")
                .replace("ṕ", "p")
                .replace("ł", "l")
                .replace("đ", "d")
                .replace("\\s+", "-")
                .replace("&", "-and-")
                .replace("[^\\w\\-]+", "")
                .replace("--+", "-")
                .replace("^-+", "")
                .replace("-+$", "");
    }

}
