package thedivazo;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import thedivazo.command.DefaultCommand;
import thedivazo.config.ConfigManager;
import thedivazo.dice.DiceManager;
import thedivazo.metrics.MetricsManager;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.utils.TernFunction;
import thedivazo.utils.TernaryOperator;
import thedivazo.wrapper.*;

import java.lang.constant.Constable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name = PluginSettings.NAME_PLUGIN, version = PluginSettings.VERSION)
//@Dependency(value = "ProtocolLib")
//@SoftDependsOn(value = {
//        @SoftDependency(value = "PlaceholderAPI"),
//        @SoftDependency(value = "SuperVanish"),
//        @SoftDependency(value = "PremiumVanish"),
//        @SoftDependency(value = "ChatControllerRed"),
//        @SoftDependency(value = "Essentials"),
//        @SoftDependency(value = "CMI")
//})
@Author(value = "TheDiVaZo")
@ApiVersion(value = ApiVersion.Target.v1_13)
public class DiceSystem extends JavaPlugin {
    @Getter
    private final DiceManager diceManager = new DiceManager();

    @Getter
    @Setter
    private ParserExpression<Player, Constable, Constable> parserExpression;

    private PaperCommandManager commandManager;
    private static ConfigManager configManager;

    public DiceSystem() throws CompileException {
    }

    public static ConfigManager getConfigManager() {
        return DiceSystem.configManager;
    }

    public static DiceSystem getInstance() {
        return JavaPlugin.getPlugin(DiceSystem.class);
    }

    private static void setConfigManager(ConfigManager configManager) {
        DiceSystem.configManager = configManager;
    }

    @Override
    public void onEnable() {
        api.logging.Logger.init(new JULHandler(getLogger()));
        api.logging.Logger.info("Starting...");
        setConfigManager(new ConfigManager(DiceSystem.getInstance()));
        generateDefaultParser();
        checkPluginVersion();
        new MetricsManager(this);
        registerEvents();
        registerCommands();
    }

    public void registerEvents() {

    }
    public void registerCommands() {
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new DefaultCommand());
        commandManager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t)-> {
            getLogger().warning("Error occurred while executing command "+command.getName());
            return true;
        });
    }

    public void reloadConfigManager() throws CompileException {
        saveDefaultConfig();
        reloadConfig();
        getConfigManager().reloadConfigFile();
        registerEvents();
        registerCommands();
        getDiceManager().reload(getConfigManager(), getParserExpression());
        reloadDiceSystem();
    }

    public void reloadDiceSystem() throws CompileException {
        diceManager.reload(getConfigManager(), getParserExpression());
    }

    private void checkPluginVersion() {
        if (!PluginSettings.VERSION.equals(ConfigManager.getLastVersionOfPlugin())) {
            for (int i = 0; i < 5; i++) {
                Logger.warn("PLEASE, UPDATE NAME_PLUGIN! LINK: https://www.spigotmc.org/resources/link/");
            }
        } else {
            Logger.info("Plugin have last version");
        }
    }

    public static Float getVersion() {
        String version = Bukkit.getVersion();
        Pattern pattern = Pattern.compile("\\(MC: ([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(version);
        if (matcher.find())
        {
            return Float.parseFloat(matcher.group(1));
        }
        else return null;
    }

    protected void generateDefaultParser() {
        parserExpression = new ParserExpression<>();

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

        parserExpression.setCondition("[0-9]+(\\.[0-9]+)?", (player, sign)->NumberUtils.createDouble(sign));

        parserExpression.setAlternativeConditionParser(NumberUtils::createDouble);
        parserExpression.addDelimiter("\\,");
        parserExpression.addSkipSymbols(" +");
        parserExpression.addCompoundOperators("\\(","\\)");

        parserExpression.addVariableStartSymbols("\\$");
        parserExpression.addMethodReferenceSymbols("\\#");

        parserExpression.setCondition("[a-zA-Z0-9\\.]+");

        parserExpression.addObjects(
                new LocationWrapper(null),
                new PlayerInventoryWrapper(null),
                new PotionEffectWrapper(null),
                new WorldWrapper(null),
                new PlayerWrapper(null));
    }
}