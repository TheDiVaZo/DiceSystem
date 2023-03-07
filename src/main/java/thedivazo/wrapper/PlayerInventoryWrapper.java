package thedivazo.wrapper;

import org.bukkit.inventory.PlayerInventory;
import thedivazo.parserexpression.wrappers.Handler;

public class PlayerInventoryWrapper extends Handler<PlayerInventory> {
    protected PlayerInventoryWrapper(PlayerInventory input) {
        super("inventory", input);
    }
}
