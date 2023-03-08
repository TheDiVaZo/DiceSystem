package thedivazo.command;

import api.logging.Logger;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import thedivazo.DiceSystem;
import thedivazo.dice.Dice;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;
import thedivazo.utils.StringUtil;

import java.lang.constant.Constable;
import java.util.Objects;

@CommandAlias("dicesystem|dice")
public class DefaultCommand extends BaseCommand {
    @Subcommand("reload")
    @CommandPermission("moh.admin")
    public void onCommand(CommandSender commandSender) throws CompileException {
        DiceSystem.getInstance().reloadConfigManager();
        commandSender.sendMessage("Config has been reloaded");
    }

    @Default
    @Description("Кидать ролл")
    @CommandCompletion("@diceList")
    public static void onDice(Player player, String diceName) throws InterpreterException, CompileException {
        try {
            Dice.ThrewDiceObject<Constable> dice = DiceSystem.getInstance().getDiceManager().throwDice(diceName, player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',dice.message()));
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
            player.sendMessage(e.getMessage());
            throw e;
        }
    }
}
