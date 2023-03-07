package thedivazo.wrapper;

import org.bukkit.potion.PotionEffect;
import thedivazo.parserexpression.wrappers.Handler;

public class PotionEffectWrapper extends Handler<PotionEffect> {
    protected PotionEffectWrapper(PotionEffect input) {
        super("potionEffect", input);
        methods.put("getAmplifier", (EmptyMethod<Integer>) input::getAmplifier);
        methods.put("getDuration", (EmptyMethod<Integer>) input::getDuration);
        methods.put("getType", (EmptyMethod<String>) ()->input.getType().getName());

    }


}
