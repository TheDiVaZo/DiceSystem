package thedivazo.dice;

import org.bukkit.entity.Player;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeInText {
    private static final Pattern patternCode = Pattern.compile("\\{(.+?)\\}");

    private final ParserExpression<Object, Object> parserExpression;
    private final String originalText;
    private List<Serializable> codesBlockInText;

    public CodeInText(String originalText, ParserExpression<Object, Object> parserExpression) throws CompileException {
        this.originalText = originalText;
        this.parserExpression = parserExpression;
        compileCodeInText();
    }

    protected void compileCodeInText() throws CompileException {
        List<Serializable> listCode = new ArrayList<>();
        Matcher matcher = patternCode.matcher(originalText);
        while (matcher.find()) {
            listCode.add(parserExpression.compile(matcher.group(1)));
        }
        codesBlockInText = listCode;
    }

    public String getText(Object input, Map<String, Object> localConditions) throws InterpreterException, CompileException {
        String text = originalText;
        for (Serializable serializable : codesBlockInText) {
            text = text.replaceFirst(patternCode.pattern(), parserExpression.execute(serializable, input, localConditions).toString());
        }
        return text;
    }

    public String getText() throws InterpreterException, CompileException {
        return getText(null, null);
    }



}
