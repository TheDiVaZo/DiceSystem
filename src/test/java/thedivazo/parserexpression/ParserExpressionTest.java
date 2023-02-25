package thedivazo.parserexpression;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.*;
import org.junit.jupiter.api.Test;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.utils.TernaryOperator;

import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ParserExpressionTest {

    @Test
    void testParserForDouble() throws InterpreterException, CompileException {
        ParserExpression<Double, Double> parserExpression = new ParserExpression<Double, Double>();
        parserExpression.addUnaryOperator(
                new ParserExpression.UnaryOperatorWrapper<Double>() {
            @Override
            public String getSign() {
                return "-";
            }

            @Override
            public UnaryOperator<Double> getUnaryOperator() {
                return aDouble -> -aDouble;
            }
        });
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<Double>() {
                    @Override
                    public String getSign() {
                        return "*";
                    }

                    @Override
                    public BinaryOperator<Double> getBinaryOperator() {
                        return (aDouble, aDouble2) -> aDouble*aDouble2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<Double>() {
                    @Override
                    public String getSign() {
                        return "/";
                    }

                    @Override
                    public BinaryOperator<Double> getBinaryOperator() {
                        return (aDouble, aDouble2) -> aDouble/aDouble2;
                    }
                });
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<Double>() {
                    @Override
                    public String getSign() {
                        return "+";
                    }

                    @Override
                    public BinaryOperator<Double> getBinaryOperator() {
                        return Double::sum;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<Double>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public BinaryOperator<Double> getBinaryOperator() {
                        return (aDouble, aDouble2) -> aDouble-aDouble2;
                    }
                });

        parserExpression.addCondition("pi", aDouble -> Math.PI);
        parserExpression.addCondition("[0-9]+");
        parserExpression.setAlternativeConditionParser(Double::parseDouble);

        parserExpression.addDelimiter("\\,");
        parserExpression.addSkipSymbols(" +");
        parserExpression.addCompoundOperators("\\(","\\)");

        parserExpression.addFunction("cos", doubles -> Math.cos(doubles.get(0)), 1);
        parserExpression.addFunction("sin", doubles -> Math.sin(doubles.get(0)), 1);
        parserExpression.addFunction("pow", doubles -> Math.pow(doubles.get(0), doubles.get(1)), 2);

        String code = "-pow(cos(pi/2),2)-pow(sin(pi/2),2)-cos(0)+pi-pow(pi,2)+cos(pow(pi/2,2))/sin(pow(pow(pi+3/2,2),3))+1/2*2"; //-5.510169876647481
        assertEquals(parserExpression.execute(code),-5.510169876647481d);
    }

    @Test
    void testParserForDoubleAndBoolean() throws InterpreterException, CompileException {
        ParserExpression<Object, Object> parserExpression = new ParserExpression<Object, Object>();
        parserExpression.addUnaryOperator(
                new ParserExpression.UnaryOperatorWrapper<Object>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public UnaryOperator<Object> getUnaryOperator() {
                        return aDouble -> -Double.parseDouble(aDouble.toString());
                    }
                });
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<Object>() {
                    @Override
                    public String getSign() {
                        return "*";
                    }

                    @Override
                    public BinaryOperator<Object> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())*Double.parseDouble(aDouble2.toString());
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<Object>() {
                    @Override
                    public String getSign() {
                        return "/";
                    }

                    @Override
                    public BinaryOperator<Object> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())/Double.parseDouble(aDouble2.toString());
                    }
                });
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<Object>() {
                    @Override
                    public String getSign() {
                        return "+";
                    }

                    @Override
                    public BinaryOperator<Object> getBinaryOperator() {
                        return (aDouble1, aDouble2)->Double.parseDouble(aDouble1.toString())+Double.parseDouble(aDouble2.toString());
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<Object>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public BinaryOperator<Object> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())-Double.parseDouble(aDouble2.toString());
                    }
                });
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<Object>() {
                    @Override
                    public String getSign() {
                        return "==";
                    }

                    @Override
                    public BinaryOperator<Object> getBinaryOperator() {
                        return Object::equals;
                    }
                });
        parserExpression.addTernaryOperator(
                new ParserExpression.TernaryOperatorWrapper<Object>() {
            @Override
            public String getSignOne() {
                return "?";
            }

            @Override
            public String getSignTwo() {
                return ":";
            }

            @Override
            public TernaryOperator<Object> getTernaryOperator() {
                return (cond1, cond2, cond3) -> Boolean.parseBoolean(cond1.toString()) ? cond2:cond3;
            }
        });

        parserExpression.addCondition("pi", aDouble -> Math.PI);
        parserExpression.addCondition("[0-9]+(\\.[0-9]+)?");
        parserExpression.addCondition("true", cond->true);
        parserExpression.addCondition("false", cond->false);
        parserExpression.setAlternativeConditionParser(Double::parseDouble);

        parserExpression.addDelimiter("\\,");
        parserExpression.addSkipSymbols(" +");
        parserExpression.addCompoundOperators("\\(","\\)");

        parserExpression.addFunction("cos", doubles -> Math.cos(Double.parseDouble(doubles.get(0).toString())), 1);
        parserExpression.addFunction("sin", doubles -> Math.sin(Double.parseDouble(doubles.get(0).toString())), 1);
        parserExpression.addFunction("pow", doubles -> Math.pow(Double.parseDouble(doubles.get(0).toString()),Double.parseDouble(doubles.get(1).toString())), 2);


        String code = "-pow(cos(pi/2),2)-pow(sin(pi/2),2)-cos(0)+pi-pow(pi,2)+cos(pow(pi/2,2))/sin(pow(pow(pi+3/2,2),3))+1/2*2==-5.510169876647481 ? true:false"; //-5.510169876647481
        System.out.println(parserExpression.execute(code));
    }

}