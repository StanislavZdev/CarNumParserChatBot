package org.example.carnumparserchatbot.service;


import org.springframework.stereotype.Component;
import java.util.*;
import java.util.regex.*;


@Component
public class SearchNumber {

    private static final Pattern PATTERN =
            Pattern.compile("(?:^|\\s)([хХ]\\d{3}[аА][мМ]\\d{2,3})(?:$|\\s)"
    );

    public List<String> numParser(String text) {
        if (text == null) {
            return List.of();
        }

        Matcher m = PATTERN.matcher(text);
        List<String> result = new ArrayList<>();

        while (m.find()) {
            result.add(normalize(m.group(1)));
        }

        return result;
    }

    private String normalize(String n) {
        return "Х" +
                n.substring(1, 4).toUpperCase() +
                "А" +
                "М" +
                n.substring(5);
    }
}