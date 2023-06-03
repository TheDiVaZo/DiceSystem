package org.thedivazo.dicesystem.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.thedivazo.dicesystem.DiceSystem;
import org.thedivazo.dicesystem.dice.exception.WeightArgumentException;
import org.thedivazo.dicesystem.manager.TextManager;

@CommandAlias("adice")
@CommandPermission("dicesystem.admin")
public class AdminCommand extends BaseCommand {

    @Dependency
    private DiceSystem diceSystem;

    @Subcommand("reload")
    public void onReload(Player sender) throws WeightArgumentException, InvalidConfigurationException {
        sender.sendMessage("Let's start reloading the config!");
        try {
            diceSystem.reload();
            sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
        } catch (WeightArgumentException | InvalidConfigurationException e) {
            sender.sendMessage(ChatColor.RED + "Oops... error in the config! Details in the console!");
            sender.sendMessage(e.getMessage());
            throw e;
        }
    }
}
