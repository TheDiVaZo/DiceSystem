package thedivazo.utils;

import javax.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MatcherWrapper {
    private Pattern pattern;

    public MatcherWrapper(Pattern pattern) {
        this.pattern = pattern;
    }

    @Nullable
    public String matchOne(CharSequence input) {
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()) return matcher.group();
        else return null;
    }

    public List<String> matchAll(CharSequence input) {
        Matcher matcher = pattern.matcher(input);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    public String pattern() {
        return pattern.pattern();
    }

    public int flags() {
        return pattern.flags();
    }

    public String[] split(CharSequence input, int limit) {
        return pattern.split(input, limit);
    }

    public String[] split(CharSequence input) {
        return pattern.split(input);
    }

    public Predicate<String> asPredicate() {
        return pattern.asPredicate();
    }

    public Predicate<String> asMatchPredicate() {
        return pattern.asMatchPredicate();
    }

    public Stream<String> splitAsStream(CharSequence input) {
        return pattern.splitAsStream(input);
    }

    public boolean find(CharSequence input) {
        return pattern.matcher(input).find();
    }
}
