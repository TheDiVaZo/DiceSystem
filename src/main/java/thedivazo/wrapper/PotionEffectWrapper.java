package thedivazo.wrapper;

import api.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;

public class PotionEffectWrapper extends AbstractWrapperObject<PotionEffect> {
    public PotionEffectWrapper( PotionEffect input) {
        super(input);
        addMethod(methods,"getAmplifier", ()->input.getAmplifier());
        addMethod(methods,"getDuration", ()->input.getDuration());
        addMethod(methods,"getType", ()->input.getType().getName());

    }


}
