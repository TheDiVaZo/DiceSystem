package thedivazo.wrapper;

import org.bukkit.Location;
import thedivazo.parserexpression.wrappers.Handler;

public class LocationWrapper extends Handler<Location> {
    protected LocationWrapper(Location input) {
        super("location", input);
    }
}
