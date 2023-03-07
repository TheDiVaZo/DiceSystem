package thedivazo.wrapper;

import org.bukkit.World;
import thedivazo.parserexpression.wrappers.Handler;

public class WorldWrapper extends Handler<World> {
    protected WorldWrapper(World input) {
        super("world", input);
    }
}
