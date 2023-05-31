package org.thedivazo.dicesystem.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Dependency;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.thedivazo.dicesystem.DiceSystem;

@CommandAlias("dice")
public class DefaultCommand extends BaseCommand {

    @Dependency
    private DiceSystem diceSystem;

    @Subcommand("throw")
    @CommandCompletion("@diceTitles")
    public void onThrow(CommandSender sender, String diceTitle) {
        if (!diceSystem.getDiceMap().containsKey(diceTitle)) sender.sendMessage("Кубика с названием '"+diceTitle+"' нет!");
        sender.sendMessage("Бросок был совершен. Вам выпало: "+diceSystem.getDiceMap().get(diceTitle).roll());
    }
}
