import org.junit.jupiter.api.Test;
import org.mvel2.MVEL;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    @Test
    void fff() {
        Serializable serializable = MVEL.compileExpression("f:d && false", new HashMap<>(){{put("f:d", true);}});
        System.out.println(MVEL.executeExpression(serializable));
    }
}
