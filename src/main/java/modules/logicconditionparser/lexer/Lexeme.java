package modules.logicconditionparser.lexer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.beans.ConstructorProperties;

@AllArgsConstructor
@EqualsAndHashCode
public class Lexeme {

    @Getter
    private final String sign;

    @Getter
    private final Token token;

    @Getter
    private final int priority;




}
