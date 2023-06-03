package org.thedivazo.dicesystem.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.thedivazo.dicesystem.DiceSystem;
import org.thedivazo.dicesystem.dice.Dice;
import org.thedivazo.dicesystem.manager.TextManager;
import org.thedivazo.dicesystem.manager.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CommandPermission("dicesystem.use")
@CommandAlias("dice")
public class DefaultCommand extends BaseCommand {

    @Dependency
    private DiceSystem diceSystem;

    @Dependency
    private ConfigManager configManager;

    @Subcommand("throw")
    @CommandCompletion("@diceTitles")
    public void onThrow(Player sender, String diceTitle) {
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("dice_title", diceTitle);
        placeholders.put("dice_name", "unfounded");
        placeholders.put("roll", "unfounded");
        placeholders.put("roll_probability", "0");
        placeholders.put("thrower_name", sender.getName());
        if (!configManager.getDiceManager().isDice(diceTitle)) {
            TextManager.sendMessage(sender, diceSystem.getConfigManager().getDiceNotFound(), placeholders);
            return;
        }
        Dice<?> dice = configManager.getDiceManager().getDice(diceTitle);
        placeholders.put("dice_name", dice.getName());
        if (Objects.nonNull(dice.getPermission()) && !sender.isPermissionSet(dice.getPermission())) {
            TextManager.sendMessage(sender, diceSystem.getConfigManager().getNoPermission(), placeholders);
        }
        Object roll = dice.roll();
        placeholders.put("roll", roll);
        placeholders.put("roll_probability", dice.getProbability(roll));
        TextManager.sendMessage(sender, diceSystem.getConfigManager().getFormatThrower(), placeholders);
    }
}
