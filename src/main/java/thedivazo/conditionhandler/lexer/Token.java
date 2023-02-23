package thedivazo.conditionhandler.lexer;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;


public record Token(@Getter TokenType lexemeType, @Getter String sign, int position) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token token)) return false;
        return lexemeType() == token.lexemeType() && sign().equals(token.sign());
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexemeType(), sign());
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

    public int getPosition() {
        return position-1;
    }
}
