package thedivazo.wrapper;

import org.bukkit.inventory.ItemStack;

import java.lang.constant.ConstantDesc;
import java.util.Objects;
import java.util.Optional;

public class ItemStackWrapper<T> extends AbstractWrapperObject<ItemStack, T> {

    public ItemStackWrapper(ItemStack object) {
        super(object);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getAmount",
                wrapperObjectContext -> wrapperObjectContext.getObject().getAmount());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getType",
                wrapperObjectContext -> wrapperObjectContext.getObject().getType());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "hasItemMeta",
                wrapperObjectContext -> wrapperObjectContext.getObject().hasItemMeta());
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getAsString",
                wrapperObjectContext -> Objects.isNull(wrapperObjectContext.getObject().getItemMeta()) ? wrapperObjectContext.getObject().getItemMeta().getAsString():null);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getCustomModelData",
                wrapperObjectContext -> Objects.isNull(wrapperObjectContext.getObject().getItemMeta()) ? wrapperObjectContext.getObject().getItemMeta().getCustomModelData():null);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "getDisplayName",
                wrapperObjectContext -> Objects.isNull(wrapperObjectContext.getObject().getItemMeta()) ? wrapperObjectContext.getObject().getItemMeta().getDisplayName():null);
        AbstractWrapperObject.addMethod(getWrapperMethodSet(),
                "hasAttributeModifiers",
                wrapperObjectContext -> Objects.isNull(wrapperObjectContext.getObject().getItemMeta()) ? wrapperObjectContext.getObject().getItemMeta().hasAttributeModifiers():null);
    }

    //TODO: Доделать
    @Override
    public Optional<? extends ConstantDesc> describeConstable() {
        return Optional.empty();
    }
}
