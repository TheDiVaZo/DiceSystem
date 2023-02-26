package thedivazo.parserexpression;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.*;
import org.junit.jupiter.api.Test;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.utils.TernaryOperator;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class ParserExpressionTest {
     static ParserExpression<Constable, Constable> parserExpression = new ParserExpression<>();
    static {
        //arithmetic operators
        // unary "-"
        parserExpression.addUnaryOperator(
                new ParserExpression.UnaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public UnaryOperator<Constable> getUnaryOperator() {
                        return aDouble -> -Double.parseDouble(aDouble.toString());
                    }
                });
        // "*" and "/" and "%"
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "*";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())*Double.parseDouble(aDouble2.toString());
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "/";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())/Double.parseDouble(aDouble2.toString());
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "%";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())%Double.parseDouble(aDouble2.toString());
                    }
                });
        // "+" and "-"
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "+";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (aDouble1, aDouble2)->Double.parseDouble(aDouble1.toString())+Double.parseDouble(aDouble2.toString());
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) -> Double.parseDouble(aDouble.toString())-Double.parseDouble(aDouble2.toString());
                    }
                });

        // "<=" and ">=" and "<" and ">"
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "<=";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 <= (double) dob2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return ">=";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 >= (double) dob2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "<";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 < (double) dob2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return ">";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 > (double) dob2;
                    }
                });
        // "==" and "!="
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "==";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return Object::equals;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "!=";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (obj1, obj2)->!obj1.equals(obj2);
                    }
                });

        //Boolean operators
        // unary "!"
        parserExpression.addUnaryOperator(
                new ParserExpression.UnaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "!";
                    }

                    @Override
                    public UnaryOperator<Constable> getUnaryOperator() {
                        return bolean1 -> !(boolean) bolean1;
                    }
                });
        // "&&"
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "&&";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (bol1, bol2)->(boolean) bol1 && (boolean) bol2;
                    }
                });
        // "||"
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "||";
                    }

                    @Override
                    public BinaryOperator<Constable> getBinaryOperator() {
                        return (bol1, bol2)->(boolean) bol1 || (boolean) bol2;
                    }
                });


        //Ternary condition operator " : ? "
        parserExpression.addTernaryOperator(
                new ParserExpression.TernaryOperatorWrapper<>() {
                    @Override
                    public String getSignOne() {
                        return "?";
                    }

                    @Override
                    public String getSignTwo() {
                        return ":";
                    }

                    @Override
                    public TernaryOperator<Constable> getTernaryOperator() {
                        return (cond1, cond2, cond3) -> (boolean) cond1 ? cond2:cond3;
                    }
                });

        //function
        parserExpression.addFunction("cos", doubles -> Math.cos((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("sin", doubles -> Math.sin((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("tg", doubles -> Math.tan((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("ctg", doubles -> 1/Math.tan((double) doubles.get(0)), count->count==1);

        parserExpression.addFunction("abs", doubles -> Math.abs((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("floor", doubles -> Math.floor((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("round", doubles -> Math.round((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("ceil", doubles -> Math.ceil((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("rint", doubles -> Math.rint((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("copySign", doubles -> Math.copySign((double) doubles.get(0), (double) doubles.get(1)), count->count==2);

        parserExpression.addFunction("ln", doubles -> Math.log((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("exp", doubles -> Math.exp((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("log", doubles -> Math.log((double) doubles.get(1))/Math.log((double) doubles.get(0)), count->count==2);
        parserExpression.addFunction("pow", doubles -> Math.pow((double) doubles.get(0),(double) doubles.get(1)), count->count==2);
        parserExpression.addFunction("sqrt", doubles -> Math.sqrt((double) doubles.get(0)), count->count==1);
        parserExpression.addFunction("cbrt", doubles -> Math.cbrt((double) doubles.get(0)), count->count==1);


        parserExpression.addFunction("random", doubles -> Math.random()*((double) doubles.get(1)-(double) doubles.get(0))+(double) doubles.get(0), count->count==2);

        parserExpression.addFunction("max", doubles -> NumberUtils.max(doubles.stream().mapToDouble(Double.class::cast).toArray()), count->count>=2);
        parserExpression.addFunction("min", doubles -> NumberUtils.min(doubles.stream().mapToDouble(Double.class::cast).toArray()), count->count>=2);

        parserExpression.addFunction("signum", doubles -> Math.signum((double) doubles.get(0)), count->count==1);

        //Condition and constant
        parserExpression.addCondition("PI", Math.PI);
        parserExpression.addCondition("E", Math.E);

        parserExpression.addCondition("[a-zA-Z_]+"); //regEx in localConditions

        parserExpression.addCondition("[0-9]+(\\.[0-9]+)?", (player, sign)->NumberUtils.createDouble(sign)); //todo: ERROR!
        parserExpression.addCondition("true", true);
        parserExpression.addCondition("false", false);
        //parserExpression.setAlternativeConditionParser(NumberUtils::createDouble); //todo: ERROR!
        //todo: ERROR!
        parserExpression.addDelimiter("\\,");
        parserExpression.addSkipSymbols(" +");
        parserExpression.addCompoundOperators("\\(","\\)");
    }

    @Test
    void globalTest() throws CompileException, InterpreterException {
        Serializable code1 = parserExpression.compile("1+1-1+1-1+1"); //2
        Serializable code2 = parserExpression.compile("---cos(PI/2)+signum(sin(3))"); //0.9999999999999999
        Serializable code3 = parserExpression.compile("cos(sin(cos(sin(cos(exp(3))))))"); //0.6877064801469988
        Serializable code4 = parserExpression.compile("pow(cos(PI/2),2)+pow(sin(PI/2),2) == 1 ? 4+3*cos(3)/sin(3) : PI"); //-17.045757654303603
        Serializable code5 = parserExpression.compile("random(5,6)"); //random number 5-6
        Serializable code6 = parserExpression.compile("max(1,2,3,4,(((((((((5))))))))),pow(sqrt(15),2),15)"); // 15.000000000000002
        Serializable code7 = parserExpression.compile("sqrt(((((((4))*2)/2)*2)/2))*2");//4
        Serializable code8 = parserExpression.compile("log(E,100)-ln(100)"); // 0
        Serializable code9 = parserExpression.compile("cos(random(0,10)) < 2 && sin(random(0,100)) > -2 ? 1:0"); //always 1
        Serializable code10 = parserExpression.compile("((((((2*random(5,5)))))))--4+1*6/2+-3*(((((((6*6)))))))-2+1"); //-92
        Serializable code11 = parserExpression.compile("cos(PI/2+3)>3 ? cos(0.8):sin(pow(sqrt(PI/2),2))==1 ? sqrt(9)*3==9 ? -228--3----3-6:1:1"); //-228

        Serializable code12 = parserExpression.compile("8.4+1.6-3.333333+2.9-1.0000000000000 + 3"); //11.566667

        assertEquals(2d, (double) parserExpression.execute(code1));
        assertEquals(0.9999999999999999d, (double) parserExpression.execute(code2));
        assertEquals(0.6877064801469988d, (double) parserExpression.execute(code3));
        assertEquals(-17.045757654303603d, (double) parserExpression.execute(code4));
        assertTrue((double) parserExpression.execute(code5) <= 6d || (double) parserExpression.execute(code5) >= 5d);
        assertEquals(15.000000000000002d, (double) parserExpression.execute(code6));
        assertEquals(4d, (double) parserExpression.execute(code7));
        assertEquals(0d, (double) parserExpression.execute(code8));
        assertEquals(1d, (double) parserExpression.execute(code9));
        assertEquals(-92d, (double) parserExpression.execute(code10));
        assertEquals(-228d, (double) parserExpression.execute(code11));
        assertEquals(11.566667d, (double) parserExpression.execute(code12));
    }

    @Test
    void localConditionTest() throws InterpreterException, CompileException {
        Map<String, Constable> localConditions = new HashMap<>(){{put("test_condition", 5d);}};

        assertEquals(10d, parserExpression.execute("test_condition + 5", 0d, localConditions));
    }
}