package thedivazo.dice;

import org.junit.jupiter.api.Test;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.ParserExpressionTest;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.lang.constant.Constable;

import static org.junit.jupiter.api.Assertions.*;

class CodeInTextTest {

    @Test
    void test1() throws CompileException, InterpreterException {

        CodeInText<Constable, Constable, Constable> codeInText1 = new CodeInText<>("{10+10}",ParserExpressionTest.parserExpression);
        CodeInText<Constable, Constable, Constable> codeInText2 = new CodeInText<>("{sin(0)} - this is sin(0)",ParserExpressionTest.parserExpression);
        CodeInText<Constable, Constable, Constable> codeInText3 = new CodeInText<>("{10+10} + {10+10} = {20+20}",ParserExpressionTest.parserExpression);
        CodeInText<Constable, Constable, Constable> codeInText4 = new CodeInText<>("{'Hello'} my {'friend'}",ParserExpressionTest.parserExpression);
        CodeInText<Constable, Constable, Constable> codeInText5 = new CodeInText<>("{true} = {'true'}",ParserExpressionTest.parserExpression);
        CodeInText<Constable, Constable, Constable> codeInText6 = new CodeInText<>("{number('20.0')} = {20.0} = {'20'}",ParserExpressionTest.parserExpression);

        assertEquals("20", codeInText1.getText());
        assertEquals("0 - this is sin(0)", codeInText2.getText());
        assertEquals("20 + 20 = 40", codeInText3.getText());
        assertEquals("Hello my friend", codeInText4.getText());
        assertEquals("true = true", codeInText5.getText());
        assertEquals("20 = 20 = 20", codeInText6.getText());

    }

}