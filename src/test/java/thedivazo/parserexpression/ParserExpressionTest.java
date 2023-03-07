package thedivazo.parserexpression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.*;
import org.junit.jupiter.api.Test;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.parserexpression.interpreter.wrapper.WrapperMethod;
import thedivazo.parserexpression.interpreter.wrapper.WrapperObject;
import thedivazo.utils.TernFunction;

import java.io.Serializable;
import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class ParserExpressionTest {
     public static ParserExpression<Constable, Constable, Constable> parserExpression = new ParserExpression<>();
    static {
        parserExpression.setCondition("'.*?'", (input, string)->string.substring(1,string.length()-1));

        //arithmetic operators
        // unary "-"
        parserExpression.addUnaryOperator(
                new ParserExpression.UnaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public Function<Constable, Constable> getUnaryOperator() {
                        return object -> {
                            if(NumberUtils.isCreatable(object.toString())) return -NumberUtils.createDouble(object.toString());
                            else return BooleanUtils.toBoolean(object.toString());
                        };
                    }
                });
        // "*" and and "//" and "/" and "%"
        parserExpression.addBinaryOperator(
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "*";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) ->(Double) aDouble * (Double) aDouble2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "//";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) ->(double)((int) ((Double) aDouble / (Double) aDouble2));
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "/";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) ->(Double) aDouble / (Double) aDouble2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "%";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) ->(Double) aDouble % (Double) aDouble2;
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
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (object1, object2)->{
                            if(object1 instanceof Double double1 && object2 instanceof Double double2)
                                return double1+double2;
                            else if(object1 instanceof Double double1) return NumberUtils.toScaledBigDecimal(double1).stripTrailingZeros().toPlainString() + object2.toString();
                            else if(object2 instanceof Double double2) return object1.toString() + NumberUtils.toScaledBigDecimal(double2).stripTrailingZeros().toPlainString();
                            else return object1.toString()+object2.toString();
                        };
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "-";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (aDouble, aDouble2) -> (Double) aDouble - (Double) aDouble2;
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
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 <= (double) dob2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return ">=";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 >= (double) dob2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "<";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return (dob1, dob2)->(double) dob1 < (double) dob2;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return ">";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
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
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
                        return Constable::equals;
                    }
                },
                new ParserExpression.BinaryOperatorWrapper<>() {
                    @Override
                    public String getSign() {
                        return "!=";
                    }

                    @Override
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
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
                    public Function<Constable, Constable> getUnaryOperator() {
                        return (anyObject)->{
                            if(anyObject instanceof Double doubleObject) return -doubleObject;
                            else return  !(boolean) (anyObject);
                        };
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
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
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
                    public BiFunction<Constable, Constable, Constable> getBinaryOperator() {
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
                    public TernFunction<Boolean, Constable, Constable, Constable> getTernaryOperator() {
                        return (cond1, cond2, cond3) -> cond1 ? cond2:cond3;
                    }
                });

        //function
        parserExpression.setFunction("cos", doubles -> Math.cos((double) doubles.get(0)));
        parserExpression.setFunction("sin", doubles -> Math.sin((double) doubles.get(0)));
        parserExpression.setFunction("tg", doubles -> Math.tan((double) doubles.get(0)));
        parserExpression.setFunction("ctg", doubles -> 1/Math.tan((double) doubles.get(0)));

        parserExpression.setFunction("abs", doubles -> Math.abs((double) doubles.get(0)));
        parserExpression.setFunction("floor", doubles -> Math.floor((double) doubles.get(0)));
        parserExpression.setFunction("round", doubles -> Math.round((double) doubles.get(0)));
        parserExpression.setFunction("ceil", doubles -> Math.ceil((double) doubles.get(0)));
        parserExpression.setFunction("rint", doubles -> Math.rint((double) doubles.get(0)));
        parserExpression.setFunction("copySign", doubles -> Math.copySign((double) doubles.get(0), (double) doubles.get(1)));

        parserExpression.setFunction("ln", doubles -> Math.log((double) doubles.get(0)));
        parserExpression.setFunction("exp", doubles -> Math.exp((double) doubles.get(0)));
        parserExpression.setFunction("log", doubles -> Math.log((double) doubles.get(1))/Math.log((double) doubles.get(0)));
        parserExpression.setFunction("pow", doubles -> Math.pow((double) doubles.get(0),(double) doubles.get(1)));
        parserExpression.setFunction("sqrt", doubles -> Math.sqrt((double) doubles.get(0)));
        parserExpression.setFunction("cbrt", doubles -> Math.cbrt((double) doubles.get(0)));


        parserExpression.setFunction("random", doubles -> Math.random()*((double) doubles.get(1)-(double) doubles.get(0))+(double) doubles.get(0));

        parserExpression.setFunction("max", doubles -> NumberUtils.max(doubles.stream().mapToDouble(Double.class::cast).toArray()));
        parserExpression.setFunction("min", doubles -> NumberUtils.min(doubles.stream().mapToDouble(Double.class::cast).toArray()));

        parserExpression.setFunction("signum", doubles -> Math.signum((double) doubles.get(0)));

        parserExpression.setFunction("str", objects->{
            Object constable = objects.get(0);
            if(constable instanceof Double double1) return BigDecimal.valueOf(double1).stripTrailingZeros().toPlainString();
            else return constable.toString();
        });
        parserExpression.setFunction("number", objects->{
            Object constable = objects.get(0);
            if(NumberUtils.isCreatable(constable.toString())) return NumberUtils.createDouble(constable.toString());
            else if(constable instanceof Boolean boolean1) return (boolean1) ? 1 : 0;
            else return 0;
        });
        parserExpression.setFunction("boolean", objects->{
            Object constable = objects.get(0);
            if(constable instanceof Boolean boolean1) return boolean1;
            else if(constable instanceof Double double1) return BooleanUtils.toBoolean((int) double1.doubleValue());
            else return BooleanUtils.toBoolean(constable.toString());
        });

        parserExpression.setFunction("emptyFunction", (input)-> 20d);


        parserExpression.setCondition("true", true);
        parserExpression.setCondition("false", false);

        //Condition and constant
        parserExpression.setCondition("PI", Math.PI);
        parserExpression.setCondition("E", Math.E);

        parserExpression.setCondition("[0-9]+(\\.[0-9]+)?", (player, sign)->NumberUtils.createDouble(sign)); //todo: ERROR!

        parserExpression.setAlternativeConditionParser(NumberUtils::createDouble);
        //todo: ERROR!
        parserExpression.addDelimiter("\\,");
        parserExpression.addSkipSymbols(" +");
        parserExpression.addCompoundOperators("\\(","\\)");

        parserExpression.addVariableStartSymbols("\\$");
        parserExpression.addLocalVariablesRegExs("[a-zA-Z_\\-0-9\\.]+");
    }

    @Test
    void globalTest() throws CompileException, InterpreterException {
        Serializable code1 = parserExpression.compile("1+1-1+1-1+PI//2"); //2
        Serializable code2 = parserExpression.compile("---cos(PI/2)+signum(sin(3))"); //0.9999999999999999
        Serializable code3 = parserExpression.compile("cos(sin(cos(sin(cos(exp(3))))))"); //0.6877064801469988
        Serializable code4 = parserExpression.compile("pow(cos(PI/2),2)+pow(sin(PI/2),2) == 1 ? 4+3*cos(3)/sin(3) : PI"); //-17.045757654303603
        Serializable code5 = parserExpression.compile("random(10//2,6)"); //random number 5-6
        Serializable code6 = parserExpression.compile("max(11.214356,2,3.234567,4,(((((((((5))))))))),pow(sqrt(15),2),15)"); // 15.000000000000002
        Serializable code7 = parserExpression.compile("sqrt(((((((4))*2)/2)*2)/2))*2");//4
        Serializable code8 = parserExpression.compile("log(E,100)-ln(100)"); // 0
        Serializable code9 = parserExpression.compile("cos(random(0,10)) < 2 && sin(random(0,100)) > -2 ? 1:0"); //always 1
        Serializable code10 = parserExpression.compile("((((((2*random(5,5)))))))--4+1*6/2+-3*(((((((6*6)))))))-2+1"); //-92
        Serializable code11 = parserExpression.compile("cos(PI/2+3)>3 ? cos(0.8):sin(pow(sqrt(PI/2),2))==1 ? sqrt(9)*3==9 ? -228--3----3-6:1:1"); //-228
        Serializable code12 = parserExpression.compile("8.4+1.6-3.333333+2.9-1.0000000000000 + 3"); //11.566667
        Serializable code13 = parserExpression.compile("'test_string' == 'test_string'");
        Serializable code14 = parserExpression.compile("'test_string' == 'test_string' && 'test2_string' == 'test2_string'");
        Serializable code15 = parserExpression.compile("'result cos: '+cos(PI/2) == 'result cos: '+cos(PI/2)");
        Serializable code16 = parserExpression.compile("'random: '+random(0,100000) != 'random: '+random(0,100000)");
        Serializable code17 = parserExpression.compile("''+''+''+''+''+''+''+''+''+''+''+''+''+''+''+''+''+''+'' == ''");
        Serializable code18 = parserExpression.compile("1+'2'+3+'4'+5 == '12345'");
        Serializable code19 = parserExpression.compile("str(5)");
        Serializable code20 = parserExpression.compile("str('5')");
        Serializable code21 = parserExpression.compile("str('5'+'6')");
        Serializable code22 = parserExpression.compile("number('5')");
        Serializable code23 = parserExpression.compile("number(5)");
        Serializable code24 = parserExpression.compile("number('5'+'6')");
        Serializable code25 = parserExpression.compile("number(str(5))");
        Serializable code26 = parserExpression.compile("4.5//2");
        Serializable code27 = parserExpression.compile("4.5//2.5");
        Serializable code28 = parserExpression.compile("4//2");
        Serializable code29 = parserExpression.compile("5 - 4//2 + 3");
        Serializable code30 = parserExpression.compile("boolean(true)");
        Serializable code31 = parserExpression.compile("boolean('true')");
        Serializable code32 = parserExpression.compile("boolean(1)");
        Serializable code33 = parserExpression.compile("boolean('1')");
        Serializable code34 = parserExpression.compile("boolean(random(2,1000))");
        Serializable code35 = parserExpression.compile("boolean(false)");
        Serializable code36 = parserExpression.compile("boolean('false')");
        Serializable code37 = parserExpression.compile("boolean(0)");
        Serializable code38 = parserExpression.compile("boolean('0')");
        Serializable code39 = parserExpression.compile("emptyFunction()");

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
        assertTrue((boolean) parserExpression.execute(code13));
        assertTrue((boolean) parserExpression.execute(code14));
        assertTrue((boolean) parserExpression.execute(code15));
        assertTrue((boolean) parserExpression.execute(code16));
        assertTrue((boolean) parserExpression.execute(code17));
        assertTrue((boolean) parserExpression.execute(code18));
        assertEquals("5",parserExpression.execute(code19));
        assertEquals("5",parserExpression.execute(code20));
        assertEquals("56",parserExpression.execute(code21));
        assertEquals(5d, (double) parserExpression.execute(code22));
        assertEquals(5d, (double) parserExpression.execute(code23));
        assertEquals(56d, (double) parserExpression.execute(code24));
        assertEquals(5d, (double) parserExpression.execute(code25));
        assertEquals(2d, (double) parserExpression.execute(code26));
        assertEquals(1d, (double) parserExpression.execute(code27));
        assertEquals(2d, (double) parserExpression.execute(code28));
        assertEquals(6d, (double) parserExpression.execute(code29));
        assertTrue((boolean)parserExpression.execute(code30));
        assertTrue((boolean)parserExpression.execute(code31));
        assertTrue((boolean)parserExpression.execute(code32));
        assertTrue((boolean)parserExpression.execute(code33));
        assertTrue((boolean)parserExpression.execute(code34));
        assertFalse((boolean)parserExpression.execute(code35));
        assertFalse((boolean)parserExpression.execute(code36));
        assertFalse((boolean)parserExpression.execute(code37));
        assertFalse((boolean)parserExpression.execute(code38));
        assertEquals(20d, parserExpression.execute(code39));
    }

    @Test
    void variableTest() throws InterpreterException, CompileException {

        Map<String, Constable> variables = new HashMap<>();
        variables.put("variable.0.1", 5d);
        variables.put("variable.0.2", "5");
        variables.put("variable.0.3", true);
        variables.put("variable.0.4", parserExpression.execute("1+1"));
        variables.put("variable.0.5", false);
        variables.put("variable.0.6", parserExpression.execute("false"));
        
        assertEquals(5d,(double) parserExpression.execute("$variable.0.1", 0, variables));
        assertEquals("5",parserExpression.execute("$variable.0.2", 0, variables));
        assertTrue((boolean) parserExpression.execute("$variable.0.3", 0, variables));
        assertEquals(2d,(double) parserExpression.execute("$variable.0.4", 0, variables));
        assertFalse((boolean) parserExpression.execute("$variable.0.5", 0, variables));
        assertFalse((boolean) parserExpression.execute("$variable.0.6", 0, variables));
    }

    @Test
    void objectTest() throws CompileException, InterpreterException {

        @RequiredArgsConstructor
        @Getter
        class Human {
            private final int age;
            private final String name;
            private final String sex;
        }

        @RequiredArgsConstructor
        class WrapperHuman implements WrapperObject<Human>, Constable {

            private final Human object;

            @Getter
            private final Set<WrapperMethod<?>> methods = new HashSet<>(){{
                add(new WrapperMethod<Integer>() {
                    @Override
                    public String getMethodName() {
                        return "getAge";
                    }

                    @Override
                    public Class<Object>[] getArgumentTypes() {
                        return new Class[0];
                    }

                    @Override
                    public Integer execute(Object... arguments) {
                        return object.getAge();
                    }
                });
                add(new WrapperMethod<String>() {
                    @Override
                    public String getMethodName() {
                        return "getName";
                    }

                    @Override
                    public Class<Object>[] getArgumentTypes() {
                        return new Class[0];
                    }

                    @Override
                    public String execute(Object... arguments) {
                        return object.getName();
                    }
                });
                add(new WrapperMethod<String>() {
                    @Override
                    public String getMethodName() {
                        return "getSex";
                    }

                    @Override
                    public Class<Object>[] getArgumentTypes() {
                        return new Class[0];
                    }

                    @Override
                    public String execute(Object... arguments) {
                        return object.getSex();
                    }
                });
            }};
            @Override
            public Human getObject() {
                return object;
            }

            @Override
            public boolean hasMethod(String nameMethod, Class<?>... methodArgumentsType) {
                return !methods.stream().filter(wrapperMethod -> wrapperMethod.getMethodName().equals(nameMethod) && Arrays.equals(wrapperMethod.getArgumentTypes(), methodArgumentsType)).findAny().isEmpty();
            }

            @Override
            public boolean hasMethod(String nameMethod, Collection<?> methodArguments) {
                return hasMethod(nameMethod, methodArguments.stream().map(Object::getClass).toList().toArray(new Class[0]));
            }

            @Override
            public Object executeMethod(String nameMethod, Collection<?> methodArguments) {
                return methods.stream().filter(wrapperMethod -> wrapperMethod.getMethodName().equals(nameMethod) && Arrays.equals(wrapperMethod.getArgumentTypes(), methodArguments.stream().map(Object::getClass).toList().toArray(new Class[0]))).findAny().get().execute(methodArguments);
            }

            @Override
            public Optional<? extends ConstantDesc> describeConstable() {
                return Optional.empty();
            }
        }

        Human developer = new Human(19, "TheDiVaZo", "real man");
        Human adminVotiveRP = new Human(20, "lorendel", "cool man");
        Human feminist = new Human(-4, "Feminist Fat Big Life", "Attack Helicopter");

        WrapperHuman wrapperDeveloper = new WrapperHuman(developer);
        WrapperHuman wrapperAdminVotiveRP = new WrapperHuman(adminVotiveRP);
        WrapperHuman wrapperFeminist = new WrapperHuman(feminist);

        parserExpression.addMethodReferenceSymbols("#");

        Map<String, Constable> localVariables = new HashMap<>(){{
            put("developer", wrapperDeveloper);
        }};
        parserExpression.setFunction("getAdminVotiveRP", (string)->wrapperAdminVotiveRP);
        parserExpression.setCondition("@fiministka@", wrapperFeminist);

        parserExpression.addObjects(new WrapperHuman(null));

        Serializable code1 = parserExpression.compile("'cool developer -> ' + $developer#getName() + ' ' + $developer#getAge() + ' ' + $developer#getSex()");
        Serializable code2 = parserExpression.compile("'cool admin votive rp -> ' + getAdminVotiveRP()#getName() + ' ' + getAdminVotiveRP()#getAge() + ' ' + getAdminVotiveRP()#getSex()");
        Serializable code3 = parserExpression.compile("'cool bad feminist -> ' + @fiministka@#getName() + ' ' + @fiministka@#getAge() + ' ' + @fiministka@#getSex()");

        assertEquals("cool developer -> TheDiVaZo 19 real man", parserExpression.execute(code1, 1, localVariables));
        assertEquals("cool admin votive rp -> lorendel 20 cool man", parserExpression.execute(code2, 2, localVariables));
        assertEquals("cool bad feminist -> Feminist Fat Big Life -4 Attack Helicopter", parserExpression.execute(code3, 3, localVariables));




    }
    
    
}