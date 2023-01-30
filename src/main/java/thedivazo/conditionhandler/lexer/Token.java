package thedivazo.conditionhandler.lexer;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
public class Token {

    @Getter
    protected TokenType lexemeType;
    @Getter
    protected String sign;
    @Getter
    protected int position;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token)) return false;
        return getLexemeType() == token.getLexemeType() && getSign().equals(token.getSign());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLexemeType(), getSign());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("{").append(lexemeType);
        sb.append(", '").append(sign).append('\'');
        sb.append(", ").append(position);
        sb.append('}');
        return sb.toString();
    }
}
