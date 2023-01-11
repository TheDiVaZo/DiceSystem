package modules.logicconditionparser.lexer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.beans.ConstructorProperties;

@AllArgsConstructor
@EqualsAndHashCode
public class Lexeme {

    @Getter
    public final String sign;

    @Getter
    public final Token token;




}
