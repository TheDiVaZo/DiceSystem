package thedivazo;

import api.logging.Logger;
import api.logging.handlers.JULHandler;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.*;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import thedivazo.config.ConfigManager;
import thedivazo.dice.DiceManager;
import thedivazo.metrics.MetricsManager;
import thedivazo.parserexpression.ParserExpression;
import thedivazo.utils.TernaryOperator;
import thedivazo.parserexpression.exception.*;
import thedivazo.utils.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.constant.Constable;
import java.util.function.BinaryOperator;
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

    @Setter
    private ParserExpression<Player, Constable> parserExpression;

    private final PaperCommandManager manager = new PaperCommandManager(this);
    private static ConfigManager configManager;

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
        manager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t)-> {
            getLogger().warning("Error occurred while executing command "+command.getName());
            return true;
        });
    }

    public void reloadConfigManager() {
        saveDefaultConfig();
        reloadConfig();
        getConfigManager().reloadConfigFile();
        registerEvents();
        registerCommands();
        getDiceManager().reload(getConfigManager());
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

        parserExpression.addDelimiter("\\,");
        parserExpression.addSkipSymbols(" +");
        parserExpression.addCompoundOperators("\\(","\\)");

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
        
        parserExpression.addFunction("max", doubles -> NumberUtils.max(doubles.stream().mapToDouble(double.class::cast).toArray()), count->count>=2);
        parserExpression.addFunction("min", doubles -> NumberUtils.min(doubles.stream().mapToDouble(double.class::cast).toArray()), count->count>=2);

        parserExpression.addFunction("signum", doubles -> Math.signum((double) doubles.get(0)), count->count==1);

        //Condition and constant
        parserExpression.addCondition("PI", Math.PI);
        parserExpression.addCondition("E", Math.E);
        parserExpression.addCondition("[0-9]+(\\.[0-9]+)?", (player, sign)->NumberUtils.createDouble(sign));
        parserExpression.addCondition("\\%.+?\\%",(player, placeholder)-> getConfigManager().placeholderSet(player, placeholder));
        parserExpression.addCondition("true", true);
        parserExpression.addCondition("false", false);
        parserExpression.addCondition("[a-zA-Z_]+\\:[0-9a-zA-Z_]+");
        parserExpression.addCondition("[a-zA-Z_]+");
        parserExpression.setAlternativeConditionParser(Double::parseDouble);
    }
}