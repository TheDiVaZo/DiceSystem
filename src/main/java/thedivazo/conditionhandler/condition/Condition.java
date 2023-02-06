package thedivazo.conditionhandler.condition;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface Condition {

    public abstract boolean getResult(Player player);

}
