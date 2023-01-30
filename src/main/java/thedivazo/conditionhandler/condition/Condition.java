package thedivazo.conditionhandler.condition;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class Condition {

    protected abstract String getName();

    public abstract boolean getResult(Player player);

    public abstract boolean getResult(OfflinePlayer player);

}
