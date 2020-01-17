package com.facevisitor.api.common.utils;


import java.util.Random;

public class Utils {
    public static int ascPriority(int priority, int priority2) {
        return Integer.compare(priority, priority2);
    }

    public static int descPriority(int priority, int priority2) {
        return Integer.compare(priority2, priority);
    }

    public static String getRandomNumber(int length) {
        Random randomNum = new Random();
        String[] numTemp = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String auth = "";

        for (int i = 0; i < length; i++) {
            auth = auth + numTemp[randomNum.nextInt(10)];
        }
        return auth;
    }

//    private String getDisposition(String filename, String browser) throws Exception {
//        String dispositionPrefix = "attachment;filename=";
//        String encodedFilename = null;
//
//        if (browser.equals("MSIE")) {
//
//            encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
//        } else if (browser.equals("Firefox")) {
//
//            encodedFilename = new String(filename.getBytes(StandardCharsets.UTF_8), "8859_1");
//
//        } else if (browser.equals("Opera")) {
//
//            encodedFilename = new String(filename.getBytes(StandardCharsets.UTF_8), "8859_1");
//        } else if (browser.equals("Chrome")) {
//
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0; i < filename.length(); i++) {
//                char c = filename.charAt(i);
//                if (c > '~') {
//                    sb.append(URLEncoder.encode("" + c, StandardCharsets.UTF_8));
//                } else {
//                    sb.append(c);
//                }
//            }
//            encodedFilename = sb.toString();
//        } else {
//            throw new RuntimeException("Not supported browser");
//        }
//
//        return dispositionPrefix + encodedFilename;
//    }

    public static String getUrlEmbedVideo(String linkUrl) {
        final String startWithVimeo = "https://vimeo.com/channels/staffpicks/";
        final String startWithYouTube = "https://youtu.be/";
        StringBuffer result = new StringBuffer();


        if (linkUrl.startsWith(startWithVimeo)) {
            result.append("https://player.vimeo.com/video/");
            result.append(linkUrl.substring(startWithVimeo.length()));
        } else if (linkUrl.startsWith(startWithYouTube)) {
            result.append("https://www.youtube.com/embed/");
            result.append(linkUrl.substring(startWithYouTube.length()));
        } else {
            return linkUrl;
        }

        return result.toString();

    }

    public static String convertLineSeparatorToBrTag(String content) {
        if (content == null) {
            return null;
        }
        return content
                .replaceAll("\r\n", "<br/>")
                .replaceAll(System.getProperty("line.separator"), "<br/>")
                .replaceAll("\n", "<br/>");
    }

    public static String convertBrTagToLineSeparator(String content) {
        if (content == null) {
            return null;
        }
        return content
                .replaceAll("<br/>", System.getProperty("line.separator"))
                .replaceAll("<br>", System.getProperty("line.separator"));
    }

    public static String convertNameToXXX(String name) {

        if (name == null) {
            return null;
        }

        int length = name.length() - 1;

        String result = "";

        for (int i = 0; i < length; i++) {
            result += "*";
        }

        return name.substring(0, 1) + result;
    }

//    public static String compressHtml(String content) {
//        return new HtmlCompressor().compress(content);  // html minify
//    }
}
