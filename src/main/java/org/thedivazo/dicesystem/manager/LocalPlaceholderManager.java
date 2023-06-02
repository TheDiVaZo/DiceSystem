package org.thedivazo.dicesystem.manager;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
public class LocalPlaceholderManager {
    Pattern placeholderPattern = Pattern.compile("\\{[a-zA-Z0-9_-]+}");

    public String setPlaceholders(String text, Map<String, Object> placeholders) {
        Matcher matcher = placeholderPattern.matcher(text);
        StringBuilder result = new StringBuilder(text);
        while (matcher.find()) {
            String placeholder = matcher.group();
            result.replace(matcher.start(), matcher.end(), placeholders.getOrDefault(placeholder, placeholder).toString());
        }
        return result.toString();
    }
}
