package com.facevisitor.api.common.utils;

import java.util.Locale;
import java.util.UUID;


public class StringUtils {
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

    public static String toLanguageTag(Locale locale) {
        return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    public static String getRandomStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String[] toStringArray(String str, String regex) {
        return str.split(regex);
    }


    public static String format(String format, String content) {
        return String.format(format, content);
    }
}
