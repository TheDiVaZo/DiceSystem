package thedivazo.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nullable;
import java.util.Objects;

public class PlayerWrapper extends AbstractWrapperObject<Player> {

    LocationWrapper locationWrapper = null;
    PlayerInventoryWrapper playerInventoryWrapper = null;
    WorldWrapper worldWrapper = null;

    public PlayerWrapper(@Nullable Player player) {
        super(player);
        if(!Objects.isNull(player)) {
            locationWrapper = new LocationWrapper(player.getLocation());
            playerInventoryWrapper = new PlayerInventoryWrapper(player.getInventory());
            worldWrapper = new WorldWrapper(player.getWorld());
        }
        addMethod(methods, "getLocation", () -> locationWrapper);
        addMethod(methods,"getWorld", () -> worldWrapper);

        addMethod(methods,"getDisplayName", ()->player.getDisplayName());
        addMethod(methods,"getExp", ()->player.getExp());
        addMethod(methods,"getHealthScale", ()->player.getHealthScale());
        addMethod(methods,"getLevel", ()->player.getLevel());
        addMethod(methods,"getWalkSpeed", ()->player.getWalkSpeed());
        addMethod(methods,"getTotalExperience", ()->player.getTotalExperience());
        addMethod(methods,"isSleepingIgnore", ()->player.isSleepingIgnored());
        addMethod(methods,"isSneaking", ()->player.isSneaking());
        addMethod(methods,"isSprinting", ()->player.isSprinting());
        addMethod(methods,"isFlying", ()->player.isFlying());
        addMethod(methods,"getPlayerWeather", () -> Objects.toString(player.getPlayerWeather(), "no"));
        addMethod(methods,"getExhaustion", ()->player.getExhaustion());
        addMethod(methods,"getExpToLevel", ()->player.getExpToLevel());
        addMethod(methods,"getFoodLevel", ()->player.getFoodLevel());
        addMethod(methods,"getSaturation", ()->player.getSaturation());
        addMethod(methods,"isBlocking", ()->player.isBlocking());

        addMethod(methods,"isGlowing", ()->player.isGlowing());
        addMethod(methods,"isInWater", ()->player.isInWater());

        addMethod(methods,"isGliding", ()->player.isGliding());
        addMethod(methods,"isInvisible", ()-> player.getActivePotionEffects().stream().anyMatch(p->p.getType() == PotionEffectType.INVISIBILITY));
        addMethod(methods,"isSleeping", ()->player.isSleeping());
        addMethod(methods, "isSwimming", ()->player.isSwimming());

        addMethod(methods,"hasPotionEffect", (ArgMethod<Boolean>) arg->{
            String potionName = arg[0].toString();
            PotionEffectType potionEffectType = PotionEffectType.getByName(potionName);
            if(potionEffectType == null) throw new IllegalArgumentException("effect \""+potionName+"\" does not exist");
            return player.hasPotionEffect(potionEffectType);
        });

        addMethod(methods,"getPotionEffect", (ArgMethod<PotionEffectWrapper>) (arg)->{
            String potionName = arg[0].toString();
            PotionEffectType potionEffectType = PotionEffectType.getByName(potionName);
            if(potionEffectType == null) throw new IllegalArgumentException("effect \""+potionName+"\" does not exist");
            return new PotionEffectWrapper(player.getPotionEffect(potionEffectType));
        });




    }
}
