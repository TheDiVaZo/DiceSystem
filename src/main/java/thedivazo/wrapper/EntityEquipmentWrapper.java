package thedivazo.wrapper;

import org.bukkit.inventory.EntityEquipment;
import thedivazo.parserexpression.interpreter.wrapper.WrapperObject;

import java.lang.constant.ConstantDesc;
import java.util.Arrays;
import java.util.Optional;

public class EntityEquipmentWrapper<T> extends AbstractWrapperObject<EntityEquipment, T> {
    public EntityEquipmentWrapper(EntityEquipment object) {
        super(object);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getArmorContents",
                wrapperObjectContext -> new CollectionWrapper<>(Arrays.stream(wrapperObjectContext.getObject().getArmorContents()).map(ItemStackWrapper::new).toList()));
        //TODO: Доделать
    }

    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.empty();
    }
}
