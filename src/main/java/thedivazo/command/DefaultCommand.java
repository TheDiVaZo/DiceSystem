package thedivazo.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import thedivazo.DiceSystem;
import thedivazo.dice.Dice;
import thedivazo.parserexpression.exception.CompileException;
import thedivazo.parserexpression.exception.InterpreterException;

import java.util.Objects;

@CommandAlias("dicesystem|dice")
public class DefaultCommand extends BaseCommand {
    @Subcommand("reload")
    @CommandPermission("moh.admin")
    public void onCommand(CommandSender commandSender) {
        DiceSystem.getInstance().reloadConfigManager();
        commandSender.sendMessage("Config has been reloaded");
    }

    @Default
    @Description("Кидать ролл")
    @CommandCompletion("@diceList")
    public static void onDice(Player player, String diceList) throws InterpreterException, CompileException {
         Dice dice = DiceSystem.getInstance().getDiceManager().getDice(diceList);
         player.sendMessage(dice.diceText(player));
    }
}
