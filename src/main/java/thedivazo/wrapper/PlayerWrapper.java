package thedivazo.wrapper;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import thedivazo.parserexpression.wrappers.Handler;

import java.util.Objects;

public class PlayerWrapper extends Handler<Player> {

    LocationWrapper locationWrapper = new LocationWrapper(input.getLocation());
    PlayerInventoryWrapper playerInventoryWrapper = new PlayerInventoryWrapper(input.getInventory());
    WorldWrapper worldWrapper = new WorldWrapper(input.getWorld());

    protected PlayerWrapper(Player player) {

        super("player", player);
        methods.put("getLocation", (EmptyMethod<LocationWrapper>) () -> locationWrapper);
        methods.put("getWorld", (EmptyMethod<WorldWrapper>) () -> worldWrapper);

        methods.put("getDisplayName", (EmptyMethod<String>) player::getDisplayName);
        methods.put("getExp", (EmptyMethod<Float>) player::getExp);
        methods.put("getHealthScale", (EmptyMethod<Double>) player::getHealthScale);
        methods.put("getLevel", (EmptyMethod<Integer>) player::getLevel);
        methods.put("getWalkSpeed", (EmptyMethod<Float>) player::getWalkSpeed);
        methods.put("getTotalExperience", (EmptyMethod<Integer>) player::getTotalExperience);
        methods.put("isSleepingIgnore", (EmptyMethod<Boolean>) player::isSleepingIgnored);
        methods.put("isSneaking", (EmptyMethod<Boolean>) player::isSneaking);
        methods.put("isSprinting", (EmptyMethod<Boolean>) player::isSprinting);
        methods.put("isFlying", (EmptyMethod<Boolean>) player::isFlying);
        methods.put("getPlayerWeather", (EmptyMethod<String>) () -> Objects.toString(player.getPlayerWeather(), "no"));
        methods.put("getExhaustion", (EmptyMethod<Float>) player::getExhaustion);
        methods.put("getExpToLevel", (EmptyMethod<Integer>) player::getExpToLevel);
        methods.put("getFoodLevel", (EmptyMethod<Integer>) player::getFoodLevel);
        methods.put("getSaturation", (EmptyMethod<Float>) player::getSaturation);
        methods.put("isBlocking", (EmptyMethod<Boolean>) player::isBlocking);

        methods.put("isGlowing", (EmptyMethod<Boolean>) player::isGlowing);
        methods.put("isOnGround", (EmptyMethod<Boolean>) player::isOnGround);

        methods.put("isGliding", (EmptyMethod<Boolean>) player::isGliding);
        methods.put("isInvisible", (EmptyMethod<Boolean>) ()-> player.getActivePotionEffects().stream().anyMatch(p->p.getType() == PotionEffectType.INVISIBILITY));
        methods.put("isSleeping", (EmptyMethod<Boolean>) player::isSleeping);
        //methods.put("isSwimming", (EmptyMethod<Boolean>)player::);

        methods.put("hasPotionEffect", (UnaryMethod<Boolean, String>) (input)->{
            PotionEffectType potionEffectType = PotionEffectType.getByName(input);
            if(potionEffectType == null) throw new IllegalArgumentException("effect \""+input+"\" does not exist");
            return player.hasPotionEffect(potionEffectType);
        });

        methods.put("getPotionEffect", (UnaryMethod<PotionEffectWrapper, String>) (input)->{
            PotionEffectType potionEffectType = PotionEffectType.getByName(input);
            if(potionEffectType == null) throw new IllegalArgumentException("effect \""+input+"\" does not exist");
            return new PotionEffectWrapper(player.getPotionEffect(potionEffectType));
        });




    }
}
