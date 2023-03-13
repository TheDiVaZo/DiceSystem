package thedivazo.wrapper;

import org.bukkit.entity.LivingEntity;
import thedivazo.parserexpression.exception.InterpreterException;

import java.lang.constant.Constable;
import java.lang.constant.ConstantDesc;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

public abstract class LivingEntityWrapper<T extends LivingEntity> extends AbstractWrapperObject<T, Pattern> {

    public LivingEntityWrapper(T object) {
        super(object);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "canBreatheUnderwater",
                wrapperObjectContext -> wrapperObjectContext.getObject().canBreatheUnderwater());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getActivePotionEffects",
                wrapperObjectContext -> new CollectionWrapper<>(wrapperObjectContext.getObject().getActivePotionEffects()));
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getArrowCooldown",
                wrapperObjectContext -> wrapperObjectContext.getObject().getArrowCooldown());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getArrowsInBody",
                wrapperObjectContext -> wrapperObjectContext.getObject().getArrowsInBody());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getCanPickupItems",
                wrapperObjectContext -> wrapperObjectContext.getObject().getCanPickupItems());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getCategory",
                wrapperObjectContext -> wrapperObjectContext.getObject().getCategory().name());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getEquipment",
                wrapperObjectContext -> new EntityEquipmentWrapper(wrapperObjectContext.getObject().getEquipment()));
        //TODO: Доделать
    }
}
