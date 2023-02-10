import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    @Test
    void fff() {
        Matcher matcher = Pattern.compile("(!)\s?([a-zA-Z0-9]+)").matcher("cond1 !cond2 cond3");
        matcher.find();
        System.out.println(matcher.group(2));
    }
}
